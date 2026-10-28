package com.api.gateway.gatewayapi.core.proxy;

import java.util.List;
import java.util.Map;

public record ProxyRequest(
        String method,
        String path,
        Map<String, List<String>> headers,
        byte[] body
) {
}