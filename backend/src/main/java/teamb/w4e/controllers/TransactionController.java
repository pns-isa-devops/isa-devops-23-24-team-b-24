package teamb.w4e.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import teamb.w4e.components.TransactionRegistry;
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

    private final TransactionRegistry registry;

    private final TransactionFinder transactionFinder;

    private final CustomerFinder customerFinder;

    private final Payment payment;

    @Autowired
    public TransactionController(TransactionRegistry registry, TransactionFinder transactionFinder, CustomerFinder customerFinder, Payment payment) {
        this.registry = registry;
        this.transactionFinder = transactionFinder;
        this.customerFinder = customerFinder;
        this.payment = payment;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDTO handleExceptions(MethodArgumentNotValidException e) {
        return new ErrorDTO("Cannot process Transaction information", e.getMessage());
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionDTO> transaction(@RequestBody TransactionDTO transactionDTO) {
        Customer customer = customerFinder.findByName(transactionDTO.customerName()).get();

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertTransactionToDto(payment.createTransaction(customer, transactionDTO.amount())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping(path = "/{id_customer}/{id_transaction}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable Long id_customer, @PathVariable Long id_transaction) {
        return transactionFinder.findTransactionById(id_transaction)
                .filter(transaction -> transaction.getCustomer().getId().equals(id_customer))
                .map(TransactionController::convertTransactionToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getTransactions() {
        return ResponseEntity.ok(transactionFinder.findAllTransactions()
                .stream()
                .map(TransactionController::convertTransactionToDto)
                .toList());
    }

    private static TransactionDTO convertTransactionToDto(Transaction transaction) {
        return new TransactionDTO(transaction.getId(), transaction.getCustomer().getName(), transaction.getAmount(), transaction.getPaymentId());
    }

}
