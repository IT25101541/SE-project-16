package com.creativepulse.pattern.task;

/**
 * DESIGN PATTERN – COMMAND (Employee Task Management)
 *
 * Encapsulates a single task-related action (assign, start, complete…).
 * Makes it easy to log, undo, or queue actions later.
 *
 * Compatible with creativepulse 10-3.
 */
public interface TaskCommand {

    /** Performs the action. */
    void execute();

    /** Optional short description for logging / audit. */
    default String description() {
        return this.getClass().getSimpleName();
    }
}
