package com.servicetrack.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/servicetrack";

    private static final String USER =
            "root";

    private static final String CONFIG_FILE =
            "conf/servicetrack-db.properties";

    private static final String PASSWORD =
            loadDatabasePassword();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    "MySQL JDBC Driver not found.",
                    e
            );
        }
    }

    private DBConnection() {
        // Prevent object creation
    }

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }

    private static String loadDatabasePassword() {

        String catalinaBase =
                System.getProperty("catalina.base");

        if (catalinaBase == null || catalinaBase.isBlank()) {
            throw new IllegalStateException(
                    "Tomcat catalina.base is not available."
            );
        }

        Path configPath = Paths.get(
                catalinaBase,
                CONFIG_FILE
        );

        Properties properties = new Properties();

        try (InputStream inputStream =
                     Files.newInputStream(configPath)) {

            properties.load(inputStream);

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Unable to read database configuration.",
                    e
            );
        }

        String password =
                properties.getProperty("db.password");

        if (password == null || password.isBlank()) {
            throw new IllegalStateException(
                    "Database password is missing from "
                            + configPath
            );
        }

        return password;
    }
}