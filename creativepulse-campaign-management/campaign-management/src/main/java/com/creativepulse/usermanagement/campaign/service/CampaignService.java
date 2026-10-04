package com.creativepulse.usermanagement.campaign.service;

import com.creativepulse.usermanagement.campaign.dto.CampaignForm;
import com.creativepulse.usermanagement.campaign.model.Campaign;
import com.creativepulse.usermanagement.campaign.model.CampaignStatus;
import com.creativepulse.usermanagement.campaign.repository.CampaignRepository;
import com.creativepulse.usermanagement.campaign.repository.TeamMemberRepository;
import com.creativepulse.usermanagement.model.Role;
import com.creativepulse.usermanagement.model.User;
import com.creativepulse.usermanagement.model.UserStatus;
import com.creativepulse.usermanagement.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@Transactional
public class CampaignService {

    /** Roles a campaign can be assigned to. */
    static final List<Role> ASSIGNABLE_ROLES = List.of(Role.MANAGER, Role.DESIGNER, Role.EMPLOYEE);

    private final CampaignRepository campaignRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserService userService;

    public CampaignService(CampaignRepository campaignRepository,
                           TeamMemberRepository teamMemberRepository,
                           UserService userService) {
        this.campaignRepository = campaignRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.userService = userService;
    }

    // ------------------------------------------------------------------ create

    /** New campaigns always start in Planning with 0% progress. */
    public Campaign create(CampaignForm form, String actingEmail) {
        form.setStatus(CampaignStatus.PLANNING);
        form.setProgress(0);

        Campaign campaign = new Campaign();
        apply(campaign, form);
        campaign.setCreatedByName(userService.getByEmail(actingEmail).getFullName());
        return campaignRepository.save(campaign);
    }

    // -------------------------------------------------------------------- edit

    public Campaign update(Long id, CampaignForm form) {
        Campaign campaign = get(id);
        apply(campaign, form);
        return campaign;
    }

    // ------------------------------------------------------------------ delete

    /** Only cancelled campaigns can be removed. */
    public void delete(Long id) {
        Campaign campaign = get(id);
        if (!campaign.isRemovable()) {
            throw new IllegalStateException("Only cancelled campaigns can be removed. Set the status to Cancelled first.");
        }
        campaignRepository.delete(campaign);
    }

    // -------------------------------------------------------------------- read

    @Transactional(readOnly = true)
    public Campaign get(Long id) {
        return campaignRepository.findById(id).orElseThrow(() -> new CampaignNotFoundException(id));
    }

    /** Filters by optional text (campaign or client name) and optional status. */
    @Transactional(readOnly = true)
    public List<Campaign> search(String query, CampaignStatus status) {
        String text = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        return campaignRepository.findAllByOrderByCreatedAtDesc().stream()
                .filter(c -> status == null || c.getStatus() == status)
                .filter(c -> text.isEmpty()
                        || c.getName().toLowerCase(Locale.ROOT).contains(text)
                        || c.getClientName().toLowerCase(Locale.ROOT).contains(text))
                .toList();
    }

    @Transactional(readOnly = true)
    public Map<CampaignStatus, Long> countByStatus() {
        Map<CampaignStatus, Long> counts = new EnumMap<>(CampaignStatus.class);
        for (CampaignStatus status : CampaignStatus.values()) {
            counts.put(status, campaignRepository.countByStatus(status));
        }
        return counts;
    }

    @Transactional(readOnly = true)
    public List<User> assignableMembers() {
        return teamMemberRepository.findByRoleInAndStatusOrderByFullNameAsc(ASSIGNABLE_ROLES, UserStatus.ACTIVE);
    }

    // ----------------------------------------------------------------- helpers

    private void apply(Campaign campaign, CampaignForm form) {
        if (form.getEndDate().isBefore(form.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before the start date");
        }

        campaign.setName(form.getName().trim());
        campaign.setClientName(form.getClientName().trim());
        campaign.setDescription(blankToNull(form.getDescription()));
        campaign.setCategory(form.getCategory());
        campaign.setStartDate(form.getStartDate());
        campaign.setEndDate(form.getEndDate());
        campaign.setBudget(form.getBudget().setScale(2, RoundingMode.HALF_UP));
        campaign.setStatus(form.getStatus());
        // a completed campaign is always 100% done
        campaign.setProgress(form.getStatus() == CampaignStatus.COMPLETED ? 100 : form.getProgress());

        if (form.getAssignedToId() == null) {
            campaign.setAssignedToId(null);
            campaign.setAssignedToName(null);
        } else {
            User member = teamMemberRepository.findById(form.getAssignedToId())
                    .filter(u -> u.getStatus() == UserStatus.ACTIVE && ASSIGNABLE_ROLES.contains(u.getRole()))
                    .orElseThrow(() -> new IllegalArgumentException("The selected team member is not available"));
            campaign.setAssignedToId(member.getId());
            campaign.setAssignedToName(member.getFullName());
        }
    }

    private static String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}
