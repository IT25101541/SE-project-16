package com.creativepulse.dao;

import com.creativepulse.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Repository
public class TaskDAO {

    private final JdbcTemplate jdbcTemplate;

    public TaskDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // GET ALL TASKS
    public List<Task> getAllTasks() {

        String sql = """
                SELECT task_id, campaign_id, task_title, description,
                       priority, deadline, status, created_at
                FROM tasks
                ORDER BY task_id DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            Task task = new Task();

            task.setTaskId(rs.getInt("task_id"));
            task.setCampaignId(rs.getInt("campaign_id"));
            task.setTaskTitle(rs.getString("task_title"));
            task.setDescription(rs.getString("description"));
            task.setPriority(rs.getString("priority"));

            Date deadline = rs.getDate("deadline");
            if (deadline != null) {
                task.setDeadline(deadline.toLocalDate());
            }

            task.setStatus(rs.getString("status"));

            Timestamp createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                task.setCreatedAt(createdAt.toLocalDateTime());
            }

            return task;
        });
    }

    // GET TASK BY ID
    public Task getTaskById(int id) {

        String sql = """
                SELECT task_id, campaign_id, task_title, description,
                       priority, deadline, status, created_at
                FROM tasks
                WHERE task_id = ?
                """;

        List<Task> tasks = jdbcTemplate.query(sql, new Object[]{id}, (rs, rowNum) -> {

            Task task = new Task();

            task.setTaskId(rs.getInt("task_id"));
            task.setCampaignId(rs.getInt("campaign_id"));
            task.setTaskTitle(rs.getString("task_title"));
            task.setDescription(rs.getString("description"));
            task.setPriority(rs.getString("priority"));

            Date deadline = rs.getDate("deadline");
            if (deadline != null) {
                task.setDeadline(deadline.toLocalDate());
            }

            task.setStatus(rs.getString("status"));

            Timestamp createdAt = rs.getTimestamp("created_at");
            if (createdAt != null) {
                task.setCreatedAt(createdAt.toLocalDateTime());
            }

            return task;
        });

        return tasks.isEmpty() ? null : tasks.get(0);
    }

    // INSERT TASK
    public void addTask(Task task) {

        String sql = """
                INSERT INTO tasks
                (campaign_id, task_title, description, priority,
                 deadline, status, created_at)
                VALUES (?, ?, ?, ?, ?, ?, GETDATE())
                """;

        jdbcTemplate.update(
                sql,
                task.getCampaignId(),
                task.getTaskTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getDeadline() != null
                        ? Date.valueOf(task.getDeadline())
                        : null,
                task.getStatus()
        );
    }

    // UPDATE TASK
    public void updateTask(Task task) {

        String sql = """
                UPDATE tasks
                SET campaign_id = ?,
                    task_title = ?,
                    description = ?,
                    priority = ?,
                    deadline = ?,
                    status = ?
                WHERE task_id = ?
                """;

        jdbcTemplate.update(
                sql,
                task.getCampaignId(),
                task.getTaskTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getDeadline() != null
                        ? Date.valueOf(task.getDeadline())
                        : null,
                task.getStatus(),
                task.getTaskId()
        );
    }

    // DELETE TASK
    public void deleteTask(int id) {

        String sql = "DELETE FROM tasks WHERE task_id = ?";

        jdbcTemplate.update(sql, id);
    }
}
