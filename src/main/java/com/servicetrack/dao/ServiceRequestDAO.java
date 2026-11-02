package com.servicetrack.dao;

import com.servicetrack.model.ServiceRequest;
import com.servicetrack.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequestDAO {

    public ServiceRequest findById(Long serviceRequestId)
            throws SQLException {

        String sql = """
                SELECT service_request_id,
                       product_id,
                       complaint_description,
                       created_at,
                       updated_at
                FROM service_requests
                WHERE service_request_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, serviceRequestId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }

        return null;
    }

    public List<ServiceRequest> findByProductId(Long productId)
            throws SQLException {

        String sql = """
                SELECT service_request_id,
                       product_id,
                       complaint_description,
                       created_at,
                       updated_at
                FROM service_requests
                WHERE product_id = ?
                ORDER BY created_at DESC
                """;

        List<ServiceRequest> serviceRequests =
                new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, productId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {
                    serviceRequests.add(
                            mapRow(resultSet)
                    );
                }
            }
        }

        return serviceRequests;
    }

    /*
     * Normal create method.
     *
     * This method creates its own database connection.
     * It is kept for normal non-transactional use.
     */
    public Long create(ServiceRequest serviceRequest)
            throws SQLException {

        String sql = """
                INSERT INTO service_requests
                (product_id, complaint_description)
                VALUES (?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(
                    1,
                    serviceRequest.getProductId()
            );

            statement.setString(
                    2,
                    serviceRequest.getComplaintDescription()
            );

            int rowsAffected =
                    statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException(
                        "Creating service request failed."
                );
            }

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    Long serviceRequestId =
                            generatedKeys.getLong(1);

                    serviceRequest.setServiceRequestId(
                            serviceRequestId
                    );

                    return serviceRequestId;
                }
            }
        }

        throw new SQLException(
                "Creating service request failed: no ID obtained."
        );
    }

    /*
     * Transaction-capable create method.
     *
     * The connection is provided by the Service layer.
     * This allows ServiceRequestDAO and TicketDAO
     * to use the same JDBC transaction.
     */
    public Long create(
            Connection connection,
            ServiceRequest serviceRequest)
            throws SQLException {

        String sql = """
                INSERT INTO service_requests
                (product_id, complaint_description)
                VALUES (?, ?)
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(
                    1,
                    serviceRequest.getProductId()
            );

            statement.setString(
                    2,
                    serviceRequest.getComplaintDescription()
            );

            int rowsAffected =
                    statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException(
                        "Creating service request failed."
                );
            }

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    Long serviceRequestId =
                            generatedKeys.getLong(1);

                    serviceRequest.setServiceRequestId(
                            serviceRequestId
                    );

                    return serviceRequestId;
                }
            }
        }

        throw new SQLException(
                "Creating service request failed: no ID obtained."
        );
    }

    public boolean update(ServiceRequest serviceRequest)
            throws SQLException {

        String sql = """
                UPDATE service_requests
                SET complaint_description = ?
                WHERE service_request_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(
                    1,
                    serviceRequest.getComplaintDescription()
            );

            statement.setLong(
                    2,
                    serviceRequest.getServiceRequestId()
            );

            return statement.executeUpdate() > 0;
        }
    }

    public boolean delete(Long serviceRequestId)
            throws SQLException {

        String sql = """
                DELETE FROM service_requests
                WHERE service_request_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, serviceRequestId);

            return statement.executeUpdate() > 0;
        }
    }

    private ServiceRequest mapRow(ResultSet resultSet)
            throws SQLException {

        ServiceRequest serviceRequest =
                new ServiceRequest();

        serviceRequest.setServiceRequestId(
                resultSet.getLong(
                        "service_request_id"
                )
        );

        serviceRequest.setProductId(
                resultSet.getLong(
                        "product_id"
                )
        );

        serviceRequest.setComplaintDescription(
                resultSet.getString(
                        "complaint_description"
                )
        );

        Timestamp createdAt =
                resultSet.getTimestamp("created_at");

        if (createdAt != null) {
            serviceRequest.setCreatedAt(
                    createdAt.toLocalDateTime()
            );
        }

        Timestamp updatedAt =
                resultSet.getTimestamp("updated_at");

        if (updatedAt != null) {
            serviceRequest.setUpdatedAt(
                    updatedAt.toLocalDateTime()
            );
        }

        return serviceRequest;
    }
}