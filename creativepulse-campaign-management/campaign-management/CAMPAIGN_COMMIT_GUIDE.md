# Suggested commit order

Run from the root of the project after copying the module in (see `MERGE.md`).

| # | Commit message | Files to add |
|---|----------------|--------------|
| 1 | Add campaign model, status and category enums | `src/main/java/com/creativepulse/usermanagement/campaign/model/` |
| 2 | Add campaign repositories | `campaign/repository/` |
| 3 | Add campaign form with validation | `campaign/dto/` |
| 4 | Add campaign service (create, edit, remove, search) | `campaign/service/` |
| 5 | Add campaign controller with role-based access | `campaign/controller/`, `campaign/config/CampaignSecurityConfig.java` |
| 6 | Add campaign list and detail pages | `templates/campaigns/list.html`, `templates/campaigns/detail.html` |
| 7 | Add create/edit campaign form | `templates/campaigns/form.html` |
| 8 | Add campaign styles | `static/css/campaign.css` |
| 9 | Link campaigns from dashboard and top bar | your edits to `model/Role.java` and `templates/fragments.html` |
| 10 | Add sample campaign data | `campaign/config/CampaignDataInitializer.java` |
| 11 | Add campaign tests and docs | `src/test/.../campaign/`, `CAMPAIGN_README.md`, `MERGE.md` |

Commits 6-8 only work once 1-5 are in, so keep this order.
