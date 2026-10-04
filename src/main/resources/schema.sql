-- Run this in SQL Server Management Studio (or IntelliJ Database tool) BEFORE starting the app.
IF DB_ID('CreativePulseDB') IS NULL CREATE DATABASE CreativePulseDB;
GO
USE CreativePulseDB;
GO

-- Minimal users table (owned by the User Management teammate).
-- If your teammate already created it, skip this block and keep user_id as the PK name.
IF OBJECT_ID('users') IS NULL
CREATE TABLE users (
    user_id       INT IDENTITY(1,1) PRIMARY KEY,
    full_name     NVARCHAR(100) NOT NULL,
    email         NVARCHAR(150) NOT NULL UNIQUE,
    password_hash NVARCHAR(255) NOT NULL,
    role          NVARCHAR(40)  NOT NULL,
    created_at    DATETIME2 DEFAULT SYSDATETIME()
);
GO

-- ===== CLIENT MANAGEMENT =====
IF OBJECT_ID('clients') IS NULL
CREATE TABLE clients (
    client_id      INT IDENTITY(1,1) PRIMARY KEY,
    company_name   NVARCHAR(150) NOT NULL,
    contact_person NVARCHAR(100) NOT NULL,
    email          NVARCHAR(150) NOT NULL UNIQUE,
    phone          NVARCHAR(30),
    address        NVARCHAR(255),
    industry       NVARCHAR(80),
    status         NVARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
                   CHECK (status IN ('ACTIVE','INACTIVE')),
    created_at     DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);
CREATE INDEX ix_clients_company ON clients(company_name);
GO

-- ===== CAMPAIGN MANAGEMENT =====
IF OBJECT_ID('campaigns') IS NULL
CREATE TABLE campaigns (
    campaign_id INT IDENTITY(1,1) PRIMARY KEY,
    client_id   INT NOT NULL REFERENCES clients(client_id),
    name        NVARCHAR(150) NOT NULL,
    description NVARCHAR(1000),
    budget      DECIMAL(12,2) NOT NULL DEFAULT 0,
    start_date  DATE NOT NULL,
    end_date    DATE NOT NULL,
    status      NVARCHAR(20) NOT NULL DEFAULT 'PLANNED'
                CHECK (status IN ('PLANNED','IN_PROGRESS','ON_HOLD','COMPLETED','CANCELLED')),
    progress    INT NOT NULL DEFAULT 0 CHECK (progress BETWEEN 0 AND 100),
    created_at  DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    CONSTRAINT ck_campaign_dates CHECK (end_date >= start_date)
);
CREATE INDEX ix_campaigns_client ON campaigns(client_id);
CREATE INDEX ix_campaigns_status ON campaigns(status);
GO

IF OBJECT_ID('campaign_assignments') IS NULL
CREATE TABLE campaign_assignments (
    assignment_id INT IDENTITY(1,1) PRIMARY KEY,
    campaign_id   INT NOT NULL REFERENCES campaigns(campaign_id) ON DELETE CASCADE,
    user_id       INT NOT NULL REFERENCES users(user_id),
    role_in_team  NVARCHAR(60),
    assigned_at   DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    CONSTRAINT uq_campaign_user UNIQUE (campaign_id, user_id)
);
GO

-- Sample data for demo
IF NOT EXISTS (SELECT 1 FROM clients)
INSERT INTO clients(company_name,contact_person,email,phone,industry)
VALUES ('Lanka Foods','Nimal Perera','nimal@lankafoods.lk','0771234567','Food'),
       ('Ceylon Tech','Sara Fernando','sara@ceylontech.lk','0712345678','Technology');
GO
