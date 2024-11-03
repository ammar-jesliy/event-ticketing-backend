package lk.ac.iit.eventticketingbackend.controller;

import lk.ac.iit.eventticketingbackend.model.Customer;
import lk.ac.iit.eventticketingbackend.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
