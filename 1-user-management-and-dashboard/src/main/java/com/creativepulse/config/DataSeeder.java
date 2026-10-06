package com.creativepulse.config;

import com.creativepulse.model.*;
import com.creativepulse.pattern.InvoiceNumberGenerator;
import com.creativepulse.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Creates demo accounts and sample data the first time the app starts. */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final CampaignRepository campaignRepository;
    private final InvoiceRepository invoiceRepository;
    private final PasswordEncoder encoder;

    public DataSeeder(UserRepository userRepository, ClientRepository clientRepository,
                      CampaignRepository campaignRepository, InvoiceRepository invoiceRepository,
                      PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.campaignRepository = campaignRepository;
        this.invoiceRepository = invoiceRepository;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        // keep the Singleton counter in sync after a restart
        InvoiceNumberGenerator.getInstance().syncWith(invoiceRepository.count());

        if (userRepository.count() > 0) return;   // already seeded

        createUser("System Admin", "admin", "admin@creativepulse.lk", "0771000001", Role.ADMIN);
        createUser("Sahan Perera", "sales01", "sales@creativepulse.lk", "0771000002", Role.SALES);
        createUser("Menaka Silva", "manager01", "manager@creativepulse.lk", "0771000003", Role.MANAGER);
        createUser("Ishara Fernando", "designer01", "designer@creativepulse.lk", "0771000004", Role.DESIGNER);
        createUser("Nadeesha Jay", "finance01", "finance@creativepulse.lk", "0771000005", Role.FINANCE);
        createUser("Client User", "client01", "client@abc.lk", "0771000006", Role.CLIENT);

        Client c1 = new Client();
        c1.setCompanyName("ABC Beverages PLC");
        c1.setContactPerson("Kasun Ranaweera");
        c1.setEmail("kasun@abcbeverages.lk");
        c1.setPhone("0112345678");
        c1.setAddress("120 Galle Road, Colombo 03");
        c1.setIndustry("Food & Beverage");
        clientRepository.save(c1);

        Client c2 = new Client();
        c2.setCompanyName("LankaTel Mobile");
        c2.setContactPerson("Dilani Weerasinghe");
        c2.setEmail("dilani@lankatel.lk");
        c2.setPhone("0117654321");
        c2.setAddress("45 Union Place, Colombo 02");
        c2.setIndustry("Telecommunication");
        clientRepository.save(c2);

        Campaign camp = new Campaign();
        camp.setName("ABC Summer Splash 2026");
        camp.setClient(c1);
        camp.setManager(userRepository.findByUsername("manager01").orElse(null));
        camp.setStartDate(LocalDate.now().minusDays(10));
        camp.setEndDate(LocalDate.now().plusDays(50));
        camp.setBudget(new BigDecimal("850000.00"));
        camp.setStatus(Campaign.Status.ACTIVE);
        camp.setProgress(35);
        camp.setDescription("Island-wide summer promotion across TV, radio and social media.");
        campaignRepository.save(camp);

        System.out.println("=====================================================");
        System.out.println(" CreativePulse demo data created.");
        System.out.println(" Login with any of these (password for all: 1234):");
        System.out.println("   admin / sales01 / manager01 / designer01 / finance01 / client01");
        System.out.println("=====================================================");
    }

    private void createUser(String fullName, String username, String email, String phone, Role role) {
        User u = new User();
        u.setFullName(fullName);
        u.setUsername(username);
        u.setEmail(email);
        u.setPhone(phone);
        u.setRole(role);
        u.setActive(true);
        u.setPassword(encoder.encode("1234"));
        userRepository.save(u);
    }
}
