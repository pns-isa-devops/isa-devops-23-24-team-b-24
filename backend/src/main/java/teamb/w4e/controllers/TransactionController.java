package teamb.w4e.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import teamb.w4e.dto.*;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Group;
import teamb.w4e.entities.PointTransaction;
import teamb.w4e.entities.Transaction;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.group.NotEnoughException;
import teamb.w4e.interfaces.CustomerFinder;
import teamb.w4e.interfaces.GroupFinder;
import teamb.w4e.interfaces.TradeCreator;
import teamb.w4e.interfaces.TransactionFinder;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = CustomerCareController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class TransactionController {

    public static final String TRANSACTION_URI = "/transactions";
    public static final String POINT_TRANSACTION = "/point-transactions";
    private final TransactionFinder transactionFinder;
    private final CustomerFinder customerFinder;
    private final GroupFinder groupFinder;
    private final TradeCreator tradeCreator;

    @Autowired
    public TransactionController(TransactionFinder transactionFinder, CustomerFinder customerFinder,
                                 GroupFinder groupFinder, TradeCreator tradeCreator) {
        this.transactionFinder = transactionFinder;
        this.customerFinder = customerFinder;
        this.groupFinder = groupFinder;
        this.tradeCreator = tradeCreator;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorDTO handleExceptions(MethodArgumentNotValidException e) {
        return new ErrorDTO("Cannot process Transaction information", e.getMessage());
    }

    @GetMapping(path = TRANSACTION_URI + "/{idCustomer}/transaction")
    public ResponseEntity<List<TransactionDTO>> getTransactionOfCustomer(@PathVariable Long idCustomer) throws IdNotFoundException {
        CustomerDTO customer = CustomerCareController.convertCustomerToDto(customerFinder.retrieveCustomer(idCustomer));
        List<TransactionDTO> transactionDTOs = transactionFinder.findTransactionsByCustomer(customer.id()).stream().map(TransactionController::convertTransactionToDto).toList();
        return ResponseEntity.ok(transactionDTOs);
    }

    @GetMapping(path = POINT_TRANSACTION + "/{idCustomer}/transaction")
    public ResponseEntity<List<PointTransactionDTO>> getPointTransactionOfCustomer(@PathVariable Long idCustomer) throws IdNotFoundException {
        CustomerDTO customer = CustomerCareController.convertCustomerToDto(customerFinder.retrieveCustomer(idCustomer));
        List<PointTransactionDTO> pointTransactionDTOs = transactionFinder.findPointTransactionsByCustomer(customer.id()).stream().map(TransactionController::convertPointTransactionToDto).toList();
        return ResponseEntity.ok(pointTransactionDTOs);
    }

    @GetMapping(path = TRANSACTION_URI)
    public ResponseEntity<List<TransactionDTO>> getTransactions() {
        return ResponseEntity.ok(transactionFinder.findAllTransactions()
                .stream()
                .map(TransactionController::convertTransactionToDto)
                .toList());
    }

    @GetMapping(path = POINT_TRANSACTION)
    public ResponseEntity<List<PointTransactionDTO>> getPointTransactions() {
        return ResponseEntity.ok(transactionFinder.findAllPointTransactions()
                .stream()
                .map(TransactionController::convertPointTransactionToDto)
                .toList());
    }

    @PostMapping(path = "/groups/trade", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PointTransactionDTO> tradePoints(@RequestBody @Valid PointTradeDTO pointTradeDTO) throws IdNotFoundException, NotEnoughException {
        List<Group> groups = groupFinder.findAll();
        Customer sender = customerFinder.retrieveCustomer(pointTradeDTO.sender().id());
        Customer receiver = customerFinder.retrieveCustomer(pointTradeDTO.receiver().id());
        boolean areInSameGroup = groups.stream()
                .anyMatch(group -> {
                    boolean isSenderInGroup = group.getLeader().equals(sender) || group.getMembers().contains(sender);
                    boolean isReceiverInGroup = group.getLeader().equals(receiver) || group.getMembers().contains(receiver);
                    return isSenderInGroup && isReceiverInGroup;
                });
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(convertPointTransactionToDto(tradeCreator.createTrade(sender, receiver, pointTradeDTO.points(), areInSameGroup)));

    }

    public static TransactionDTO convertTransactionToDto(Transaction transaction) {
        return new TransactionDTO(transaction.getId(), CustomerCareController.convertCustomerToDto(transaction.getCustomer()), transaction.getAmount(), transaction.getPaymentId());
    }

    public static PointTransactionDTO convertPointTransactionToDto(PointTransaction pointTransaction) {
        return new PointTransactionDTO(pointTransaction.getId(), CustomerCareController.convertCustomerToDto(pointTransaction.getCustomer()), pointTransaction.getPoints(), pointTransaction.getTrade());
    }
}
