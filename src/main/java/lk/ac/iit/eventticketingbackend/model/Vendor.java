package lk.ac.iit.eventticketingbackend.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Vendor {
    private String id;
    private String name;
    private String email;
    private int releaseRate;

    public Vendor(String name, String email, int releaseRate) {
        this.name = name;
        this.email = email;
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

    public int getReleaseRate() {
        return releaseRate;
    }

    public void setReleaseRate(int releaseRate) {
        this.releaseRate = releaseRate;
    }
}
