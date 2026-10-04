package com.creativepulse.dao;

import com.creativepulse.model.Client;
import java.sql.Timestamp;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDao {

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert insert;

    private static final RowMapper<Client> MAPPER = (rs, i) -> {
        Timestamp ts = rs.getTimestamp("created_at");
        return new Client(rs.getInt("client_id"), rs.getString("company_name"),
                rs.getString("contact_person"), rs.getString("email"), rs.getString("phone"),
                rs.getString("address"), rs.getString("industry"), rs.getString("status"),
                ts == null ? null : ts.toLocalDateTime());
    };

    public ClientDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.insert = new SimpleJdbcInsert(jdbc).withTableName("clients")
                .usingGeneratedKeyColumns("client_id")
                .usingColumns("company_name", "contact_person", "email", "phone", "address", "industry", "status");
    }

    public int create(Client c) {
        Map<String, Object> p = new HashMap<>();
        p.put("company_name", c.companyName());
        p.put("contact_person", c.contactPerson());
        p.put("email", c.email());
        p.put("phone", c.phone());
        p.put("address", c.address());
        p.put("industry", c.industry());
        p.put("status", c.status() == null ? "ACTIVE" : c.status());
        return insert.executeAndReturnKey(p).intValue();
    }

    public List<Client> findAll() {
        return jdbc.query("SELECT * FROM clients ORDER BY company_name", MAPPER);
    }

    public Optional<Client> findById(int id) {
        return jdbc.query("SELECT * FROM clients WHERE client_id = ?", MAPPER, id).stream().findFirst();
    }

    public List<Client> search(String keyword) {
        String k = "%" + keyword + "%";
        return jdbc.query("""
                SELECT * FROM clients
                WHERE company_name LIKE ? OR contact_person LIKE ? OR email LIKE ? OR industry LIKE ?
                ORDER BY company_name""", MAPPER, k, k, k, k);
    }

    public int update(int id, Client c) {
        return jdbc.update("""
                UPDATE clients SET company_name=?, contact_person=?, email=?, phone=?, address=?,
                industry=?, status=? WHERE client_id=?""",
                c.companyName(), c.contactPerson(), c.email(), c.phone(), c.address(), c.industry(),
                c.status() == null ? "ACTIVE" : c.status(), id);
    }

    public int delete(int id) {
        return jdbc.update("DELETE FROM clients WHERE client_id = ?", id);
    }
}
