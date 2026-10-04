package com.creativepulse.usermanagement.controller;

import com.creativepulse.usermanagement.dto.ChangePasswordForm;
import com.creativepulse.usermanagement.dto.ProfileForm;
import com.creativepulse.usermanagement.model.User;
import com.creativepulse.usermanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** View/update own profile and change own password. */
@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        User user = userService.getByEmail(authentication.getName());
        ProfileForm profileForm = new ProfileForm();
        profileForm.setFullName(user.getFullName());
        profileForm.setPhone(user.getPhone());
        model.addAttribute("profileForm", profileForm);
        model.addAttribute("passwordForm", new ChangePasswordForm());
        model.addAttribute("activePage", "profile");
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("profileForm") ProfileForm profileForm,
                                BindingResult bindingResult,
                                Authentication authentication,
                                Model model,
                                RedirectAttributes redirect) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("passwordForm", new ChangePasswordForm());
            model.addAttribute("activePage", "profile");
            return "profile";
        }
        userService.updateProfile(authentication.getName(), profileForm);
        redirect.addFlashAttribute("success", "Profile updated");
        return "redirect:/profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(@Valid @ModelAttribute("passwordForm") ChangePasswordForm passwordForm,
                                 BindingResult bindingResult,
                                 Authentication authentication,
                                 Model model,
                                 RedirectAttributes redirect) {
        if (passwordForm.getNewPassword() != null
                && !passwordForm.getNewPassword().equals(passwordForm.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "mismatch", "Passwords do not match");
        }
        if (!bindingResult.hasErrors()) {
            try {
                userService.changePassword(authentication.getName(), passwordForm);
                redirect.addFlashAttribute("success", "Password changed");
                return "redirect:/profile";
            } catch (IllegalArgumentException e) {
                bindingResult.rejectValue("currentPassword", "incorrect", e.getMessage());
            }
        }
        User user = userService.getByEmail(authentication.getName());
        ProfileForm profileForm = new ProfileForm();
        profileForm.setFullName(user.getFullName());
        profileForm.setPhone(user.getPhone());
        model.addAttribute("profileForm", profileForm);
        model.addAttribute("activePage", "profile");
        return "profile";
    }
}
