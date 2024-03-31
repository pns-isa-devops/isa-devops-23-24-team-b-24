package teamb.w4e.components;

import org.springframework.stereotype.Service;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.catalog.Leisure;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.items.Item;
import teamb.w4e.entities.items.ServiceItem;
import teamb.w4e.entities.transactions.PointTransaction;
import teamb.w4e.exceptions.AlreadyExistingException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.NegativeAmountTransactionException;
import teamb.w4e.interfaces.*;
import teamb.w4e.interfaces.leisure.ServiceFinder;
import teamb.w4e.interfaces.leisure.ServiceRegistration;

import java.util.Set;

@Service
public class Applier implements AdvantageApplier {

    private final AdvantagePayment advantagePayment;
    private final PointAdder pointAdder;
    private final ServiceRegistration serviceRegistration;
    private final ServiceFinder serviceFinder;

    public Applier(AdvantagePayment advantagePayment, PointAdder pointAdder, ServiceRegistration serviceRegistration, ServiceFinder serviceFinder) {
        this.advantagePayment = advantagePayment;
        this.pointAdder = pointAdder;
        this.serviceRegistration = serviceRegistration;
        this.serviceFinder = serviceFinder;
    }

    @Override
    public PointTransaction apply(Customer customer, Advantage advantage, Leisure leisure) throws NegativeAmountTransactionException, AlreadyExistingException, IdNotFoundException {
        PointTransaction pointTransaction = advantagePayment.payAdvantageFromCart(customer, advantage, advantage.getPartner());
        customer.getAdvantageCaddy().getAdvantages().remove(advantage);
        Set<Item> items = customer.getCaddy().getLeisure();
        teamb.w4e.entities.catalog.Service service = serviceFinder.findServiceByName(advantage.getName())
                .orElse(serviceRegistration.registerService(advantage.getPartner().getId(), advantage.getName(), advantage.getType().getName(), 0.0));
        items.add(new ServiceItem(service));
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
