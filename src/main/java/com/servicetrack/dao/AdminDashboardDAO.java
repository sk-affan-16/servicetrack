package com.servicetrack.dao;

import com.servicetrack.model.AdminDashboard;
import com.servicetrack.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDashboardDAO {

    private static final String DASHBOARD_SQL = """
        SELECT
            (SELECT COUNT(*) FROM users
             WHERE role = 'CUSTOMER'
             AND status = 'ACTIVE') AS total_customers,

            (SELECT COUNT(*) FROM users
             WHERE role = 'TECHNICIAN'
             AND status = 'ACTIVE') AS total_technicians,

            (SELECT COUNT(*) FROM products)
                AS total_products,

            (SELECT COUNT(*) FROM service_requests)
                AS total_service_requests,

            (SELECT COUNT(*) FROM tickets
             WHERE status = 'PENDING') AS pending_tickets,

            (SELECT COUNT(*) FROM tickets
             WHERE status = 'COMPLETED') AS completed_tickets,

            (SELECT COALESCE(SUM(total_amount), 0)
             FROM bills) AS total_revenue,

            (SELECT COALESCE(SUM(total_amount), 0)
             FROM bills
             WHERE payment_status = 'UNPAID')
                AS unpaid_amount
        """;

    public AdminDashboard getDashboardData()
            throws SQLException {

        AdminDashboard dashboard = new AdminDashboard();

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(DASHBOARD_SQL);

             ResultSet rs = statement.executeQuery()) {

            if (rs.next()) {

                dashboard.setTotalCustomers(
                        rs.getLong("total_customers"));

                dashboard.setTotalTechnicians(
                        rs.getLong("total_technicians"));

                dashboard.setTotalProducts(
                        rs.getLong("total_products"));

                dashboard.setTotalServiceRequests(
                        rs.getLong("total_service_requests"));

                dashboard.setPendingTickets(
                        rs.getLong("pending_tickets"));

                dashboard.setCompletedTickets(
                        rs.getLong("completed_tickets"));

                dashboard.setTotalRevenue(
                        rs.getBigDecimal("total_revenue"));

                dashboard.setUnpaidAmount(
                        rs.getBigDecimal("unpaid_amount"));
            }
        }

        return dashboard;
    }
}
