package com.api.gateway.gatewayapi.application;

import com.api.gateway.gatewayapi.core.proxy.HttpProxy;
import com.api.gateway.gatewayapi.core.proxy.ProxyRequest;
import com.api.gateway.gatewayapi.core.proxy.ProxyResponse;
import com.api.gateway.gatewayapi.core.routing.Route;
import com.api.gateway.gatewayapi.core.routing.RouteRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@Service
public class GatewayService {

    private final RouteRegistry registry;
    private final HttpProxy proxy;

    public GatewayService(RouteRegistry registry, HttpProxy proxy) {
        this.registry = registry;
        this.proxy = proxy;
    }

    public ProxyResponse process(ProxyRequest request) {
        String fullPath = request.path();
        String path = fullPath.split("\\?", 2)[0];

        Route route = registry.findRoute(path)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found"));

        URI target = buildTarget(route.target(), fullPath);

        return proxy.forward(request, target);
    }

    private URI buildTarget(URI baseUri, String fullPath) {
        String base = baseUri.toString();

        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }

        return URI.create(base + fullPath);
    }
}
