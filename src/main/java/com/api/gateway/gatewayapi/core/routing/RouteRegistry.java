package com.api.gateway.gatewayapi.core.routing;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class RouteRegistry {

    private final List<Route> routes;
    private final RouteMatcher routeMatcher;

    public RouteRegistry(List<Route> routes, RouteMatcher routeMatcher) {
        this.routes = List.copyOf(routes);
        this.routeMatcher = routeMatcher;
    }

    public Optional<Route> findRoute(String path) {

        return routes.stream()
                .filter(route -> routeMatcher.matches(route, path))
                .max(Comparator.comparingInt(
                        route -> route.path().length()
                ));
    }
}
