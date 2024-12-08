/**
 * This class represents a Data Transfer Object (DTO) for login requests.
 * It is used to encapsulate the data required for a user to log in to the system.
 * 
 * 
 * Attributes include:
 * - email
 * - password
 * 
 */
package lk.ac.iit.eventticketingbackend.dto;

public class LoginRequest {
    private String email;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
