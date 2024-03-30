package teamb.w4e.interfaces;

import teamb.w4e.entities.transactions.PointTransaction;

import java.util.List;
import java.util.Optional;

public interface PointTransactionFinder {
    Optional<PointTransaction> findPointTransactionByCustomer(Long customerId);

    Optional<PointTransaction> findPointTransactionById(Long id);

    List<PointTransaction> findAllPointTransactions();

    List<PointTransaction> findPointTransactionsByCustomer(Long customerId);
}
