package teamb.w4e.interfaces;

import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionFinder {

    Optional<Transaction> findTransactionByCustomer(Customer customer);

    Optional<Transaction> findTransactionById(Long id);

    List<Transaction> findAllTransactions();
}
