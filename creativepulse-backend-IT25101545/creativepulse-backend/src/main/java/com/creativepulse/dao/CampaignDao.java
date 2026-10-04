package com.creativepulse.dao;

import com.creativepulse.model.Campaign;
import com.creativepulse.model.CampaignAssignment;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class CampaignDao {

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert insert;
    private final SimpleJdbcInsert assignInsert;

    private static final String SELECT = """
            SELECT c.*, cl.company_name AS client_name
            FROM campaigns c JOIN clients cl ON cl.client_id = c.client_id
            """;

    private static final RowMapper<Campaign> MAPPER = (rs, i) -> new Campaign(
            rs.getInt("campaign_id"), rs.getInt("client_id"), rs.getString("client_name"),
            rs.getString("name"), rs.getString("description"), rs.getBigDecimal("budget"),
            rs.getDate("start_date").toLocalDate(), rs.getDate("end_date").toLocalDate(),
            rs.getString("status"), rs.getInt("progress"));

    private static final RowMapper<CampaignAssignment> ASSIGN_MAPPER = (rs, i) -> {
        Timestamp ts = rs.getTimestamp("assigned_at");
        return new CampaignAssignment(rs.getInt("assignment_id"), rs.getInt("campaign_id"),
                rs.getInt("user_id"), rs.getString("full_name"), rs.getString("role_in_team"),
                ts == null ? null : ts.toLocalDateTime());
    };

    public CampaignDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.insert = new SimpleJdbcInsert(jdbc).withTableName("campaigns")
                .usingGeneratedKeyColumns("campaign_id")
                .usingColumns("client_id", "name", "description", "budget", "start_date", "end_date", "status", "progress");
        this.assignInsert = new SimpleJdbcInsert(jdbc).withTableName("campaign_assignments")
                .usingGeneratedKeyColumns("assignment_id")
                .usingColumns("campaign_id", "user_id", "role_in_team");
    }

    public int create(Campaign c) {
        Map<String, Object> p = new HashMap<>();
        p.put("client_id", c.clientId());
        p.put("name", c.name());
        p.put("description", c.description());
        p.put("budget", c.budget());
        p.put("start_date", Date.valueOf(c.startDate()));
        p.put("end_date", Date.valueOf(c.endDate()));
        p.put("status", c.status() == null ? "PLANNED" : c.status());
        p.put("progress", c.progress() == null ? 0 : c.progress());
        return insert.executeAndReturnKey(p).intValue();
    }

    public List<Campaign> findAll(String status, String keyword) {
        StringBuilder sql = new StringBuilder(SELECT).append(" WHERE 1=1 ");
        List<Object> args = new ArrayList<>();
        if (status != null && !status.isBlank()) {
            sql.append(" AND c.status = ? ");
            args.add(status);
        }
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (c.name LIKE ? OR cl.company_name LIKE ?) ");
            args.add("%" + keyword + "%");
            args.add("%" + keyword + "%");
        }
        sql.append(" ORDER BY c.start_date DESC");
        return jdbc.query(sql.toString(), MAPPER, args.toArray());
    }

    public Optional<Campaign> findById(int id) {
        return jdbc.query(SELECT + " WHERE c.campaign_id = ?", MAPPER, id).stream().findFirst();
    }

    public int update(int id, Campaign c) {
        return jdbc.update("""
                UPDATE campaigns SET client_id=?, name=?, description=?, budget=?, start_date=?,
                end_date=?, status=?, progress=? WHERE campaign_id=?""",
                c.clientId(), c.name(), c.description(), c.budget(), Date.valueOf(c.startDate()),
                Date.valueOf(c.endDate()), c.status() == null ? "PLANNED" : c.status(),
                c.progress() == null ? 0 : c.progress(), id);
    }

    public int updateStatus(int id, String status, int progress) {
        return jdbc.update("UPDATE campaigns SET status=?, progress=? WHERE campaign_id=?", status, progress, id);
    }

    public int updateSchedule(int id, LocalDate start, LocalDate end) {
        return jdbc.update("UPDATE campaigns SET start_date=?, end_date=? WHERE campaign_id=?",
                Date.valueOf(start), Date.valueOf(end), id);
    }

    public int delete(int id) {
        return jdbc.update("DELETE FROM campaigns WHERE campaign_id = ?", id);
    }

    public Map<String, Integer> countByStatus() {
        Map<String, Integer> m = new LinkedHashMap<>();
        jdbc.query("SELECT status, COUNT(*) AS n FROM campaigns GROUP BY status",
                rs -> { m.put(rs.getString("status"), rs.getInt("n")); });
        return m;
    }

    public int assign(int campaignId, int userId, String role) {
        Map<String, Object> p = new HashMap<>();
        p.put("campaign_id", campaignId);
        p.put("user_id", userId);
        p.put("role_in_team", role);
        return assignInsert.executeAndReturnKey(p).intValue();
    }

    public List<CampaignAssignment> findAssignments(int campaignId) {
        return jdbc.query("""
                SELECT a.*, u.full_name FROM campaign_assignments a
                JOIN users u ON u.user_id = a.user_id
                WHERE a.campaign_id = ? ORDER BY a.assigned_at""", ASSIGN_MAPPER, campaignId);
    }

    public int unassign(int campaignId, int userId) {
        return jdbc.update("DELETE FROM campaign_assignments WHERE campaign_id=? AND user_id=?", campaignId, userId);
    }
}
