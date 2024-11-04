package lk.ac.iit.eventticketingbackend.repository;

import lk.ac.iit.eventticketingbackend.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    // Method to return customer by email
    Optional<Customer> findCustomerByEmail(String email);

    // Method to check if the customer exists by email
    boolean existsByEmail(String email);
}
