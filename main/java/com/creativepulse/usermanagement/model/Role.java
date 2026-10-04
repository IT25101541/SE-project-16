package com.creativepulse.usermanagement.model;

import java.util.List;

/**
 * The seven user roles of CreativePulse Advertising.
 * Each role also lists the dashboard module cards it should see.
 * Modules owned by other team members are linked with "#" until they are merged.
 */
public enum Role {

    ADMIN("System Administrator", List.of(
            new ModuleLink("User Management", "Register users, assign roles, suspend or reactivate accounts.", "/admin/users"),
            new ModuleLink("Reports", "System-wide campaign, task and financial reports.", "#"))),

    SALES("Sales Executive", List.of(
            new ModuleLink("Client Management", "Register clients and maintain client information.", "#"))),

    MANAGER("Campaign Manager", List.of(
            new ModuleLink("Campaigns", "Create, assign and monitor advertising campaigns.", "#"),
            new ModuleLink("Employee Tasks", "Create tasks, assign employees and track workload.", "#"))),

    DESIGNER("Graphic Designer", List.of(
            new ModuleLink("Advertisements", "Upload designs, manage versions, submit for approval.", "#"))),

    FINANCE("Finance Officer", List.of(
            new ModuleLink("Billing & Payments", "Generate invoices and record client payments.", "#"))),

    EMPLOYEE("Employee", List.of(
            new ModuleLink("My Tasks", "View assigned tasks and update progress.", "#"))),

    CLIENT("Client", List.of(
            new ModuleLink("My Campaigns", "Review campaigns and advertisement designs.", "#"),
            new ModuleLink("My Invoices", "View invoices and payment history.", "#")));

    private final String displayName;
    private final List<ModuleLink> modules;

    Role(String displayName, List<ModuleLink> modules) {
        this.displayName = displayName;
        this.modules = modules;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<ModuleLink> getModules() {
        return modules;
    }

    /** A card shown on the role-specific dashboard. */
    public record ModuleLink(String title, String description, String path) {
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getPath() { return path; }
        public boolean isAvailable() { return !"#".equals(path); }
    }
}
