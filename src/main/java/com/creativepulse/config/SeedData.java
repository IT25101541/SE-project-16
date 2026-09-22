package com.creativepulse.config;

import com.creativepulse.entity.Client;
import com.creativepulse.entity.Employee;
import com.creativepulse.repository.ClientRepository;
import com.creativepulse.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedData {
    @Bean
    CommandLineRunner seed(ClientRepository clients, EmployeeRepository employees) {
        return args -> {
            if (clients.count() == 0) {
                Client c1 = new Client(); c1.setCompanyName("ABC Pvt Ltd"); c1.setContactPerson("Nimal Perera"); c1.setEmail("nimal@abc.com"); c1.setPhone("0771234567"); c1.setAddress("Colombo"); clients.save(c1);
                Client c2 = new Client(); c2.setCompanyName("XYZ Holdings"); c2.setContactPerson("Kamal Silva"); c2.setEmail("kamal@xyz.com"); c2.setPhone("0717654321"); c2.setAddress("Kandy"); clients.save(c2);
            }
            if (employees.count() == 0) {
                Employee e1 = new Employee(); e1.setName("Kasun Perera"); e1.setEmail("kasun@creativepulse.com"); e1.setRole("Advertising Staff"); employees.save(e1);
                Employee e2 = new Employee(); e2.setName("Tharushi Silva"); e2.setEmail("tharushi@creativepulse.com"); e2.setRole("Graphic Designer"); employees.save(e2);
            }
        };
    }
}
