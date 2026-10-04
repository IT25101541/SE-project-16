package com.creativepulse.advertising.dao;

import com.creativepulse.advertising.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.util.List;

@Repository
public class TaskDao {
    private final JdbcTemplate jdbc;
    public TaskDao(JdbcTemplate jdbc){this.jdbc=jdbc;}
    private Task map(java.sql.ResultSet rs,int row)throws java.sql.SQLException{
        return new Task(rs.getLong("id"),rs.getLong("campaign_id"),rs.getString("title"),
                rs.getString("employee_name"),rs.getString("priority"),rs.getString("status"),
                rs.getDate("deadline").toLocalDate());
    }
    public List<Task> findAll(){return jdbc.query("SELECT * FROM tasks ORDER BY id DESC",this::map);}
    public void save(Task t){
        jdbc.update("INSERT INTO tasks(campaign_id,title,employee_name,priority,status,deadline) VALUES(?,?,?,?,?,?)",
                t.getCampaignId(),t.getTitle(),t.getEmployeeName(),t.getPriority(),t.getStatus(),Date.valueOf(t.getDeadline()));
    }
    public void update(Task t){
        jdbc.update("UPDATE tasks SET campaign_id=?,title=?,employee_name=?,priority=?,status=?,deadline=? WHERE id=?",
                t.getCampaignId(),t.getTitle(),t.getEmployeeName(),t.getPriority(),t.getStatus(),Date.valueOf(t.getDeadline()),t.getId());
    }
    public void delete(Long id){jdbc.update("DELETE FROM tasks WHERE id=?",id);}
    public int count(){return jdbc.queryForObject("SELECT COUNT(*) FROM tasks",Integer.class);}
    public int pendingCount(){return jdbc.queryForObject("SELECT COUNT(*) FROM tasks WHERE status='Pending'",Integer.class);}
}
