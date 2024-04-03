package teamb.w4e.components;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.Partner;
import teamb.w4e.entities.transactions.PointTransaction;
import teamb.w4e.entities.transactions.Transaction;
import teamb.w4e.interfaces.*;
import teamb.w4e.repositories.transactions.PointTransactionRepository;
import teamb.w4e.repositories.transactions.TransactionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionRegistry implements TransactionFinder, TransactionCreator {

    private final TransactionRepository transactionRepository;

    private final PointTransactionRepository pointTransactionRepository;

    @Autowired
    public TransactionRegistry(TransactionRepository transactionRepository, PointTransactionRepository pointTransactionRepository) {
        this.transactionRepository = transactionRepository;
        this.pointTransactionRepository = pointTransactionRepository;
    }

    @Override
    public Optional<Transaction> findTransactionByCustomer(Long customerId) {
        return transactionRepository.findTransactionByCustomer(customerId);
    }

    @Override
    public Optional<PointTransaction> findPointTransactionByCustomer(Long customerId) {
        return pointTransactionRepository.findPointTransactionByCustomer(customerId);
    }

    @Override
    public Optional<Transaction> findTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Optional<PointTransaction> findPointTransactionById(Long id) {
        return pointTransactionRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<PointTransaction> findAllPointTransactions() {
        return pointTransactionRepository.findAll();
    }

    @Override
    public List<Transaction> findTransactionsByCustomer(Long customerId) {
        return transactionRepository.findTransactionsByCustomer(customerId);
    }

    @Override
    public List<PointTransaction> findPointTransactionsByCustomer(Long customerId) {
        return pointTransactionRepository.findPointTransactionsByCustomer(customerId);
    }

    @Override
    public Transaction createTransaction(Customer customer, double amount, String payReceiptId) {
        Transaction transaction = new Transaction(customer, amount, payReceiptId);
        return transactionRepository.save(transaction);
    }

    @Override
    public PointTransaction createPointTransactionWithPartner(Customer customer, int amount, Partner issuer) {
        PointTransaction pointTransaction = new PointTransaction(customer, amount, issuer.getName());
        return pointTransactionRepository.save(pointTransaction);
    }

    @Override
    public PointTransaction createPointTransaction(Customer sender, Customer receiver, int amount) {
        int points = receiver.getCard().getPoints();
        PointTransaction senderTransaction = new PointTransaction(sender, amount, "Give " + amount + " points to " + receiver.getName());
        int tradePoints =  receiver.getCard().getPoints()  - points;
        PointTransaction receiverTransaction = new PointTransaction(receiver, amount, "Receive " + tradePoints + " points from " + sender.getName());
        pointTransactionRepository.save(senderTransaction);
        pointTransactionRepository.save(receiverTransaction);
        return senderTransaction;
    }
}
