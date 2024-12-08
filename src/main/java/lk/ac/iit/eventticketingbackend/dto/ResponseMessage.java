/**
 * This class represents a response message that can be used to convey
 * information back to the client in the event ticketing backend system.
 * It is part of the Data Transfer Object (DTO) layer.
 * 
 * Attributes include:
 * - message
 *  
 */
package lk.ac.iit.eventticketingbackend.dto;

public class ResponseMessage {
    private String message;

    public ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
