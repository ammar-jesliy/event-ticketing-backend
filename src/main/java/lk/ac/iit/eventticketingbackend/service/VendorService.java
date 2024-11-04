package lk.ac.iit.eventticketingbackend.service;

import lk.ac.iit.eventticketingbackend.model.Customer;
import lk.ac.iit.eventticketingbackend.model.Vendor;
import lk.ac.iit.eventticketingbackend.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    public boolean isEmailAvailable(String email) {
        return !vendorRepository.existsByEmail(email);
    }

    public Vendor registerVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }
}
