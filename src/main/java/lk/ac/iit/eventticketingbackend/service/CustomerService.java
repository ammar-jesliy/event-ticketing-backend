package lk.ac.iit.eventticketingbackend.service;

import lk.ac.iit.eventticketingbackend.model.Customer;
import lk.ac.iit.eventticketingbackend.model.Vendor;
import lk.ac.iit.eventticketingbackend.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
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

    public Customer getCustomerByEmail(String email){
        return customerRepository.findCustomerByEmail(email);
    }
}
