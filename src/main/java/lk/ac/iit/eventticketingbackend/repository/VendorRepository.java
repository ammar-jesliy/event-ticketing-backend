/**
 * Repository interface for managing Vendor entities.
 * This interface provides methods for performing CRUD operations on Vendor entities.
 * 
 * This interface extends the MongoRepository interface provided by Spring Data 
 * MongoDB.
 * 
 * Methods include:
 * - findVendorByEmail (find vendor by email)
 * - findVendorById (find vendor by id)
 * - existsByEmail (check if the vendor exists by email)
 * - deleteVendorById (delete vendor by id)
 * 
 * 
 */
package lk.ac.iit.eventticketingbackend.repository;

import lk.ac.iit.eventticketingbackend.model.Vendor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VendorRepository extends MongoRepository<Vendor, String> {
    // Method to return vendor by email
    Vendor findVendorByEmail(String email);

    // Method to return vendor by id
    Vendor findVendorById(String id);

    // Method to check if the vendor exists by email
    boolean existsByEmail(String email);

    // Method to delete vendor by id
    void deleteVendorById(String vendorId);

}
