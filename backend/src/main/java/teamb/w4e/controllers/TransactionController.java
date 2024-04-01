package teamb.w4e.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import teamb.w4e.dto.ErrorDTO;
import teamb.w4e.dto.PointTransactionDTO;
import teamb.w4e.dto.TransactionDTO;
import teamb.w4e.entities.transactions.PointTransaction;
import teamb.w4e.entities.transactions.Transaction;
import teamb.w4e.interfaces.TransactionFinder;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = CustomerController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class TransactionController {

    public static final String TRANSACTION_URI = "/transactions";
    public static final String POINT_TRANSACTION_URI = "/point-transactions";
    private final TransactionFinder transactionFinder;


    @Autowired
    public TransactionController(TransactionFinder transactionFinder) {
        this.transactionFinder = transactionFinder;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDTO handleExceptions(MethodArgumentNotValidException e) {
        return new ErrorDTO("Cannot process Transaction information", e.getMessage());
    }

    @GetMapping(path = TRANSACTION_URI + "/{idCustomer}/transaction")
    public ResponseEntity<List<TransactionDTO>> getTransactionOfCustomer(@PathVariable Long idCustomer) {
        List<TransactionDTO> transactionDTOs = transactionFinder.findTransactionsByCustomer(idCustomer).stream().map(TransactionController::convertTransactionToDto).toList();
        return ResponseEntity.ok(transactionDTOs);
    }

    @GetMapping(path = POINT_TRANSACTION_URI + "/{idCustomer}/transaction")
    public ResponseEntity<List<PointTransactionDTO>> getPointTransactionOfCustomer(@PathVariable Long idCustomer) {
        List<PointTransactionDTO> pointTransactionDTOs = transactionFinder.findPointTransactionsByCustomer(idCustomer).stream().map(TransactionController::convertPointTransactionToDto).toList();
        return ResponseEntity.ok(pointTransactionDTOs);
    }

    @GetMapping(path = TRANSACTION_URI)
    public ResponseEntity<List<TransactionDTO>> getTransactions() {
        return ResponseEntity.ok(transactionFinder.findAllTransactions()
                .stream()
                .map(TransactionController::convertTransactionToDto)
                .toList());
    }

    @GetMapping(path = POINT_TRANSACTION_URI)
    public ResponseEntity<List<PointTransactionDTO>> getPointTransactions() {
        return ResponseEntity.ok(transactionFinder.findAllPointTransactions()
                .stream()
                .map(TransactionController::convertPointTransactionToDto)
                .toList());
    }

    public static TransactionDTO convertTransactionToDto(Transaction transaction) {
        return new TransactionDTO(transaction.getId(), CustomerController.convertCustomerToDto(transaction.getCustomer()), transaction.getAmount(), transaction.getPaymentId());
    }

    public static PointTransactionDTO convertPointTransactionToDto(PointTransaction pointTransaction) {
        return new PointTransactionDTO(pointTransaction.getId(), CustomerController.convertCustomerToDto(pointTransaction.getCustomer()), pointTransaction.getPoints(), pointTransaction.getTrade());
    }
}
