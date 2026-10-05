package com.creativepulse.dao;

import com.creativepulse.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class TaskDAO {

    private final JdbcTemplate jdbc;

    public TaskDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Task> mapper = (rs, rowNum) -> {
        Task t = new Task();
        t.setTaskId(rs.getInt("task_id"));
        t.setCampaignId(rs.getInt("campaign_id"));
        t.setTaskTitle(rs.getString("task_title"));
        t.setDescription(rs.getString("description"));
        t.setPriority(rs.getString("priority"));
        Date d = rs.getDate("deadline");
        t.setDeadline(d != null ? d.toLocalDate() : null);
        t.setStatus(rs.getString("status"));
        return t;
    };

    public List<Task> getAllTasks() {
        String sql = "SELECT task_id, campaign_id, task_title, description, "
                + "priority, deadline, status FROM Tasks ORDER BY task_id DESC";
        return jdbc.query(sql, mapper);
    }

    public Task getTaskById(int id) {
        String sql = "SELECT task_id, campaign_id, task_title, description, "
                + "priority, deadline, status FROM Tasks WHERE task_id = ?";
        List<Task> list = jdbc.query(sql, mapper, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public boolean addTask(Task t) {
        String sql = "INSERT INTO Tasks "
                + "(campaign_id, task_title, description, priority, deadline, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        return jdbc.update(sql,
                t.getCampaignId(),
                t.getTaskTitle(),
                t.getDescription(),
                t.getPriority(),
                t.getDeadline() != null ? Date.valueOf(t.getDeadline()) : null,
                t.getStatus()) > 0;
    }

    public boolean updateTask(Task t) {
        String sql = "UPDATE Tasks SET campaign_id = ?, task_title = ?, "
                + "description = ?, priority = ?, deadline = ?, status = ? "
                + "WHERE task_id = ?";
        return jdbc.update(sql,
                t.getCampaignId(),
                t.getTaskTitle(),
                t.getDescription(),
                t.getPriority(),
                t.getDeadline() != null ? Date.valueOf(t.getDeadline()) : null,
                t.getStatus(),
                t.getTaskId()) > 0;
    }

    public boolean deleteTask(int id) {
        return jdbc.update("DELETE FROM Tasks WHERE task_id = ?", id) > 0;
    }
}