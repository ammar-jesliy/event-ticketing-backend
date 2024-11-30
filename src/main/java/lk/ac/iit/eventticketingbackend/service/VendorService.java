package lk.ac.iit.eventticketingbackend.service;

import lk.ac.iit.eventticketingbackend.model.Ticket;
import lk.ac.iit.eventticketingbackend.model.TicketPool;
import lk.ac.iit.eventticketingbackend.model.Vendor;
import lk.ac.iit.eventticketingbackend.repository.TicketPoolRepository;
import lk.ac.iit.eventticketingbackend.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final TicketPoolRepository ticketPoolRepository;

    public VendorService(VendorRepository vendorRepository, TicketPoolRepository ticketPoolRepository) {
        this.vendorRepository = vendorRepository;
        this.ticketPoolRepository = ticketPoolRepository;
    }

    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    public boolean isEmailAvailable(String email) {
        return !vendorRepository.existsByEmail(email);
    }

    public Vendor registerVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    public boolean authenticate(String email, String password) {
        Vendor vendor = vendorRepository.findVendorByEmail(email);
        if (vendor != null) {
            return vendor.getPassword().equals(password);
        }
        return false;
    }

    public Vendor updateVendorProfile(Vendor vendor) {
        Vendor existingVendor = vendorRepository.findVendorByEmail(vendor.getEmail());

        existingVendor.setName(vendor.getName());
        existingVendor.setEmail(vendor.getEmail());
        existingVendor.setPassword(vendor.getPassword());
        existingVendor.setReleaseRate(vendor.getReleaseRate());

        return vendorRepository.save(existingVendor);

    }

    public Vendor getVendorByEmail(String email){
        return vendorRepository.findVendorByEmail(email);
    }


    // Handles ticket release from vendors
    public boolean releaseTicket(String eventId, String vendorId, int numberOfTickets, double price) {
        TicketPool ticketPool = ticketPoolRepository.findTicketPoolByEventId(eventId);

        if (ticketPool == null) {
            throw new IllegalArgumentException("Event not found");
        }

        if (numberOfTickets > (ticketPool.getMaxTicketCapacity() - ticketPool.getTotalTickets())) {
            return false;
        }

        // Create and add tickets to the pool
        for (int i = 0; i < numberOfTickets; i++) {
            Ticket ticket = new Ticket();
            ticket.setVendorId(vendorId);
            ticket.setEventId(eventId);
            ticket.setPrice(price);
            ticket.setAvailable(true);

            boolean success = ticketPool.addTicket(ticket);

            if (!success) {
                return false;
            }
        }

        // Set total available tickets for sale and total tickets used in the ticket pool
        ticketPool.setTotalTickets(ticketPool.getTotalTickets() + numberOfTickets);
        ticketPool.setAvailableTickets(ticketPool.getAvailableTickets() + numberOfTickets);

        ticketPoolRepository.save(ticketPool);

        return true;
    }
}
