/**
 * This package contains the repository interfaces for the Event Ticketing 
 * Backend application.
 * The repository interfaces are responsible for providing CRUD operations and 
 * custom queries for the application's data models.
 * 
 * The repository interfaces extend the MongoRepository interface provided by
 * Spring Data MongoDB.
 * 
 * 
 */
package lk.ac.iit.eventticketingbackend.repository;

import lk.ac.iit.eventticketingbackend.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {

}
