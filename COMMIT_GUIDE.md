# Suggested commit order

Add the module in small steps so the history shows how it was built. Run these from the repo root
(adjust the folder if you put the module in a sub-folder of your team repo).

| # | Commit message | Files to add |
|---|----------------|--------------|
| 1 | Add Spring Boot project setup | `pom.xml`, `.gitignore`, `src/main/java/.../UserManagementApplication.java`, `src/main/resources/application.properties` |
| 2 | Add User entity, Role and UserStatus | `model/` , `repository/UserRepository.java` |
| 3 | Add user service and registration/admin DTOs | `dto/`, `service/` |
| 4 | Add Spring Security login, logout and role-based access | `security/`, `config/SecurityConfig.java`, `config/DataInitializer.java` |
| 5 | Add register and login pages | `controller/AuthController.java`, `controller/GlobalModelAttributes.java`, `templates/fragments.html`, `login.html`, `register.html`, `access-denied.html` |
| 6 | Add role-based dashboard | `controller/DashboardController.java`, `templates/dashboard.html` |
| 7 | Add admin user management (UM01) | `controller/AdminUserController.java`, `templates/admin/` |
| 8 | Add profile page and password change | `controller/ProfileController.java`, `templates/profile.html` |
| 9 | Add stylesheet | `static/css/style.css` |
| 10 | Add tests and README | `src/test/`, `README.md` |

Example:

```bash
git add pom.xml .gitignore src/main/resources/application.properties src/main/java/com/creativepulse/usermanagement/UserManagementApplication.java
git commit -m "Add Spring Boot project setup"
```

Commits 5-9 only compile once commits 1-4 are in, so keep this order.
