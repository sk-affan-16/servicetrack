package com.servicetrack.service;

import com.servicetrack.dao.ReportsDAO;
import com.servicetrack.model.ReportSummary;
import com.servicetrack.model.TechnicianReport;

import java.sql.SQLException;
import java.util.List;

public class ReportsService {

    private final ReportsDAO reportsDAO;

    public ReportsService() {
        reportsDAO = new ReportsDAO();
    }

    public ReportSummary getReportSummary()
            throws SQLException {

        return reportsDAO.getReportSummary();
    }

    public List<TechnicianReport> getTechnicianReports()
            throws SQLException {

        return reportsDAO.getTechnicianReports();
    }
}
