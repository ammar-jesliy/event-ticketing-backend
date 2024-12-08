/**
 * This class represents the configuration settings for the event ticketing backend.
 * It is part of the model package and is used to manage various configuration parameters.
 * 
 * Attributes include:
 * - maxCapacity (maximum number of tickets that can be sold)
 * - releaseRate (rate at which tickets are released by the vendors)
 * - retrievalRate (rate at which tickets are retrieved by the customers)
 * 
 * 
 */
package lk.ac.iit.eventticketingbackend.model;

public class Configuration {
    private int maxCapacity;
    private int releaseRate;
    private int retrievalRate;

    public Configuration() {
    }

    public Configuration(int maxCapacity, int releaseRate, int retrievalRate) {
        this.maxCapacity = maxCapacity;
        this.releaseRate = releaseRate;
        this.retrievalRate = retrievalRate;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getReleaseRate() {
        return releaseRate;
    }

    public void setReleaseRate(int releaseRate) {
        this.releaseRate = releaseRate;
    }

    public int getRetrievalRate() {
        return retrievalRate;
    }

    public void setRetrievalRate(int retrievalRate) {
        this.retrievalRate = retrievalRate;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "maxCapacity=" + maxCapacity +
                ", releaseRate=" + releaseRate +
                ", retrievalRate=" + retrievalRate +
                '}';
    }
}
