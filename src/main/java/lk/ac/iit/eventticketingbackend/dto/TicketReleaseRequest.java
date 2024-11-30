package lk.ac.iit.eventticketingbackend.dto;

public class TicketReleaseRequest {
    private String eventId;
    private String vendorId;
    private int numberOfTickets;
    private double price;

    public TicketReleaseRequest(String eventId, String vendorId, int numberOfTickets, double price) {
        this.eventId = eventId;
        this.vendorId = vendorId;
        this.numberOfTickets = numberOfTickets;
        this.price = price;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "TicketReleaseRequest{" +
                "eventId='" + eventId + '\'' +
                ", vendorId='" + vendorId + '\'' +
                ", numberOfTickets=" + numberOfTickets +
                ", price=" + price +
                '}';
    }
}
