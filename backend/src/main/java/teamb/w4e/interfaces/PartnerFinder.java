package teamb.w4e.interfaces;

import teamb.w4e.entities.Partner;
import teamb.w4e.exceptions.IdNotFoundException;

import java.util.List;
import java.util.Optional;

public interface PartnerFinder {

    Optional<Partner> findByName(String name);

    Optional<Partner> findById(Long id);

    Partner retrievePartner(Long customerId) throws IdNotFoundException;

    List<Partner> findAll();

}
