package teamb.w4e.components;

import org.springframework.stereotype.Service;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.catalog.Leisure;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.items.Item;
import teamb.w4e.entities.items.ServiceItem;
import teamb.w4e.entities.transactions.PointTransaction;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.NegativeAmountTransactionException;
import teamb.w4e.interfaces.*;

import java.util.Set;

@Service
public class Applier implements AdvantageApplier {

    private final AdvantagePayment advantagePayment;
    private final PointAdder pointAdder;

    public Applier(AdvantagePayment advantagePayment, PointAdder pointAdder) {
        this.advantagePayment = advantagePayment;
        this.pointAdder = pointAdder;
    }

    @Override
    public PointTransaction apply(Customer customer, Advantage advantage, Leisure leisure) throws NegativeAmountTransactionException {
        PointTransaction pointTransaction = advantagePayment.payAdvantageFromCart(customer, advantage, advantage.getPartner());
        customer.getAdvantageCaddy().getAdvantages().remove(advantage);
        Set<Item> items = customer.getCaddy().getLeisure();
        items.add(new ServiceItem((new teamb.w4e.entities.catalog.Service(advantage.getPartner(), advantage.getName(), advantage.getType().getName(), 0.0))));
        return pointTransaction;
    }

    @Override
    public PointTransaction reduction(Customer customer, Advantage advantage, Leisure leisure) throws NegativeAmountTransactionException, IdNotFoundException {
        PointTransaction pointTransaction = advantagePayment.payAdvantageFromCart(customer, advantage, advantage.getPartner());
        customer.getAdvantageCaddy().getAdvantages().remove(advantage);
        Set<Item> items = customer.getCaddy().getLeisure();
        if (items.stream().anyMatch(item -> item.getLeisure().equals(leisure))) {
            items.stream()
                    .filter(item -> item.getLeisure().equals(leisure))
                    .findFirst()
                    .ifPresent(item -> item.getLeisure().setPrice(item.getLeisure().getPrice() - pointAdder.convertPointsToPrice(advantage.getPoints())));
            return pointTransaction;
        }
        throw new IdNotFoundException(leisure.getId());
    }
}
