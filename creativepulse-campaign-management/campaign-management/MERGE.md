# How to add Campaign Management to the User Management project

This zip is a drop-in module. It uses the login, roles and navbar from the User Management & Dashboard project,
so it must be added to that project (it does not run on its own).

## 1. Copy the files

Unzip and copy the `src` folder into the root of the User Management project and let your system merge the folders.
No existing file is overwritten (the guides are named `CAMPAIGN_README.md` and `CAMPAIGN_COMMIT_GUIDE.md` for that reason). The new code lives in the sub-package
`com.creativepulse.usermanagement.campaign`, so Spring Boot finds it without changing the main class.

## 2. Two small edits (optional but recommended)

### a) Dashboard card links to the module - `model/Role.java`

Change the `#` to `/campaigns` on the Campaign Manager card, and give the admin the same card:

```java
ADMIN("System Administrator", List.of(
        new ModuleLink("User Management", "Register users, assign roles, suspend or reactivate accounts.", "/admin/users"),
        new ModuleLink("Campaigns", "Create, assign and monitor advertising campaigns.", "/campaigns"),
        new ModuleLink("Reports", "System-wide campaign, task and financial reports.", "#"))),

MANAGER("Campaign Manager", List.of(
        new ModuleLink("Campaigns", "Create, assign and monitor advertising campaigns.", "/campaigns"),
        new ModuleLink("Employee Tasks", "Create tasks, assign employees and track workload.", "#"))),
```

### b) "Campaigns" link in the top bar - `templates/fragments.html`

Add this line inside `<nav class="topbar__nav">`, right after the Dashboard link:

```html
<a th:href="@{/campaigns}" sec:authorize="hasAnyRole('ADMIN','MANAGER','SALES','DESIGNER','EMPLOYEE','FINANCE')"
   th:classappend="${active == 'campaigns'} ? 'is-active'"
   th:attr="aria-current=${active == 'campaigns'} ? 'page'">Campaigns</a>
```

## 3. Run

```bash
mvn test
mvn spring-boot:run
```

Sign in as `manager@creativepulse.lk` / `Password@123` and open **Campaigns**. The `campaigns` table is created
automatically, and three sample campaigns are added on first start (turn them off with `app.seed-demo-users=false`).
