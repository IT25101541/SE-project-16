package com.creativepulse.advertising.model;

import java.time.LocalDate;

public class Task {
    private Long id;
    private Long campaignId;
    private String title;
    private String employeeName;
    private String priority;
    private String status;
    private LocalDate deadline;

    public Task() {}

    public Task(Long id, Long campaignId, String title, String employeeName,
                String priority, String status, LocalDate deadline) {
        this.id=id; this.campaignId=campaignId; this.title=title;
        this.employeeName=employeeName; this.priority=priority; this.status=status;
        this.deadline=deadline;
    }

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Long getCampaignId(){return campaignId;} public void setCampaignId(Long v){campaignId=v;}
    public String getTitle(){return title;} public void setTitle(String v){title=v;}
    public String getEmployeeName(){return employeeName;} public void setEmployeeName(String v){employeeName=v;}
    public String getPriority(){return priority;} public void setPriority(String v){priority=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public LocalDate getDeadline(){return deadline;} public void setDeadline(LocalDate v){deadline=v;}
}
