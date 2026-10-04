package com.creativepulse.usermanagement;

import com.creativepulse.usermanagement.dto.AdminUserForm;
import com.creativepulse.usermanagement.dto.RegistrationForm;
import com.creativepulse.usermanagement.model.Role;
import com.creativepulse.usermanagement.model.User;
import com.creativepulse.usermanagement.service.CreatedUser;
import com.creativepulse.usermanagement.service.DuplicateEmailException;
import com.creativepulse.usermanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(properties = "app.seed-demo-users=true")
class UserManagementTests {

    @Autowired UserService userService;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired MockMvc mockMvc;

    private RegistrationForm registration(String email) {
        RegistrationForm form = new RegistrationForm();
        form.setFullName("Test Client");
        form.setEmail(email);
        form.setPassword("Secret123");
        form.setConfirmPassword("Secret123");
        form.setConsent(true);
        return form;
    }

    @Test
    void registeredUserIsClientAndPasswordIsHashed() {
        User user = userService.registerClient(registration("  New.Client@Example.com "));

        assertEquals("new.client@example.com", user.getEmail());
        assertEquals(Role.CLIENT, user.getRole());
        assertNotEquals("Secret123", user.getPasswordHash());
        assertTrue(passwordEncoder.matches("Secret123", user.getPasswordHash()));
    }

    @Test
    void duplicateEmailIsRejected() {
        userService.registerClient(registration("dup@example.com"));
        assertThrows(DuplicateEmailException.class,
                () -> userService.registerClient(registration("DUP@example.com")));
    }

    @Test
    void adminCreatedUserGetsRoleAndTemporaryPassword() {
        AdminUserForm form = new AdminUserForm();
        form.setFullName("New Designer");
        form.setEmail("designer.new@example.com");
        form.setRole(Role.DESIGNER);

        CreatedUser created = userService.createByAdmin(form);

        assertEquals(Role.DESIGNER, created.getUser().getRole());
        assertTrue(created.getUser().isMustChangePassword());
        assertTrue(passwordEncoder.matches(created.getTemporaryPassword(), created.getUser().getPasswordHash()));
    }

    @Test
    void adminCannotSuspendOwnAccount() {
        User admin = userService.getByEmail("admin@creativepulse.lk");
        assertThrows(IllegalStateException.class, () -> userService.setStatus(
                admin.getId(), com.creativepulse.usermanagement.model.UserStatus.SUSPENDED, admin.getEmail()));
    }

    @Test
    void loginPageIsPublicAndDashboardRequiresSignIn() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("login"));
        mockMvc.perform(get("/dashboard")).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void registerEndpointCreatesAccountAndRedirectsToLogin() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                        .param("fullName", "Web Client")
                        .param("email", "web.client@example.com")
                        .param("password", "Secret123")
                        .param("confirmPassword", "Secret123")
                        .param("consent", "true"))
                .andExpect(redirectedUrl("/login?registered"));
    }

    @Test
    void signedInClientSeesDashboardButNotAdminPages() throws Exception {
        mockMvc.perform(get("/dashboard").with(user("client@creativepulse.lk").roles("CLIENT")))
                .andExpect(status().isOk()).andExpect(view().name("dashboard"));
        mockMvc.perform(get("/admin/users").with(user("client@creativepulse.lk").roles("CLIENT")))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanOpenUserManagement() throws Exception {
        mockMvc.perform(get("/admin/users").with(user("admin@creativepulse.lk").roles("ADMIN")))
                .andExpect(status().isOk()).andExpect(view().name("admin/users"));
    }
}
