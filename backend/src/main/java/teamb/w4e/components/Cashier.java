package teamb.w4e.components;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Transaction;
import teamb.w4e.exceptions.CustomerIdNotFoundException;
import teamb.w4e.exceptions.NegativeAmountTransactionException;
import teamb.w4e.exceptions.PaymentException;
import teamb.w4e.interfaces.Bank;
import teamb.w4e.interfaces.CustomerFinder;
import teamb.w4e.interfaces.Payment;
import teamb.w4e.interfaces.TransactionCreator;

@Service
public class Cashier implements Payment {
    private final Bank bankProxy;
    private final CustomerFinder finder;
    private final TransactionCreator transactionCreator;

    @Autowired
    public Cashier(Bank bankProxy, CustomerFinder finder, TransactionCreator transactionCreator) {
        this.bankProxy = bankProxy;
        this.finder = finder;
        this.transactionCreator = transactionCreator;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Transaction pay(Customer customer, double amount) throws PaymentException, CustomerIdNotFoundException, NegativeAmountTransactionException {
        if (finder.findById(customer.getId()).isEmpty()) {
            throw new CustomerIdNotFoundException(customer.getId());
        }
        if (amount < 0) {
            throw new NegativeAmountTransactionException(amount);
        }
        String payment = bankProxy.pay(customer, amount).orElseThrow(() -> new PaymentException(customer.getName(), amount));
        return transactionCreator.createTransaction(customer, amount, payment);
    }
}
