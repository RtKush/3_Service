package com.demo.warehouse.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@Component @Profile("!stub")
public class RealNotificationClient implements NotificationClient {
    private final WebClient webClient;
    private static final Logger log=LoggerFactory.getLogger(RealNotificationClient.class);
    public RealNotificationClient(WebClient.Builder builder,
            @Value("${clients.notification.url:http://notification-service}") String url,
            @Value("${sc360.internal-api-key:local-dev-key}") String apiKey) {
        webClient=builder.baseUrl(url).defaultHeader("X-Internal-Key",apiKey).build();
    }
    public void sendStockNotification(String sku, String message) {
        // TODO: CONNECT TO REAL notification-service (Consul name: notification-service):
        // POST /api/v1/notifications with {userId,type,title,message,severity,link} -> persisted notification JSON.
        try {
            webClient.post().uri("/api/v1/notifications")
                .bodyValue(Map.of("userId",1L,"type","STOCK","title","Low stock",
                                  "message",message,"severity","HIGH","link","/warehouse"))
                .retrieve().toBodilessEntity().block();
        } catch (Exception ex) {
            log.warn("Notification call failed for SKU {}: {}",sku,ex.getMessage());
        }
    }
}
