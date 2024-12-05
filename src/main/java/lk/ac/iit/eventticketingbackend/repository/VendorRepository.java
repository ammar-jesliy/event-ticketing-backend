package lk.ac.iit.eventticketingbackend.repository;

import lk.ac.iit.eventticketingbackend.model.Vendor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VendorRepository extends MongoRepository<Vendor, String> {
    Vendor findVendorByEmail(String email);

    Vendor findVendorById(String id);

    boolean existsByEmail(String email);

}
