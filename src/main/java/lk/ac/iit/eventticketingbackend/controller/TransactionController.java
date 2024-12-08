/**
 * This class is responsible for handling HTTP requests related to transactions.
 * 
 */
package lk.ac.iit.eventticketingbackend.controller;

import lk.ac.iit.eventticketingbackend.model.Transaction;
import lk.ac.iit.eventticketingbackend.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Fetches all transactions.
     *
     * @return a list of all transactions
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public List<Transaction> fetchAllTransactions() {
        return transactionService.getAllTransactions();
    }

    /**
     * Retrieves the transactions for a specific vendor.
     *
     * @param vendorId the ID of the vendor whose transactions are to be retrieved
     * @return a ResponseEntity containing the list of transactions for the
     *         specified vendor,
     *         or a NOT_FOUND status with a message if no transactions are
     *         found, or an INTERNAL_SERVER_ERROR status with an error message
     *         if an unexpected error occurs
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<?> getVendorTransactions(@PathVariable String vendorId) {
        try {
            List<Transaction> transactions = transactionService.getVendorTransactions(vendorId);

            if (transactions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "message", "No transactions found for vendorId: " + vendorId));
            }

            return ResponseEntity.ok().body(transactions);
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "An error occurred while retrieving transactions: " + e.getMessage()));
        }
    }

    /**
     * Retrieves the transactions for a specific customer.
     *
     * @param customerId the ID of the customer whose transactions are to be
     *                   retrieved
     * @return a ResponseEntity containing the list of transactions for the
     *         specified customer,
     *         or a NOT_FOUND status with a message if no transactions are
     *         found,
     *         or an INTERNAL_SERVER_ERROR status with an error message if an
     *         unexpected error occurs
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getCustomerTransactions(@PathVariable String customerId) {
        try {
            List<Transaction> transactions = transactionService.getCustomerTransactions(customerId);

            if (transactions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "message", "No transactions found for customerId: " + customerId));
            }

            return ResponseEntity.ok().body(transactions);
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "An error occurred while retrieving transactions: " + e.getMessage()));
        }
    }

}
