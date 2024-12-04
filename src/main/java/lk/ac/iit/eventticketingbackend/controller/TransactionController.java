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

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public List<Transaction> fetchAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<?> getVendorTransactions(@PathVariable String vendorId) {
        try {
            List<Transaction> transactions = transactionService.getVendorTransactions(vendorId);

            if (transactions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "message", "No transactions found for vendorId: " + vendorId
                ));
            }

            return ResponseEntity.ok().body(transactions);
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "An error occurred while retrieving transactions: " + e.getMessage()
            ));
        }
    }

}
