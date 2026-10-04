<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>

  <meta charset="UTF-8">

  <title>Employee Task Management - CreativePulse</title>

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/css/style.css">

</head>

<body>

<div class="app">

  <aside class="sidebar">

    <div class="brand">
      Creative<span>Pulse</span>
    </div>

    <div class="small">
      Advertising Agency Management
    </div>

    <nav>

      <a href="${pageContext.request.contextPath}/dashboard">
        Dashboard
      </a>

      <a href="${pageContext.request.contextPath}/users">
        User Management
      </a>

      <a href="${pageContext.request.contextPath}/tasks">
        Employee Task Management
      </a>

      <a href="${pageContext.request.contextPath}/campaigns">
        Campaign Management
      </a>

      <a href="${pageContext.request.contextPath}/advertisements">
        Advertisement Management
      </a>

      <a href="${pageContext.request.contextPath}/billing">
        Billing & Payment
      </a>

      <a href="${pageContext.request.contextPath}/reports">
        Report Management
      </a>

      <a href="${pageContext.request.contextPath}/profile">
        Profile
      </a>

      <a href="${pageContext.request.contextPath}/logout">
        Logout
      </a>

    </nav>

  </aside>


  <main class="main">

    <h1>Employee Task Management</h1>

    <p class="muted">
      Manage campaign tasks, priorities, deadlines and statuses.
    </p>


    <div class="toolbar">

      <a href="${pageContext.request.contextPath}/tasks/add">
        <button class="primary">
          Add Task
        </button>
      </a>

    </div>


    <div class="panel">

      <table>

        <thead>

        <tr>
          <th>ID</th>
          <th>Task</th>
          <th>Campaign ID</th>
          <th>Priority</th>
          <th>Deadline</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>

        </thead>


        <tbody>

        <%
          java.util.List<com.creativepulse.model.Task> tasks =
                  (java.util.List<com.creativepulse.model.Task>)
                          request.getAttribute("tasks");

          if (tasks != null && !tasks.isEmpty()) {

            for (com.creativepulse.model.Task task : tasks) {
        %>

        <tr>

          <td>
            <%= task.getTaskId() %>
          </td>

          <td>
            <%= task.getTaskTitle() %>
          </td>

          <td>
            <%= task.getCampaignId() %>
          </td>

          <td>
            <%= task.getPriority() %>
          </td>

          <td>
            <%= task.getDeadline() %>
          </td>

          <td>
                        <span class="badge">
                            <%= task.getStatus() %>
                        </span>
          </td>

          <td>

            <a href="${pageContext.request.contextPath}/tasks/edit/<%= task.getTaskId() %>">
              <button type="button">
                Edit
              </button>
            </a>

            <a href="${pageContext.request.contextPath}/tasks/delete/<%= task.getTaskId() %>"
               onclick="return confirm('Are you sure you want to delete this task?');">

              <button type="button">
                Delete
              </button>

            </a>

          </td>

        </tr>

        <%
          }

        } else {
        %>

        <tr>

          <td colspan="7">
            No tasks found.
          </td>

        </tr>

        <%
          }
        %>

        </tbody>

      </table>

    </div>

  </main>

</div>

</body>
</html>