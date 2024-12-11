/**
 * This service class provides methods to manage customer-related operations
 * in the event ticketing backend system.
 * 
 * It is part of the service layer in the application architecture and is
 * responsible for handling business logic related to customers.
 * 
 */
package lk.ac.iit.eventticketingbackend.service;

import lk.ac.iit.eventticketingbackend.model.*;
import lk.ac.iit.eventticketingbackend.repository.CustomerRepository;
import lk.ac.iit.eventticketingbackend.repository.TicketPoolRepository;
import lk.ac.iit.eventticketingbackend.repository.TicketRepository;
import lk.ac.iit.eventticketingbackend.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final TicketPoolRepository ticketPoolRepository;
    private final TicketRepository ticketRepository;
    private final TransactionRepository transactionRepository;

    public CustomerService(CustomerRepository customerRepository, TicketPoolRepository ticketPoolRepository,
            TicketRepository ticketRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.ticketPoolRepository = ticketPoolRepository;
        this.ticketRepository = ticketRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Retrieves a list of all customers from the repository.
     *
     * @return a list of all customers
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * Retrieves a customer by their unique identifier.
     *
     * @param customerId the unique identifier of the customer to retrieve
     * @return the customer with the specified identifier, or null if no such
     *         customer exists
     */
    public Customer getCustomerById(String customerId) {
        return customerRepository.findCustomerById(customerId);
    }

    /**
     * Retrieves a list of customers with names starting with "customer".
     * 
     * These customers are used for simulation purposes.
     *
     * @param limit the maximum number of customers to return
     * @return a list of customers whose names start with "customer", limited
     *         to the specified number
     */
    public List<Customer> getSimulationCustomers(int limit) {
        List<Customer> allCustomers = customerRepository.findAll();
        return allCustomers.stream()
                .filter(customer -> customer.getName().startsWith("customer"))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Checks if the given email is available for registration.
     *
     * @param email the email address to check
     * @return true if the email is available, false if it is already in use
     */
    public boolean isEmailAvailable(String email) {
        return !customerRepository.existsByEmail(email);
    }

    /**
     * Registers a new customer by saving the customer details to the repository.
     *
     * @param customer the customer object containing the details to be
     *                 registered
     * @return the saved customer object
     */
    public Customer registerCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    /**
     * Authenticates a customer based on the provided email and password.
     *
     * @param email    the email of the customer to authenticate
     * @param password the password of the customer to authenticate
     * @return true if the email and password match a customer in the
     *         repository, false otherwise
     */
    public boolean authenticate(String email, String password) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer != null) {
            return customer.getPassword().equals(password);
        }
        return false;
    }

    /**
     * Updates the profile of an existing customer with the provided customer
     * details.
     *
     * @param customer the customer object containing updated details
     * @return the updated customer object after saving to the repository
     */
    public Customer updateCustomerProfile(Customer customer) {
        Customer existingCustomer = customerRepository.findCustomerByEmail(customer.getEmail());

        existingCustomer.setName(customer.getName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPassword(customer.getPassword());
        existingCustomer.setPurchaseRate(customer.getPurchaseRate());
        existingCustomer.setVipPoints(customer.getVipPoints());
        existingCustomer.setDiscountRate(customer.getDiscountRate());

        return customerRepository.save(existingCustomer);

    }

    /**
     * Retrieves a customer by their email address.
     *
     * @param email the email address of the customer to retrieve
     * @return the customer associated with the given email address
     */
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findCustomerByEmail(email);
    }

    /**
     * Buys a specified number of tickets for a given event and customer.
     * 
     * This method also creates transaction for each call.
     * 
     *
     * @param eventId         the ID of the event for which tickets are being
     *                        purchased
     * @param customerId      the ID of the customer purchasing the tickets
     * @param numberOfTickets the number of tickets to purchase
     * 
     * @return a list of purchased tickets
     * 
     * @throws IllegalArgumentException if the event is not found
     * @throws IllegalStateException    if there are not enough tickets available
     *                                  in the ticket pool
     */
    public List<Ticket> buyTicket(String eventId, String customerId, int numberOfTickets) {
        TicketPool ticketPool = ticketPoolRepository.findTicketPoolByEventId(eventId);
        Customer customer = customerRepository.findCustomerById(customerId);

        int customerVipPoints = customer.getVipPoints();
        int discountRate = 0;

        if (customerVipPoints > 300) {
            discountRate = 30;
        } else if (customerVipPoints > 200) {
            discountRate = 20;
        } else if (customerVipPoints > 100) {
            discountRate = 10;
        }

        if (ticketPool == null) {
            throw new IllegalArgumentException("Event not found");
        }

        if (ticketPool.getAvailableTickets() < numberOfTickets) {
            throw new IllegalStateException("Not enough tickets available in Ticket pool");
        }

        // Initialize array to store all sold tickets
        List<Ticket> soldTickets = new ArrayList<>();

        // Initialize list for ticket IDs
        Map<String, List<Ticket>> ticketsByVendor = new HashMap<>();

        for (int i = 0; i < numberOfTickets; i++) {
            Ticket soldTicket = ticketPool.buyTicket(customerId);

            soldTicket.setPrice(soldTicket.getPrice() * ((double) (100 - discountRate) / 100));

            ticketRepository.save(soldTicket);

            soldTickets.add(soldTicket);

            // Group Tickets by vendorId
            ticketsByVendor.computeIfAbsent(soldTicket.getVendorId(), k -> new ArrayList<>()).add(soldTicket);
        }

        // Save the update Ticket pool
        ticketPoolRepository.save(ticketPool);



        // Process Transactions for each vendor
        for (Map.Entry<String, List<Ticket>> entry : ticketsByVendor.entrySet()) {

            String vendorId = entry.getKey();
            List<Ticket> vendorTickets = entry.getValue();

            // Collect ticket Ids and calculate total amount
            List<String> ticketIds = vendorTickets.stream()
                    .map(Ticket::getId)
                    .collect(Collectors.toList());

            double totalAmount = vendorTickets.stream()
                    .mapToDouble(Ticket::getPrice)
                    .sum();

            // Log the transaction for current vendor
            Transaction transaction = new Transaction();
            transaction.setEventId(eventId);
            transaction.setCustomerId(customerId);
            transaction.setVendorId(vendorId);
            transaction.setTicketIds(ticketIds);
            transaction.setQuantity(vendorTickets.size());
            transaction.setPricePerTicket(totalAmount / vendorTickets.size());
            transaction.setTotalAmount(totalAmount);
            transaction.setTransactionType("PURCHASE");
            transaction.setTimeStamp(LocalDateTime.now());

            transactionRepository.save(transaction);

        }

        // Add vip points to the customer
        customer.setVipPoints(customer.getVipPoints() + (10 * numberOfTickets));
        customerRepository.save(customer);

        return soldTickets;

    }
}
