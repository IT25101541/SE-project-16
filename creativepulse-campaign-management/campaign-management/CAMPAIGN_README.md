# CreativePulse Advertising - Campaign Management

Module for **Campaign Management** (SE2030, Group 16). Java 17, Spring Boot 3, Thymeleaf, Spring Data JPA, plain CSS.
It plugs into the User Management & Dashboard project - see `MERGE.md`.

## What it does (basic operations)

| Operation | Who | Notes |
|-----------|-----|-------|
| Create a campaign | Admin, Campaign Manager | Name, client, category, description, start and end date, budget, optional team member. Starts in **Planning** at 0%. |
| Edit a campaign | Admin, Campaign Manager | Change any detail, the schedule, the **status** and the **progress**. Completed campaigns are set to 100% automatically. |
| View list and details | Admin, Campaign Manager, Sales, Designer, Employee, Finance | Filter by status, search by campaign or client name, overdue campaigns are flagged. |
| Assign to a team member | Admin, Campaign Manager | Pick from active Managers, Designers and Employees. |
| Remove a campaign | Admin, Campaign Manager | Only campaigns whose status is **Cancelled** can be removed. |

Clients have no access to this module. Everyone else must be signed in. The role rules are enforced on the server
(`@PreAuthorize` in `CampaignController`), and the buttons are hidden for roles that cannot use them.

Validation: name and client are required, end date cannot be before the start date, budget cannot be negative
(max 10 digits and 2 decimals), progress is 0-100, and an assignee must be an active team member.

## Structure

```
src/main/java/com/creativepulse/usermanagement/campaign
  config/        CampaignSecurityConfig (turns on @PreAuthorize), CampaignDataInitializer (sample data)
  controller/    CampaignController
  dto/           CampaignForm
  model/         Campaign, CampaignStatus, CampaignCategory
  repository/    CampaignRepository, TeamMemberRepository
  service/       CampaignService, CampaignNotFoundException
src/main/resources
  templates/campaigns/   list.html, detail.html, form.html
  static/css/            campaign.css
src/test/java/.../campaign   CampaignManagementTests
```

## Routes

| Method | Path | Who |
|--------|------|-----|
| GET | `/campaigns` (query: `q`, `status`) | Staff roles |
| GET | `/campaigns/{id}` | Staff roles |
| GET / POST | `/campaigns/new` | Admin, Manager |
| GET / POST | `/campaigns/{id}/edit` | Admin, Manager |
| POST | `/campaigns/{id}/delete` | Admin, Manager |

## Design notes for the team

- The assigned team member and the creator are saved as an id plus a name (no database foreign key to `users`),
  so deleting a user in User Management never breaks a campaign. The name is a snapshot taken when the campaign is saved.
- The client is a plain text name for now. When the Client module is merged, replace `clientName` with a reference to it.
- Status values: Planning, Active, On hold, Completed, Cancelled. Task, Advertisement and Billing modules can link to
  `Campaign` by its `id`.

## Not included (not needed for the basic version)

Notifications to the assigned team member, workload view for assigning by availability, campaign history/audit log,
and file attachments.
