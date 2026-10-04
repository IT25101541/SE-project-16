package com.creativepulse.controller;

import com.creativepulse.model.*;
import com.creativepulse.service.CampaignService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campaigns")
@CrossOrigin
public class CampaignController {

    private final CampaignService service;

    public CampaignController(CampaignService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Campaign create(@Valid @RequestBody Campaign c) {
        return service.create(c);
    }

    @GetMapping
    public List<Campaign> list(@RequestParam(required = false) String status,
                               @RequestParam(required = false) String search) {
        return service.list(status, search);
    }

    @GetMapping("/summary")
    public Map<String, Integer> summary() {
        return service.summary();
    }

    @GetMapping("/{id}")
    public Campaign get(@PathVariable int id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public Campaign update(@PathVariable int id, @Valid @RequestBody Campaign c) {
        return service.update(id, c);
    }

    @PatchMapping("/{id}/status")
    public Campaign updateStatus(@PathVariable int id, @Valid @RequestBody StatusUpdate u) {
        return service.updateStatus(id, u);
    }

    @PatchMapping("/{id}/schedule")
    public Campaign updateSchedule(@PathVariable int id, @Valid @RequestBody ScheduleUpdate u) {
        return service.updateSchedule(id, u);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    @GetMapping("/{id}/assignments")
    public List<CampaignAssignment> assignments(@PathVariable int id) {
        return service.assignments(id);
    }

    @PostMapping("/{id}/assignments")
    @ResponseStatus(HttpStatus.CREATED)
    public List<CampaignAssignment> assign(@PathVariable int id, @Valid @RequestBody CampaignAssignment a) {
        return service.assign(id, a);
    }

    @DeleteMapping("/{id}/assignments/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unassign(@PathVariable int id, @PathVariable int userId) {
        service.unassign(id, userId);
    }
}
