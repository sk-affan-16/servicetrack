package com.servicetrack.service;

import com.servicetrack.dao.AdminDashboardDAO;
import com.servicetrack.model.AdminDashboard;

import java.sql.SQLException;

public class AdminDashboardService {

    private final AdminDashboardDAO dashboardDAO;

    public AdminDashboardService() {
        this.dashboardDAO = new AdminDashboardDAO();
    }

    public AdminDashboard getDashboardData()
            throws SQLException {

        return dashboardDAO.getDashboardData();
    }
}
