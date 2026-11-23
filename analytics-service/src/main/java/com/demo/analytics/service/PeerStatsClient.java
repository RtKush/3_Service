package com.demo.analytics.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class PeerStatsClient {
    private final WebClient webClient;
    private final Logger log = LoggerFactory.getLogger(PeerStatsClient.class);
    private final Map<String,String> peers = new LinkedHashMap<>();

    public PeerStatsClient(WebClient.Builder builder,
                           @Value("${sc360.internal-api-key:local-dev-key}") String apiKey) {
        webClient = builder.defaultHeader("X-Internal-Key", apiKey).build();
        peers.put("warehouse-summary","http://warehouse-service");
        peers.put("inventory-health","http://inventory-service");
        peers.put("procurement-summary","http://procurement-service");
        peers.put("supplier-performance","http://supplier-service");
        peers.put("logistics-summary","http://transport-service");
    }

    public List<String> getWarnings() {
        List<String> warnings = new ArrayList<>();
        for (Map.Entry<String,String> peer : peers.entrySet()) {
            try {
                // TODO: CONNECT TO REAL peer service at the exact Consul name and endpoint:
                // GET <service-name>/internal/stats/<endpoint> -> {asOf, metrics, breakdowns}.
                webClient.get().uri(peer.getValue()+"/internal/stats/"+peer.getKey())
                    .retrieve().toBodilessEntity().block();
            } catch (Exception ex) {
                String warning = peer.getKey()+" unavailable";
                warnings.add(warning);
                log.warn("{}: {}", warning, ex.getMessage());
            }
        }
        return warnings;
    }
}
