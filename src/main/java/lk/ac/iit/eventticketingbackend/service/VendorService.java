/**
 * This class provides services related to vendors in the event ticketing 
 * backend system.
 * 
 * It contains methods to manage vendor-related operations such as creating, 
 * updating, and retrieving vendor information.
 * 
 * It is part of the service layer in the application architecture and is 
 * responsible for handling business logic related to vendors.
 * 
 */
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
import java.util.stream.Collectors;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final TicketPoolRepository ticketPoolRepository;
    private final TicketRepository ticketRepository;
    private final TransactionRepository transactionRepository;

    public VendorService(VendorRepository vendorRepository, TicketPoolRepository ticketPoolRepository,
            TicketRepository ticketRepository, TransactionRepository transactionRepository) {
        this.vendorRepository = vendorRepository;
        this.ticketPoolRepository = ticketPoolRepository;
        this.ticketRepository = ticketRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Retrieves a list of all vendors.
     *
     * @return a list of Vendor objects representing all vendors.
     */
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    /**
     * Retrieves a vendor by their unique identifier.
     *
     * @param vendorId the unique identifier of the vendor
     * @return the vendor associated with the given identifier
     */
    public Vendor getVendorById(String vendorId) {
        return vendorRepository.findVendorById(vendorId);
    }

    /**
     * Retrieves a list of vendors with names starting with "vendor".
     * 
     * These vendors are used for simulation purposes.
     *
     * @param limit the maximum number of vendors to return
     * @return a list of vendors whose names start with "vendor", limited to
     *         the specified number
     */
    public List<Vendor> getSimulationVendors(int limit) {
        List<Vendor> allVendors = vendorRepository.findAll();
        return allVendors.stream()
                .filter(vendor -> vendor.getName().startsWith("vendor"))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Checks if the given email is available for registration.
     *
     * @param email the email address to check for availability
     * @return true if the email is available, false if it is already in use
     */
    public boolean isEmailAvailable(String email) {
        return !vendorRepository.existsByEmail(email);
    }

    /**
     * Registers a new vendor by saving the vendor details to the repository.
     *
     * @param vendor the vendor object containing the details to be registered
     * @return the saved vendor object
     */
    public Vendor registerVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    /**
     * Authenticates a vendor based on the provided email and password.
     *
     * @param email    the email of the vendor to authenticate
     * @param password the password of the vendor to authenticate
     * 
     * @return true if the vendor is authenticated successfully, false otherwise
     */
    public boolean authenticate(String email, String password) {
        Vendor vendor = vendorRepository.findVendorByEmail(email);
        if (vendor != null) {
            return vendor.getPassword().equals(password);
        }
        return false;
    }

    /**
     * Updates the profile of an existing vendor.
     *
     * @param vendor the vendor object containing updated information
     * @return the updated vendor object after saving changes to the repository
     */
    public Vendor updateVendorProfile(Vendor vendor) {
        Vendor existingVendor = vendorRepository.findVendorByEmail(vendor.getEmail());

        existingVendor.setName(vendor.getName());
        existingVendor.setEmail(vendor.getEmail());
        existingVendor.setPassword(vendor.getPassword());
        existingVendor.setReleaseRate(vendor.getReleaseRate());

        return vendorRepository.save(existingVendor);

    }

    /**
     * Retrieves a vendor by their email address.
     *
     * @param email the email address of the vendor to retrieve
     * 
     * @return the vendor associated with the given email address, or null if
     *         no vendor is found
     */
    public Vendor getVendorByEmail(String email) {
        return vendorRepository.findVendorByEmail(email);
    }

    // Handles ticket release from vendors
    /**
     * Releases a specified number of tickets for an event by a vendor.
     * 
     * The tickets are added to the ticket pool for the event, and a transaction
     * record is created to track the release.
     * 
     * This method performs the following steps:
     * 1. Checks if the event exists in the ticket pool repository.
     * 2. Checks if the number of tickets to be released exceeds the available
     * capacity of the ticket pool.
     * 3. Creates the specified number of tickets and adds them to the pool.
     * 4. Saves the tickets to the ticket repository.
     * 5. Creates a transaction record for the release.
     * 6. Saves the transaction and ticket pool to the repositories.
     * 
     *
     * @param eventId         the ID of the event for which tickets are being
     *                        released
     * @param vendorId        the ID of the vendor releasing the tickets
     * @param numberOfTickets the number of tickets to be released
     * @param price           the price of each ticket
     * 
     * @return true if the tickets were successfully released, false otherwise
     * 
     * @throws IllegalArgumentException if the event is not found
     * 
     */
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

        ticketPoolRepository.save(ticketPool);

        return true;
    }

    /**
     * Deletes a vendor and its associated transactions by the given vendor ID.
     *
     * @param vendorId the ID of the vendor to be deleted
     */
    public void deleteVendorById(String vendorId) {
        vendorRepository.deleteVendorById(vendorId);
        transactionRepository.deleteTransactionsByVendorId(vendorId);
    }
}
