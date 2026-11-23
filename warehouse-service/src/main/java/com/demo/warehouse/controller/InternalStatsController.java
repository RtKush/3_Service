package com.demo.warehouse.controller;

import com.demo.warehouse.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping({"/internal/stats","/api/v1/warehouse/internal/stats"})
public class InternalStatsController {
    private final ProductService service;

    public InternalStatsController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/{endpoint}")
    public Map<String,Object> stats(@PathVariable String endpoint) {
        Map<String,Object> metrics = new LinkedHashMap<>();
        metrics.put("totalProducts", service.findAll().size());
        metrics.put("lowStockCount", service.lowStock().size());
        Map<String,Object> result = new LinkedHashMap<>();
        result.put("asOf", Instant.now());
        result.put("metrics", metrics);
        result.put("breakdowns", Map.of("endpoint", endpoint));
        return result;
    }
}
