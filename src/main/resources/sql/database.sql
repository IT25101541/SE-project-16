CREATE DATABASE CreativePulseDB;
GO
USE CreativePulseDB;
GO

CREATE TABLE roles (
    role_id INT IDENTITY PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users (
    user_id INT IDENTITY PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    username VARCHAR(80) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    active BIT NOT NULL DEFAULT 1,
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

CREATE TABLE clients (
    client_id INT IDENTITY PRIMARY KEY,
    client_name VARCHAR(120) NOT NULL,
    email VARCHAR(150),
    phone VARCHAR(30),
    company_name VARCHAR(150),
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);

CREATE TABLE campaigns (
    campaign_id INT IDENTITY PRIMARY KEY,
    client_id INT NOT NULL,
    campaign_name VARCHAR(150) NOT NULL,
    objective VARCHAR(500),
    budget DECIMAL(12,2) DEFAULT 0,
    start_date DATE,
    end_date DATE,
    status VARCHAR(30) NOT NULL DEFAULT 'Planning',
    FOREIGN KEY (client_id) REFERENCES clients(client_id)
);

CREATE TABLE campaign_assignments (
    assignment_id INT IDENTITY PRIMARY KEY,
    campaign_id INT NOT NULL,
    user_id INT NOT NULL,
    assignment_role VARCHAR(60),
    FOREIGN KEY (campaign_id) REFERENCES campaigns(campaign_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE tasks (
    task_id INT IDENTITY PRIMARY KEY,
    campaign_id INT NOT NULL,
    task_title VARCHAR(180) NOT NULL,
    description VARCHAR(1000),
    priority VARCHAR(20) NOT NULL DEFAULT 'Medium',
    deadline DATE,
    status VARCHAR(30) NOT NULL DEFAULT 'Assigned',
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    FOREIGN KEY (campaign_id) REFERENCES campaigns(campaign_id)
);

CREATE TABLE task_assignments (
    task_assignment_id INT IDENTITY PRIMARY KEY,
    task_id INT NOT NULL,
    employee_id INT NOT NULL,
    assigned_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    FOREIGN KEY (task_id) REFERENCES tasks(task_id),
    FOREIGN KEY (employee_id) REFERENCES users(user_id)
);

CREATE TABLE advertisements (
    advertisement_id INT IDENTITY PRIMARY KEY,
    campaign_id INT NOT NULL,
    title VARCHAR(180) NOT NULL,
    description VARCHAR(1000),
    status VARCHAR(40) NOT NULL DEFAULT 'Pending Client Review',
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    FOREIGN KEY (campaign_id) REFERENCES campaigns(campaign_id)
);

CREATE TABLE advertisement_versions (
    version_id INT IDENTITY PRIMARY KEY,
    advertisement_id INT NOT NULL,
    version_no INT NOT NULL,
    file_path VARCHAR(500),
    client_comment VARCHAR(1000),
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    FOREIGN KEY (advertisement_id) REFERENCES advertisements(advertisement_id)
);

CREATE TABLE invoices (
    invoice_id INT IDENTITY PRIMARY KEY,
    campaign_id INT NOT NULL,
    invoice_number VARCHAR(50) NOT NULL UNIQUE,
    amount DECIMAL(12,2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'Pending',
    invoice_date DATE NOT NULL DEFAULT CAST(GETDATE() AS DATE),
    FOREIGN KEY (campaign_id) REFERENCES campaigns(campaign_id)
);

CREATE TABLE payments (
    payment_id INT IDENTITY PRIMARY KEY,
    invoice_id INT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    payment_date DATE NOT NULL DEFAULT CAST(GETDATE() AS DATE),
    method VARCHAR(50),
    notes VARCHAR(500),
    FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id)
);

CREATE TABLE payment_history (
    history_id INT IDENTITY PRIMARY KEY,
    payment_id INT NOT NULL,
    action VARCHAR(100),
    action_time DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    FOREIGN KEY (payment_id) REFERENCES payments(payment_id)
);

CREATE TABLE login_history (
    login_id INT IDENTITY PRIMARY KEY,
    user_id INT NULL,
    login_time DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    success BIT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE reports (
    report_id INT IDENTITY PRIMARY KEY,
    report_name VARCHAR(150) NOT NULL,
    report_type VARCHAR(50),
    generated_by INT NULL,
    generated_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    FOREIGN KEY (generated_by) REFERENCES users(user_id)
);

INSERT INTO roles(role_name) VALUES
('ADMIN'), ('SALES_EXECUTIVE'), ('CAMPAIGN_MANAGER'),
('GRAPHIC_DESIGNER'), ('EMPLOYEE'), ('FINANCE_OFFICER'),
('CLIENT'), ('MANAGEMENT');

-- Demo login: admin / admin123
INSERT INTO users(full_name,email,username,password_hash,role_id)
VALUES ('System Administrator','admin@creativepulse.local','admin',
        '$2a$10$7EqJtq98hPqEX7fNZaFWoO5R1vL1Qq7Ww3n6vQhFQ0W0m3JxJvW5K',
        1);
