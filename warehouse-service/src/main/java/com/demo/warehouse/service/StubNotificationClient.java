package com.demo.warehouse.service;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile; import org.springframework.stereotype.Component;

@Component @Profile("stub")
public class StubNotificationClient implements NotificationClient {
    private static final Logger log = LoggerFactory.getLogger(StubNotificationClient.class);
    public void sendStockNotification(String sku, String message) { log.info("[stub notification] {}: {}", sku, message); }
}
