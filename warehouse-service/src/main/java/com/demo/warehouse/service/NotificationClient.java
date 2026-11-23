package com.demo.warehouse.service;

public interface NotificationClient {
    void sendStockNotification(String sku, String message);
}
