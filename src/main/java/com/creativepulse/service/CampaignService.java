package com.creativepulse.service;

import com.creativepulse.dao.CampaignDao;
import com.creativepulse.model.*;
import java.time.LocalDate;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class CampaignService {

    private final CampaignDao dao;

    public CampaignService(CampaignDao dao) {
        this.dao = dao;
    }

    private void checkDates(LocalDate s, LocalDate e) {
        if (e.isBefore(s)) throw new IllegalArgumentException("End date cannot be before start date");
    }

    public Campaign create(Campaign c) {
        checkDates(c.startDate(), c.endDate());
        return get(dao.create(c));
    }

    public List<Campaign> list(String status, String keyword) {
        return dao.findAll(status, keyword);
    }

    public Campaign get(int id) {
        return dao.findById(id).orElseThrow(() -> new NotFoundException("Campaign " + id + " not found"));
    }

    public Campaign update(int id, Campaign c) {
        checkDates(c.startDate(), c.endDate());
        if (dao.update(id, c) == 0) throw new NotFoundException("Campaign " + id + " not found");
        return get(id);
    }

    public Campaign updateStatus(int id, StatusUpdate u) {
        if (dao.updateStatus(id, u.status(), u.progress()) == 0)
            throw new NotFoundException("Campaign " + id + " not found");
        return get(id);
    }

    public Campaign updateSchedule(int id, ScheduleUpdate u) {
        checkDates(u.startDate(), u.endDate());
        if (dao.updateSchedule(id, u.startDate(), u.endDate()) == 0)
            throw new NotFoundException("Campaign " + id + " not found");
        return get(id);
    }

    public void delete(int id) {
        if (dao.delete(id) == 0) throw new NotFoundException("Campaign " + id + " not found");
    }

    public List<CampaignAssignment> assign(int campaignId, CampaignAssignment a) {
        get(campaignId);
        dao.assign(campaignId, a.userId(), a.roleInTeam());
        return dao.findAssignments(campaignId);
    }

    public List<CampaignAssignment> assignments(int campaignId) {
        get(campaignId);
        return dao.findAssignments(campaignId);
    }

    public void unassign(int campaignId, int userId) {
        if (dao.unassign(campaignId, userId) == 0) throw new NotFoundException("Assignment not found");
    }

    public Map<String, Integer> summary() {
        return dao.countByStatus();
    }
}
