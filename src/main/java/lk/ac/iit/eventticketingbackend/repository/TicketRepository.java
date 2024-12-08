/**
 * This package contains the repository interfaces for the event ticketing 
 * backend application.
 * The repository interfaces are responsible for providing CRUD operations and 
 * custom queries for the application's data models.
 * 
 * The repository interfaces extend the MongoRepository interface provided by
 * Spring Data MongoDB.
 * 
 * Methods include:
 * - findTicketsByVendorId (find tickets by vendor id)
 * 
 * 
 */
package lk.ac.iit.eventticketingbackend.repository;

import lk.ac.iit.eventticketingbackend.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    // Method to return tickets by vendor id
    List<Ticket> findTicketsByVendorId(String vendorId);
}
