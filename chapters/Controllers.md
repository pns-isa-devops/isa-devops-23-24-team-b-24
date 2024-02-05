# REST controllers (and templates)

  * Author: Philippe Collet

The aim of this chapter is to provide the basics about implementing RESTful services, as well as calling external REST services. Details about testing are available in the [dedicated chapter](Testing.md).

To implement RESTful services, Spring can rely on two framework, a simple and synchronous Web model-view-controller (MVC) framework, and a new asynchronous WebFlux framework. To ease understanding, we use here the MVC one, which was originally designed around a DispatcherServlet that dispatches requests to handlers, with configurable handler mappings, view resolution, as well as support for uploading files. The default handler is based on the `@Controller` and `@RequestMapping annotations`, offering handling methods for the *http* protocol. Afterwards the controller mechanism enables one to create RESTful servers with extended annotations.

## Golden rules of REST Controllers

1. **Thou shalt not implement business logic in a controller.** The controller is handling interoperability (transfering information back and forth), handling exceptions to return appropriate status codes, and coordinating call to business components. That's all.
2. **Thou shalt not make a controller stateful.** The controller shoud be stateless. It should not keep *conversational state* information between the client and the server (i.e., some information that would oblige to keep the controller object to be the same to handle several distinct calls from the same client to the server). This enables to handle network failure, and to scale horizontally.

## A very basic REST Controller: `RecipeController`

We focus on the implementation of a first very simple `@RestController` that serves the catalog of cookie recipes. This annotation declares the class as a component to be instantiated by the Spring MVC container. This separate container handles RESTful http requests while sharing the same namespace as the main Spring container (in which classic `@Service` and `@Component` beans are instantiated). This enables the injection through the auto-wiring of a component that will implement the `CatalogExplorator` interface, i.e., the `Catalog` component in our case.

REST routes are served by annotating methods. The `listAllRecipes()` method is then annotated with `@GetMapping` in which:

   * the `path` value is set to "/recipes" (through a constant). This path will be concatenated to the deployment path of the controllers ("/" by default).
   * The `produces` value, set to "APPLICATION\_JSON\_VALUE", so that the `Accept` field in the http header will be set to "application/json", showing that this service is **only** answering with JSON (by default, the SpringBoot implementation converts the responses to JSON but other types such as XML could be supported).


```java
@RestController
public class RecipeController {

    public static final String BASE_URI = "/recipes";

    @Autowired
    private CatalogExplorator catalogExp;

    @GetMapping(path = RecipeController.BASE_URI, produces = APPLICATION_JSON_VALUE)
    public Set<Cookies> listAllRecipes() {
        return catalogExp.listPreMadeRecipes();
    }
}
```

The implementation is really straightforward, as the method is directly calling the `listPreMadeRecipes`on the `catalogExp` interfaces, returning a set of the `Cookies` enum. By default, the Spring MVC support will take the returned Object and convert it to JSON through its getters (remember that entities should be POJOs so this a sensible way for a default implementation). In our case, it will return a JSON Array with 3 strings corresponding to each cookie enum. See the [CookieEnum](../cli/src/main/java/teamb.w4e/cli/model/CookieEnum.java) and [RecipeCommands](../cli/src/main/java/teamb.w4e/cli/commands/RecipeCommands.java) classes on the *cli* side for explanations on their handling with RestTemplate (see also the section below for technical details).

The controller will also return back a 200 status code as the request is complete (without any exceptions), with the returned object in the http body. Without specific code (see next section), a REST controller sends 404 if a resource is not present, or 500 when an exception is thrown out of controller and there is no handler associated with the exception (exception handlers are explained further below).


## A more complex example: `CustomerCareController`

Let us now focus on the implementation of a more complex controller with the one that handles the registration of new customers.

### Mapping, body, and return codes

First, in the code below, one can note several changes:

   * There is a `@RequestMapping` annotation on the **class**. This enables all other annotations on methods in the class to be added to this base URI (here "/customers"). Same thing for the "produces" header, which we use at method level in the first controller above.
   * There is a `@PostMapping` annotation on the register method. While `@ResquestMapping` defaults to `Get`, there are also specific `@GetMapping` and `@PostMapping` (actually all http verbs, such as Patch, are supported).
   * As a POST message, the body of the request is transmitted (from JSON to an object) into the `CustomerDTO` parameter of the method thanks to the `@RequestBody` annotation. The next subsection deals with the [concept of DTO](#the-dto-pattern).
   * The method itself returns a `ResponseEntity<CustomerDTO>`, this generic type allowing to define the return code and the body of the response.
   * The business logic consists in calling the "register" method on the "registry" interface (being `@Autowired`) passing the information from the `CustomerDTO` parameter. If no exception is thrown, a Customer (from the `entity` package) is returned and converted to `CustomerDTO` using the private method `convertCustomerToDto`. It is then added in the body of the `ResponseEntity`. If the Customer name is already used, an exception is thrown by the registry component, caught and the ResponseEntity status is set to 409.

```java
@RestController
@RequestMapping(path = CustomerCareController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class CustomerCareController {

    public static final String BASE_URI = "/customers";

    private final CustomerRegistration registry;

    private final CustomerFinder finder;

    @Autowired
    public CustomerCareController(CustomerRegistration registry, CustomerFinder finder) {
        this.registry = registry;
        this.finder = finder;
    }

    [...]

    @PostMapping(consumes = APPLICATION_JSON_VALUE) // path is a REST CONTROLLER NAME
    public ResponseEntity<CustomerDTO> register(@RequestBody @Valid CustomerDTO cusdto) {
        // Note that there is no validation at all on the CustomerDto mapped
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertCustomerToDto(registry.register(cusdto.name(), cusdto.creditCard())));
        } catch (AlreadyExistingCustomerException e) {
            // Note: Returning 409 (Conflict) can also be seen a security/privacy vulnerability, exposing a service for account enumeration
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    [...]

    private static CustomerDTO convertCustomerToDto(Customer customer) { // In more complex cases, we could use a ModelMapper such as MapStruct
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getCreditCard());
    }
}
```

### The DTO Pattern

DTO stands for Data Transfer Object. DTOs are objects that carry data between processes in order to decouple the domain models from the presentation layer (allowing both to change independently), encapsulate the serialization logic (in our case it is generally automatically done by Spring and Jackson from JSON to objects and vice versa), and potentially reduce the number of method calls (lowering the network overhead).

With DTOs, one can build different views from the domain models, allowing us to create other representations of the same domain but optimizing them to the clients' needs without affecting the domain design.

In the Cookie Factory, we have made different decisions to use and not use the DTO pattern:

   * The Customer entity (thus in our domain model) contains id, name, creditCard, a set of items (representing the cart) and a set of Orders (in the persistent version). The CustomerDTO, used to send the data to the clients, only contains the first three attributes, and not the orders. Note that in this case, we could have used `@JsonIgnore` directives on the set of Orders defined in the Customer class. However, the DTO pattern can be used when the data are reworked, on when there are several client views. Here we use the new record construct in Java to facilitate the implementation

```java
public record CustomerDTO (
    Long id, // expected to be empty when POSTing the creation of Customer, and containing the Id when returned
    @NotBlank(message = "name should not be blank") String name,
    @Pattern(regexp = "\\d{10}+", message = "credit card should be exactly 10 digits") String creditCard) {
}
```
   * An OrderDTO gives back compact information on an order, with the price and the customer, but not the details of the items (just to exemplify the DTO pattern).
   * An ErrorDTO is used to give back some details on error in a uniform way.
   * The PaymentRequestDTO and PaymentReceipt DTOs are located in another package as it is used to post and get the result back from the call to the external Bank system (see RestTemplate below).

## Path variables and exception handling: the `CartController` 

Let us now study a more complex example with the `CartController`.

The first thing to notice is that some all sub-URI used in all mappings of this controller are using a *path variable* to identify the customerId in the URL, i.e., `"/{customerId}/cart"`. Then, when it used, for example in the * updateCustomerCart* method, the `@PathVariable("customerId")` annotation enables to value the parameter of the method. As a result, this POST route is implemented by a method which parameters are the customerId from the path, and the Item object (used as a DTO and transformed from the JSON sent in the request body).

```java
    public static final String CART_URI = "/{customerId}/cart";
...
    @PostMapping(path = CART_URI, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Item> updateCustomerCart(@PathVariable("customerId") Long customerId, @RequestBody Item it) throws CustomerIdNotFoundException, NegativeQuantityException {
        return ResponseEntity.ok(cart.update(customerId, it)); // Item is used as a DTO in and out here...
    }
```

The rest of the implementation is simple, we retrieve a `ResponseEntity` with an OK status and in the body, an `Item` object with the updated quantity. But wait, the method is not handling any of the possible exception, two of them being declared in the signature. What happens them? With no handling, this would throw a 500 status code.

Let us now introduce the concept of `@ExceptionHandler`, which relies on the AOP capabilities of Spring like many other smart technical functionalities. The code below shows the implementation of two handlers, one for each exception of our method, in the class `GlobalControllerAdvice`. The annotation is parameterized by the exception caught, and this exception can be passed as parameter to the handling method. In our case, we use the return object as a `ResponseEntity<ErrorDTO>` so that we can both build an error code (404 if the customerId is not found, 403 if the amount would create a negative quantity) and pass an `ErrorDTO` object with details of the error.

```java
@RestControllerAdvice(assignableTypes = {CustomerCareController.class, CartController.class})
public class GlobalControllerAdvice {

[...]

    @ExceptionHandler({CustomerIdNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleExceptions(CustomerIdNotFoundException e) {
        return new ErrorDTO("Customer not found", e.getId() + " is not a valid customer Id");
    }

    @ExceptionHandler({NegativeQuantityException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleExceptions(NegativeQuantityException e) {
        return new ErrorDTO("Attempting to update the cookie quantity to a negative value",
                "from Customer " + e.getName() + " with cookie " + e.getCookie() +
                        " leading to quantity " + e.getPotentialQuantity());
    }
```

This kind of implementation has several benefits:

   * The exception handling code is separated from the business logic, which itself is not polluted
   * The exception handling code can be reused, and it is reused: The `CustomerIdNotFoundException` may be thrown in all route implementations of the corresponding path.
   * Both the business logic and the exception handler can be properly typed with different `ResponseEntity<T>` whereas it should usually be typed with `ResponseEntity<Object>` if you mix the two concerns.

## Calling REST services

One of the simplest solution to call a REST service from Spring is to use a `RestTemplate`.
Using a template object, one can call a route and get an object or a `ResponseEntity<T>`. Objects being passed or returned are *automatically* transformed from JSON (by default) using the same mechanism as for controllers.
[A set of methods correspond to the different http verbs, with overloaded version for different variants.](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html).

In the code below, the `pay` method:

   * creates a `PaymentRequestDTO` object from the creditCard number and the amount to be paid, this object being transformed by Spring (and Jackson) in a JSON payload compatible with what the bank implementation in NestJs is expecting.
   * POST it and use the `postForEntity` variant that returns a `ResponseEntity<PaymentReceiptDTO>`. This allows for checking the HTTP status code (201 is expected if the payment is accepted, 400 otherwise).
   * internally catches a specific RuntimeException (`HttpClientErrorException`) that is thrown when 4xx codes are returned (warning, there is another exception for 5xx codes...).
   * returns an `Optional<String>` with the id of the payment receipt inside if it is not empty.

One can note that the url used in injected in a constant with `@Value` and a environment variable, so that it can be configured as a property, but also change in the command line so that it is easier to pass a network address, for example in a network within a *docker compose*.

```java
@Component
public class BankProxy implements Bank {

    @Value("${bank.host.baseurl}")
    private String bankHostandPort;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Optional<String> pay(Customer customer, double value) {
        try {
            ResponseEntity<PaymentReceiptDTO> result = restTemplate.postForEntity(
                    bankHostandPort + "/cctransactions",
                    new PaymentRequestDTO(customer.getCreditCard(), value),
                    PaymentReceiptDTO.class
            );
            if (result.getStatusCode().equals(HttpStatus.CREATED) && result.hasBody()) {
                return Optional.of(result.getBody().payReceiptId());
            } else {
                return Optional.empty();
            }
        }
        catch (HttpClientErrorException errorException) {
            if (errorException.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                return Optional.empty();
            }
            throw errorException;
        }
    }


}
```

It must be noted that a `RestTemplate` object can also be created once and for all as a Spring Bean, and then injected in all components that need it. This is the case in the *cli* which uses Springshell so that the template is configured with the url of the backend. This also enables to configure the template with a builder pattern.

```java
@SpringBootApplication
public class CliApplication {

    @Value("${tcf.host.baseurl}")
    private String serverHostandPort;

...
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
     return builder
                .rootUri(serverHostandPort)
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .build();
    }
```











