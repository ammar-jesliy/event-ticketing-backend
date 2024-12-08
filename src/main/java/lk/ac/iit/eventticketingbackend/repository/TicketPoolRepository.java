/**
 * Repository interface for managing TicketPool entities.
 * This interface provides methods for performing CRUD operations on TicketPool entities.
 * 
 * This interface extends the MongoRepository interface provided by Spring Data MongoDB.
 * 
 * Methods include:
 * - findTicketPoolByEventId (find ticket pool by event id)
 * 
 */
package lk.ac.iit.eventticketingbackend.repository;

import lk.ac.iit.eventticketingbackend.model.TicketPool;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TicketPoolRepository extends MongoRepository<TicketPool, String> {
    // Method to return ticket pool by event id
    TicketPool findTicketPoolByEventId(String eventId);
}
