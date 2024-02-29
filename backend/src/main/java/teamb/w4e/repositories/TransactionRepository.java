package teamb.w4e.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Transaction;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.customer.id = :customerId")
    Optional<Transaction> findTransactionByCustomer(@Param("customerId") Long customerId);

    @Query("SELECT t FROM Transaction t WHERE t.customer.id = :customerId")
    List<Transaction> findTransactionsByCustomer(@Param("customerId") Long customerId);
}
