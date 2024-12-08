package lk.ac.iit.eventticketingbackend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ac.iit.eventticketingbackend.model.Configuration;
import lk.ac.iit.eventticketingbackend.model.Customer;
import lk.ac.iit.eventticketingbackend.model.TicketPool;
import lk.ac.iit.eventticketingbackend.model.Vendor;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cli {

    // Lists to store customers and vendors
    private static List<Customer> customers = new ArrayList<>();
    private static List<Vendor> vendors = new ArrayList<>();
    private static Configuration configuration;

    public static void main(String[] args) {
        System.out.println("Running standalone task...");
        Scanner input = new Scanner(System.in);

        try {
            loadVendors();
            loadCustomers();
            loadConfiguration();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("---------------------------------------------------------------------");
        System.out.println("|            WELCOME TO THE EVENT TICKETING APPLICATION             |");
        System.out.println("---------------------------------------------------------------------");
        System.out.println();

        while (true) {
            System.out.println("*********************************************************************");
            System.out.println("*                            MENU OPTIONS                           *");
            System.out.println("*********************************************************************");
            System.out.println("\t1) Start simulation of multiple users buying and selling tickets");
            System.out.println("\t2) Print current configuration settings");
            System.out.println("\t3) Edit configuration settings");
            System.out.println("\t4) Print customer and vendor details");
            System.out.println("\t5) Print ticket pool details");
            System.out.println("\t6) Search ticket");
            System.out.println("\t0) Quit");
            System.out.println("*********************************************************************");

            System.out.print("Please select an option: ");

            // Validate option input
            int selected_option = 0;
            try {
                selected_option = Integer.parseInt(input.next());
            } catch (NumberFormatException e) {
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
                } else if (selected_option == 6) {
                    searchTicket();
                } else if (selected_option == 0) {
                    break;
                } else {
                    System.out.println("Invalid Option, Try again!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println();
        }
    }


    private static void simulate() throws Exception {
        // TODO
        System.out.println("Simulation started\n");

        TicketPool ticketPool = new TicketPool();
        ticketPool.setMaxTicketCapacity(configuration.getMaxCapacity());
        ticketPool.setEventId("675531d9170be813fdee43e6");

        Thread[] vendorThreads = new Thread[vendors.size()];

        for (int i = 0; i < vendors.size(); i++) {
            vendors.get(i).setReleaseRate(configuration.getReleaseRate());
            vendors.get(i).setTicketPool(ticketPool);

            Thread vendorThread = new Thread(vendors.get(i), vendors.get(i).getName());
            vendorThreads[i] = vendorThread;
            vendorThreads[i].start();
        }

        Thread[] customerThreads = new Thread[customers.size()];

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

        System.out.println();
        System.out.println("Total tickets in Ticket Pool: " + ticketPool.getTotalTickets());
        System.out.println("Available tickets for sale in Ticket Pool: " + ticketPool.getAvailableTickets());
        System.out.println("Number of tickets sold: " + ticketPool.getTicketSold());
    }


    private static void printConfiguration() {
        // TODO
        System.out.println("Configuration settings");
        System.out.println("Max Capacity: " + configuration.getMaxCapacity());
        System.out.println("Release Rate: " + configuration.getReleaseRate());
        System.out.println("Retrieval Rate: " + configuration.getRetrievalRate());
    }


    private static void configure() throws Exception {
        // TODO
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

        configuration = newConfiguration;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void printUserDetails() {
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

    private static void searchTicket() {
        // TODO
        System.out.println("Search Tickets");
    }

    private static void loadConfiguration() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            System.out.println("Working Directory: " + System.getProperty("user.dir"));

            configuration = objectMapper.readValue(new File("src/main/resources/configuration.json"), Configuration.class);

            System.out.println(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadVendors() throws Exception {
        String apiUrl = "http://localhost:8080/api/v1/vendors/simulation";
        vendors = fetchListFromApi(apiUrl, new TypeReference<List<Vendor>>() {
        });
    }

    private static void loadCustomers() throws Exception {
        String apiUrl = "http://localhost:8080/api/v1/customers/simulation";
        customers = fetchListFromApi(apiUrl, new TypeReference<List<Customer>>() {
        });
    }

    private static <T> List<T> fetchListFromApi(String apiUrl, TypeReference<List<T>> typeReference) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(apiUrl))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body(), typeReference);

        } else {
            System.out.println("Failed to fetch data from API. Http status code: " + response.statusCode());
            return new ArrayList<>();
        }
    }

}


