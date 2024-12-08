/**
 * This class provides services related to transactions in the event ticketing backend.
 * 
 * It contains methods to handle various transaction-related operations.
 * 
 * It is part of the service layer in the application architecture and is 
 * responsible for handling business logic related to transactions.
 * 
 */
package lk.ac.iit.eventticketingbackend.service;

import lk.ac.iit.eventticketingbackend.model.Transaction;
import lk.ac.iit.eventticketingbackend.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Retrieves all transactions from the repository.
     *
     * @return a list of all transactions
     */
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    /**
     * Retrieves a list of transactions associated with a specific vendor.
     *
     * @param vendorId the unique identifier of the vendor
     * @return a list of transactions for the specified vendor
     */
    public List<Transaction> getVendorTransactions(String vendorId) {
        return transactionRepository.findTransactionsByVendorId(vendorId);
    }

    /**
     * Retrieves a list of transactions for a specific customer.
     *
     * @param customerId the ID of the customer whose transactions are to be
     *                   retrieved
     * 
     * @return a list of transactions associated with the specified customer
     */
    public List<Transaction> getCustomerTransactions(String customerId) {
        return transactionRepository.findTransactionsByCustomerId(customerId);
    }

}
