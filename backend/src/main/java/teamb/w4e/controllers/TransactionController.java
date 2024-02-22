package teamb.w4e.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import teamb.w4e.dto.CustomerDTO;
import teamb.w4e.dto.ErrorDTO;
import teamb.w4e.dto.TransactionDTO;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Transaction;
import teamb.w4e.interfaces.CustomerFinder;
import teamb.w4e.interfaces.Payment;
import teamb.w4e.interfaces.TransactionFinder;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = TransactionController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class TransactionController {

    public static final String BASE_URI = "/customers/transactions";

    private final TransactionFinder transactionFinder;
    private final CustomerFinder customerFinder;
    private final Payment payment;

    @Autowired
    public TransactionController(TransactionFinder transactionFinder, CustomerFinder customerFinder, Payment payment) {
        this.transactionFinder = transactionFinder;
        this.customerFinder = customerFinder;
        this.payment = payment;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDTO handleExceptions(MethodArgumentNotValidException e) {
        return new ErrorDTO("Cannot process Transaction information", e.getMessage());
    }

    @PostMapping(path = "/{id_customer}/transactions", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDTO> transaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        Customer customer = customerFinder.findById(transactionDTO.customer().id()).orElseThrow();
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertTransactionToDto(payment.pay(customer, transactionDTO.amount())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping(path = "/{idCustomer}/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactionOfCustomer(@PathVariable Long idCustomer) {
        Customer customer = customerFinder.findById(idCustomer).orElseThrow();
        List<TransactionDTO> transactionDTOs = transactionFinder.findTransactionsByCustomer(customer).stream().map(TransactionController::convertTransactionToDto).toList();
        return ResponseEntity.ok(transactionDTOs);
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getTransactions() {
        return ResponseEntity.ok(transactionFinder.findAllTransactions()
                .stream()
                .map(TransactionController::convertTransactionToDto)
                .toList());
    }

    private static TransactionDTO convertTransactionToDto(Transaction transaction) {
        return new TransactionDTO(transaction.getId(), convertCustomerToDto(transaction.getCustomer()), transaction.getAmount() , transaction.getPaymentId());
    }

    private static CustomerDTO convertCustomerToDto(Customer customer) {
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getCreditCard(), customer.getCard().getId());
    }

}
