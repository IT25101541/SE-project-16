-- CreativePulse - Campaign Management schema + seed (MySQL 8+)
-- Requires db/01_users.sql to have been run first (FKs to users).
CREATE DATABASE IF NOT EXISTS creativepulse_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE creativepulse_db;

CREATE TABLE IF NOT EXISTS employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNIQUE NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_employees_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS clients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNIQUE NULL,
    company_name VARCHAR(255) NOT NULL,
    contact_person VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    address VARCHAR(255),
    created_by_sales_exec_id BIGINT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_clients_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL,
    CONSTRAINT fk_clients_sales_exec FOREIGN KEY (created_by_sales_exec_id) REFERENCES employees (id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS campaigns (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    campaign_name VARCHAR(255) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    start_date DATE NOT NULL,
    deadline DATE NOT NULL,
    progress INT NOT NULL DEFAULT 0,
    status VARCHAR(50) NOT NULL DEFAULT 'PLANNED',
    budget DECIMAL(12, 2) DEFAULT 0.00,
    client_id BIGINT NOT NULL,
    employee_id BIGINT NULL,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    cancelled BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_campaign_progress CHECK (progress >= 0 AND progress <= 100),
    CONSTRAINT fk_campaigns_client FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE RESTRICT,
    CONSTRAINT fk_campaigns_employee FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE SET NULL,
    INDEX idx_campaigns_status (status),
    INDEX idx_campaigns_client (client_id)
) ENGINE=InnoDB;

INSERT IGNORE INTO employees (id, user_id, name, email, role) VALUES
(1, 3, 'Sunil Mendis', 'sunil@creativepulse.com', 'Sales Executive'),
(2, 4, 'Anura Ukwaththa', 'anura@creativepulse.com', 'Campaign Manager'),
(3, 5, 'Tharushi Silva', 'tharushi@creativepulse.com', 'Graphic Designer'),
(4, 6, 'Kasun Perera', 'kasun@creativepulse.com', 'Advertising Staff'),
(5, 7, 'Kumari Dias', 'kumari@creativepulse.com', 'Finance Officer');

INSERT IGNORE INTO clients (id, user_id, company_name, contact_person, email, phone, address, created_by_sales_exec_id) VALUES
(1, 8, 'ABC Pvt Ltd', 'Nimal Perera', 'nimal@abc.com', '0771234567', 'No. 45, Galle Road, Colombo 03', 1),
(2, NULL, 'XYZ Holdings', 'Kamal Silva', 'kamal@xyz.com', '0717654321', 'No. 12, Peradeniya Road, Kandy', 1);

-- Status values must match the CampaignStatus enum (the original seed used 'ACTIVE', which does not exist)
INSERT IGNORE INTO campaigns (id, campaign_name, description, start_date, deadline, progress, status, budget, client_id, employee_id, archived, cancelled) VALUES
(1, 'Summer Beverage Launch 2026', 'Comprehensive 360-degree social media, billboard, and digital ad campaign for a refreshing tropical fruit drink.', '2026-06-01', '2026-08-31', 45, 'IN_PROGRESS', 750000.00, 1, 4, FALSE, FALSE),
(2, 'TechFest Annual Conference Promo', 'Branding, print flyers, and video ad promos for Sri Lanka tech summit.', '2026-09-01', '2026-11-15', 10, 'PLANNED', 500000.00, 2, 4, FALSE, FALSE),
(3, 'Autumn Seasonal Discount Push', 'Direct marketing and digital promotions targeting seasonal retail buyers.', '2026-04-01', '2026-05-30', 100, 'COMPLETED', 320000.00, 1, 4, FALSE, FALSE);
