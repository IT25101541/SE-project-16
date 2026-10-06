# CreativePulse 10-3 — Design Patterns Integration

All six planned design patterns are connected to the existing Spring Boot application.

1. **Singleton — Billing & Payment Management**
   - Existing `InvoiceNumberGenerator` remains unchanged.
   - `InvoiceService` continues to use `InvoiceNumberGenerator.getInstance()`.

2. **Strategy — Report Management**
   - Existing `ReportStrategyFactory` and report strategies remain unchanged.
   - `ReportService` continues to generate content through the selected strategy.

3. **Factory Method — User Management**
   - `UserFactory` / `DefaultUserFactory` added.
   - `UserService.create(...)` now creates users through the factory.

4. **Observer — Campaign Management**
   - `CampaignSubject` and `CampaignNotificationObserver` added.
   - `CampaignService` publishes `CREATED` and `STATUS_CHANGED` events.

5. **State — Advertisement Design Management**
   - State classes model DRAFT, SUBMITTED, APPROVED and REJECTED.
   - `AdvertisementService` uses `DesignStateFactory` for transitions and version operations.
   - Advertisement detail page exposes the lifecycle actions.

6. **Command — Employee Task Management**
   - Command classes and `TaskCommandInvoker` added.
   - `TaskController` executes assign/start/complete commands.
   - Task detail page exposes start/complete actions.

No entity/database schema changes were made for these integrations.
