package com.servicetrack.service;

import com.servicetrack.dao.WarrantyDAO;
import com.servicetrack.model.AdminWarrantyView;
import com.servicetrack.model.Warranty;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AdminWarrantyService {
 private final WarrantyDAO dao=new WarrantyDAO();
 public List<AdminWarrantyView> getAll() throws SQLException { return dao.findAllForAdmin(); }
 public void save(Long productId, LocalDate start, LocalDate end) throws SQLException {
  if(productId==null||productId<=0) throw new IllegalArgumentException("Select a valid product.");
  if(start==null||end==null) throw new IllegalArgumentException("Both warranty dates are required.");
  if(end.isBefore(start)) throw new IllegalArgumentException("Warranty end date cannot be before start date.");
  dao.createOrUpdateForAdmin(productId,start,end);
 }
 public void updateStatus(Long warrantyId,String status) throws SQLException {
  if(warrantyId==null||warrantyId<=0) throw new IllegalArgumentException("Invalid warranty.");
  if(!"ACTIVE".equals(status)&&!"VOID".equals(status)) throw new IllegalArgumentException("Only ACTIVE or VOID can be selected by Admin.");
  if(!dao.updateAdminStatus(warrantyId,status)) throw new IllegalArgumentException("Warranty not found.");
 }
}
