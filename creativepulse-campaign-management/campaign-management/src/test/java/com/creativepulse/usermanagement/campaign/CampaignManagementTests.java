package com.creativepulse.usermanagement.campaign;

import com.creativepulse.usermanagement.campaign.dto.CampaignForm;
import com.creativepulse.usermanagement.campaign.model.Campaign;
import com.creativepulse.usermanagement.campaign.model.CampaignCategory;
import com.creativepulse.usermanagement.campaign.model.CampaignStatus;
import com.creativepulse.usermanagement.campaign.repository.CampaignRepository;
import com.creativepulse.usermanagement.campaign.service.CampaignService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CampaignManagementTests {

    @Autowired CampaignService campaignService;
    @Autowired CampaignRepository campaignRepository;
    @Autowired MockMvc mockMvc;

    private CampaignForm validForm(String name) {
        CampaignForm form = new CampaignForm();
        form.setName(name);
        form.setClientName("Test Client Ltd");
        form.setCategory(CampaignCategory.DIGITAL);
        form.setStartDate(LocalDate.now().plusDays(1));
        form.setEndDate(LocalDate.now().plusDays(30));
        form.setBudget(new BigDecimal("150000"));
        return form;
    }

    // ----------------------------------------------------------------- service

    @Test
    void newCampaignStartsInPlanningWithZeroProgress() {
        CampaignForm form = validForm("Launch Campaign");
        form.setStatus(CampaignStatus.COMPLETED); // ignored on create
        form.setProgress(80);                     // ignored on create

        Campaign created = campaignService.create(form, "manager@creativepulse.lk");

        assertNotNull(created.getId());
        assertEquals(CampaignStatus.PLANNING, created.getStatus());
        assertEquals(0, created.getProgress());
        assertEquals("Mina Manager", created.getCreatedByName());
        assertEquals(new BigDecimal("150000.00"), created.getBudget());
    }

    @Test
    void editingUpdatesFieldsAndCompletedMeansFullProgress() {
        Campaign created = campaignService.create(validForm("Edit Me"), "manager@creativepulse.lk");

        CampaignForm edit = CampaignForm.from(created);
        edit.setName("Edited Name");
        edit.setStatus(CampaignStatus.COMPLETED);
        edit.setProgress(40);
        Campaign updated = campaignService.update(created.getId(), edit);

        assertEquals("Edited Name", updated.getName());
        assertEquals(CampaignStatus.COMPLETED, updated.getStatus());
        assertEquals(100, updated.getProgress());
    }

    @Test
    void endDateBeforeStartDateIsRejected() {
        CampaignForm form = validForm("Bad Dates");
        form.setEndDate(form.getStartDate().minusDays(1));
        assertThrows(IllegalArgumentException.class,
                () -> campaignService.create(form, "manager@creativepulse.lk"));
    }

    @Test
    void onlyCancelledCampaignsCanBeRemoved() {
        Campaign created = campaignService.create(validForm("Remove Me"), "manager@creativepulse.lk");
        assertThrows(IllegalStateException.class, () -> campaignService.delete(created.getId()));

        CampaignForm cancel = CampaignForm.from(created);
        cancel.setStatus(CampaignStatus.CANCELLED);
        campaignService.update(created.getId(), cancel);
        campaignService.delete(created.getId());

        assertEquals(false, campaignRepository.existsById(created.getId()));
    }

    // -------------------------------------------------------------- web / roles

    @Test
    void managerCanOpenListAndCreateForm() throws Exception {
        mockMvc.perform(get("/campaigns").with(user("manager@creativepulse.lk").roles("MANAGER")))
                .andExpect(status().isOk()).andExpect(view().name("campaigns/list"));
        mockMvc.perform(get("/campaigns/new").with(user("manager@creativepulse.lk").roles("MANAGER")))
                .andExpect(status().isOk()).andExpect(view().name("campaigns/form"));
    }

    @Test
    void managerCanCreateACampaign() throws Exception {
        mockMvc.perform(post("/campaigns/new").with(csrf())
                        .with(user("manager@creativepulse.lk").roles("MANAGER"))
                        .param("name", "Web Created")
                        .param("clientName", "Web Client")
                        .param("category", "PRINT")
                        .param("startDate", LocalDate.now().plusDays(1).toString())
                        .param("endDate", LocalDate.now().plusDays(10).toString())
                        .param("budget", "50000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/campaigns/*"));
    }

    @Test
    void createFormShowsErrorsForBadDatesAndMissingFields() throws Exception {
        mockMvc.perform(post("/campaigns/new").with(csrf())
                        .with(user("manager@creativepulse.lk").roles("MANAGER"))
                        .param("name", "")
                        .param("clientName", "Client")
                        .param("category", "PRINT")
                        .param("startDate", "2030-05-10")
                        .param("endDate", "2030-05-01")
                        .param("budget", "50000"))
                .andExpect(status().isOk())
                .andExpect(view().name("campaigns/form"))
                .andExpect(model().attributeHasFieldErrors("form", "name", "endDate"));
    }

    @Test
    void designerCanViewButNotCreateOrEdit() throws Exception {
        mockMvc.perform(get("/campaigns").with(user("designer@creativepulse.lk").roles("DESIGNER")))
                .andExpect(status().isOk());
        mockMvc.perform(get("/campaigns/new").with(user("designer@creativepulse.lk").roles("DESIGNER")))
                .andExpect(status().isForbidden());
        mockMvc.perform(post("/campaigns/new").with(csrf())
                        .with(user("designer@creativepulse.lk").roles("DESIGNER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void clientCannotOpenCampaigns() throws Exception {
        mockMvc.perform(get("/campaigns").with(user("client@creativepulse.lk").roles("CLIENT")))
                .andExpect(status().isForbidden());
    }

    @Test
    void unknownCampaignGives404() throws Exception {
        mockMvc.perform(get("/campaigns/999999").with(user("manager@creativepulse.lk").roles("MANAGER")))
                .andExpect(status().isNotFound());
    }
}
