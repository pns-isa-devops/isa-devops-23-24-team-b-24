package teamb.w4e.controllers;

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
import teamb.w4e.exceptions.CustomerIdNotFoundException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.interfaces.CustomerFinder;
import teamb.w4e.interfaces.TransactionFinder;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = TransactionController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class TransactionController {

    public static final String BASE_URI = "/customers/transactions";
    private final TransactionFinder transactionFinder;
    private final CustomerFinder customerFinder;

    @Autowired
    public TransactionController(TransactionFinder transactionFinder, CustomerFinder customerFinder) {
        this.transactionFinder = transactionFinder;
        this.customerFinder = customerFinder;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDTO handleExceptions(MethodArgumentNotValidException e) {
        return new ErrorDTO("Cannot process Transaction information", e.getMessage());
    }

    @GetMapping(path = "/{idCustomer}/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactionOfCustomer(@PathVariable Long idCustomer) throws CustomerIdNotFoundException {
        CustomerDTO customer = CustomerCareController.convertCustomerToDto(customerFinder.retrieveCustomer(idCustomer));
        List<TransactionDTO> transactionDTOs = transactionFinder.findTransactionsByCustomer(customer.id()).stream().map(TransactionController::convertTransactionToDto).toList();
        return ResponseEntity.ok(transactionDTOs);
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getTransactions() {
        return ResponseEntity.ok(transactionFinder.findAllTransactions()
                .stream()
                .map(TransactionController::convertTransactionToDto)
                .toList());
    }

    public static TransactionDTO convertTransactionToDto(Transaction transaction) {
        return new TransactionDTO(transaction.getId(), CustomerCareController.convertCustomerToDto(transaction.getCustomer()), transaction.getAmount(), transaction.getPaymentId());
    }
}
