package com.creativepulse.advertising.dao;

import com.creativepulse.advertising.model.Campaign;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.util.List;

@Repository
public class CampaignDao {
    private final JdbcTemplate jdbc;
    public CampaignDao(JdbcTemplate jdbc){this.jdbc=jdbc;}

    private Campaign map(java.sql.ResultSet rs, int row) throws java.sql.SQLException {
        return new Campaign(rs.getLong("id"), rs.getString("name"), rs.getString("client_name"),
                rs.getString("description"), rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate(), rs.getString("status"),
                rs.getInt("progress"), rs.getString("manager_name"));
    }

    public List<Campaign> findAll(){
        return jdbc.query("SELECT * FROM campaigns ORDER BY id DESC", this::map);
    }
    public Campaign findById(Long id){
        return jdbc.query("SELECT * FROM campaigns WHERE id=?", this::map, id)
                .stream().findFirst().orElse(null);
    }
    public void save(Campaign c){
        jdbc.update("INSERT INTO campaigns(name,client_name,description,start_date,end_date,status,progress,manager_name) VALUES(?,?,?,?,?,?,?,?)",
                c.getName(),c.getClientName(),c.getDescription(),Date.valueOf(c.getStartDate()),
                Date.valueOf(c.getEndDate()),c.getStatus(),c.getProgress(),c.getManagerName());
    }
    public void update(Campaign c){
        jdbc.update("UPDATE campaigns SET name=?,client_name=?,description=?,start_date=?,end_date=?,status=?,progress=?,manager_name=? WHERE id=?",
                c.getName(),c.getClientName(),c.getDescription(),Date.valueOf(c.getStartDate()),
                Date.valueOf(c.getEndDate()),c.getStatus(),c.getProgress(),c.getManagerName(),c.getId());
    }
    public void delete(Long id){ jdbc.update("DELETE FROM campaigns WHERE id=?", id); }
    public int count(){return jdbc.queryForObject("SELECT COUNT(*) FROM campaigns",Integer.class);}
    public int activeCount(){return jdbc.queryForObject("SELECT COUNT(*) FROM campaigns WHERE status='Active'",Integer.class);}
    public int completedCount(){return jdbc.queryForObject("SELECT COUNT(*) FROM campaigns WHERE status='Completed'",Integer.class);}
}
