package com.demo.analytics.service;
import org.springframework.context.annotation.Profile; import org.springframework.stereotype.Component;
import java.util.*;
@Component @Profile("stub")
public class StubWarehouseClient implements WarehouseClient {
    public List<Map<String,Object>> getProducts(){return List.of(Map.of("sku","STUB-1","name","Stub Product","quantity",5,"reorderLevel",10,"price",99.0));}
    public List<Map<String,Object>> getLowStock(){return getProducts();}
}
