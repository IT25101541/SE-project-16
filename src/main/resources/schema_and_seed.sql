-- ====================================================================
-- CREATIVEPULSE ADVERTISING AGENCY MANAGEMENT SYSTEM
-- Unified Relational Database Schema & Seed Data
-- Group ID: 2026-Y2-S1-KU-16 (SKU Batch 01 | Group 16)
-- Database: MySQL 8.0+ / 9.x
-- ====================================================================

DROP DATABASE IF EXISTS creativepulse_db;
CREATE DATABASE creativepulse_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE creativepulse_db;

-- ====================================================================
-- 1. USER MANAGEMENT & DASHBOARD
-- Owner: Ranasinghe R.M.M.K. (IT25101541)
-- Stakeholder Roles: ADMINISTRATOR, MANAGEMENT, SALES_EXECUTIVE,
--                    CAMPAIGN_MANAGER, GRAPHIC_DESIGNER,
--                    EMPLOYEE (Advertising Staff), FINANCE_OFFICER, CLIENT
-- ====================================================================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    role ENUM(
        'ADMINISTRATOR',
        'MANAGEMENT',
        'SALES_EXECUTIVE',
        'CAMPAIGN_MANAGER',
        'GRAPHIC_DESIGNER',
        'EMPLOYEE',
        'FINANCE_OFFICER',
        'CLIENT'
    ) NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE',
    phone VARCHAR(30),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_role (role),
    INDEX idx_user_status (status)
) ENGINE=InnoDB;

-- ====================================================================
-- EMPLOYEES / AGENCY STAFF TABLE
-- Standardized Roles: Campaign Manager, Graphic Designer,
--                     Advertising Staff, Sales Executive, Finance Officer
-- ====================================================================
CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNIQUE NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_employees_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ====================================================================
-- CLIENT REGISTRATION & MANAGEMENT
-- Owner: Sales Executive (Requirement B.1 #3 & Issue 1 Fix)
-- ====================================================================
CREATE TABLE clients (
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
    CONSTRAINT fk_clients_user FOREIGN KEY (user_id)
        REFERENCES users (id) ON DELETE SET NULL,
    CONSTRAINT fk_clients_sales_exec FOREIGN KEY (created_by_sales_exec_id)
        REFERENCES employees (id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ====================================================================
-- 2. CAMPAIGN MANAGEMENT (Central Hub)
-- Owner: Ukwaththa U.K.A.A.N. (IT25101545)
-- Primary User: Campaign Manager
-- Precondition: Links to an EXISTING Client (Never creates new client)
-- ====================================================================
CREATE TABLE campaigns (
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
    CONSTRAINT fk_campaigns_client FOREIGN KEY (client_id)
        REFERENCES clients (id) ON DELETE RESTRICT,
    CONSTRAINT fk_campaigns_employee FOREIGN KEY (employee_id)
        REFERENCES employees (id) ON DELETE SET NULL,
    INDEX idx_campaigns_status (status),
    INDEX idx_campaigns_client (client_id)
) ENGINE=InnoDB;

-- ====================================================================
-- 3. ADVERTISEMENT DESIGN MANAGEMENT
-- Owner: Medagama M.S. (IT25101527)
-- Primary User: Graphic Designer (Client reviews/approves)
-- ====================================================================
CREATE TABLE advertisement_designs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    designer_id BIGINT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    file_path VARCHAR(500) NOT NULL,
    version INT NOT NULL DEFAULT 1,
    approval_status ENUM('PENDING', 'APPROVED', 'REVISION_REQUESTED', 'REJECTED') DEFAULT 'PENDING',
    client_feedback TEXT,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reviewed_at TIMESTAMP NULL,
    CONSTRAINT fk_designs_campaign FOREIGN KEY (campaign_id)
        REFERENCES campaigns (id) ON DELETE CASCADE,
    CONSTRAINT fk_designs_designer FOREIGN KEY (designer_id)
        REFERENCES employees (id) ON DELETE SET NULL,
    INDEX idx_designs_campaign (campaign_id),
    INDEX idx_designs_status (approval_status)
) ENGINE=InnoDB;

-- ====================================================================
-- 4. EMPLOYEE TASK MANAGEMENT
-- Owner: Rathnayaka R.M.B.G.T.A.B.R. (IT25101550)
-- Primary User: Campaign Manager (assigns) -> Employee (completes)
-- ====================================================================
CREATE TABLE tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    assigned_to_id BIGINT NOT NULL,
    assigned_by_id BIGINT NULL,
    task_name VARCHAR(255) NOT NULL,
    description TEXT,
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') DEFAULT 'MEDIUM',
    status ENUM('TODO', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') DEFAULT 'TODO',
    due_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_tasks_campaign FOREIGN KEY (campaign_id)
        REFERENCES campaigns (id) ON DELETE CASCADE,
    CONSTRAINT fk_tasks_assigned_to FOREIGN KEY (assigned_to_id)
        REFERENCES employees (id) ON DELETE CASCADE,
    CONSTRAINT fk_tasks_assigned_by FOREIGN KEY (assigned_by_id)
        REFERENCES employees (id) ON DELETE SET NULL,
    INDEX idx_tasks_campaign (campaign_id),
    INDEX idx_tasks_assigned_to (assigned_to_id),
    INDEX idx_tasks_status (status)
) ENGINE=InnoDB;

-- ====================================================================
-- 5. BILLING AND PAYMENT MANAGEMENT
-- Owner: Dias L.I.K. (IT25101521)
-- Primary User: Finance Officer (Client views)
-- ====================================================================
CREATE TABLE invoices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_number VARCHAR(50) NOT NULL UNIQUE,
    campaign_id BIGINT NOT NULL,
    client_id BIGINT NOT NULL,
    subtotal DECIMAL(12, 2) NOT NULL,
    tax_amount DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(12, 2) NOT NULL,
    paid_amount DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    status ENUM('UNPAID', 'PARTIALLY_PAID', 'PAID', 'CANCELLED') DEFAULT 'UNPAID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_invoices_campaign FOREIGN KEY (campaign_id)
        REFERENCES campaigns (id) ON DELETE RESTRICT,
    CONSTRAINT fk_invoices_client FOREIGN KEY (client_id)
        REFERENCES clients (id) ON DELETE RESTRICT,
    INDEX idx_invoices_campaign (campaign_id),
    INDEX idx_invoices_client (client_id),
    INDEX idx_invoices_status (status)
) ENGINE=InnoDB;

CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_id BIGINT NOT NULL,
    payment_reference VARCHAR(100) NOT NULL UNIQUE,
    amount DECIMAL(12, 2) NOT NULL,
    payment_method ENUM('BANK_TRANSFER', 'CREDIT_CARD', 'CHEQUE', 'CASH') NOT NULL,
    payment_date DATE NOT NULL,
    recorded_by_id BIGINT NULL,
    notes VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payments_invoice FOREIGN KEY (invoice_id)
        REFERENCES invoices (id) ON DELETE CASCADE,
    CONSTRAINT fk_payments_recorded_by FOREIGN KEY (recorded_by_id)
        REFERENCES employees (id) ON DELETE SET NULL,
    INDEX idx_payments_invoice (invoice_id)
) ENGINE=InnoDB;

-- ====================================================================
-- 6. REPORT MANAGEMENT
-- Owner: Marasinghe M.M.B.I. (IT25101538)
-- Primary User: System Administrator / Management
-- ====================================================================
CREATE TABLE saved_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    report_title VARCHAR(255) NOT NULL,
    report_type ENUM(
        'CAMPAIGN_PERFORMANCE',
        'CLIENT_SUMMARY',
        'FINANCIAL_SUMMARY',
        'EMPLOYEE_WORKLOAD'
    ) NOT NULL,
    generated_by_id BIGINT NOT NULL,
    date_from DATE NULL,
    date_to DATE NULL,
    filters_json TEXT NULL,
    file_path VARCHAR(500) NULL,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_reports_generated_by FOREIGN KEY (generated_by_id)
        REFERENCES users (id) ON DELETE RESTRICT,
    INDEX idx_reports_type (report_type)
) ENGINE=InnoDB;

-- ====================================================================
-- SEED DATA (Consistent with Project Specification & All Roles)
-- ====================================================================

-- 1. Users for all Stakeholder Roles
INSERT INTO users (id, username, password_hash, full_name, email, role, status, phone) VALUES
(1, 'admin', 'admin123_hashed', 'System Administrator', 'admin@creativepulse.com', 'ADMINISTRATOR', 'ACTIVE', '0112345670'),
(2, 'management', 'mgmt123_hashed', 'Agency Director', 'management@creativepulse.com', 'MANAGEMENT', 'ACTIVE', '0112345671'),
(3, 'sales.exec', 'sales123_hashed', 'Sunil Mendis', 'sunil@creativepulse.com', 'SALES_EXECUTIVE', 'ACTIVE', '0771112233'),
(4, 'campaign.mgr', 'camp123_hashed', 'Anura Ukwaththa', 'anura@creativepulse.com', 'CAMPAIGN_MANAGER', 'ACTIVE', '0772223344'),
(5, 'graphic.des', 'des123_hashed', 'Tharushi Silva', 'tharushi@creativepulse.com', 'GRAPHIC_DESIGNER', 'ACTIVE', '0773334455'),
(6, 'staff.kasun', 'staff123_hashed', 'Kasun Perera', 'kasun@creativepulse.com', 'EMPLOYEE', 'ACTIVE', '0774445566'),
(7, 'finance.off', 'fin123_hashed', 'Kumari Dias', 'kumari@creativepulse.com', 'FINANCE_OFFICER', 'ACTIVE', '0775556677'),
(8, 'client.abc', 'client123_hashed', 'Nimal Perera', 'nimal@abc.com', 'CLIENT', 'ACTIVE', '0771234567');

-- 2. Employees (Agency Staff)
INSERT INTO employees (id, user_id, name, email, role) VALUES
(1, 3, 'Sunil Mendis', 'sunil@creativepulse.com', 'Sales Executive'),
(2, 4, 'Anura Ukwaththa', 'anura@creativepulse.com', 'Campaign Manager'),
(3, 5, 'Tharushi Silva', 'tharushi@creativepulse.com', 'Graphic Designer'),
(4, 6, 'Kasun Perera', 'kasun@creativepulse.com', 'Advertising Staff'),
(5, 7, 'Kumari Dias', 'kumari@creativepulse.com', 'Finance Officer');

-- 3. Clients (Registered by Sales Executive)
INSERT INTO clients (id, user_id, company_name, contact_person, email, phone, address, created_by_sales_exec_id) VALUES
(1, 8, 'ABC Pvt Ltd', 'Nimal Perera', 'nimal@abc.com', '0771234567', 'No. 45, Galle Road, Colombo 03', 1),
(2, NULL, 'XYZ Holdings', 'Kamal Silva', 'kamal@xyz.com', '0717654321', 'No. 12, Peradeniya Road, Kandy', 1);

-- 4. Campaigns (Created by Campaign Manager - Ukwaththa U.K.A.A.N.)
INSERT INTO campaigns (id, campaign_name, description, start_date, deadline, progress, status, budget, client_id, employee_id, archived, cancelled) VALUES
(1, 'Summer Beverage Launch 2026', 'Comprehensive 360-degree social media, billboard, and digital ad campaign for a refreshing tropical fruit drink.', '2026-06-01', '2026-08-31', 45, 'ACTIVE', 750000.00, 1, 4, FALSE, FALSE),
(2, 'TechFest Annual Conference Promo', 'Branding, print flyers, and video ad promos for Sri Lanka tech summit.', '2026-09-01', '2026-11-15', 10, 'PLANNED', 500000.00, 2, 4, FALSE, FALSE),
(3, 'Autumn Seasonal Discount Push', 'Direct marketing and digital promotions targeting seasonal retail buyers.', '2026-04-01', '2026-05-30', 100, 'COMPLETED', 320000.00, 1, 4, FALSE, FALSE);

-- 5. Advertisement Designs (Uploaded by Graphic Designer - Medagama M.S.)
INSERT INTO advertisement_designs (id, campaign_id, designer_id, title, description, file_path, version, approval_status, client_feedback) VALUES
(1, 1, 3, 'Summer Drink Billboard v1', 'High-res vector banner design featuring mango splash.', '/uploads/designs/summer_billboard_v1.png', 1, 'REVISION_REQUESTED', 'Please make the logo slightly larger and brighten colors.'),
(2, 1, 3, 'Summer Drink Billboard v2', 'Updated billboard with requested enlarged logo and higher contrast.', '/uploads/designs/summer_billboard_v2.png', 2, 'APPROVED', 'Looks fantastic! Approved for printing.'),
(3, 1, 3, 'Instagram Story Ad Carousel', 'Set of 4 interactive animated story frames.', '/uploads/designs/summer_insta_story.png', 1, 'PENDING', NULL);

-- 6. Tasks (Assigned by Campaign Manager to Staff - Rathnayaka R.M.B.G.T.A.B.R.)
INSERT INTO tasks (id, campaign_id, assigned_to_id, assigned_by_id, task_name, description, priority, status, due_date) VALUES
(1, 1, 3, 2, 'Finalize Print Prepress File', 'Export billboard design in CMYK format 300 DPI for billboard printer.', 'HIGH', 'COMPLETED', '2026-06-20'),
(2, 1, 4, 2, 'Draft Social Media Copywriting', 'Write captions and hashtags for 10 Facebook & Instagram posts.', 'MEDIUM', 'IN_PROGRESS', '2026-07-05'),
(3, 2, 4, 2, 'Contact Media Outlets for TechFest', 'Send press kit to tech blogs and news journalists.', 'LOW', 'TODO', '2026-09-25');

-- 7. Invoices & Payments (Finance Officer - Dias L.I.K.)
INSERT INTO invoices (id, invoice_number, campaign_id, client_id, subtotal, tax_amount, total_amount, paid_amount, issue_date, due_date, status) VALUES
(1, 'INV-2026-001', 1, 1, 750000.00, 112500.00, 862500.00, 400000.00, '2026-06-05', '2026-07-05', 'PARTIALLY_PAID'),
(2, 'INV-2026-002', 3, 1, 320000.00, 48000.00, 368000.00, 368000.00, '2026-04-10', '2026-05-10', 'PAID');

INSERT INTO payments (id, invoice_id, payment_reference, amount, payment_method, payment_date, recorded_by_id, notes) VALUES
(1, 1, 'PAY-20260610-01', 400000.00, 'BANK_TRANSFER', '2026-06-10', 5, 'Advance 50% deposit received via Commercial Bank.'),
(2, 2, 'PAY-20260415-02', 368000.00, 'BANK_TRANSFER', '2026-04-15', 5, 'Full payment settled.');

-- 8. Saved Reports (Admin / Management - Marasinghe M.M.B.I.)
INSERT INTO saved_reports (id, report_title, report_type, generated_by_id, date_from, date_to, filters_json, file_path) VALUES
(1, 'Q2 2026 Campaign Performance Summary', 'CAMPAIGN_PERFORMANCE', 1, '2026-04-01', '2026-06-30', '{"status":"ALL","client_id":"ALL"}', '/reports/q2_campaign_summary.pdf'),
(2, 'Mid-Year Agency Financial Revenue', 'FINANCIAL_SUMMARY', 2, '2026-01-01', '2026-06-30', '{"include_tax":true}', '/reports/financial_h1_2026.pdf');
