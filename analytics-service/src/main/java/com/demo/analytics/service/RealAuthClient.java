package com.demo.analytics.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@Component @Profile("!stub")
public class RealAuthClient implements AuthClient {
    private final WebClient webClient;
    private final Logger log=LoggerFactory.getLogger(RealAuthClient.class);

    public RealAuthClient(WebClient.Builder builder,
            @Value("${clients.auth.url:http://auth-service}") String url,
            @Value("${sc360.internal-api-key:local-dev-key}") String apiKey) {
        webClient=builder.baseUrl(url).defaultHeader("X-Internal-Key",apiKey).build();
    }

    public Map<String,Object> validateUser(String authorization) {
        // TODO: CONNECT TO REAL auth-service (Consul name: auth-service):
        // GET /internal/users?role={role}&locationId={warehouseId}&supplierId={supplierId}
        // -> [{id,email,fullName}].
        try {
            return webClient.get().uri("/api/v1/auth/me")
                .header("Authorization",authorization==null?"":authorization)
                .retrieve().bodyToMono(Map.class).block();
        } catch (Exception ex) {
            log.warn("Auth validation unavailable: {}",ex.getMessage());
            return Map.of("authenticated",false,"warning","auth-service unavailable");
        }
    }
}
