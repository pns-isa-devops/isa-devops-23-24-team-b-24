package teamb.w4e.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamb.w4e.dto.AppliedAdvantageDTO;
import teamb.w4e.dto.PointTransactionDTO;
import teamb.w4e.entities.catalog.Advantage;
import teamb.w4e.entities.catalog.AdvantageType;
import teamb.w4e.entities.customers.Customer;
import teamb.w4e.entities.items.AdvantageItem;
import teamb.w4e.entities.items.Item;
import teamb.w4e.exceptions.AlreadyExistingException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.exceptions.NegativeAmountTransactionException;
import teamb.w4e.exceptions.NotFoundException;
import teamb.w4e.interfaces.AdvantageApplier;
import teamb.w4e.interfaces.CustomerFinder;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = CustomerController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class PointController {

    private final CustomerFinder customerFinder;
    private final AdvantageApplier advantageApplier;

    public PointController(CustomerFinder customerFinder, AdvantageApplier advantageApplier) {
        this.customerFinder = customerFinder;
        this.advantageApplier = advantageApplier;
    }

    @PostMapping(path = "/{customerId}/using-advantages", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PointTransactionDTO> useAdvantage(@PathVariable("customerId") Long customerId, @RequestBody @Valid AppliedAdvantageDTO appliedAdvantageDTO) throws IdNotFoundException, NegativeAmountTransactionException, AlreadyExistingException, NotFoundException {
        Customer customer = customerFinder.retrieveCustomer(customerId);
        Advantage advantage = customer.getCaddy().getCatalogItem().stream()
                .filter(item -> item.getName().equals(appliedAdvantageDTO.advantage().name()))
                .findFirst()
                .map(AdvantageItem.class::cast)
                .orElseThrow(() -> new IdNotFoundException(appliedAdvantageDTO.advantage().id())).getAdvantage();
        Item item = customer.getCaddy().getCatalogItem().stream()
                .filter(i -> i.getName().equals(appliedAdvantageDTO.leisure().name()))
                .findFirst()
                .map(Item.class::cast)
                .orElseThrow(() -> new IdNotFoundException(appliedAdvantageDTO.leisure().id()));

        if (appliedAdvantageDTO.advantage().type().equals(AdvantageType.REDUCTION)) {
            return ResponseEntity.ok()
                    .body(TransactionController.convertPointTransactionToDto(advantageApplier.reduction(customer, advantage, item)));
        } else
            return ResponseEntity.ok(
                    TransactionController.convertPointTransactionToDto(advantageApplier.apply(customer, advantage, item)));
    }


}
