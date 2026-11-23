package com.demo.analytics.controller;

import com.demo.analytics.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/internal/stats")
public class InternalStatsController {
    private final AnalyticsService service;

    public InternalStatsController(AnalyticsService service) {
        this.service = service;
    }

    @GetMapping("/{endpoint}")
    public Map<String, Object> stats(@PathVariable String endpoint) {
        return Map.of("asOf", Instant.now(), "metrics", service.summary(),
                "breakdowns", Map.of("endpoint", endpoint));
    }
}
