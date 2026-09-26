# CreativePulse Advertising Agency Management System

Spring Boot + JSP + SQL Server starter project for SLIIT SE2030.

## Requirements
- JDK 17
- IntelliJ IDEA
- Maven (IntelliJ can use the Maven wrapper/imported Maven)
- SQL Server + SSMS

## Run
1. Open this folder in IntelliJ IDEA.
2. Set Project SDK to JDK 17.
3. Let Maven download dependencies.
4. In SQL Server Management Studio run `src/main/resources/sql/database.sql`.
5. If your SA password is not `password`, change it in `src/main/resources/application.properties`.
6. Run `CreativePulseApplication`.
7. Open http://localhost:8080/login

Demo login:
- Username: admin
- Password: admin123

The six modules are separate menu entries and pages:
User Management, Employee Task Management, Campaign Management, Advertisement Management, Billing & Payment, Report Management.

This is a clean starter/demo implementation. CRUD controllers/DAOs and real report exports should be connected to the database as the next development step.
