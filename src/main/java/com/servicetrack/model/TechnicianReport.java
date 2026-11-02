package com.servicetrack.model;

public class TechnicianReport {

    private Long technicianId;
    private String technicianName;
    private long totalTickets;
    private long completedRepairs;

    public Long getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public long getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(long totalTickets) {
        this.totalTickets = totalTickets;
    }

    public long getCompletedRepairs() {
        return completedRepairs;
    }

    public void setCompletedRepairs(long completedRepairs) {
        this.completedRepairs = completedRepairs;
    }
}
