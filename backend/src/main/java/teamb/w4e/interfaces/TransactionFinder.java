package teamb.w4e.interfaces;

import teamb.w4e.entities.transactions.PointTransaction;
import teamb.w4e.entities.transactions.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionFinder {

    Optional<Transaction> findTransactionByCustomer(Long customerId);

    Optional<Transaction> findTransactionById(Long id);

    List<Transaction> findAllTransactions();

    List<Transaction> findTransactionsByCustomer(Long customerId);

    Optional<PointTransaction> findPointTransactionByCustomer(Long customerId);

    Optional<PointTransaction> findPointTransactionById(Long id);

    List<PointTransaction> findAllPointTransactions();

    List<PointTransaction> findPointTransactionsByCustomer(Long customerId);
}
