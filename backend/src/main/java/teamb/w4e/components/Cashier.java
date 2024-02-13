package teamb.w4e.components;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Transaction;
import teamb.w4e.exceptions.NegativeAmountTransactionException;
import teamb.w4e.exceptions.PaymentException;
import teamb.w4e.interfaces.Bank;
import teamb.w4e.interfaces.Payment;
import teamb.w4e.repositories.TransactionRepository;

@Service
public class Cashier implements Payment {

    private final TransactionRepository transactionRepository;

    private final Bank bankProxy;


    public Cashier(TransactionRepository transactionRepository, Bank bankProxy) {
        this.transactionRepository = transactionRepository;
        this.bankProxy = bankProxy;
    }

    @Override
    @Transactional
    public Transaction createTransaction(Customer customer, double amount) throws NegativeAmountTransactionException, PaymentException {
        if (amount <= 0) {
            throw new NegativeAmountTransactionException(amount);
        }
        Transaction transaction = new Transaction(customer, amount);
        transactionRepository.save(transaction);
        if (bankProxy.pay(customer, amount).isEmpty()) {
            throw new PaymentException(customer.getName(), amount);
        }
        return transaction;
    }

}
