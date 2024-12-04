package lk.ac.iit.eventticketingbackend.service;

import lk.ac.iit.eventticketingbackend.model.*;
import lk.ac.iit.eventticketingbackend.repository.CustomerRepository;
import lk.ac.iit.eventticketingbackend.repository.TicketPoolRepository;
import lk.ac.iit.eventticketingbackend.repository.TicketRepository;
import lk.ac.iit.eventticketingbackend.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        List<String> ticketIds = new ArrayList<>();

        // Initialize variable to store total amount
        double totalAmount = 0;

        for (int i = 0; i < numberOfTickets; i++) {
            Ticket soldTicket = ticketPool.sellTicket(customerId);

            ticketRepository.save(soldTicket);

            soldTickets.add(soldTicket);

            ticketIds.add(soldTicket.getId());
            totalAmount += soldTicket.getPrice();
        }

        // Save the update Ticket pool
        ticketPoolRepository.save(ticketPool);


        // Log the transaction
        Transaction transaction = new Transaction();
        transaction.setEventId(eventId);
        transaction.setCustomerId(customerId);
        transaction.setTicketIds(ticketIds);
        transaction.setQuantity(numberOfTickets);
        transaction.setTotalAmount(totalAmount);
        transaction.setTransactionType("PURCHASE");
        transaction.setTimeStamp(LocalDateTime.now());

        transactionRepository.save(transaction);

        return soldTickets;

    }
}
