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

    @GetMapping
    public List<Customer> fetchAllCustomers() {
        return customerService.getAllCustomers();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{customerId}")
    public Customer fetchCustomerById(@PathVariable String customerId) {
        return customerService.getCustomerById(customerId);
    }

    @GetMapping("/simulation")
    public List<Customer> fetchSimulationCustomers(@RequestParam(defaultValue = "5") int limit) {
        return customerService.getSimulationCustomers(limit);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/check-email")
    public boolean checkEmail(@RequestParam String email) {
        return customerService.isEmailAvailable(email);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/register")
    public Customer registerCustomer(@RequestBody Customer customer) {
        return customerService.registerCustomer(customer);
    }

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

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/update-profile")
    public ResponseEntity<Customer> updateCustomerProfile(@RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomerProfile(customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/buy-tickets")
    public ResponseEntity<?> buyTicket(@RequestBody TicketBuyRequest request) {
        System.out.println("Buy Tickets Running...");

        try {
            List<Ticket> boughtTickets = customerService.buyTicket(request.getEventId(), request.getCustomerId(), request.getNumberOfTickets());

            return ResponseEntity.ok().body(Map.of(
                    "message", "Ticket sold Successfully",
                    "tickets", boughtTickets
            ));
        } catch (IllegalArgumentException e) {
            // Handle invalid event IDs or other validation errors
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "error", e.getMessage()
            ));
        } catch (IllegalStateException e) {
            // Handle insufficient tickets or other logical issues
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            // Handle any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "error", "An unexpected error occurred: " + e.getMessage()
            ));
        }


    }
}
