-- ============================================
-- ServiceTrack - M4 Repair
-- Module: Technician Repair
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