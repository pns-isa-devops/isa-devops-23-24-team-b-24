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

    @Autowired
    public PartnerRegistry(PartnerRepository customerRepository) {
        this.partnerRepository = customerRepository;
    }

    @Override
    @Transactional
    public Partner register(String name) throws AlreadyExistingException {
        if (findByName(name).isPresent())
            throw new AlreadyExistingException(name);
        Partner newPartner = new Partner(name);
        return partnerRepository.save(newPartner);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Partner> findByName(String name) {
        return partnerRepository.findPartnerByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Partner> findById(Long id) {
        return partnerRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Partner retrievePartner(Long partnerId) throws IdNotFoundException {
        return findById(partnerId).orElseThrow(() -> new IdNotFoundException(partnerId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Partner> findAll() {
        return partnerRepository.findAll();
    }

}
