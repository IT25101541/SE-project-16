# CreativePulse Advertising – Campaign Manager & Client Module

## IntelliJ setup
1. Open this folder in IntelliJ IDEA as a Maven project.
2. Use JDK 17.
3. Make sure Maven reloads `pom.xml`.
4. Create SQL Server database by running `src/main/resources/schema.sql`.
5. Edit `src/main/resources/application.properties` and replace `YOUR_SQL_SERVER_PASSWORD`.
6. Start `CreativePulseApplication`.
7. Open `http://localhost:8080/login`.

## Implemented in this member module
- Campaign Manager Dashboard
- Campaign Management: Create / Read / Update / Delete
- Employee Task Management: separate from Campaign Management, Create / Read / Update / Delete
- Client Management: Create / Read / Update / Delete
- SQL Server JDBC/DAO layer
- JSP + CSS responsive dashboard UI
- Separate navigation entries

## Important integration note
The uploaded assignment PDF is an IT2011 AI/ML specification, not the SE2030 CreativePulse software engineering specification. This project therefore follows the CreativePulse requirements supplied in the chat.

## Final integrated project
Authentication/BCrypt, role permissions, Advertisement Management, Billing & Payment, Report Management, PDF/Excel export and the remaining team members' modules should be integrated into the team's master project rather than duplicated in this member branch.
