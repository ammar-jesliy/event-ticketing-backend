package lk.ac.iit.eventticketingbackend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ac.iit.eventticketingbackend.model.Customer;
import lk.ac.iit.eventticketingbackend.model.Vendor;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Cli {

    // Lists to store customers and vendors
    private static List<Customer> customers = new ArrayList<>();
    private static List<Vendor> vendors = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Running standalone task...");

        try {
            loadVendors();
            loadCustomers();

            System.out.println("Vendors List: ");
            for (Vendor vendor : vendors) {
                System.out.println(vendor);
            }

            System.out.println("\nCustomers List: ");
            for (Customer customer : customers) {
                System.out.println(customer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadVendors() throws Exception {
        String apiUrl = "http://localhost:8080/api/v1/vendors/simulation";
        vendors = fetchListFromApi(apiUrl, new TypeReference<List<Vendor>>() {});
    }

    private static void loadCustomers() throws Exception {
        String apiUrl = "http://localhost:8080/api/v1/customers/simulation";
        customers = fetchListFromApi(apiUrl, new TypeReference<List<Customer>>() {});
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


