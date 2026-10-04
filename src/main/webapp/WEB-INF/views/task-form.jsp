<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Task Form - CreativePulse</title>

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
            Create or update an employee task.
        </p>


        <div class="panel">

            <form method="post"
                  action="${pageContext.request.contextPath}/tasks/save">

                <input type="hidden"
                       name="taskId"
                       value="${task.taskId}">


                <div class="form-group">

                    <label>Campaign ID</label>

                    <input type="number"
                           name="campaignId"
                           value="${task.campaignId}"
                           required>

                </div>


                <div class="form-group">

                    <label>Task Title</label>

                    <input type="text"
                           name="taskTitle"
                           value="${task.taskTitle}"
                           maxlength="180"
                           required>

                </div>


                <div class="form-group">

                    <label>Description</label>

                    <textarea name="description"
                              maxlength="1000"
                              rows="5">${task.description}</textarea>

                </div>


                <div class="form-group">

                    <label>Priority</label>

                    <select name="priority" required>

                        <option value="">Select Priority</option>

                        <option value="Low"
                        ${task.priority == 'Low' ? 'selected' : ''}>
                            Low
                        </option>

                        <option value="Medium"
                        ${task.priority == 'Medium' ? 'selected' : ''}>
                            Medium
                        </option>

                        <option value="High"
                        ${task.priority == 'High' ? 'selected' : ''}>
                            High
                        </option>

                    </select>

                </div>


                <div class="form-group">

                    <label>Deadline</label>

                    <input type="date"
                           name="deadline"
                           value="${task.deadline}">

                </div>


                <div class="form-group">

                    <label>Status</label>

                    <select name="status" required>

                        <option value="">Select Status</option>

                        <option value="Assigned"
                        ${task.status == 'Assigned' ? 'selected' : ''}>
                            Assigned
                        </option>

                        <option value="In Progress"
                        ${task.status == 'In Progress' ? 'selected' : ''}>
                            In Progress
                        </option>

                        <option value="Blocked"
                        ${task.status == 'Blocked' ? 'selected' : ''}>
                            Blocked
                        </option>

                        <option value="Completed"
                        ${task.status == 'Completed' ? 'selected' : ''}>
                            Completed
                        </option>

                    </select>

                </div>


                <div class="toolbar">

                    <button type="submit" class="primary">
                        Save Task
                    </button>

                    <a href="${pageContext.request.contextPath}/tasks">
                        <button type="button">
                            Cancel
                        </button>
                    </a>

                </div>

            </form>

        </div>

    </main>

</div>

</body>
</html>