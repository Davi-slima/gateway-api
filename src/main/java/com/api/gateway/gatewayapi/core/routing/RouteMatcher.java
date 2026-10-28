package com.api.gateway.gatewayapi.core.routing;

public class RouteMatcher {

    public boolean matches(Route route, String requestPath) {

        String routePath = route.path();

        return requestPath.equals(routePath)
                || requestPath.startsWith(routePath + "/");
    }
}
