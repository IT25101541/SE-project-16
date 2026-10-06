package com.creativepulse.repository;

import com.creativepulse.model.TaskItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<TaskItem, Long> {
    List<TaskItem> findByTitleContainingIgnoreCase(String title);
    List<TaskItem> findByAssigneeId(Long assigneeId);
    List<TaskItem> findByCampaignId(Long campaignId);
    long countByStatus(TaskItem.Status status);
}
