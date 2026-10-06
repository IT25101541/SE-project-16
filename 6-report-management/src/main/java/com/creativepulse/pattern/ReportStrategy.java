package com.creativepulse.pattern;

import com.creativepulse.model.Report;

/**
 * DESIGN PATTERN 2 - STRATEGY.
 *
 * Each report type has its own algorithm for building the report body.
 * The ReportService does not care which one it is using - it just calls build().
 * Adding a new report type later means adding one new class, not editing old code
 * (Open/Closed Principle).
 */
public interface ReportStrategy {

    /** Which report type this strategy handles. */
    Report.ReportType supports();

    /** Builds the report content for the given period. */
    String build(Report report);
}
