package teamb.w4e.components;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.catalog.Leisure;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.items.AdvantageItem;
import teamb.w4e.entities.items.Item;
import teamb.w4e.entities.items.ServiceItem;
import teamb.w4e.entities.reservations.ReservationType;
import teamb.w4e.entities.transactions.PointTransaction;
import teamb.w4e.exceptions.AlreadyExistingException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.NegativeAmountTransactionException;
import teamb.w4e.exceptions.NotFoundException;
import teamb.w4e.interfaces.*;
import teamb.w4e.interfaces.leisure.ServiceFinder;
import teamb.w4e.interfaces.leisure.ServiceRegistration;

import java.util.Optional;
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
    @Transactional
    public PointTransaction apply(Customer customer, Advantage advantage, Item item) throws NegativeAmountTransactionException, AlreadyExistingException, IdNotFoundException {
        PointTransaction pointTransaction = advantagePayment.payAdvantageFromCart(customer, advantage, advantage.getPartner());
        Set<Item> items = customer.getCaddy().getCatalogItem();
        AdvantageItem advantageItem = items.stream()
                .filter(i -> i.getType().equals(ReservationType.NONE))
                .map(AdvantageItem.class::cast)
                .filter(i -> i.getAdvantage().equals(advantage))
                .findFirst().orElseThrow(() -> new IdNotFoundException(advantage.getId()));
        items.remove(advantageItem);
        teamb.w4e.entities.catalog.Service service = serviceFinder.findServiceByName(advantage.getName())
                .orElse(serviceRegistration.registerService(advantage.getPartner().getId(), advantage.getName(), advantage.getType().getName(), 0.0));
        items.add(new ServiceItem(service));
        return pointTransaction;
    }

    @Override
    @Transactional
    public PointTransaction reduction(Customer customer, Advantage advantage, Item item) throws NegativeAmountTransactionException, IdNotFoundException, NotFoundException {
        PointTransaction pointTransaction = advantagePayment.payAdvantageFromCart(customer, advantage, advantage.getPartner());
        Set<Item> items = customer.getCaddy().getCatalogItem();
        AdvantageItem advantageItem = items.stream()
                .filter(i -> i.getType().equals(ReservationType.NONE))
                .map(AdvantageItem.class::cast)
                .filter(i -> i.getAdvantage().equals(advantage))
                .findFirst().orElseThrow(() -> new IdNotFoundException(advantage.getId()));
        items.remove(advantageItem);
        if (items.stream().anyMatch(i -> i.getName().equals(item.getName()))) {
            items.stream()
                    .filter(i -> i.getName().equals(item.getName()))
                    .findFirst()
                    .ifPresent(i -> item.setAmount(item.getAmount() - pointAdder.convertPointsToPrice(advantage.getPoints())));
            return pointTransaction;
        }
        throw new NotFoundException("Item not found in cart");
    }
}
