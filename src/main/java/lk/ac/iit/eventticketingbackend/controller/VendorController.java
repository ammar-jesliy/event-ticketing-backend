/**
 * This class is responsible for handling HTTP requests related to vendors.
 */
package lk.ac.iit.eventticketingbackend.controller;

import lk.ac.iit.eventticketingbackend.dto.LoginRequest;
import lk.ac.iit.eventticketingbackend.dto.ResponseMessage;
import lk.ac.iit.eventticketingbackend.dto.TicketReleaseRequest;
import lk.ac.iit.eventticketingbackend.model.Vendor;
import lk.ac.iit.eventticketingbackend.service.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/vendors")
public class VendorController {
    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    /**
     * Fetches all vendors.
     *
     * @return a list of all vendors
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public List<Vendor> fetchAllVendors() {
        return vendorService.getAllVendors();
    }

    /**
     * Fetches a vendor by their ID.
     *
     * @param vendorId the ID of the vendor to fetch
     * @return the vendor with the specified ID
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{vendorId}")
    public Vendor fetchVendorById(@PathVariable String vendorId) {
        return vendorService.getVendorById(vendorId);
    }

    /**
     * Fetches a list of vendors created for simulation.
     *
     * @param limit the maximum number of vendors to fetch, defaults to 5 if not
     *              specified
     * @return a list of Vendor objects
     */
    @GetMapping("/simulation")
    public List<Vendor> fetchSimulationVendors(@RequestParam(defaultValue = "5") int limit) {
        return vendorService.getSimulationVendors(limit);
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
        return vendorService.isEmailAvailable(email);
    }

    /**
     * Registers a new vendor.
     *
     * @param vendor the vendor to be registered
     * @return the registered vendor
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/register")
    public Vendor registerVendor(@RequestBody Vendor vendor) {
        return vendorService.registerVendor(vendor);
    }

    /**
     * Handles the login request for a vendor.
     * 
     * @param request the login request containing email and password
     * @return a ResponseEntity containing the vendor details if authenticated,
     *         or an unauthorized status with an error message if authentication
     *         fails
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/login")
    public ResponseEntity<?> loginVendor(@RequestBody LoginRequest request) {
        boolean authenticated = vendorService.authenticate(request.getEmail(), request.getPassword());
        if (authenticated) {
            return ResponseEntity.ok(vendorService.getVendorByEmail(request.getEmail()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("Invalid Credentials"));
        }
    }

    /**
     * Endpoint to release tickets for an event.
     * 
     * @param request the request body containing ticket release details
     * @return ResponseEntity with appropriate HTTP status and message
     * 
     *         Possible responses:
     *         - 200 OK: Tickets successfully released
     *         - 400 BAD REQUEST: Ticket pool has reached its maximum capacity
     *         - 404 NOT FOUND: Event not found
     *         - 500 INTERNAL SERVER ERROR: Error releasing tickets
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/release-tickets")
    public ResponseEntity<?> releaseTickets(@RequestBody TicketReleaseRequest request) {
        System.out.println("Release tickets running");
        try {
            boolean success = vendorService.releaseTicket(
                    request.getEventId(),
                    request.getVendorId(),
                    request.getNumberOfTickets(),
                    request.getPrice());

            if (success) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Tickets successfully released"));
            } else {
                return new ResponseEntity<>("Ticket pool has reached its maximum capacity", HttpStatus.BAD_REQUEST);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error releasing tickets", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the profile of an existing vendor.
     *
     * @param vendor the vendor object containing updated profile information
     * @return a ResponseEntity containing the updated vendor profile
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/update-profile")
    public ResponseEntity<Vendor> updateVendorProfile(@RequestBody Vendor vendor) {
        Vendor updatedVendor = vendorService.updateVendorProfile(vendor);
        return ResponseEntity.ok(updatedVendor);
    }

    /**
     * Deletes a vendor by their ID.
     *
     * @param vendorId the ID of the vendor to be deleted
     * @return a ResponseEntity containing a success message if the vendor is
     *         deleted,
     *         or an error message if the vendor is not found
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{vendorId}")
    public ResponseEntity<?> deleteVendor(@PathVariable String vendorId) {
        try {
            vendorService.deleteVendorById(vendorId);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Vendor Deleted Succesfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
