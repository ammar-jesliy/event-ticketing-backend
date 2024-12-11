/**
 * The Cli class provides a command-line interface for the Event Ticketing Application.
 * It allows users to simulate ticket buying and selling, view and edit
 * configuration settings, and print customer and vendor details.
 * <p>
 * The class includes methods to load initial data, display menu options,
 * handle user input, and perform various operations based on the selected menu
 * option.
 * <p>
 * Methods:
 * - main(String[] args): The main method that starts the CLI application and
 * handles user input.
 * - simulate(): Simulates multiple users buying and selling tickets.
 * - printConfiguration(): Prints the current configuration settings.
 * - configure(): Allows the user to edit configuration settings.
 * - printUserDetails(): Prints the details of customers and vendors.
 * - loadConfiguration(): Loads configuration settings from a JSON file.
 * - loadVendors(): Fetches vendor data from the backend.
 * - loadCustomers(): Fetches customer data from the backend.
 * - fetchListFromApi(String apiUrl, TypeReference<List<T>> typeReference):
 * Sends an API request to fetch data and returns a list of the specified type.
 * <p>
 * The class uses the SLF4J logging framework to log information, warnings, and
 * errors.
 * It also uses the Jackson library to handle JSON serialization and
 * deserialization.
 */
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
import java.util.InputMismatchException;
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
            loadConfiguration();
            loadVendors();
            loadCustomers();
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
                    logger.info("User selected option 1: Simulate.");
                    simulate();
                } else if (selected_option == 2) {
                    logger.info("User selected option 2: Print Configuration.");
                    printConfiguration();
                } else if (selected_option == 3) {
                    logger.info("User selected option 3: Edit Configuration.");
                    configure();
                } else if (selected_option == 4) {
                    logger.info("User selected option 4: Print Customer and Vendor Details.");
                    printUserDetails();
                } else if (selected_option == 0) {
                    logger.info("User selected option 0: Exit.");
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

    /**
     * Simulates the ticket selling process by initializing and starting vendor and
     * customer threads.
     * Each vendor releases tickets to the pool, and each customer attempts to
     * purchase tickets from the pool.
     * The simulation waits for all threads to complete before logging the results.
     *
     * @throws Exception if any thread is interrupted while waiting for completion.
     */
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

        logger.info("Simulation completed. Tickets sold: {}. Available tickets: {}", ticketPool.getTicketSold(),
                ticketPool.getAvailableTickets());

        System.out.println();
        System.out.println("Total tickets in Ticket Pool: " + ticketPool.getTotalTickets());
        System.out.println("Available tickets for sale in Ticket Pool: " + ticketPool.getAvailableTickets());
        System.out.println("Number of tickets sold: " + ticketPool.getTicketSold());
    }

    /**
     * Prints the current configuration settings to the console.
     * This includes the maximum capacity, release rate, and retrieval rate.
     * Logs an informational message before displaying the settings.
     */
    private static void printConfiguration() {
        logger.info("Displaying current configuration settings.");
        System.out.println("Configuration settings");
        System.out.println();
        System.out.println("Max Capacity: " + configuration.getMaxCapacity());
        System.out.println("Release Rate: " + configuration.getReleaseRate());
        System.out.println("Retrieval Rate: " + configuration.getRetrievalRate());
    }

    /**
     * Configures the application settings by prompting the user for input
     * values.
     * The method ensures that the input values are within specified ranges and
     * handles invalid inputs.
     * The configuration is then saved to a JSON file.
     *
     * @throws Exception if an error occurs during configuration or file
     *                   operations.
     */
    private static void configure() throws Exception {
        logger.info("Configuring application settings.");
        System.out.println("Configure");
        System.out.println();
        Scanner input = new Scanner(System.in);

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            int maxCapacity = 0;
            int releaseRate = 0;
            int retrievalRate = 0;

            // Handle invalid input for MaxCapacity
            while (true) {
                System.out.print("Enter MaxCapacity (1-500): ");
                try {
                    maxCapacity = input.nextInt();
                    if (maxCapacity < 1 || maxCapacity > 500) {
                        System.out.println("Invalid input. MaxCapacity must be between 1 and 500.");
                    } else {
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    input.next(); // Clear the invalid input
                }
            }

            System.out.println();

            // Handle invalid input for ReleaseRate
            while (true) {
                System.out.print("Enter ReleaseRate (1-20): ");
                try {
                    releaseRate = input.nextInt();
                    if (releaseRate < 1 || releaseRate > 20) {
                        System.out.println("Invalid input. ReleaseRate must be between 1 and 20.");
                    } else {
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    input.next(); // Clear the invalid input
                }
            }

            System.out.println();

            // Handle invalid input for Retrieval Rate
            while (true) {
                System.out.print("Enter Retrieval Rate (1-20): ");
                try {
                    retrievalRate = input.nextInt();
                    if (retrievalRate < 1 || retrievalRate > 20) {
                        System.out.println("Invalid input. Retrieval Rate must be between 1 and 20.");
                    } else {
                        break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    input.next(); // Clear the invalid input
                }
            }

            System.out.println();

            Configuration newConfiguration = new Configuration(maxCapacity, releaseRate, retrievalRate);

            objectMapper.writeValue(new File("src/main/resources/configuration.json"), newConfiguration);
            System.out.println("JSON file saved successfully");

            logger.info("Configuration updated and saved to JSON file: {}", newConfiguration);

            configuration = newConfiguration;

            for (Vendor vendor : vendors) {
                vendor.setReleaseRate(configuration.getReleaseRate());
            }

            for (Customer customer : customers) {
                customer.setPurchaseRate(configuration.getRetrievalRate());
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Error configuring settings.", e);
        }

    }

    /**
     * Prints the details of all customers and vendors to the console.
     * This method logs the action of printing details and then iterates
     * through the lists of customers and vendors, printing each one.
     *
     * It first prints a header for customer details, followed by each
     * customer object. Then it prints a header for vendor details,
     * followed by each vendor object.
     */
    private static void printUserDetails() {
        logger.info("Printing customer and vendor details.");

        System.out.println("Customer Details: ");
        System.out.println();
        for (Customer customer : customers) {
            System.out.println(customer);
        }

        System.out.println();

        System.out.println("Vendor Details: ");
        System.out.println();
        for (Vendor vendor : vendors) {
            System.out.println(vendor);
        }
    }

    /**
     * Loads the configuration from a JSON file located at "src/main/resources/
     * configuration.json".
     * This method uses the Jackson ObjectMapper to read the JSON file and map
     * its contents to a Configuration object.
     * If the configuration is loaded successfully, a log message is generated
     * with the loaded configuration.
     * If an error occurs during the loading process, the stack trace is
     * printed and an error log message is generated.
     */
    private static void loadConfiguration() {
        logger.info("Loading configuration from JSON file.");
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            configuration = objectMapper.readValue(new File("src/main/resources/configuration.json"),
                    Configuration.class);
            logger.info("Configuration loaded successfully: {}", configuration);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error loading configuration.", e);
        }
    }

    /**
     * Loads vendor data from the backend API and sets the release rate for each
     * vendor.
     *
     * @throws Exception if there is an error fetching the vendor data from the
     *                   API.
     */
    private static void loadVendors() throws Exception {
        logger.info("Fetching vendor data from Backend.");

        String apiUrl = "http://localhost:8080/api/v1/vendors/simulation";
        vendors = fetchListFromApi(apiUrl, new TypeReference<List<Vendor>>() {
        });

        for (Vendor vendor : vendors) {
            vendor.setReleaseRate(configuration.getReleaseRate());
        }
    }

    /**
     * Loads customer data from the backend API and sets the purchase rate for
     * each customer.
     *
     * @throws Exception if there is an error while fetching the customer data.
     */
    private static void loadCustomers() throws Exception {
        logger.info("Fetching customer data from Backend.");

        String apiUrl = "http://localhost:8080/api/v1/customers/simulation";
        customers = fetchListFromApi(apiUrl, new TypeReference<List<Customer>>() {
        });

        for (Customer customer : customers) {
            customer.setPurchaseRate(configuration.getRetrievalRate());
        }
    }

    /**
     * Fetches a list of objects from the specified API URL.
     *
     * @param <T>           the type of objects in the list
     * @param apiUrl        the URL of the API endpoint to fetch data from
     * @param typeReference a reference to the type of the list to be returned
     * @return a list of objects of type T fetched from the API
     * @throws Exception if an error occurs during the API request or
     *                   response processing
     */
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
