CREATE TABLE IF NOT EXISTS bills (
    bill_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,

    ticket_id BIGINT UNSIGNED NOT NULL,

    parts_cost DECIMAL(10,2) NOT NULL DEFAULT 0.00,

    labour_charge DECIMAL(10,2) NOT NULL DEFAULT 0.00,

    tax_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00,

    total_amount DECIMAL(10,2) NOT NULL,

    payment_status VARCHAR(20) NOT NULL DEFAULT 'UNPAID',

    generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    paid_at TIMESTAMP NULL DEFAULT NULL,

    PRIMARY KEY (bill_id),

    UNIQUE KEY uk_bills_ticket_id (ticket_id),

    KEY idx_bills_payment_status (payment_status),

    CONSTRAINT fk_bills_ticket
        FOREIGN KEY (ticket_id)
        REFERENCES tickets(ticket_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT chk_bills_parts_cost
        CHECK (parts_cost >= 0),

    CONSTRAINT chk_bills_labour_charge
        CHECK (labour_charge >= 0),

    CONSTRAINT chk_bills_tax_amount
        CHECK (tax_amount >= 0),

    CONSTRAINT chk_bills_total_amount
        CHECK (total_amount >= 0),

    CONSTRAINT chk_bills_payment_status
        CHECK (payment_status IN ('UNPAID', 'PAID')),

    CONSTRAINT chk_bills_total_calculation
        CHECK (total_amount = parts_cost + labour_charge + tax_amount)

) ENGINE=InnoDB;
