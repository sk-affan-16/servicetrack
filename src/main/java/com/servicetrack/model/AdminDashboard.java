package com.servicetrack.model;

import java.math.BigDecimal;

public class AdminDashboard {

    private long totalCustomers;
    private long totalTechnicians;
    private long totalProducts;
    private long totalServiceRequests;
    private long pendingTickets;
    private long completedTickets;

    private BigDecimal totalRevenue;
    private BigDecimal unpaidAmount;

    public AdminDashboard() {
        this.totalRevenue = BigDecimal.ZERO;
        this.unpaidAmount = BigDecimal.ZERO;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public long getTotalTechnicians() {
        return totalTechnicians;
    }

    public void setTotalTechnicians(long totalTechnicians) {
        this.totalTechnicians = totalTechnicians;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getTotalServiceRequests() {
        return totalServiceRequests;
    }

    public void setTotalServiceRequests(long totalServiceRequests) {
        this.totalServiceRequests = totalServiceRequests;
    }

    public long getPendingTickets() {
        return pendingTickets;
    }

    public void setPendingTickets(long pendingTickets) {
        this.pendingTickets = pendingTickets;
    }

    public long getCompletedTickets() {
        return completedTickets;
    }

    public void setCompletedTickets(long completedTickets) {
        this.completedTickets = completedTickets;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getUnpaidAmount() {
        return unpaidAmount;
    }

    public void setUnpaidAmount(BigDecimal unpaidAmount) {
        this.unpaidAmount = unpaidAmount;
    }
}
