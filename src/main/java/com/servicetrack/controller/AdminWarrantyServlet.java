package com.servicetrack.controller;

import com.servicetrack.service.AdminWarrantyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/admin/warranties")
public class AdminWarrantyServlet extends HttpServlet {
 private final AdminWarrantyService service=new AdminWarrantyService();
 protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException { load(req,resp); }
 protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
  req.setCharacterEncoding("UTF-8"); String action=req.getParameter("action");
  try {
   if("status".equals(action)){ service.updateStatus(Long.valueOf(req.getParameter("warrantyId")),req.getParameter("status")); }
   else { service.save(Long.valueOf(req.getParameter("productId")),LocalDate.parse(req.getParameter("warrantyStart")),LocalDate.parse(req.getParameter("warrantyEnd"))); }
   resp.sendRedirect(req.getContextPath()+"/admin/warranties?success=1");
  } catch(IllegalArgumentException e){ req.setAttribute("error",e.getMessage()); load(req,resp); }
    catch(SQLException e){ throw new ServletException("Unable to save warranty.",e); }
 }
 private void load(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
  try { req.setAttribute("warranties",service.getAll()); req.getRequestDispatcher("/WEB-INF/views/admin/warranties.jsp").forward(req,resp); }
  catch(SQLException e){ throw new ServletException("Unable to load warranties.",e); }
 }
}
