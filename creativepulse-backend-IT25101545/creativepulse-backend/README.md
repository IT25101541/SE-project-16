# CreativePulse Advertising – Backend (Client Management + Campaign Management)

SE2030 – Year 2 Semester 1 (2026). Spring Boot 3 · Java 17 · JDBC + DAO · SQL Server.

## Setup
1. Run `src/main/resources/schema.sql` in SQL Server (creates `CreativePulseDB`, tables, sample clients).
2. Set your `sa` password in `src/main/resources/application.properties`.
3. Open the folder in IntelliJ (it detects `pom.xml`), then run `CreativePulseApplication`.
4. API runs at http://localhost:8080

## Client Management  `/api/clients`
| Method | URL | Purpose |
|---|---|---|
| POST | /api/clients | Register client |
| GET | /api/clients?search=text | List / search |
| GET | /api/clients/{id} | View |
| PUT | /api/clients/{id} | Update |
| DELETE | /api/clients/{id} | Delete (blocked if client has campaigns) |

## Campaign Management  `/api/campaigns`
| Method | URL | Purpose |
|---|---|---|
| POST | /api/campaigns | Create |
| GET | /api/campaigns?status=&search= | List / filter |
| GET | /api/campaigns/{id} | View |
| PUT | /api/campaigns/{id} | Update all details |
| PATCH | /api/campaigns/{id}/status | Update status + progress |
| PATCH | /api/campaigns/{id}/schedule | Change start/end dates |
| DELETE | /api/campaigns/{id} | Remove |
| GET/POST | /api/campaigns/{id}/assignments | View / assign team member |
| DELETE | /api/campaigns/{id}/assignments/{userId} | Unassign |
| GET | /api/campaigns/summary | Count by status (dashboard cards) |

### Sample JSON
Client: `{"companyName":"Lanka Foods","contactPerson":"Nimal","email":"n@lf.lk","phone":"0771234567","industry":"Food","status":"ACTIVE"}`

Campaign: `{"clientId":1,"name":"Avurudu Promo","description":"Festival ads","budget":150000,"startDate":"2026-04-01","endDate":"2026-04-30","status":"PLANNED","progress":0}`

Status values: PLANNED, IN_PROGRESS, ON_HOLD, COMPLETED, CANCELLED.

## Notes
- Team assignment needs rows in the `users` table (User Management teammate). Keep PK `user_id` and name column `full_name`.
- Package root is `com.creativepulse`, so it merges cleanly with teammates' modules.
