package com.demo.analytics.controller;
import com.demo.analytics.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsService service;
    public AnalyticsController(AnalyticsService service){this.service=service;}
    @GetMapping({"/summary","/inventory-summary","/inventory"})
    public Map<String,Object> summary(){return service.summary();}
    @GetMapping({"/low-stock","/inventory/low-stock"})
    public List<Map<String,Object>> lowStock(){return service.lowStock();}
    @GetMapping({"/dashboard","/report"})
    public Map<String,Object> dashboard(){return service.report();}
    @GetMapping("/health")
    public Map<String,Object> health(){return Map.of("status","UP","service","analytics-service");}

}
