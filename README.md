# CreativePulse Advertising - User Management & Dashboard

Module 1 of the SE2030 group project (Group 16): **User Management & Dashboard**.
Built with Java 17, Spring Boot 3, Spring Security, Spring Data JPA, Thymeleaf and plain CSS.

## Features

| Area | What it does |
|------|--------------|
| Sign up | Clients register themselves (name, email, phone, password, consent notice). Passwords are hashed with BCrypt. |
| Sign in / sign out | Email + password login, suspended accounts are blocked, last sign-in time is recorded. |
| Role-based dashboard | 7 roles (Admin, Sales, Campaign Manager, Designer, Finance, Employee, Client). Each role sees its own module cards. |
| System statistics (Admin) | Total / active / suspended users, new users in 7 days, users by role, recent registrations. |
| Register user - UM01 (Admin) | Admin enters details, picks one role, system generates a temporary password and creates an Active account. |
| Manage users (Admin) | Search, change role, suspend / reactivate, delete. An admin cannot suspend, delete or re-role themselves. |
| My profile | Update name and phone, change password (temporary-password banner until changed). |
| Access control | `/admin/**` is ADMIN only, everything else needs sign in, forms are CSRF protected. |

## Run it

Requirements: JDK 17+ and Maven 3.8+.

```bash
mvn spring-boot:run
```

Open http://localhost:8080 . The database is a file-based H2 database (`./data/`), so no setup is needed.

### Demo accounts (created on first start)

| Role | Email | Password |
|------|-------|----------|
| Administrator | admin@creativepulse.lk | Admin@123 |
| Sales Executive | sales@creativepulse.lk | Password@123 |
| Campaign Manager | manager@creativepulse.lk | Password@123 |
| Graphic Designer | designer@creativepulse.lk | Password@123 |
| Finance Officer | finance@creativepulse.lk | Password@123 |
| Employee | employee@creativepulse.lk | Password@123 |
| Client | client@creativepulse.lk | Password@123 |

Set `app.seed-demo-users=false` in `application.properties` to stop creating the demo accounts
(the admin account is always created so someone can register other users). Change the admin password after first sign in.

### Use MySQL instead of H2

Comment the H2 lines in `src/main/resources/application.properties`, uncomment the MySQL ones and set your password.
The MySQL driver is already in `pom.xml`.

### Tests

```bash
mvn test
```

## Project structure

```
src/main/java/com/creativepulse/usermanagement
  UserManagementApplication.java
  config/        SecurityConfig, DataInitializer
  controller/    AuthController, DashboardController, AdminUserController, ProfileController, GlobalModelAttributes
  dto/           RegistrationForm, AdminUserForm, ProfileForm, ChangePasswordForm
  model/         User, Role, UserStatus
  repository/    UserRepository
  security/      CustomUserDetailsService, LoginSuccessHandler
  service/       UserService, DashboardStats, CreatedUser, DuplicateEmailException
src/main/resources
  templates/     fragments, login, register, dashboard, profile, access-denied, admin/users, admin/user-form
  static/css/    style.css
  application.properties
src/test/java    UserManagementTests
```

## Routes

| Method | Path | Who |
|--------|------|-----|
| GET / POST | `/register` | Anyone |
| GET / POST | `/login`, POST `/logout` | Anyone / signed in |
| GET | `/dashboard` | Signed in |
| GET / POST | `/profile`, POST `/profile/password` | Signed in |
| GET | `/admin/users` | Admin |
| GET / POST | `/admin/users/new` | Admin |
| POST | `/admin/users/{id}/role`, `/suspend`, `/reactivate`, `/delete` | Admin |

## Joining this with the other modules

- `User` and `Role` are the shared account model. Other modules can look up the signed-in user through `UserService.getByEmail(authentication.getName())`.
- Dashboard module cards for other members' modules link to `#` for now. When a module is merged, change its path in `model/Role.java`.
- Role names match the class diagram in the Design Document (`ADMIN, SALES, MANAGER, DESIGNER, FINANCE, EMPLOYEE, CLIENT`).

## Not included yet (planned for Sprint 5 in the design document)

Password reset by email, email/SMS notification delivery (the temporary password is shown once on screen instead), and the full login/audit history page.
