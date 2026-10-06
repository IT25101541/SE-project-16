package com.creativepulse.service;

import com.creativepulse.model.Client;
import com.creativepulse.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository repo;

    public ClientService(ClientRepository repo) { this.repo = repo; }

    public List<Client> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return repo.findAll();
        return repo.findByCompanyNameContainingIgnoreCaseOrContactPersonContainingIgnoreCase(keyword, keyword);
    }

    public List<Client> findAll() { return repo.findAll(); }

    public Client findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id " + id));
    }

    public boolean emailTaken(String email, Long ignoreId) {
        return repo.findAll().stream()
                .anyMatch(c -> c.getEmail().equalsIgnoreCase(email) && !c.getId().equals(ignoreId));
    }

    public Client save(Client client) { return repo.save(client); }

    public Client update(Long id, Client form) {
        Client db = findById(id);
        db.setCompanyName(form.getCompanyName());
        db.setContactPerson(form.getContactPerson());
        db.setEmail(form.getEmail());
        db.setPhone(form.getPhone());
        db.setAddress(form.getAddress());
        db.setIndustry(form.getIndustry());
        db.setActive(form.isActive());
        return repo.save(db);
    }

    public void delete(Long id) { repo.deleteById(id); }

    public long count() { return repo.count(); }
}
