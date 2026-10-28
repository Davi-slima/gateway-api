package com.api.gateway.gatewayapi.core.proxy;

import java.util.List;
import java.util.Map;

public record ProxyResponse(
        int statusCode,
        Map<String, List<String>> headers,
        byte[] body
) {
}