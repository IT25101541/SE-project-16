package com.creativepulse.dao;

import com.creativepulse.model.Task;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskDAOImpl implements TaskDAO {

 private final JdbcTemplate jdbcTemplate;
 private final RowMapper<Task> taskMapper;

 private static final String SELECT_BASE =
         "SELECT t.*, c.campaign_name " +
                 "FROM tasks t " +
                 "LEFT JOIN campaigns c ON t.campaign_id = c.campaign_id ";

 public TaskDAOImpl(JdbcTemplate jdbcTemplate) {
  this.jdbcTemplate = jdbcTemplate;
  this.taskMapper = new BeanPropertyRowMapper<>(Task.class);
 }

 @Override
 public List<Task> findAll() {
  return jdbcTemplate.query(SELECT_BASE + "ORDER BY t.task_id DESC", taskMapper);
 }

 @Override
 public Task findById(int taskId) {
  List<Task> list = jdbcTemplate.query(
          SELECT_BASE + "WHERE t.task_id = ?", taskMapper, taskId);
  return list.isEmpty() ? null : list.get(0);
 }

 @Override
 public void save(Task task) {
  jdbcTemplate.update(
          "INSERT INTO tasks (campaign_id, task_title, description, priority, deadline, status) " +
                  "VALUES (?, ?, ?, ?, ?, ?)",
          task.getCampaignId(), task.getTaskTitle(), task.getDescription(),
          task.getPriority(), task.getDeadline(), task.getStatus());
 }

 @Override
 public void update(Task task) {
  jdbcTemplate.update(
          "UPDATE tasks SET campaign_id = ?, task_title = ?, description = ?, " +
                  "priority = ?, deadline = ?, status = ? WHERE task_id = ?",
          task.getCampaignId(), task.getTaskTitle(), task.getDescription(),
          task.getPriority(), task.getDeadline(), task.getStatus(), task.getTaskId());
 }

 @Override
 public void delete(int taskId) {
  jdbcTemplate.update("DELETE FROM tasks WHERE task_id = ?", taskId);
 }

 @Override
 public List<Task> search(String keyword) {
  String like = "%" + keyword.trim() + "%";
  return jdbcTemplate.query(
          SELECT_BASE +
                  "WHERE t.task_title LIKE ? OR t.description LIKE ? " +
                  "OR t.status LIKE ? OR t.priority LIKE ? OR c.campaign_name LIKE ? " +
                  "ORDER BY t.task_id DESC",
          taskMapper, like, like, like, like, like);
 }
}