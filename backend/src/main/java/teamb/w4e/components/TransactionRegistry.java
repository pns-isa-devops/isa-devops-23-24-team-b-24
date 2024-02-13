package teamb.w4e.components;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Transaction;
import teamb.w4e.interfaces.TransactionFinder;
import teamb.w4e.repositories.TransactionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionRegistry implements TransactionFinder {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionRegistry(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<Transaction> findTransactionByCustomer(Customer customer) {
        return transactionRepository.findTransactionByCustomer(customer);
    }

    @Override
    public Optional<Transaction> findTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }
}
