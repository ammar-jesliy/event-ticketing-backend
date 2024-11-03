package lk.ac.iit.eventticketingbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Customer {
    @Id
    private String id;
    private String name;
    private String email;
    private boolean isVip;
    private double discountRate;
    private int purchaseRate;

    public Customer(String name, String email, boolean isVip, double discountRate, int purchaseRate) {
        this.name = name;
        this.email = email;
        this.isVip = isVip;
        this.discountRate = discountRate;
        this.purchaseRate = purchaseRate;
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

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
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
}
