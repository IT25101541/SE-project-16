package com.creativepulse.pattern;

import com.creativepulse.model.Report;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * DESIGN PATTERN 3 - FACTORY.
 *
 * Returns the correct ReportStrategy object for a given report type.
 * The caller (ReportService) never uses "new" and never writes if/else chains.
 */
@Component
public class ReportStrategyFactory {

    private final Map<Report.ReportType, ReportStrategy> strategies =
            new EnumMap<>(Report.ReportType.class);

    public ReportStrategyFactory(List<ReportStrategy> allStrategies) {
        for (ReportStrategy s : allStrategies) {
            strategies.put(s.supports(), s);
        }
    }

    public ReportStrategy getStrategy(Report.ReportType type) {
        ReportStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("No report strategy found for type: " + type);
        }
        return strategy;
    }
}
