package com.servicetrack.controller;

import com.servicetrack.model.ReportSummary;
import com.servicetrack.model.TechnicianReport;
import com.servicetrack.service.ReportsService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/reports")
public class ReportsServlet extends HttpServlet {

    private ReportsService reportsService;

    @Override
    public void init() {
        reportsService = new ReportsService();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            ReportSummary summary =
                    reportsService.getReportSummary();

            List<TechnicianReport> technicians =
                    reportsService.getTechnicianReports();

            request.setAttribute("summary", summary);
            request.setAttribute("technicians", technicians);

            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/reports.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to generate reports.", e
            );
        }
    }
}