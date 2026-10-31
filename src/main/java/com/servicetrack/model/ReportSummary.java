package com.servicetrack.model;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportSummary {

    private Map<String, Long> ticketStatuses = new LinkedHashMap<>();

    private BigDecimal totalRevenue = BigDecimal.ZERO;
    private BigDecimal paidRevenue = BigDecimal.ZERO;
    private BigDecimal unpaidRevenue = BigDecimal.ZERO;

    public Map<String, Long> getTicketStatuses() {
        return ticketStatuses;
    }

    public void setTicketStatuses(Map<String, Long> ticketStatuses) {
        this.ticketStatuses = ticketStatuses;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getPaidRevenue() {
        return paidRevenue;
    }

    public void setPaidRevenue(BigDecimal paidRevenue) {
        this.paidRevenue = paidRevenue;
    }

    public BigDecimal getUnpaidRevenue() {
        return unpaidRevenue;
    }

    public void setUnpaidRevenue(BigDecimal unpaidRevenue) {
        this.unpaidRevenue = unpaidRevenue;
    }
}
