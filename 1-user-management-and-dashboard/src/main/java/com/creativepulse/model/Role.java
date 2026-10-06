package com.creativepulse.model;

/** The six user roles of the system (Slide 6 of the proposal). */
public enum Role {
    ADMIN("System Administrator"),
    SALES("Sales Executive"),
    MANAGER("Campaign Manager"),
    DESIGNER("Graphic Designer"),
    FINANCE("Finance Officer"),
    CLIENT("Client");

    private final String label;
    Role(String label) { this.label = label; }
    public String getLabel() { return label; }
}
