package lk.ac.iit.eventticketingbackend.repository;

import lk.ac.iit.eventticketingbackend.model.TicketPool;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface TicketPoolRepository extends MongoRepository<TicketPool, String> {
    TicketPool findTicketPoolByEventId(String eventId);
}
