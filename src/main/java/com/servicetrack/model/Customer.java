package com.servicetrack.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long customerId;
    private Long userId;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Customer() {
    }

    public Customer(Long customerId,
                    Long userId,
                    String address,
                    String city,
                    String state,
                    String pincode,
                    LocalDateTime createdAt,
                    LocalDateTime updatedAt) {

        this.customerId = customerId;
        this.userId = userId;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
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