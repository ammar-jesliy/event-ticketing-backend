/**
 * This class is a controller for handling customer-related operations
 * in the event ticketing backend system.
 * 
 * It is part of the package lk.ac.iit.eventticketingbackend.controller.
 */

package lk.ac.iit.eventticketingbackend.controller;

import lk.ac.iit.eventticketingbackend.dto.TicketBuyRequest;
import lk.ac.iit.eventticketingbackend.model.Customer;
import lk.ac.iit.eventticketingbackend.dto.LoginRequest;
import lk.ac.iit.eventticketingbackend.dto.ResponseMessage;
import lk.ac.iit.eventticketingbackend.model.Ticket;
import lk.ac.iit.eventticketingbackend.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Fetches all customers.
     *
     * This method handles HTTP GET requests and retrieves a list of all customers
     * from the customer service. It allows cross-origin requests from
     * "http://localhost:4200".
     *
     * @return a list of all customers.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public List<Customer> fetchAllCustomers() {
        return customerService.getAllCustomers();
    }

    /**
     * Fetches a customer by their ID.
     *
     * @param customerId the ID of the customer to fetch
     * @return the customer with the specified ID
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{customerId}")
    public Customer fetchCustomerById(@PathVariable String customerId) {
        return customerService.getCustomerById(customerId);
    }

    /**
     * Fetches a list of simulated customers.
     *
     * @param limit the maximum number of customers to fetch, defaults to 5 if not
     *              specified
     * @return a list of simulated customers
     */
    @GetMapping("/simulation")
    public List<Customer> fetchSimulationCustomers(@RequestParam(defaultValue = "5") int limit) {
        return customerService.getSimulationCustomers(limit);
    }

    /**
     * Checks if the given email is available.
     * 
     * @param email the email address to check
     * @return true if the email is available, false otherwise
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/check-email")
    public boolean checkEmail(@RequestParam String email) {
        return customerService.isEmailAvailable(email);
    }

    /**
     * Registers a new customer.
     *
     * @param customer the customer to be registered
     * @return the registered customer
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/register")
    public Customer registerCustomer(@RequestBody Customer customer) {
        return customerService.registerCustomer(customer);
    }

    /**
     * Handles the login request for a customer.
     * 
     * @param request the login request containing email and password
     * @return ResponseEntity containing the customer details if authenticated,
     *         or an unauthorized status with an error message if authentication
     *         fails
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@RequestBody LoginRequest request) {
        boolean authenticated = customerService.authenticate(request.getEmail(), request.getPassword());
        if (authenticated) {
            return ResponseEntity.ok(customerService.getCustomerByEmail(request.getEmail()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("Invalid Credentials"));
        }
    }

    /**
     * Updates the profile of an existing customer.
     *
     * @param customer the customer object containing updated profile information
     * @return a ResponseEntity containing the updated customer object
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/update-profile")
    public ResponseEntity<Customer> updateCustomerProfile(@RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomerProfile(customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    /**
     * Endpoint to handle ticket purchase requests.
     * 
     * @param request the request body containing ticket purchase details
     * @return a ResponseEntity containing the result of the ticket purchase
     *         operation
     * 
     *         Possible responses:
     *         - 200 OK: Ticket sold successfully, returns a map with a success
     *         message and the list of bought tickets.
     *         - 400 BAD REQUEST: Invalid event ID or other validation errors,
     *         returns a map with an error message.
     *         - 409 CONFLICT: Insufficient tickets or other logical issues, returns
     *         a map with an error message.
     *         - 500 INTERNAL SERVER ERROR: Unexpected errors, returns a map with an
     *         error message.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/buy-tickets")
    public ResponseEntity<?> buyTicket(@RequestBody TicketBuyRequest request) {
        System.out.println("Buy Tickets Running...");

        try {
            List<Ticket> boughtTickets = customerService.buyTicket(request.getEventId(), request.getCustomerId(),
                    request.getNumberOfTickets());

            return ResponseEntity.ok().body(Map.of(
                    "message", "Ticket sold Successfully",
                    "tickets", boughtTickets));
        } catch (IllegalArgumentException e) {
            // Handle invalid event IDs or other validation errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", e.getMessage()));
        } catch (IllegalStateException e) {
            // Handle insufficient tickets or other logical issues
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "error", e.getMessage()));
        } catch (Exception e) {
            // Handle any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "An unexpected error occurred: " + e.getMessage()));
        }

    }
}
