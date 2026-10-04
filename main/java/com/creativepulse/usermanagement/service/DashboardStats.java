package com.creativepulse.usermanagement.service;

import com.creativepulse.usermanagement.model.Role;
import com.creativepulse.usermanagement.model.User;

import java.util.List;
import java.util.Map;

/** System statistics shown on the administrator dashboard. */
public class DashboardStats {

    private final long totalUsers;
    private final long activeUsers;
    private final long suspendedUsers;
    private final long newThisWeek;
    private final Map<Role, Long> usersByRole;
    private final List<User> recentUsers;

    public DashboardStats(long totalUsers, long activeUsers, long suspendedUsers, long newThisWeek,
                          Map<Role, Long> usersByRole, List<User> recentUsers) {
        this.totalUsers = totalUsers;
        this.activeUsers = activeUsers;
        this.suspendedUsers = suspendedUsers;
        this.newThisWeek = newThisWeek;
        this.usersByRole = usersByRole;
        this.recentUsers = recentUsers;
    }

    public long getTotalUsers() { return totalUsers; }
    public long getActiveUsers() { return activeUsers; }
    public long getSuspendedUsers() { return suspendedUsers; }
    public long getNewThisWeek() { return newThisWeek; }
    public Map<Role, Long> getUsersByRole() { return usersByRole; }
    public List<User> getRecentUsers() { return recentUsers; }
}
