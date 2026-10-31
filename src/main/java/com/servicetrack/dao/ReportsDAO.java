package com.servicetrack.dao;

import com.servicetrack.model.ReportSummary;
import com.servicetrack.model.TechnicianReport;
import com.servicetrack.util.DBConnection;

import java.sql.*;
import java.math.BigDecimal;
import java.util.*;

public class ReportsDAO {

    private static final String STATUS_SQL = """
        SELECT status, COUNT(*) AS total
        FROM tickets
        GROUP BY status
        """;

    private static final String REVENUE_SQL = """
        SELECT
            COALESCE(SUM(total_amount), 0) AS total_revenue,
            COALESCE(SUM(
                CASE WHEN payment_status = 'PAID'
                THEN total_amount ELSE 0 END
            ), 0) AS paid_revenue,
            COALESCE(SUM(
                CASE WHEN payment_status = 'UNPAID'
                THEN total_amount ELSE 0 END
            ), 0) AS unpaid_revenue
        FROM bills
        """;

    private static final String TECHNICIAN_SQL = """
        SELECT
            u.user_id,
            u.full_name,
            COUNT(DISTINCT t.ticket_id) AS total_tickets,
            COUNT(DISTINCT CASE
                WHEN r.repair_status = 'COMPLETED'
                THEN r.ticket_id
            END) AS completed_repairs
        FROM users u
        LEFT JOIN tickets t
            ON u.user_id = t.technician_id
        LEFT JOIN repairs r
            ON t.ticket_id = r.ticket_id
        WHERE u.role = 'TECHNICIAN'
        GROUP BY u.user_id, u.full_name
        ORDER BY u.full_name
        """;

    public ReportSummary getReportSummary()
            throws SQLException {

        ReportSummary summary = new ReportSummary();

        try (Connection connection = DBConnection.getConnection()) {

            // Ticket status report
            try (PreparedStatement statement =
                         connection.prepareStatement(STATUS_SQL);
                 ResultSet rs = statement.executeQuery()) {

                Map<String, Long> statuses = new LinkedHashMap<>();

                while (rs.next()) {
                    statuses.put(
                            rs.getString("status"),
                            rs.getLong("total")
                    );
                }

                summary.setTicketStatuses(statuses);
            }

            // Revenue report
            try (PreparedStatement statement =
                         connection.prepareStatement(REVENUE_SQL);
                 ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    summary.setTotalRevenue(
                            rs.getBigDecimal("total_revenue"));

                    summary.setPaidRevenue(
                            rs.getBigDecimal("paid_revenue"));

                    summary.setUnpaidRevenue(
                            rs.getBigDecimal("unpaid_revenue"));
                }
            }
        }

        return summary;
    }

    public List<TechnicianReport> getTechnicianReports()
            throws SQLException {

        List<TechnicianReport> reports = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(TECHNICIAN_SQL);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {

                TechnicianReport report = new TechnicianReport();

                report.setTechnicianId(
                        rs.getLong("user_id"));

                report.setTechnicianName(
                        rs.getString("full_name"));

                report.setTotalTickets(
                        rs.getLong("total_tickets"));

                report.setCompletedRepairs(
                        rs.getLong("completed_repairs"));

                reports.add(report);
            }
        }

        return reports;
    }
}
