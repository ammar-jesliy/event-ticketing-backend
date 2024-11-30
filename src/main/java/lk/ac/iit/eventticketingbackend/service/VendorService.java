package lk.ac.iit.eventticketingbackend.service;

import lk.ac.iit.eventticketingbackend.model.Ticket;
import lk.ac.iit.eventticketingbackend.model.TicketPool;
import lk.ac.iit.eventticketingbackend.model.Transaction;
import lk.ac.iit.eventticketingbackend.model.Vendor;
import lk.ac.iit.eventticketingbackend.repository.TicketPoolRepository;
import lk.ac.iit.eventticketingbackend.repository.TicketRepository;
import lk.ac.iit.eventticketingbackend.repository.TransactionRepository;
import lk.ac.iit.eventticketingbackend.repository.VendorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final TicketPoolRepository ticketPoolRepository;
    private final TicketRepository ticketRepository;
    private final TransactionRepository transactionRepository;

    public VendorService(VendorRepository vendorRepository, TicketPoolRepository ticketPoolRepository, TicketRepository ticketRepository, TransactionRepository transactionRepository) {
        this.vendorRepository = vendorRepository;
        this.ticketPoolRepository = ticketPoolRepository;
        this.ticketRepository = ticketRepository;
        this.transactionRepository = transactionRepository;
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

    public Vendor getVendorByEmail(String email) {
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

        // Initialize list for ticket IDs
        List<String> ticketIds = new ArrayList<>();

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

            // Save ticket in repository
            ticketRepository.save(ticket);

            ticketIds.add(ticket.getId());
        }

        // Create and store transaction details
        Transaction transaction = new Transaction();
        transaction.setVendorId(vendorId);
        transaction.setEventId(eventId);
        transaction.setTicketIds(ticketIds);
        transaction.setTransactionType("RELEASE");
        transaction.setPricePerTicket(price);
        transaction.setQuantity(numberOfTickets);
        transaction.setTimeStamp(LocalDateTime.now());

        transactionRepository.save(transaction);

        // Set total available tickets for sale and total tickets used in the ticket pool
        ticketPool.setTotalTickets(ticketPool.getTotalTickets() + numberOfTickets);
        ticketPool.setAvailableTickets(ticketPool.getAvailableTickets() + numberOfTickets);

        ticketPoolRepository.save(ticketPool);

        return true;
    }
}
