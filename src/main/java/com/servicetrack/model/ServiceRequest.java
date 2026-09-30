package com.servicetrack.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ServiceRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long serviceRequestId;
    private Long productId;
    private String complaintDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ServiceRequest() {
    }

    public ServiceRequest(
            Long serviceRequestId,
            Long productId,
            String complaintDescription,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {

        this.serviceRequestId = serviceRequestId;
        this.productId = productId;
        this.complaintDescription = complaintDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(Long serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getComplaintDescription() {
        return complaintDescription;
    }

    public void setComplaintDescription(String complaintDescription) {
        this.complaintDescription = complaintDescription;
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