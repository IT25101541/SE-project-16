package com.creativepulse.controller;

import com.creativepulse.model.Client;
import com.creativepulse.service.ClientService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Client create(@Valid @RequestBody Client c) {
        return service.create(c);
    }

    @GetMapping
    public List<Client> list(@RequestParam(required = false) String search) {
        return service.list(search);
    }

    @GetMapping("/{id}")
    public Client get(@PathVariable int id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    public Client update(@PathVariable int id, @Valid @RequestBody Client c) {
        return service.update(id, c);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }
}
