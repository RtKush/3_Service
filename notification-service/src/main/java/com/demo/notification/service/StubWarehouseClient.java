package com.demo.notification.service;
import org.springframework.context.annotation.Profile; import org.springframework.stereotype.Component;
import java.util.Map;
@Component @Profile("stub")
public class StubWarehouseClient implements WarehouseClient {
    public Map<String,Object> getInventoryStatus(){return Map.of("status","stub","message","Warehouse unavailable");}
}
