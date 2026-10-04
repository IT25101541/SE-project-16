# Campaign Management module

Branch: `feature/campaign-management`

## What is in this branch
- `Campaign`, `CampaignStatus`, `CampaignRepository`, `CampaignService`, `CampaignController` (`/campaigns`)
- Client CRUD (`/clients`) and `Client`/`Employee` entities, repositories, converters, `SeedData`
- Templates under `templates/campaign`, `templates/client`, `templates/employee`
- `static/css/campaign.css`, `static/js/campaign.js`
- `db/02_campaign.sql` (run after `db/01_users.sql`)

## Suggested commit order
1. Add Spring Boot project setup: `pom.xml`, `.gitignore`, `.gitattributes`, `mvnw`, `.mvn/`, `application.properties`, `CreativePulseApplication.java`, test files
2. Add shared user entities and security config: `entity/User*.java`, `config/SecurityConfig.java`
3. Add Client and Employee entities, repositories, converters and seed data
4. Add Campaign entity, enum, repository and service
5. Add Campaign and Client controllers
6. Add templates, stylesheet and script
7. Add database scripts: `db/02_campaign.sql`, `setup_database.sh`, `fix_mysql.sh`

## Merge notes
Files also present in the user-management branch are byte-identical, so Git merges them cleanly.
