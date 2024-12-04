package lk.ac.iit.eventticketingbackend.repository;

import lk.ac.iit.eventticketingbackend.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findTransactionsByVendorId(String vendorId);
}
