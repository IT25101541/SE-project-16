USE CreativePulseDB;
GO
IF OBJECT_ID('dbo.tasks','U') IS NOT NULL DROP TABLE dbo.tasks;
GO
CREATE TABLE dbo.tasks(id INT IDENTITY(1,1) PRIMARY KEY,task_name NVARCHAR(150) NOT NULL,description NVARCHAR(1000),assigned_to NVARCHAR(150) NOT NULL,campaign_name NVARCHAR(150),priority NVARCHAR(20) NOT NULL,status NVARCHAR(30) NOT NULL,due_date DATE NOT NULL);
GO
INSERT INTO dbo.tasks(task_name,description,assigned_to,campaign_name,priority,status,due_date) VALUES('Create Facebook Ad Design','Prepare design concept','Graphic Designer','Summer Promotion','HIGH','IN_PROGRESS','2026-10-10'),('Prepare Client Report','Prepare campaign report','Campaign Manager','Summer Promotion','MEDIUM','PENDING','2026-10-15'),('Update Advertisement Content','Update approved content','Content Executive','New Product Launch','LOW','COMPLETED','2026-10-05');
GO
