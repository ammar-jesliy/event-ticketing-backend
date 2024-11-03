package lk.ac.iit.eventticketingbackend.controller;

import lk.ac.iit.eventticketingbackend.model.Vendor;
import lk.ac.iit.eventticketingbackend.service.VendorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
