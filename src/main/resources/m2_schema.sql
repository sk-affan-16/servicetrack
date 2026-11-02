-- ServiceTrack M2: Customer, Product and Warranty

CREATE TABLE IF NOT EXISTS customers (
                                         customer_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                                         user_id BIGINT UNSIGNED NOT NULL UNIQUE,
                                         address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    pincode VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_customers_user
    FOREIGN KEY (user_id)
    REFERENCES users(user_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS products (
                                        product_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                                        customer_id BIGINT UNSIGNED NOT NULL,
                                        product_name VARCHAR(150) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    model_number VARCHAR(100),
    serial_number VARCHAR(100) NOT NULL UNIQUE,
    purchase_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_products_customer
    FOREIGN KEY (customer_id)
    REFERENCES customers(customer_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
    );

CREATE TABLE IF NOT EXISTS warranties (
                                          warranty_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                                          product_id BIGINT UNSIGNED NOT NULL UNIQUE,
                                          warranty_start DATE NOT NULL,
                                          warranty_end DATE NOT NULL,
                                          warranty_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_warranties_product
    FOREIGN KEY (product_id)
    REFERENCES products(product_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,

    CONSTRAINT chk_warranty_status
    CHECK (
              warranty_status IN ('ACTIVE', 'EXPIRED', 'VOID')
    )
    );
private static final String VERIFY_COMPLETED_SQL = """
        SELECT COUNT(*)
        FROM tickets t
        WHERE t.ticket_id = ?
          AND t.status = 'COMPLETED'
          AND EXISTS (
              SELECT 1
              FROM repairs r
              WHERE r.ticket_id = t.ticket_id
                AND r.repair_id = (
                    SELECT r2.repair_id
                    FROM repairs r2
                    WHERE r2.ticket_id = t.ticket_id
                    ORDER BY r2.created_at DESC, r2.repair_id DESC
                    LIMIT 1
                )
                AND r.repair_status = 'COMPLETED'
          )
        """;