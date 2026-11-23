package com.demo.analytics.service;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AnalyticsService {
    private final WarehouseClient warehouse;
    private final PeerStatsClient peers;
    public AnalyticsService(WarehouseClient warehouse, PeerStatsClient peers){
        this.warehouse=warehouse; this.peers=peers;
    }
    public Map<String,Object> summary(){
        List<Map<String,Object>> products=safe(warehouse.getProducts());
        int units=(int) products.stream().mapToDouble(p->number(p,"quantity")).sum();
        double value=products.stream().mapToDouble(p->number(p,"quantity")*number(p,"price")).sum();
        return Map.of("totalProducts",products.size(),"totalUnits",units,"inventoryValue",value,
            "lowStockCount",safe(warehouse.getLowStock()).size());
    }
    public List<Map<String,Object>> lowStock(){return safe(warehouse.getLowStock());}
    public Map<String,Object> dashboard(){return summary();}
    public Map<String,Object> report(){
        Map<String,Object> result=new LinkedHashMap<>(summary());
        result.put("products",safe(warehouse.getProducts()));
        return result;
    }
    public Map<String,Object> roleDashboard(String role) {
        Map<String,Object> result = new LinkedHashMap<>();
        result.put("role", role);
        result.put("generatedAt", java.time.Instant.now());
        result.put("warnings", peers.getWarnings());
        result.put("kpis", List.of(summary()));
        result.put("charts", List.of());
        result.put("lists", List.of());
        return result;
    }
    private static List<Map<String,Object>> safe(List<Map<String,Object>> v){return v==null?List.of():v;}
    private static double number(Map<String,Object> p,String key){Object v=p.get(key);return v instanceof Number n?n.doubleValue():0;}
}
