package teamb.w4e.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Transaction;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findTransactionByCustomer(Customer customer);
    List<Transaction> findTransactionsByCustomer(Customer customer);
}
