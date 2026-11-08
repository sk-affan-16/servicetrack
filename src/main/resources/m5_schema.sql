-- ServiceTrack
-- M5: Billing Module

CREATE TABLE IF NOT EXISTS bills (
    bill_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    ticket_id BIGINT UNSIGNED NOT NULL,

    labor_charge DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    parts_charge DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,

    payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    payment_method VARCHAR(30) DEFAULT NULL,
    paid_at TIMESTAMP NULL DEFAULT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_bills_ticket UNIQUE (ticket_id),

    CONSTRAINT fk_bills_ticket
        FOREIGN KEY (ticket_id)
        REFERENCES tickets(ticket_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT chk_bills_labor_charge
        CHECK (labor_charge >= 0),

    CONSTRAINT chk_bills_parts_charge
        CHECK (parts_charge >= 0),

    CONSTRAINT chk_bills_total_amount
        CHECK (total_amount >= 0),

    CONSTRAINT chk_bills_payment_status
        CHECK (payment_status IN ('PENDING', 'PAID', 'CANCELLED')),

    INDEX idx_bills_payment_status (payment_status),
    INDEX idx_bills_created_at (created_at)
);
