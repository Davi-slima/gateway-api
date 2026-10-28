package com.api.gateway.gatewayapi.infrastructure.http.config;

import com.api.gateway.gatewayapi.core.routing.Route;
import com.api.gateway.gatewayapi.core.routing.RouteMatcher;
import com.api.gateway.gatewayapi.core.routing.RouteRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.util.List;

@Configuration
public class RoutingConfig {

    @Bean
    public RouteMatcher routeMatcher() {
        return new RouteMatcher();
    }

    @Bean
    public RouteRegistry routeRegistry(RouteMatcher matcher) {
        List<Route> routes = List.of(
                new Route(
                        "user-service",
                        "/api/users",
                        URI.create("http://localhost:8081")
                ),
                new Route(
                        "payments-service",
                        "/api/payments",
                        URI.create("http://localhost:8082")
                ),
                new Route(
                        "products-service",
                        "/api/products",
                        URI.create("http://localhost:8083")
                )
        );

        return new RouteRegistry(routes, matcher);
    }

}
