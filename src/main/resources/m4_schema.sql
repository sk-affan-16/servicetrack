-- ============================================
-- ServiceTrack - M4 Repair & Inventory
-- Module: Technician Repair + Spare Parts
-- ============================================

CREATE TABLE repairs (
                         repair_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         ticket_id BIGINT NOT NULL,
                         diagnosis VARCHAR(1000),
                         repair_notes VARCHAR(2000),
                         repair_status VARCHAR(30) NOT NULL DEFAULT 'DIAGNOSING',
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                             ON UPDATE CURRENT_TIMESTAMP,

                         CONSTRAINT chk_repairs_status
                             CHECK (
                                 repair_status IN (
                                                   'DIAGNOSING',
                                                   'IN_REPAIR',
                                                   'WAITING_FOR_PART',
                                                   'READY',
                                                   'COMPLETED'
                                     )
                                 )
);

CREATE TABLE spare_parts (
                             spare_part_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             part_name VARCHAR(150) NOT NULL,
                             part_number VARCHAR(100) NOT NULL UNIQUE,
                             quantity INT NOT NULL DEFAULT 0,
                             unit_price DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                                 ON UPDATE CURRENT_TIMESTAMP,

                             CONSTRAINT chk_spare_parts_quantity
                                 CHECK (quantity >= 0),

                             CONSTRAINT chk_spare_parts_unit_price
                                 CHECK (unit_price >= 0)
);

CREATE TABLE repair_part_usage (
                                   usage_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                   ticket_id BIGINT NOT NULL,
                                   spare_part_id BIGINT NOT NULL,
                                   quantity_used INT NOT NULL,
                                   unit_price DECIMAL(10, 2) NOT NULL,
                                   used_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                   CONSTRAINT fk_repair_part_usage_spare_part
                                       FOREIGN KEY (spare_part_id)
                                           REFERENCES spare_parts(spare_part_id),

                                   CONSTRAINT chk_repair_part_usage_quantity
                                       CHECK (quantity_used > 0),

                                   CONSTRAINT chk_repair_part_usage_unit_price
                                       CHECK (unit_price >= 0)
);