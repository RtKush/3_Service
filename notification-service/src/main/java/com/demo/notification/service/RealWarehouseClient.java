package com.demo.notification.service;
import org.springframework.beans.factory.annotation.Value; import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component; import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;
@Component @Profile("!stub")
public class RealWarehouseClient implements WarehouseClient {
    private final WebClient webClient;
    public RealWarehouseClient(WebClient.Builder builder,
            @Value("${clients.warehouse.url:http://warehouse-service}")String url,
            @Value("${sc360.internal-api-key:local-dev-key}")String apiKey){
        webClient=builder.baseUrl(url).defaultHeader("X-Internal-Key",apiKey).build();
    }
    public Map<String,Object> getInventoryStatus(){
        // TODO: CONNECT TO REAL warehouse-service (Consul name: warehouse-service):
        // GET /internal/stats/warehouse-summary -> {asOf,metrics,breakdowns}.
        try {
            return webClient.get().uri("/internal/stats/warehouse-summary")
                .retrieve().bodyToMono(Map.class).block();
        } catch (Exception ex) {
            return Map.of("status","UNAVAILABLE","warning","warehouse-service unreachable");
        }
    }
}
