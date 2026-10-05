package com.creativepulse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CreativepulseApplication {

    public static void main(String[] args) {

        SpringApplication.run(CreativepulseApplication.class, args);

        System.out.println();
        System.out.println("==============================================");
        System.out.println("        CreativePulse Application");
        System.out.println("==============================================");
        System.out.println("Website   : http://localhost:8080");
        System.out.println("Clients   : http://localhost:8080/clients");
        System.out.println("Campaigns : http://localhost:8080/campaigns");
        System.out.println("==============================================");
        System.out.println();
    }
}
