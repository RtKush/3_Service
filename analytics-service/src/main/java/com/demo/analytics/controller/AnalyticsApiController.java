package com.demo.analytics.controller;

import com.demo.analytics.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsApiController {
    private final AnalyticsService service;
    private final com.demo.analytics.service.AuthClient auth;

    public AnalyticsApiController(AnalyticsService service,
                                  com.demo.analytics.service.AuthClient auth) {
        this.service = service; this.auth = auth;
    }

    @GetMapping("/dashboard/{role}")
    public Map<String, Object> dashboard(@PathVariable String role) {
        return service.roleDashboard(role);
    }

    @GetMapping("/report")
    public Map<String,Object> report() {
        return service.report();
    }

    @GetMapping("/auth/me")
    public Map<String,Object> authMe(@RequestHeader(value="Authorization",required=false) String authorization) {
        return auth.validateUser(authorization);
    }

    @GetMapping(value="/export/csv", produces="text/csv")
    public ResponseEntity<byte[]> exportCsv() {
        StringBuilder csv=new StringBuilder("metric,value\n");
        service.summary().forEach((key,value)->csv.append(key).append(',').append(value).append('\n'));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=analytics-report.csv")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(csv.toString().getBytes(StandardCharsets.UTF_8));
    }
}
