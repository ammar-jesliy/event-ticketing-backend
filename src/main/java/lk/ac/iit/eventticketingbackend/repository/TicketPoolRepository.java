package lk.ac.iit.eventticketingbackend.repository;

import lk.ac.iit.eventticketingbackend.model.TicketPool;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface TicketPoolRepository extends MongoRepository<TicketPool, String> {
    List<TicketPool> findByEventId(String eventId);
}
