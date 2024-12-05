package lk.ac.iit.eventticketingbackend.controller;

import lk.ac.iit.eventticketingbackend.dto.LoginRequest;
import lk.ac.iit.eventticketingbackend.dto.ResponseMessage;
import lk.ac.iit.eventticketingbackend.dto.TicketReleaseRequest;
import lk.ac.iit.eventticketingbackend.model.TicketPool;
import lk.ac.iit.eventticketingbackend.model.Vendor;
import lk.ac.iit.eventticketingbackend.service.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Locale;


@RestController
@RequestMapping("api/v1/vendors")
public class VendorController {
    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    public List<Vendor> fetchAllVendors() {
        return vendorService.getAllVendors();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{vendorId}")
    public Vendor fetchVendorById(@PathVariable String vendorId) {
        return vendorService.getVendorById(vendorId);
    }

    @GetMapping("/simulation")
    public List<Vendor> fetchSimulationVendors(@RequestParam(defaultValue = "5") int limit) {
        return vendorService.getSimulationVendors(limit);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/check-email")
    public boolean checkEmail(@RequestParam String email) {
        return vendorService.isEmailAvailable(email);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/register")
    public Vendor registerVendor(@RequestBody Vendor vendor) {
        return vendorService.registerVendor(vendor);
    }

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


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/release-tickets")
    public ResponseEntity<?> releaseTickets(@RequestBody TicketReleaseRequest request) {
        System.out.println("Release tickets running");
        try {
            boolean success = vendorService.releaseTicket(
                    request.getEventId(),
                    request.getVendorId(),
                    request.getNumberOfTickets(),
                    request.getPrice()
            );

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

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/update-profile")
    public ResponseEntity<Vendor> updateVendorProfile(@RequestBody Vendor vendor) {
        Vendor updatedVendor = vendorService.updateVendorProfile(vendor);
        return ResponseEntity.ok(updatedVendor);
    }
}
