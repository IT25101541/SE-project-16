package com.creativepulse.config;

import com.creativepulse.entity.Client;
import com.creativepulse.repository.ClientRepository;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ClientConverter implements Converter<String, Client> {

    private final ClientRepository clientRepository;

    public ClientConverter(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client convert(String source) {

        if (source == null || source.trim().isEmpty()) {
            return null;
        }

        Long id = Long.valueOf(source);

        return clientRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Client not found with ID: " + id
                        )
                );
    }
}