package com.creativepulse.advertising.dao;

import com.creativepulse.advertising.model.Client;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ClientDao {
    private final JdbcTemplate jdbc;
    public ClientDao(JdbcTemplate jdbc){this.jdbc=jdbc;}
    private Client map(java.sql.ResultSet rs,int row)throws java.sql.SQLException{
        return new Client(rs.getLong("id"),rs.getString("name"),rs.getString("email"),
                rs.getString("phone"),rs.getString("company"),rs.getString("status"));
    }
    public List<Client> findAll(){return jdbc.query("SELECT * FROM clients ORDER BY id DESC",this::map);}
    public Client findById(Long id){return jdbc.query("SELECT * FROM clients WHERE id=?",this::map,id).stream().findFirst().orElse(null);}
    public void save(Client c){jdbc.update("INSERT INTO clients(name,email,phone,company,status) VALUES(?,?,?,?,?)",
            c.getName(),c.getEmail(),c.getPhone(),c.getCompany(),c.getStatus());}
    public void update(Client c){jdbc.update("UPDATE clients SET name=?,email=?,phone=?,company=?,status=? WHERE id=?",
            c.getName(),c.getEmail(),c.getPhone(),c.getCompany(),c.getStatus(),c.getId());}
    public void delete(Long id){jdbc.update("DELETE FROM clients WHERE id=?",id);}
    public int count(){return jdbc.queryForObject("SELECT COUNT(*) FROM clients",Integer.class);}
}
