package com.demo.analytics.service;
import org.springframework.beans.factory.annotation.Value; import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component; import org.springframework.web.reactive.function.client.WebClient;
import java.util.*;
@Component @Profile("!stub")
public class RealWarehouseClient implements WarehouseClient {
    private final WebClient webClient;
    public RealWarehouseClient(WebClient.Builder builder,
            @Value("${clients.warehouse.url:http://warehouse-service}")String url,
            @Value("${sc360.internal-api-key:local-dev-key}")String apiKey){
        webClient=builder.baseUrl(url).defaultHeader("X-Internal-Key",apiKey).build();
    }
    public List<Map<String,Object>> getProducts(){
        // TODO: CONNECT TO REAL warehouse-service (Consul name: warehouse-service):
        // GET /internal/stats/warehouse-summary -> {asOf,metrics,breakdowns}.
        try {
            return webClient.get().uri("/api/products").retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String,Object>>>(){}).block();
        } catch (Exception ex) {
            return List.of();
        }
    }
    public List<Map<String,Object>> getLowStock(){
        // TODO: CONNECT TO REAL warehouse-service (Consul name: warehouse-service):
        // GET /internal/stats/orders -> {asOf,metrics,breakdowns}.
        try {
            return webClient.get().uri("/api/inventory/low-stock").retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Map<String,Object>>>(){}).block();
        } catch (Exception ex) {
            return List.of();
        }
    }
}
