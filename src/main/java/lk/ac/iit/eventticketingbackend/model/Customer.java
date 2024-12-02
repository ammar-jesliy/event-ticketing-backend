package lk.ac.iit.eventticketingbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Customer {
    @Id
    @JsonProperty("id")
    private String id;
    private String name;
    @Indexed(unique = true)
    private String email;
    private String password;
    private int vipPoints;
    private double discountRate;
    private int purchaseRate;

    public Customer() {
    }

    public Customer(String name, String email, String password, double discountRate, int purchaseRate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.discountRate = discountRate;
        this.purchaseRate = purchaseRate;
        this.vipPoints = 0;
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

    public int getVipPoints() {
        return vipPoints;
    }

    public void setVipPoints(int vipPoints) {
        this.vipPoints = vipPoints;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public int getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(int purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", vipPoints=" + vipPoints +
                ", discountRate=" + discountRate +
                ", purchaseRate=" + purchaseRate +
                '}';
    }
}
