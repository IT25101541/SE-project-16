package com.creativepulse.service;

import com.creativepulse.model.Advertisement;
import com.creativepulse.repository.AdvertisementRepository;
import com.creativepulse.pattern.design.DesignState;
import com.creativepulse.pattern.design.DesignStateFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdvertisementService {

    private final AdvertisementRepository repo;
    private final FileStorageService storage;
    private final DesignStateFactory stateFactory;

    public AdvertisementService(AdvertisementRepository repo, FileStorageService storage,
                                DesignStateFactory stateFactory) {
        this.repo = repo;
        this.storage = storage;
        this.stateFactory = stateFactory;
    }

    public List<Advertisement> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return repo.findAll();
        return repo.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Advertisement> findAll() { return repo.findAll(); }

    public Advertisement findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Advertisement not found with id " + id));
    }

    public Advertisement save(Advertisement ad) {
        // New designs always enter the lifecycle in DRAFT.
        if (ad.getId() == null) {
            ad.setStatus(Advertisement.Status.DRAFT);
            ad.setVersion(Math.max(ad.getVersion(), 1));
        }
        return repo.save(ad);
    }

    public Advertisement update(Long id, Advertisement form) {
        Advertisement db = findById(id);
        db.setTitle(form.getTitle());
        db.setCampaign(form.getCampaign());
        db.setType(form.getType());
        db.setNotes(form.getNotes());

        // A new file is a design-state operation, not just a raw version increment.
        boolean newVersion = form.getStoredFileName() != null;
        Advertisement.Status originalStatus = db.getStatus();
        if (newVersion) {
            DesignState state = stateFactory.fromStatus(originalStatus);
            state.uploadNewVersion(db);
            storage.delete(db.getStoredFileName());
            db.setFileName(form.getFileName());
            db.setStoredFileName(form.getStoredFileName());
        }

        // Status changes must follow the State pattern's legal transitions.
        // If a rejected design receives a new version, State intentionally moves
        // it to DRAFT; the old REJECTED value from the edit form must not undo that.
        if (form.getStatus() != null
                && form.getStatus() != db.getStatus()
                && !(newVersion && originalStatus == Advertisement.Status.REJECTED
                     && form.getStatus() == Advertisement.Status.REJECTED)) {
            transition(db, form.getStatus());
        }

        return repo.save(db);
    }

    public void submitForApproval(Long id) {
        Advertisement ad = findById(id);
        DesignState state = stateFactory.fromStatus(ad.getStatus());
        state.submit(ad);
        repo.save(ad);
    }

    public void approve(Long id) {
        Advertisement ad = findById(id);
        DesignState state = stateFactory.fromStatus(ad.getStatus());
        state.approve(ad);
        repo.save(ad);
    }

    public void reject(Long id) {
        Advertisement ad = findById(id);
        DesignState state = stateFactory.fromStatus(ad.getStatus());
        state.reject(ad);
        repo.save(ad);
    }

    public void uploadNewVersion(Long id) {
        Advertisement ad = findById(id);
        DesignState state = stateFactory.fromStatus(ad.getStatus());
        state.uploadNewVersion(ad);
        repo.save(ad);
    }

    private void transition(Advertisement ad, Advertisement.Status target) {
        DesignState state = stateFactory.fromStatus(ad.getStatus());
        switch (target) {
            case SUBMITTED -> state.submit(ad);
            case APPROVED -> state.approve(ad);
            case REJECTED -> state.reject(ad);
            case DRAFT -> {
                // DRAFT is reached by uploading a new version after rejection.
                if (ad.getStatus() != Advertisement.Status.REJECTED) {
                    throw new IllegalStateException(
                            "A design can only return to DRAFT after a REJECTED design receives a new version.");
                }
                state.uploadNewVersion(ad);
            }
        }
    }

    public void delete(Long id) {
        Advertisement ad = findById(id);
        storage.delete(ad.getStoredFileName());
        repo.deleteById(id);
    }

    public long count() { return repo.count(); }
    public long countPendingApproval() { return repo.countByStatus(Advertisement.Status.SUBMITTED); }
}
