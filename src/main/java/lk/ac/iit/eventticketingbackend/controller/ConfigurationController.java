package lk.ac.iit.eventticketingbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ac.iit.eventticketingbackend.model.Configuration;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/configuration")
public class ConfigurationController {

    private final String configFilePath = "src/main/resources/configuration.json";
    private  final ObjectMapper objectMapper = new ObjectMapper();

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public Configuration getJson() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(configFilePath)));
        return objectMapper.readValue(json, Configuration.class);
    }

    // Endpoint to update the JSON file
    @CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.POST})
    @PostMapping
    public void updateJson(@RequestBody Configuration updatedConfig) throws IOException {
        String json = objectMapper.writeValueAsString(updatedConfig);
        Files.write(Paths.get(configFilePath), json.getBytes());
    }

}
