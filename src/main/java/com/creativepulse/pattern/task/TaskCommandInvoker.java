package com.creativepulse.pattern.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Invoker for Task Commands.
 * Central place that executes any TaskCommand and can later
 * add logging, audit trail, or undo support.
 *
 * Compatible with creativepulse 10-3.
 */
@Component
public class TaskCommandInvoker {

    private static final Logger log = LoggerFactory.getLogger(TaskCommandInvoker.class);

    public void execute(TaskCommand command) {
        log.info("[TaskCommand] Executing: {}", command.description());
        command.execute();
    }
}
