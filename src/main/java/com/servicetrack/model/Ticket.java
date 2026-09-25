package com.servicetrack.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long ticketId;
    private Long serviceRequestId;
    private Long technicianId;
    private String ticketNumber;
    private TicketStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Ticket() {
    }

    public Ticket(
            Long ticketId,
            Long serviceRequestId,
            Long technicianId,
            String ticketNumber,
            TicketStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.ticketId = ticketId;
        this.serviceRequestId = serviceRequestId;
        this.technicianId = technicianId;
        this.ticketNumber = ticketNumber;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(Long serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }

    public Long getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Long technicianId) {
        this.technicianId = technicianId;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}