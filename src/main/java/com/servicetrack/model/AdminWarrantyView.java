package com.servicetrack.model;

import java.time.LocalDate;

public class AdminWarrantyView {
    private Long productId; private String productName, brand, modelNumber, serialNumber, customerName;
    private Long warrantyId; private LocalDate warrantyStart, warrantyEnd; private String storedStatus, effectiveStatus;
    public Long getProductId(){return productId;} public void setProductId(Long v){productId=v;}
    public String getProductName(){return productName;} public void setProductName(String v){productName=v;}
    public String getBrand(){return brand;} public void setBrand(String v){brand=v;}
    public String getModelNumber(){return modelNumber;} public void setModelNumber(String v){modelNumber=v;}
    public String getSerialNumber(){return serialNumber;} public void setSerialNumber(String v){serialNumber=v;}
    public String getCustomerName(){return customerName;} public void setCustomerName(String v){customerName=v;}
    public Long getWarrantyId(){return warrantyId;} public void setWarrantyId(Long v){warrantyId=v;}
    public LocalDate getWarrantyStart(){return warrantyStart;} public void setWarrantyStart(LocalDate v){warrantyStart=v;}
    public LocalDate getWarrantyEnd(){return warrantyEnd;} public void setWarrantyEnd(LocalDate v){warrantyEnd=v;}
    public String getStoredStatus(){return storedStatus;} public void setStoredStatus(String v){storedStatus=v;}
    public String getEffectiveStatus(){return effectiveStatus;} public void setEffectiveStatus(String v){effectiveStatus=v;}
}
