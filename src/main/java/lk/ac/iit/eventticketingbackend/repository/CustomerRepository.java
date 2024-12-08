/**
 * This interface represents the repository for managing Customer entities.
 * It provides methods for performing CRUD operations on Customer data.
 * 
 * This interface extends the MongoRepository interface provided by Spring Data 
 * MongoDB.
 * 
 * Methods include:
 * - findCustomerByEmail (find customer by email)
 * - findCustomerById (find customer by id)
 * - existsByEmail (check if the customer exists by email)
 */
package lk.ac.iit.eventticketingbackend.repository;

import lk.ac.iit.eventticketingbackend.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    // Method to return customer by email
    Customer findCustomerByEmail(String email);

    // Method to return customer by id
    Customer findCustomerById(String customerId);

    // Method to check if the customer exists by email
    boolean existsByEmail(String email);
}
