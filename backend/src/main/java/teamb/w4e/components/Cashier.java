package teamb.w4e.components;

import teamb.w4e.entities.Customer;
import teamb.w4e.entities.Order;
import teamb.w4e.exceptions.PaymentException;
import teamb.w4e.interfaces.Bank;
import teamb.w4e.interfaces.OrderCooking;
import teamb.w4e.interfaces.OrderCreator;
import teamb.w4e.interfaces.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Cashier implements Payment {

    private final Bank bank;

    private final OrderCreator orderer;

    private final OrderCooking kitchen;

    @Autowired
    public Cashier(Bank bank, OrderCreator orderer, OrderCooking orderCooking) {
        this.bank = bank;
        this.orderer = orderer;
        this.kitchen = orderCooking;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Order payOrderFromCart(Customer customer, double price) throws PaymentException {
        String paymentReceiptId  = bank.pay(customer, price).orElseThrow(() -> new PaymentException(customer.getName(), price));
        Order order = orderer.createOrder(customer, price, paymentReceiptId);
        return kitchen.processInKitchen(order);
    }

}
