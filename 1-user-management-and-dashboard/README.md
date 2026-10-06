# CreativePulse — Advertising Agency Management System

SE2030 Software Engineering · Year 2 Semester 1, 2026
Java web application (Spring Boot 3 + Thymeleaf + MySQL)

---

## 1. What you need to install first

| Tool | Version | Where |
|---|---|---|
| JDK | 17 or 21 | adoptium.net (Temurin) |
| IntelliJ IDEA | Community is enough | jetbrains.com |
| MySQL Server | 8.x | dev.mysql.com — or XAMPP (MySQL comes with it) |
| Git | latest | git-scm.com |

You do **not** need IntelliJ Ultimate and you do **not** need to install Tomcat separately — Tomcat is embedded inside the app.

---

## 2. Open the project in IntelliJ (5 steps)

1. Unzip `creativepulse.zip` somewhere without spaces in the path, e.g. `D:\SE2030\creativepulse`.
2. IntelliJ → **File → Open** → select the **`creativepulse` folder** (the one containing `pom.xml`) → OK → *Trust Project*.
3. Wait for the bottom status bar to finish "Resolving Maven dependencies". First time takes 2–5 minutes and **needs internet**.
4. **File → Project Structure → Project** → set *SDK* to **17** and *Language level* to **17**.
5. If Maven did not import automatically: right-click `pom.xml` → **Maven → Reload project**.

---

## 3. Set up the database (2 steps)

1. Start MySQL (XAMPP Control Panel → Start MySQL, or the MySQL service).
2. Open `src/main/resources/application.properties` and change **only** the password line:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

If your MySQL root has no password (default XAMPP), leave it empty: `spring.datasource.password=`

You do **not** need to create the database or any tables. `createDatabaseIfNotExist=true` creates the schema and `spring.jpa.hibernate.ddl-auto=update` creates all 8 tables from the `@Entity` classes the first time you run.

---

## 4. Run it

1. Open `src/main/java/com/creativepulse/CreativePulseApplication.java`.
2. Click the green ▶ next to `public class CreativePulseApplication`.
3. Wait for `Started CreativePulseApplication in ... seconds` in the Run window.
4. Open **http://localhost:8080** in your browser.

**Demo logins — password for all of them is `1234`:**

| Username | Role | Can access |
|---|---|---|
| `admin` | System Administrator | everything |
| `sales01` | Sales Executive | Clients |
| `manager01` | Campaign Manager | Campaigns, Tasks, Reports |
| `designer01` | Graphic Designer | Advertisements, Tasks |
| `finance01` | Finance Officer | Invoices & Payments |
| `client01` | Client | Dashboard only |

**If port 8080 is busy** (XAMPP Tomcat), change `server.port=8081` in `application.properties`.

---

## 5. What is already built (maps 1:1 to your proposal)

| # | Major function | Owner role | URL | CRUD |
|---|---|---|---|---|
| 1 | User Management & Dashboard | Administrator | `/users` | C R U D |
| 2 | Employee Task Management | Campaign Manager | `/tasks` | C R U D |
| 3 | Campaign Management | Campaign Manager | `/campaigns` | C R U D |
| 4 | Advertisement Management | Graphic Designer | `/advertisements` | C R U D + file upload + versioning |
| 5 | Billing & Payment Management | Finance Officer | `/invoices` | C R U D + payments + printable invoice |
| 6 | **Report Management** | Administrator / Management | `/reports` | **C R U D** + CSV/Excel export + PDF print |
| — | Client Management | Sales Executive | `/clients` | C R U D |

Minor functions: login, logout, role-based access control, search on every module, profile fields, data validation, access-denied page.

### Report Management CRUD (what your lecturer asked for)

| Action | Where | What happens |
|---|---|---|
| **Create** | Reports → *Generate Report* | Picks the matching Strategy class and builds the report body from live DB data |
| **Read** | Reports list / *View* | Search by title + filter by type |
| **Update** | *Edit* icon | Change title, type, period, notes → content is **regenerated** automatically |
| **Delete** | *Delete* icon | Confirmation dialog then removes it |
| Export | green spreadsheet icon | downloads `.csv` — opens directly in Excel |
| PDF | printer icon | printable page → browser *Print → Save as PDF* |

---

## 6. Validation — the error messages your lecturer wants to see

Two layers, both server-side (they cannot be bypassed by disabling JavaScript):

**Layer 1 — Bean Validation annotations on the entity** (`@NotBlank`, `@Email`, `@Pattern`, `@Size`, `@DecimalMin`, `@Min/@Max`, `@Digits`, `@PastOrPresent`). The message is written into the annotation and Thymeleaf shows it in red under the exact field.

**Layer 2 — business rules inside the controller** using `BindingResult.rejectValue(...)`.

### Demo script for the presentation — try these and the red errors appear

| Module | Enter this | Error shown |
|---|---|---|
| Users | Full name `Kasun123` | "Full name can only contain letters, spaces, . ' and -" |
| Users | Email `kasun.com` | "Please enter a valid email address" |
| Users | Phone `771234` | "Phone must be 10 digits and start with 0" |
| Users | Password `12` | "Password must be at least 4 characters long" |
| Users | Password ≠ Confirm | "Password and Confirm Password do not match" |
| Users | Username that already exists | "This username is already taken" |
| Campaigns | Budget `500` | "Budget must be at least LKR 1,000.00" |
| Campaigns | End date before start date | "End date must be after the start date" |
| Campaigns | Status = COMPLETED, progress 40 | "A completed campaign must have 100% progress" |
| Tasks | Due date yesterday | "Due date cannot be in the past" |
| Tasks | Due date after campaign end | "Due date cannot be later than the campaign end date (…)" |
| Ads | Upload a `.exe` | "Only these file types are allowed: jpg, jpeg, …" |
| Ads | Upload a file > 10 MB | "File is too large. Maximum allowed size is 10 MB." |
| Invoices | Due date before issue date | "Due date cannot be before the issue date" |
| Invoices | Campaign of a different client | "The selected campaign does not belong to the selected client" |
| Payments | Amount bigger than the balance | "Payment cannot be more than the outstanding balance (LKR …)" |
| Reports | 'To' date before 'From' date | "The 'to' date cannot be before the 'from' date" |
| Leave any required field blank | — | "… is required" |

---

## 7. Design patterns implemented (rubric: minimum 1, you have 5)

| Pattern | Class | Why it is used |
|---|---|---|
| **Singleton** | `pattern/InvoiceNumberGenerator` | One counter for the whole app → invoice numbers can never duplicate. Private constructor + static holder. |
| **Strategy** | `pattern/ReportStrategy` + 3 implementations | Each report type has its own building algorithm; `ReportService` does not care which one it uses. |
| **Factory** | `pattern/ReportStrategyFactory` | Returns the right strategy for the requested type — no `if/else` chain, no `new`. |
| **DAO / Repository** | `repository/*Repository` | Data access is separated from business logic. |
| **MVC** | `controller` / `model` / `templates` | Standard layered separation. |

Adding a 4th report type later = add **one** new class implementing `ReportStrategy`. Nothing else changes (Open/Closed Principle). That is a great viva answer.

---

## 8. Project structure (learn this for the viva)

```
src/main/java/com/creativepulse/
├── CreativePulseApplication.java   ← run this
├── config/
│   ├── SecurityConfig.java         ← login + which role can open which URL
│   ├── WebConfig.java              ← dropdown-ID ⇄ entity conversion, /uploads
│   └── DataSeeder.java             ← creates the 6 demo accounts
├── model/       (8 entities)       ← DB tables + validation annotations
├── repository/  (8 interfaces)     ← DAO layer (Spring Data JPA)
├── service/     (10 classes)       ← business logic
├── controller/  (9 classes)        ← handles the HTTP requests
└── pattern/                        ← Singleton, Strategy, Factory
src/main/resources/
├── application.properties          ← DB settings
├── templates/                      ← Thymeleaf HTML pages
└── static/css/app.css              ← styling
```

**Request flow to memorise:**
Browser → `Controller` → `@Valid` validation → `Service` → `Repository` → MySQL → back to `Controller` → Thymeleaf template → HTML page.

### The 8 database tables
`users`, `clients`, `campaigns`, `tasks`, `advertisements`, `invoices`, `payments`, `reports`

### Relationships (for your ER diagram in the design document)
- Client **1 — M** Campaign
- Campaign **1 — M** Task, **1 — M** Advertisement
- User **1 — M** Task (assignee), **1 — M** Campaign (manager), **1 — M** Advertisement (uploader)
- Client **1 — M** Invoice · Campaign **1 — M** Invoice
- Invoice **1 — M** Payment

---

## 9. Suggested split of work for the 6 members

| Member | Module | Files to own and be able to explain |
|---|---|---|
| 1 | User Management & Dashboard | `User`, `UserRepository`, `UserService`, `UserController`, `templates/users/*`, `dashboard.html`, `SecurityConfig` |
| 2 | Employee Task Management | `TaskItem`, `TaskRepository`, `TaskService`, `TaskController`, `templates/tasks/*` |
| 3 | Campaign Management | `Campaign`, `CampaignRepository`, `CampaignService`, `CampaignController`, `templates/campaigns/*` |
| 4 | Advertisement Management | `Advertisement`, `AdvertisementService`, `FileStorageService`, `AdvertisementController`, `templates/advertisements/*` |
| 5 | Billing & Payments | `Invoice`, `Payment`, `InvoiceService`, `PaymentService`, `InvoiceController`, `InvoiceNumberGenerator`, `templates/invoices/*` |
| 6 | Report Management | `Report`, `ReportService`, `ReportController`, all of `pattern/` (Strategy + Factory), `templates/reports/*` |

Everyone should also be able to explain: MVC flow, how validation works, and one design pattern.

---

## 10. Push to GitHub (the spec requires a repo link)

In the IntelliJ terminal, inside the project folder:

```bash
git init
git add .
git commit -m "CreativePulse - Advertising Agency Management System"
git branch -M main
git remote add origin https://github.com/YOUR-TEAM/creativepulse.git
git push -u origin main
```

Then give your lecturer access. Make each member commit their own module so individual contribution is visible in the history.

---

## 11. Common problems and fixes

| Problem | Fix |
|---|---|
| `Access denied for user 'root'@'localhost'` | Wrong password in `application.properties` |
| `Communications link failure` | MySQL service is not started |
| `Port 8080 was already in use` | Set `server.port=8081` |
| `Invalid bound statement` / red imports everywhere | Maven → Reload project; check internet |
| `java: invalid source release 17` | Project Structure → SDK = 17 |
| Page shows "Whitelabel Error 403" | That role has no permission — log in as `admin` |
| Changes to HTML not showing | Save the file, then refresh (DevTools restart is automatic) |
| Uploaded file not opening | Check the `uploads` folder exists in the project root |

---

## 12. What to do before Week 10 (Progress) and Week 13 (Final)

**Week 10 — 75% complete + Design Document:** you already have far more than 75%. Draw the Use Case, Class and Activity diagrams from the code in section 8, write the sprint summaries, and add the ethical considerations (password hashing with BCrypt, role-based access, client data privacy, file-upload restrictions, accessibility labels — all already implemented, so you can point at real code).

**Week 13 — Final:** add screenshots of every module to the final report, record who built what, and make sure **each member can answer questions about their own code**. The spec is explicit that work you cannot explain counts as unauthorised assistance — so read through your module line by line and change/extend things so it is genuinely yours.

Good ideas to extend it and make it your own: email notifications, a campaign calendar view, charts on the dashboard (Chart.js), an approval workflow for advertisements, or PDF export using iText.
