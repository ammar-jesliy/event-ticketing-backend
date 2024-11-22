package lk.ac.iit.eventticketingbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Vendor {
    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String email;
    private String password;
    private int releaseRate;

    public Vendor(String name, String email, String password, int releaseRate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.releaseRate = releaseRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getReleaseRate() {
        return releaseRate;
    }

    public void setReleaseRate(int releaseRate) {
        this.releaseRate = releaseRate;
    }
}
