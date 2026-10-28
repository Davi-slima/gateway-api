package com.api.gateway.gatewayapi.infrastructure.http;

import com.api.gateway.gatewayapi.core.routing.Route;
import com.api.gateway.gatewayapi.core.routing.RouteMatcher;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoutingConfigTest {

    private final RouteMatcher matcher = new RouteMatcher();

    private final Route route = new Route(
            "users-service",
            "/api/users",
            URI.create("http://localhost:8081")
    );

    @Test
    void shouldMatchExactPath() {
        assertTrue(matcher.matches(route, "/api/users"));
    }

    @Test
    void shouldMatchNestedPath() {
        assertTrue(matcher.matches(route, "/api/users/123"));
    }

    @Test
    void shouldNotMatchDifferentPath() {
        assertFalse(matcher.matches(route, "/api/products"));
    }

    @Test
    void shouldNotMatchSimilarPrefix() {
        assertFalse(matcher.matches(route, "/api/users-admin"));
    }

}
