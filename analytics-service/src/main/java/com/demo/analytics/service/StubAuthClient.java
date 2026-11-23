package com.demo.analytics.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component @Profile("stub")
public class StubAuthClient implements AuthClient {
    public Map<String,Object> validateUser(String authorization) {
        return Map.of("authenticated",false,"warning","auth-service unavailable");
    }
}
