package lk.ac.iit.eventticketingbackend.controller;

import lk.ac.iit.eventticketingbackend.model.LoginRequest;
import lk.ac.iit.eventticketingbackend.model.ResponseMessage;
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

    @GetMapping
    public List<Vendor> fetchAllVendors() {
        return vendorService.getAllVendors();
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
    @PutMapping("/update-profile")
    public ResponseEntity<Vendor> updateVendorProfile(@RequestBody Vendor vendor) {
        Vendor updatedVendor = vendorService.updateVendorProfile(vendor);
        return ResponseEntity.ok(updatedVendor);
    }
}
