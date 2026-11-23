package com.demo.analytics.service;

import java.util.Map;

public interface AuthClient {
    Map<String,Object> validateUser(String authorization);
}
