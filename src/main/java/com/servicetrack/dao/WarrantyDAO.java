package com.servicetrack.dao;

import com.servicetrack.model.Warranty;
import com.servicetrack.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WarrantyDAO {

    private static final String FIND_BY_ID_SQL =
            "SELECT warranty_id, product_id, warranty_start, " +
                    "warranty_end, warranty_status, created_at, updated_at " +
                    "FROM warranties " +
                    "WHERE warranty_id = ?";

    private static final String FIND_BY_PRODUCT_ID_SQL =
            "SELECT warranty_id, product_id, warranty_start, " +
                    "warranty_end, warranty_status, created_at, updated_at " +
                    "FROM warranties " +
                    "WHERE product_id = ?";

    private static final String INSERT_WARRANTY_SQL =
            "INSERT INTO warranties " +
                    "(product_id, warranty_start, warranty_end, warranty_status) " +
                    "VALUES (?, ?, ?, ?)";

    private static final String UPDATE_STATUS_SQL =
            "UPDATE warranties " +
                    "SET warranty_status = ? " +
                    "WHERE warranty_id = ?";

    public Warranty findById(Long warrantyId)
            throws SQLException {

        if (warrantyId == null) {
            throw new IllegalArgumentException(
                    "Warranty ID is required."
            );
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setLong(1, warrantyId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapWarranty(resultSet);
                }

                return null;
            }
        }
    }

    public Warranty findByProductId(Long productId)
            throws SQLException {

        if (productId == null) {
            throw new IllegalArgumentException(
                    "Product ID is required."
            );
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             FIND_BY_PRODUCT_ID_SQL)) {

            statement.setLong(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return mapWarranty(resultSet);
                }

                return null;
            }
        }
    }

    public Warranty create(Warranty warranty)
            throws SQLException {

        if (warranty == null) {
            throw new IllegalArgumentException(
                    "Warranty is required."
            );
        }

        if (warranty.getProductId() == null) {
            throw new IllegalArgumentException(
                    "Product ID is required."
            );
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             INSERT_WARRANTY_SQL,
                             Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(
                    1,
                    warranty.getProductId()
            );

            statement.setDate(
                    2,
                    Date.valueOf(warranty.getWarrantyStart())
            );

            statement.setDate(
                    3,
                    Date.valueOf(warranty.getWarrantyEnd())
            );

            statement.setString(
                    4,
                    warranty.getWarrantyStatus()
            );

            int affectedRows =
                    statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException(
                        "Creating warranty failed."
                );
            }

            try (ResultSet generatedKeys =
                         statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    warranty.setWarrantyId(
                            generatedKeys.getLong(1)
                    );

                } else {

                    throw new SQLException(
                            "Creating warranty failed: "
                                    + "no ID obtained."
                    );
                }
            }

            return warranty;
        }
    }

    public boolean updateStatus(
            Long warrantyId,
            String warrantyStatus)
            throws SQLException {

        if (warrantyId == null) {
            throw new IllegalArgumentException(
                    "Warranty ID is required."
            );
        }

        if (warrantyStatus == null
                || warrantyStatus.isBlank()) {

            throw new IllegalArgumentException(
                    "Warranty status is required."
            );
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             UPDATE_STATUS_SQL)) {

            statement.setString(
                    1,
                    warrantyStatus
            );

            statement.setLong(
                    2,
                    warrantyId
            );

            return statement.executeUpdate() > 0;
        }
    }

    public java.util.List<com.servicetrack.model.AdminWarrantyView> findAllForAdmin() throws SQLException {
        String sql = "SELECT p.product_id,p.product_name,p.brand,p.model_number,p.serial_number,u.full_name, w.warranty_id,w.warranty_start,w.warranty_end,w.warranty_status FROM products p JOIN customers c ON c.customer_id=p.customer_id JOIN users u ON u.user_id=c.user_id LEFT JOIN warranties w ON w.product_id=p.product_id ORDER BY p.product_id DESC";
        java.util.List<com.servicetrack.model.AdminWarrantyView> rows = new java.util.ArrayList<>();
        try (Connection con=DBConnection.getConnection(); PreparedStatement ps=con.prepareStatement(sql); ResultSet rs=ps.executeQuery()) {
            while(rs.next()) {
                com.servicetrack.model.AdminWarrantyView v=new com.servicetrack.model.AdminWarrantyView();
                v.setProductId(rs.getLong("product_id")); v.setProductName(rs.getString("product_name")); v.setBrand(rs.getString("brand")); v.setModelNumber(rs.getString("model_number")); v.setSerialNumber(rs.getString("serial_number")); v.setCustomerName(rs.getString("full_name"));
                long wid=rs.getLong("warranty_id"); if(!rs.wasNull()) v.setWarrantyId(wid);
                Date st=rs.getDate("warranty_start"), en=rs.getDate("warranty_end"); if(st!=null)v.setWarrantyStart(st.toLocalDate()); if(en!=null)v.setWarrantyEnd(en.toLocalDate());
                v.setStoredStatus(rs.getString("warranty_status"));
                v.setEffectiveStatus(computeEffectiveStatus(v.getStoredStatus(),v.getWarrantyStart(),v.getWarrantyEnd(),java.time.LocalDate.now())); rows.add(v);
            }
        } return rows;
    }

    public Warranty createOrUpdateForAdmin(Long productId, java.time.LocalDate start, java.time.LocalDate end) throws SQLException {
        String sql = "INSERT INTO warranties (product_id,warranty_start,warranty_end,warranty_status) VALUES (?,?,?,'ACTIVE') ON DUPLICATE KEY UPDATE warranty_start=VALUES(warranty_start), warranty_end=VALUES(warranty_end), warranty_status=IF(warranty_status='VOID','VOID','ACTIVE')";
        try(Connection con=DBConnection.getConnection(); PreparedStatement ps=con.prepareStatement(sql)){ ps.setLong(1,productId); ps.setDate(2,Date.valueOf(start)); ps.setDate(3,Date.valueOf(end)); ps.executeUpdate(); }
        return findByProductId(productId);
    }

    public boolean updateAdminStatus(Long warrantyId, String status) throws SQLException {
        return updateStatus(warrantyId,status);
    }

    public static String computeEffectiveStatus(String stored, java.time.LocalDate start, java.time.LocalDate end, java.time.LocalDate today) {
        if ("VOID".equals(stored)) return "VOID";
        if (start==null || end==null) return "NO_WARRANTY";
        if (today.isBefore(start)) return "NOT_STARTED";
        if (today.isAfter(end)) return "EXPIRED";
        return "ACTIVE";
    }

    private Warranty mapWarranty(
            ResultSet resultSet)
            throws SQLException {

        Warranty warranty = new Warranty();

        warranty.setWarrantyId(
                resultSet.getLong("warranty_id")
        );

        warranty.setProductId(
                resultSet.getLong("product_id")
        );

        Date warrantyStart =
                resultSet.getDate("warranty_start");

        if (warrantyStart != null) {
            warranty.setWarrantyStart(
                    warrantyStart.toLocalDate()
            );
        }

        Date warrantyEnd =
                resultSet.getDate("warranty_end");

        if (warrantyEnd != null) {
            warranty.setWarrantyEnd(
                    warrantyEnd.toLocalDate()
            );
        }

        warranty.setWarrantyStatus(
                resultSet.getString("warranty_status")
        );

        if (resultSet.getTimestamp("created_at") != null) {
            warranty.setCreatedAt(
                    resultSet.getTimestamp("created_at")
                            .toLocalDateTime()
            );
        }

        if (resultSet.getTimestamp("updated_at") != null) {
            warranty.setUpdatedAt(
                    resultSet.getTimestamp("updated_at")
                            .toLocalDateTime()
            );
        }

        return warranty;
    }
}