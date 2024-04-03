package teamb.w4e.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.dto.AdvantageDTO;
import teamb.w4e.dto.CustomerDTO;
import teamb.w4e.dto.LeisureDTO;
import teamb.w4e.dto.cart.CartElementDTO;
import teamb.w4e.entities.Partner;
import teamb.w4e.entities.catalog.Activity;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.catalog.AdvantageType;
import teamb.w4e.entities.catalog.Service;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.customers.Group;
import teamb.w4e.entities.reservations.ReservationType;
import teamb.w4e.interfaces.Bank;
import teamb.w4e.interfaces.Scheduler;
import teamb.w4e.interfaces.SkiPass;
import teamb.w4e.repositories.PartnerRepository;
import teamb.w4e.repositories.catalog.ActivityCatalogRepository;
import teamb.w4e.repositories.catalog.AdvantageCatalogRepository;
import teamb.w4e.repositories.catalog.ServiceCatalogRepository;
import teamb.w4e.repositories.customers.CustomerRepository;
import teamb.w4e.repositories.customers.GroupRepository;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class CartWebValidationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private ServiceCatalogRepository serviceCatalogRepository;

    @Autowired
    private ActivityCatalogRepository activityCatalogRepository;

    @Autowired
    private AdvantageCatalogRepository advantageCatalogRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private GroupRepository groupRepository;

    @MockBean
    private Scheduler scheduler;

    @MockBean
    private Bank bank;

    @MockBean
    private SkiPass skiPass;

    private Partner partner;

    private Customer customer;

    @BeforeEach
    void setUpContext() {
        partnerRepository.deleteAll();
        serviceCatalogRepository.deleteAll();
        activityCatalogRepository.deleteAll();
        advantageCatalogRepository.deleteAll();
        customerRepository.deleteAll();
        groupRepository.deleteAll();
        partner = new Partner("partner");
        partnerRepository.saveAndFlush(partner);
        customer = new Customer("john", "1230896983");
        customerRepository.saveAndFlush(customer);
    }

    @Test
    void updateCustomerCartWithServiceElementTest() throws Exception {
        Service service = new Service(partner, "service", "desc", 123);
        serviceCatalogRepository.saveAndFlush(service);
        CartElementDTO serviceElement = new CartElementDTO(ReservationType.SERVICE, LeisureController.convertServiceToDto(service));
        mockMvc.perform(MockMvcRequestBuilders.post(CustomerController.BASE_URI + "/" + customer.getId() + "/cart", serviceElement)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(serviceElement)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateCustomerCartWithTimeSlotActivityElementTest() throws Exception {
        Activity activity = new Activity(partner, "activity", "desc", 123);
        activityCatalogRepository.saveAndFlush(activity);
        when(scheduler.checkAvailability(activity, "07-11 21:30")).thenReturn(true);
        CartElementDTO timeSlotElement = new CartElementDTO(ReservationType.TIME_SLOT, LeisureController.convertActivityToDto(activity), "07-11 21:30");
        mockMvc.perform(MockMvcRequestBuilders.post(CustomerController.BASE_URI + "/" + customer.getId() + "/cart", timeSlotElement)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(timeSlotElement)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void timeSlotActivityElementErrorTest() throws Exception {
        Activity activity = new Activity(partner, "activity", "desc", 123);
        activityCatalogRepository.saveAndFlush(activity);
        when(scheduler.checkAvailability(activity, "07-11 21:31")).thenReturn(false);
        CartElementDTO timeSlotElement = new CartElementDTO(ReservationType.TIME_SLOT, LeisureController.convertActivityToDto(activity), "07-11 21:31");
        mockMvc.perform(MockMvcRequestBuilders.post(CustomerController.BASE_URI + "/" + customer.getId() + "/cart", timeSlotElement)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(timeSlotElement)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void updateCustomerCartWithGroupActivityElementTest() throws Exception {
        Customer jane = new Customer("jane", "1234567890");
        customerRepository.saveAndFlush(jane);
        Group group = new Group(customer, Set.of(jane));
        groupRepository.saveAndFlush(group);
        Activity activity = new Activity(partner, "activity", "desc", 123);
        activityCatalogRepository.saveAndFlush(activity);
        CartElementDTO groupElement = new CartElementDTO(ReservationType.GROUP, LeisureController.convertActivityToDto(activity), GroupController.convertGroupToDto(group));
        mockMvc.perform(MockMvcRequestBuilders.post(CustomerController.BASE_URI + "/" + customer.getId() + "/cart", groupElement)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(groupElement)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void updateCustomerCartWithSkiActivityElementTest() throws Exception {
        Activity activity = new Activity(partner, "Ski", "desc", 123);
        activityCatalogRepository.saveAndFlush(activity);
        when(skiPass.reserve(activity.getName(), "day", 3)).thenReturn(java.util.Optional.of("ok"));
        CartElementDTO skiElement = new CartElementDTO(ReservationType.SKI_PASS, LeisureController.convertActivityToDto(activity), "day", 3);
        mockMvc.perform(MockMvcRequestBuilders.post(CustomerController.BASE_URI + "/" + customer.getId() + "/cart", skiElement)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skiElement)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    void updateCustomerCartWithAdvantageElementTest() throws Exception {
        customer.getCard().addPoints(100);
        Advantage advantage = new Advantage(partner, "advantage", AdvantageType.REDUCTION, 12);
        advantageCatalogRepository.saveAndFlush(advantage);
        AdvantageDTO advantageElement = new AdvantageDTO(advantage.getId(), advantage.getName(), advantage.getType(), advantage.getPoints(), advantage.getPartner().getName());
        mockMvc.perform(MockMvcRequestBuilders.post(CustomerController.BASE_URI + "/" + customer.getId() + "/cart/advantages", advantageElement)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(advantageElement)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void advantageElementWithoutEnoughPointTest() throws Exception {
        Advantage advantage = new Advantage(partner, "advantage", AdvantageType.REDUCTION, 12);
        advantageCatalogRepository.saveAndFlush(advantage);
        AdvantageDTO advantageElement = new AdvantageDTO(advantage.getId(), advantage.getName(), advantage.getType(), advantage.getPoints(), advantage.getPartner().getName());
        mockMvc.perform(MockMvcRequestBuilders.post(CustomerController.BASE_URI + "/" + customer.getId() + "/cart/advantages", advantageElement)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(advantageElement)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void alreadyExistingAdvantageTest() throws Exception {
        customer.getCard().addPoints(100);
        Advantage advantage = new Advantage(partner, "advantage", AdvantageType.REDUCTION, 12);
        advantageCatalogRepository.saveAndFlush(advantage);
        AdvantageDTO advantageElement = new AdvantageDTO(advantage.getId(), advantage.getName(), advantage.getType(), advantage.getPoints(), advantage.getPartner().getName());
        mockMvc.perform(MockMvcRequestBuilders.post(CustomerController.BASE_URI + "/" + customer.getId() + "/cart/advantages", advantageElement)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(advantageElement)));
        mockMvc.perform(MockMvcRequestBuilders.post(CustomerController.BASE_URI + "/" + customer.getId() + "/cart/advantages", advantageElement)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(advantageElement)))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    void retrieveAdvantageCartContentsTest() throws Exception {
        customer.getCard().addPoints(100);
        Advantage advantage = new Advantage(partner, "advantage", AdvantageType.REDUCTION, 12);
        advantageCatalogRepository.saveAndFlush(advantage);
        AdvantageDTO advantageElement = new AdvantageDTO(advantage.getId(), advantage.getName(), advantage.getType(), advantage.getPoints(), advantage.getPartner().getName());
        mockMvc.perform(MockMvcRequestBuilders.post(CustomerController.BASE_URI + "/" + customer.getId() + "/cart/advantages", advantageElement)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(advantageElement)));
        mockMvc.perform(MockMvcRequestBuilders.get(CustomerController.BASE_URI + "/" + customer.getId() + "/cart/advantages")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void retrieveCartContentsTest() throws Exception {
        Activity activity = new Activity(partner, "activity", "desc", 123);
        activityCatalogRepository.saveAndFlush(activity);
        CartElementDTO timeSlotElement = new CartElementDTO(ReservationType.TIME_SLOT, LeisureController.convertActivityToDto(activity), "07-11 21:30");
        mockMvc.perform(MockMvcRequestBuilders.post(CustomerController.BASE_URI + "/" + customer.getId() + "/cart", timeSlotElement)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(timeSlotElement)));
        mockMvc.perform(MockMvcRequestBuilders.get(CustomerController.BASE_URI + "/" + customer.getId() + "/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void notFoundCustomerTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(CustomerController.BASE_URI + "/-1/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
