package com.creativepulse.config;

import com.creativepulse.entity.Employee;
import com.creativepulse.repository.EmployeeRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EmployeeConverter implements Converter<String, Employee> {
    private final EmployeeRepository repository;
    public EmployeeConverter(EmployeeRepository repository) { this.repository = repository; }
    @Override public Employee convert(String source) {
        if (source == null || source.isBlank()) return null;
        return repository.findById(Long.valueOf(source)).orElseThrow();
    }
}
