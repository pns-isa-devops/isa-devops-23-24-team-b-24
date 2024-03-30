package teamb.w4e.repositories.transactions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import teamb.w4e.entities.transactions.PointTransaction;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {

    @Query("SELECT t FROM PointTransaction t WHERE t.customer.id = :customerId")
    Optional<PointTransaction> findPointTransactionByCustomer(@Param("customerId") Long customerId);

    @Query("SELECT t FROM PointTransaction t WHERE t.customer.id = :customerId")
    List<PointTransaction> findPointTransactionsByCustomer(@Param("customerId") Long customerId);
}
