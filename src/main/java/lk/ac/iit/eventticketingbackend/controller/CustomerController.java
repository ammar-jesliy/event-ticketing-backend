package lk.ac.iit.eventticketingbackend.controller;

import lk.ac.iit.eventticketingbackend.model.Customer;
import lk.ac.iit.eventticketingbackend.dto.LoginRequest;
import lk.ac.iit.eventticketingbackend.dto.ResponseMessage;
import lk.ac.iit.eventticketingbackend.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
