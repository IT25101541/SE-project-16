package com.creativepulse.usermanagement.config;

import com.creativepulse.usermanagement.model.Role;
import com.creativepulse.usermanagement.model.User;
import com.creativepulse.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds the first administrator (someone must exist to create other accounts)
 * and, for demos, one sample account per role. Turn demo accounts off with
 * app.seed-demo-users=false.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final String ADMIN_EMAIL = "admin@creativepulse.lk";
    private static final String ADMIN_PASSWORD = "Admin@123";
    private static final String DEMO_PASSWORD = "Password@123";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final boolean seedDemoUsers;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           @Value("${app.seed-demo-users:true}") boolean seedDemoUsers) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.seedDemoUsers = seedDemoUsers;
    }

    @Override
    public void run(String... args) {
        createIfMissing("System Administrator", ADMIN_EMAIL, ADMIN_PASSWORD, Role.ADMIN);

        if (seedDemoUsers) {
            createIfMissing("Sasha Sales", "sales@creativepulse.lk", DEMO_PASSWORD, Role.SALES);
            createIfMissing("Mina Manager", "manager@creativepulse.lk", DEMO_PASSWORD, Role.MANAGER);
            createIfMissing("Dev Designer", "designer@creativepulse.lk", DEMO_PASSWORD, Role.DESIGNER);
            createIfMissing("Fiona Finance", "finance@creativepulse.lk", DEMO_PASSWORD, Role.FINANCE);
            createIfMissing("Eric Employee", "employee@creativepulse.lk", DEMO_PASSWORD, Role.EMPLOYEE);
            createIfMissing("Clara Client", "client@creativepulse.lk", DEMO_PASSWORD, Role.CLIENT);
        }
    }

    private void createIfMissing(String name, String email, String rawPassword, Role role) {
        if (userRepository.existsByEmail(email)) {
            return;
        }
        User user = new User();
        user.setFullName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        userRepository.save(user);
    }
}
