/**
 * This interface represents the repository for handling transactions in the event ticketing backend.
 * It provides methods for performing CRUD operations on transaction entities.
 * 
 * This interface extends the MongoRepository interface provided by Spring Data 
 * MongoDB.
 * 
 * Methods include:
 * - findTransactionsByVendorId (find transactions by vendor id)
 * - findTransactionsByCustomerId (find transactions by customer id)
 * - deleteTransactionsByVendorId (delete transactions by vendor id)
 * 
 * 
 */
package lk.ac.iit.eventticketingbackend.repository;

import lk.ac.iit.eventticketingbackend.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    // Method to return transactions by vendor id
    List<Transaction> findTransactionsByVendorId(String vendorId);

    // Method to return transactions by customer id
    List<Transaction> findTransactionsByCustomerId(String customerId);

    // Method to delete transactions by vendor id
    void deleteTransactionsByVendorId(String vendorId);
}
