CREATE DATABASE CreativePulseDB;
GO
USE CreativePulseDB;
GO

CREATE TABLE clients (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(120) NOT NULL,
    email NVARCHAR(180) NOT NULL UNIQUE,
    phone NVARCHAR(30),
    company NVARCHAR(160),
    status NVARCHAR(30) NOT NULL DEFAULT 'Active',
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);

CREATE TABLE campaigns (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(180) NOT NULL,
    client_name NVARCHAR(160) NOT NULL,
    description NVARCHAR(1000),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status NVARCHAR(40) NOT NULL DEFAULT 'Planned',
    progress INT NOT NULL DEFAULT 0 CHECK(progress BETWEEN 0 AND 100),
    manager_name NVARCHAR(160) NOT NULL,
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME()
);

CREATE TABLE tasks (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    title NVARCHAR(180) NOT NULL,
    employee_name NVARCHAR(160) NOT NULL,
    priority NVARCHAR(30) NOT NULL,
    status NVARCHAR(40) NOT NULL,
    deadline DATE NOT NULL,
    created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    CONSTRAINT FK_tasks_campaign FOREIGN KEY(campaign_id) REFERENCES campaigns(id)
);

CREATE INDEX IX_campaigns_status ON campaigns(status);
CREATE INDEX IX_tasks_campaign ON tasks(campaign_id);
CREATE INDEX IX_tasks_status ON tasks(status);

INSERT INTO clients(name,email,phone,company,status) VALUES
('Demo Client','client@creativepulse.com','0771234567','Demo Brand','Active');

INSERT INTO campaigns(name,client_name,description,start_date,end_date,status,progress,manager_name) VALUES
('Summer Brand Launch','Demo Client','Integrated advertising campaign for summer launch',
'2026-10-01','2026-11-30','Active',45,'Campaign Manager');

INSERT INTO tasks(campaign_id,title,employee_name,priority,status,deadline) VALUES
(1,'Prepare campaign creative brief','Graphic Designer','High','In Progress','2026-10-15'),
(1,'Review client feedback','Campaign Executive','Medium','Pending','2026-10-18');
