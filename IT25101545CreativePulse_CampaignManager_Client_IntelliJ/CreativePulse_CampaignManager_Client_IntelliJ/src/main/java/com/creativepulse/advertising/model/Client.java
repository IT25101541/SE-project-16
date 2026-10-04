package com.creativepulse.advertising.model;

public class Client {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String company;
    private String status;

    public Client() {}
    public Client(Long id,String name,String email,String phone,String company,String status){
        this.id=id;this.name=name;this.email=email;this.phone=phone;this.company=company;this.status=status;
    }
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getCompany(){return company;} public void setCompany(String v){company=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
}
