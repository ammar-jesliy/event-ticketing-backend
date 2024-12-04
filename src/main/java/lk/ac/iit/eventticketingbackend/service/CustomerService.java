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

    public CustomerService(CustomerRepository customerRepository, TicketPoolRepository ticketPoolRepository, TicketRepository ticketRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.ticketPoolRepository = ticketPoolRepository;
        this.ticketRepository = ticketRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Fetch customers created for simulation
    public List<Customer> getSimulationCustomers(int limit) {
        List<Customer> allCustomers = customerRepository.findAll();
        return allCustomers.stream()
                .filter(customer -> customer.getName().startsWith("customer"))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public boolean isEmailAvailable(String email) {
        return !customerRepository.existsByEmail(email);
    }

    public Customer registerCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public boolean authenticate(String email, String password) {
        Customer customer = customerRepository.findCustomerByEmail(email);
        if (customer != null) {
            return customer.getPassword().equals(password);
        }
        return false;
    }

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

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findCustomerByEmail(email);
    }

    public List<Ticket> buyTicket(String eventId, String customerId, int numberOfTickets) {
        TicketPool ticketPool = ticketPoolRepository.findTicketPoolByEventId(eventId);

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

        return soldTickets;

    }
}
