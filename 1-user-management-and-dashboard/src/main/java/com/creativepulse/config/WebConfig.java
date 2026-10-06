package com.creativepulse.config;

import com.creativepulse.model.*;
import com.creativepulse.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * Two jobs:
 *  1. Converts the ID posted by a &lt;select&gt; into the real entity object
 *     (String -> Client) and back again (Client -> String) so that Thymeleaf
 *     can mark the correct option as "selected" on an edit form.
 *  2. Serves uploaded design files from the /uploads folder.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ClientRepository clientRepo;
    private final CampaignRepository campaignRepo;
    private final UserRepository userRepo;
    private final InvoiceRepository invoiceRepo;
    private final String uploadDir;

    public WebConfig(ClientRepository clientRepo, CampaignRepository campaignRepo,
                     UserRepository userRepo, InvoiceRepository invoiceRepo,
                     @Value("${app.upload.dir}") String uploadDir) {
        this.clientRepo = clientRepo;
        this.campaignRepo = campaignRepo;
        this.userRepo = userRepo;
        this.invoiceRepo = invoiceRepo;
        this.uploadDir = uploadDir;
    }

    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {

        // ---------- String (form value) -> Entity ----------
        registry.addConverter(String.class, Client.class,
                (Converter<String, Client>) s -> isBlank(s) ? null
                        : clientRepo.findById(toLong(s)).orElse(null));

        registry.addConverter(String.class, Campaign.class,
                (Converter<String, Campaign>) s -> isBlank(s) ? null
                        : campaignRepo.findById(toLong(s)).orElse(null));

        registry.addConverter(String.class, User.class,
                (Converter<String, User>) s -> isBlank(s) ? null
                        : userRepo.findById(toLong(s)).orElse(null));

        registry.addConverter(String.class, Invoice.class,
                (Converter<String, Invoice>) s -> isBlank(s) ? null
                        : invoiceRepo.findById(toLong(s)).orElse(null));

        // ---------- Entity -> String (so the right option is pre-selected) ----------
        registry.addConverter(Client.class, String.class,
                (Converter<Client, String>) c -> c.getId() == null ? "" : c.getId().toString());

        registry.addConverter(Campaign.class, String.class,
                (Converter<Campaign, String>) c -> c.getId() == null ? "" : c.getId().toString());

        registry.addConverter(User.class, String.class,
                (Converter<User, String>) u -> u.getId() == null ? "" : u.getId().toString());

        registry.addConverter(Invoice.class, String.class,
                (Converter<Invoice, String>) i -> i.getId() == null ? "" : i.getId().toString());
    }

    /** Makes uploaded design files viewable at /uploads/{file} */
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        String location = Paths.get(uploadDir).toAbsolutePath().normalize().toUri().toString();
        registry.addResourceHandler("/uploads/**").addResourceLocations(location);
    }

    private boolean isBlank(String s) { return s == null || s.isBlank(); }

    private Long toLong(String s) {
        try { return Long.valueOf(s.trim()); }
        catch (NumberFormatException e) { return -1L; }
    }
}
