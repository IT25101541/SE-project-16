package com.creativepulse.service;

import com.creativepulse.dao.ClientDao;
import com.creativepulse.model.Client;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientDao dao;

    public ClientService(ClientDao dao) {
        this.dao = dao;
    }

    public Client create(Client c) {
        return get(dao.create(c));
    }

    public List<Client> list(String keyword) {
        return (keyword == null || keyword.isBlank()) ? dao.findAll() : dao.search(keyword.trim());
    }

    public Client get(int id) {
        return dao.findById(id).orElseThrow(() -> new NotFoundException("Client " + id + " not found"));
    }

    public Client update(int id, Client c) {
        if (dao.update(id, c) == 0) throw new NotFoundException("Client " + id + " not found");
        return get(id);
    }

    public void delete(int id) {
        if (dao.delete(id) == 0) throw new NotFoundException("Client " + id + " not found");
    }
}
