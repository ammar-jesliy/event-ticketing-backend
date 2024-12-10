package lk.ac.iit.eventticketingbackend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ac.iit.eventticketingbackend.model.Configuration;
import lk.ac.iit.eventticketingbackend.model.Customer;
import lk.ac.iit.eventticketingbackend.model.TicketPool;
import lk.ac.iit.eventticketingbackend.model.Vendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cli {

    private static final Logger logger = LoggerFactory.getLogger(Cli.class);

    // Lists to store customers and vendors
    private static List<Customer> customers = new ArrayList<>();
    private static List<Vendor> vendors = new ArrayList<>();
    private static Configuration configuration;

    public static void main(String[] args) {
        logger.info("Starting Event Ticketing CLI Application...");
        Scanner input = new Scanner(System.in);

        try {
            loadVendors();
            loadCustomers();
            loadConfiguration();
            logger.info("Initial data successfully loaded.");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error loading initial data.", e);
        }

        System.out.println();
        System.out.println("---------------------------------------------------------------------");
        System.out.println("|            WELCOME TO THE EVENT TICKETING APPLICATION             |");
        System.out.println("---------------------------------------------------------------------");
        System.out.println();

        while (true) {
            logger.info("Displaying menu options.");
            System.out.println("*********************************************************************");
            System.out.println("*                            MENU OPTIONS                           *");
            System.out.println("*********************************************************************");
            System.out.println("\t1) Start simulation of multiple users buying and selling tickets");
            System.out.println("\t2) Print current configuration settings");
            System.out.println("\t3) Edit configuration settings");
            System.out.println("\t4) Print customer and vendor details");
            System.out.println("\t5) Print ticket pool details");
            System.out.println("\t0) Quit");
            System.out.println("*********************************************************************");

            System.out.print("Please select an option: ");

            // Validate option input
            int selected_option = 0;
            try {
                selected_option = Integer.parseInt(input.next());
            } catch (NumberFormatException e) {
                logger.warn("Invalid menu option selected");
                System.out.println();
                System.out.println("Invalid option, Try again!");
                System.out.println();
                continue;
            }

            System.out.println();

            try {
                if (selected_option == 1) {
                    simulate();
                } else if (selected_option == 2) {
                    printConfiguration();
                } else if (selected_option == 3) {
                    configure();
                } else if (selected_option == 4) {
                    printUserDetails();
                } else if (selected_option == 5) {
                    printTicketPoolDetails();
                } else if (selected_option == 0) {
                    logger.info("Exiting application.");
                    break;
                } else {
                    System.out.println("Invalid Option, Try again!");
                }
            } catch (Exception e) {
                logger.error("An error occurred while processing the selected option.", e);
                e.printStackTrace();
            }

            System.out.println();
        }
    }


    private static void simulate() throws Exception {
        logger.info("Starting ticket simulation...");
        System.out.println("Simulation started\n");

        TicketPool ticketPool = new TicketPool();
        ticketPool.setMaxTicketCapacity(configuration.getMaxCapacity());
        ticketPool.setEventId("675531d9170be813fdee43e6");

        Thread[] vendorThreads = new Thread[vendors.size()];

        logger.debug("Initializing vendor threads...");
        for (int i = 0; i < vendors.size(); i++) {
            vendors.get(i).setReleaseRate(configuration.getReleaseRate());
            vendors.get(i).setTicketPool(ticketPool);

            Thread vendorThread = new Thread(vendors.get(i), vendors.get(i).getName());
            vendorThreads[i] = vendorThread;
            vendorThreads[i].start();
        }

        Thread[] customerThreads = new Thread[customers.size()];

        logger.debug("Initializing customer threads...");
        for (int i = 0; i < customers.size(); i++) {
            customers.get(i).setPurchaseRate(configuration.getRetrievalRate());
            customers.get(i).setTicketPool(ticketPool);

            Thread customerThread = new Thread(customers.get(i), customers.get(i).getName());
            customerThreads[i] = customerThread;
            customerThreads[i].start();
        }

        for (Thread thread : vendorThreads) {
            thread.join(); // Wait for each thread to finish
        }

        for (Thread thread : customerThreads) {
            thread.join(); // Wait for each thread to finish
        }

        logger.info("Simulation completed. Tickets sold: {}. Available tickets: {}", ticketPool.getTicketSold(), ticketPool.getAvailableTickets());

        System.out.println();
        System.out.println("Total tickets in Ticket Pool: " + ticketPool.getTotalTickets());
        System.out.println("Available tickets for sale in Ticket Pool: " + ticketPool.getAvailableTickets());
        System.out.println("Number of tickets sold: " + ticketPool.getTicketSold());
    }


    private static void printConfiguration() {
        logger.info("Displaying current configuration settings.");
        System.out.println("Configuration settings");
        System.out.println("Max Capacity: " + configuration.getMaxCapacity());
        System.out.println("Release Rate: " + configuration.getReleaseRate());
        System.out.println("Retrieval Rate: " + configuration.getRetrievalRate());
    }


    private static void configure() throws Exception {
        logger.info("Configuring application settings.");
        System.out.println("Configure");
        Scanner input = new Scanner(System.in);

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            System.out.print("Enter MaxCapacity: ");
            int maxCapacity = input.nextInt();

            System.out.print("Enter ReleaseRate: ");
            int releaseRate = input.nextInt();

            System.out.print("Enter Retrieval Rate: ");
            int retrievalRate = input.nextInt();

            Configuration newConfiguration = new Configuration(maxCapacity, releaseRate, retrievalRate);

            objectMapper.writeValue(new File("src/main/resources/configuration.json"), newConfiguration);
            System.out.println("JSON file saved successfully");

            logger.info("Configuration updated and saved to JSON file.");

            configuration = newConfiguration;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Error configuring settings.", e);
        }

    }


    private static void printUserDetails() {
        logger.info("Printing customer and vendor details.");

        System.out.println("Customer Details: ");
        for (Customer customer : customers) {
            System.out.println(customer);
        }

        System.out.println();

        System.out.println("Vendor Details: ");
        for (Vendor vendor : vendors) {
            System.out.println(vendor);
        }
    }

    private static void printTicketPoolDetails() {
        // TODO
        System.out.println("Ticket Pool Details");
    }

    private static void loadConfiguration() {
        logger.info("Loading configuration from JSON file.");
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            configuration = objectMapper.readValue(new File("src/main/resources/configuration.json"), Configuration.class);
            logger.info("Configuration loaded successfully: {}", configuration);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error loading configuration.", e);
        }
    }

    private static void loadVendors() throws Exception {
        logger.info("Fetching vendor data from Backend.");

        String apiUrl = "http://localhost:8080/api/v1/vendors/simulation";
        vendors = fetchListFromApi(apiUrl, new TypeReference<List<Vendor>>() {
        });
    }

    private static void loadCustomers() throws Exception {
        logger.info("Fetching customer data from Backend.");

        String apiUrl = "http://localhost:8080/api/v1/customers/simulation";
        customers = fetchListFromApi(apiUrl, new TypeReference<List<Customer>>() {
        });
    }

    private static <T> List<T> fetchListFromApi(String apiUrl, TypeReference<List<T>> typeReference) throws Exception {
        logger.debug("Sending API request to: {}", apiUrl);

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            logger.debug("Successfully fetched data from API.");
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), typeReference);

        } else {
            logger.error("Failed to fetch data from API. Status code: {}", response.statusCode());
            System.out.println("Failed to fetch data from API. Http status code: " + response.statusCode());
            return new ArrayList<>();
        }
    }

}


