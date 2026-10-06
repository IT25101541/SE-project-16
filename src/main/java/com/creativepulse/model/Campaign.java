package com.creativepulse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "campaigns")
public class Campaign {

    public enum Status { PLANNED, ACTIVE, PAUSED, COMPLETED, CANCELLED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Campaign name is required")
    @Size(min = 3, max = 80, message = "Campaign name must be between 3 and 80 characters")
    @Column(nullable = false, length = 80)
    private String name;

    @NotNull(message = "Please select a client")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    /** Campaign Manager responsible for this campaign. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private User manager;

    @NotNull(message = "Start date is required")
    @Column(nullable = false)
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Column(nullable = false)
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = "Budget is required")
    @DecimalMin(value = "1000.00", message = "Budget must be at least LKR 1,000.00")
    @Digits(integer = 10, fraction = 2, message = "Budget must be a valid amount (max 2 decimal places)")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal budget;

    @NotNull(message = "Please select a status")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.PLANNED;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(length = 500)
    private String description;

    @Min(value = 0, message = "Progress cannot be less than 0%")
    @Max(value = 100, message = "Progress cannot be more than 100%")
    @Column(nullable = false)
    private int progress = 0;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public User getManager() { return manager; }
    public void setManager(User manager) { this.manager = manager; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }
}
