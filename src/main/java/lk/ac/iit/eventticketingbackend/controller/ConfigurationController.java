/**
 * This class is responsible for handling configuration-related requests.
 * 
 */

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
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Handles HTTP GET requests to retrieve the configuration as a JSON object.
     * 
     * @return Configuration object parsed from the JSON file.
     * @throws IOException if an I/O error occurs reading from the file or a
     *                     malformed
     *                     or unmappable byte sequence is read.
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public Configuration getJson() throws IOException {
        String json = new String(Files.readAllBytes(Paths.get(configFilePath)));
        return objectMapper.readValue(json, Configuration.class);
    }

    /**
     * Updates the configuration JSON file with the provided configuration data.
     *
     * @param updatedConfig the updated configuration data to be written to the
     *                      JSON file
     * @throws IOException if an I/O error occurs writing to or creating the file
     */
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public void updateJson(@RequestBody Configuration updatedConfig) throws IOException {
        String json = objectMapper.writeValueAsString(updatedConfig);
        Files.write(Paths.get(configFilePath), json.getBytes());
    }

}
