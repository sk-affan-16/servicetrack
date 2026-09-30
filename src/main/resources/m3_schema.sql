-- ServiceTrack
-- M3: Service Request + Ticket Workflow
-- This file creates only M3-owned tables.

CREATE TABLE IF NOT EXISTS service_requests (
                                                service_request_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                                                product_id BIGINT UNSIGNED NOT NULL,
                                                complaint_description VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_service_request_product
    FOREIGN KEY (product_id)
    REFERENCES products(product_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,

    INDEX idx_service_requests_product_id (product_id),
    INDEX idx_service_requests_created_at (created_at)
    );


CREATE TABLE IF NOT EXISTS tickets (
                                       ticket_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                                       service_request_id BIGINT UNSIGNED NOT NULL,
                                       technician_id BIGINT UNSIGNED NULL,
                                       ticket_number VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_tickets_service_request
    UNIQUE (service_request_id),

    CONSTRAINT uq_tickets_ticket_number
    UNIQUE (ticket_number),

    CONSTRAINT fk_ticket_service_request
    FOREIGN KEY (service_request_id)
    REFERENCES service_requests(service_request_id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,

    CONSTRAINT fk_ticket_technician
    FOREIGN KEY (technician_id)
    REFERENCES users(user_id)
    ON DELETE SET NULL
    ON UPDATE CASCADE,

    CONSTRAINT chk_ticket_status
    CHECK (
              status IN (
              'PENDING',
              'ASSIGNED',
              'DIAGNOSING',
              'IN_REPAIR',
              'WAITING_FOR_PART',
              'READY',
              'COMPLETED',
              'CANCELLED'
                        )
    ),

    INDEX idx_tickets_technician_id (technician_id),
    INDEX idx_tickets_status (status),
    INDEX idx_tickets_created_at (created_at)
    );