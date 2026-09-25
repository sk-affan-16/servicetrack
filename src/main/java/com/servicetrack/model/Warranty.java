package com.servicetrack.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Warranty implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long warrantyId;
    private Long productId;
    private LocalDate warrantyStart;
    private LocalDate warrantyEnd;
    private String warrantyStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Warranty() {
    }

    public Warranty(Long warrantyId,
                    Long productId,
                    LocalDate warrantyStart,
                    LocalDate warrantyEnd,
                    String warrantyStatus,
                    LocalDateTime createdAt,
                    LocalDateTime updatedAt) {

        this.warrantyId = warrantyId;
        this.productId = productId;
        this.warrantyStart = warrantyStart;
        this.warrantyEnd = warrantyEnd;
        this.warrantyStatus = warrantyStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getWarrantyId() {
        return warrantyId;
    }

    public void setWarrantyId(Long warrantyId) {
        this.warrantyId = warrantyId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public LocalDate getWarrantyStart() {
        return warrantyStart;
    }

    public void setWarrantyStart(LocalDate warrantyStart) {
        this.warrantyStart = warrantyStart;
    }

    public LocalDate getWarrantyEnd() {
        return warrantyEnd;
    }

    public void setWarrantyEnd(LocalDate warrantyEnd) {
        this.warrantyEnd = warrantyEnd;
    }

    public String getWarrantyStatus() {
        return warrantyStatus;
    }

    public void setWarrantyStatus(String warrantyStatus) {
        this.warrantyStatus = warrantyStatus;
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