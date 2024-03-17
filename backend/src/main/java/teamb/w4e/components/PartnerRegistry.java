package teamb.w4e.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamb.w4e.entities.Partner;
import teamb.w4e.exceptions.AlreadyExistingException;
import teamb.w4e.exceptions.IdNotFoundException;
import teamb.w4e.interfaces.PartnerFinder;
import teamb.w4e.interfaces.PartnerRegistration;
import teamb.w4e.repositories.PartnerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PartnerRegistry implements PartnerRegistration, PartnerFinder {

    private final PartnerRepository partnerRepository;

    @Autowired // annotation is optional since Spring 4.3 if component has only one constructor
    public PartnerRegistry(PartnerRepository customerRepository) {
        this.partnerRepository = customerRepository;
    }

    @Override
    @Transactional
    public Partner register(String name, String creditCard)
            throws AlreadyExistingException {
        if (findByName(name).isPresent())
            throw new AlreadyExistingException(name);
        Partner newPartner = new Partner(name);
        return partnerRepository.save(newPartner);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Partner> findByName(String name) {
        return partnerRepository.findCustomerByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Partner> findById(Long id) {
        return partnerRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Partner retrievePartner(Long customerId) throws IdNotFoundException {
        return findById(customerId).orElseThrow(() -> new IdNotFoundException(customerId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Partner> findAll() {
        return partnerRepository.findAll();
    }

}
