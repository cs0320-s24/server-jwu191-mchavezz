package edu.brown.cs.student.main.server;

public class BroadbandAccessData {
    private String state;
    private String county;
    private double broadbandAccessPercentage;

    // Constructor
    public BroadbandAccessData(String state, String county, double broadbandAccessPercentage) {
        this.state = state;
        this.county = county;
        this.broadbandAccessPercentage = broadbandAccessPercentage;
    }

    // Getters and setters
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public double getBroadbandAccessPercentage() {
        return broadbandAccessPercentage;
    }

    public void setBroadbandAccessPercentage(double broadbandAccessPercentage) {
        this.broadbandAccessPercentage = broadbandAccessPercentage;
    }
}
