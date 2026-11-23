package com.demo.analytics.service;
import java.util.*;
public interface WarehouseClient {
    List<Map<String,Object>> getProducts();
    List<Map<String,Object>> getLowStock();
}
