package com.creativepulse.advertising.model;

import java.time.LocalDate;

public class Campaign {
    private Long id;
    private String name;
    private String clientName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private int progress;
    private String managerName;

    public Campaign() {}

    public Campaign(Long id, String name, String clientName, String description,
                    LocalDate startDate, LocalDate endDate, String status,
                    int progress, String managerName) {
        this.id=id; this.name=name; this.clientName=clientName; this.description=description;
        this.startDate=startDate; this.endDate=endDate; this.status=status;
        this.progress=progress; this.managerName=managerName;
    }

    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getClientName(){return clientName;} public void setClientName(String v){clientName=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public LocalDate getStartDate(){return startDate;} public void setStartDate(LocalDate v){startDate=v;}
    public LocalDate getEndDate(){return endDate;} public void setEndDate(LocalDate v){endDate=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public int getProgress(){return progress;} public void setProgress(int v){progress=v;}
    public String getManagerName(){return managerName;} public void setManagerName(String v){managerName=v;}
}
