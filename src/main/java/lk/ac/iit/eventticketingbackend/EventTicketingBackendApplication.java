package lk.ac.iit.eventticketingbackend;

import lk.ac.iit.eventticketingbackend.model.Customer;
import lk.ac.iit.eventticketingbackend.model.Vendor;
import lk.ac.iit.eventticketingbackend.repository.CustomerRepository;
import lk.ac.iit.eventticketingbackend.repository.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication
public class EventTicketingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventTicketingBackendApplication.class, args);
    }

}
