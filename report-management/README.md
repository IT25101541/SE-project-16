# Report Management Module

Report Management module for an Advertising Firm SE project. Built with **Java 17, Spring Boot 3, Spring Data JPA, Thymeleaf, and MySQL**.

## Features
- Create, edit, delete, and view reports
- Attach and download files
- Categorize reports (Campaign, Budget, Performance, Client, Other)
- Report status workflow: PENDING → APPROVED / REJECTED
- Admin dashboard to review and update the status of all reports

## Setup (IntelliJ IDEA)

1. **Open the project**: `File > Open` and select this folder. IntelliJ will detect it as a Maven project and download dependencies automatically.
2. **Create the database**: Make sure MySQL is running locally. The app will auto-create the `reportdb` database on first run (`createDatabaseIfNotExist=true`), but MySQL itself must be running.
3. **Set your credentials**: open `src/main/resources/application.properties` and update:
   ```
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   ```
4. **Run**: open `ReportManagementApplication.java` and run the `main` method (or `mvn spring-boot:run`).
5. **Visit**:
   - `http://localhost:8080/reports` — user's report list
   - `http://localhost:8080/reports/new` — create a report
   - `http://localhost:8080/reports/admin` — admin dashboard

## Notes
- `username=demoUser` is currently a placeholder default request param standing in for a logged-in user. Wire this up to your team's authentication module (e.g. pull the username from the session) once it's ready.
- Uploaded files are stored in `uploads/reports/` at the project root (ignored by git — see `.gitignore`).
- Table `reports` is auto-created/updated by Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

## Project Structure
```
src/main/java/com/advfirm/reportmanagement/
 ├── ReportManagementApplication.java
 ├── model/Report.java
 ├── repository/ReportRepository.java
 ├── service/ReportService.java
 └── controller/ReportController.java

src/main/resources/
 ├── application.properties
 ├── templates/reports/ (list, form, view, admin .html)
 └── static/css/style.css
```
