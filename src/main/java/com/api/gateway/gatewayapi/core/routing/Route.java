package com.api.gateway.gatewayapi.core.routing;

import java.net.URI;

public record Route(
        String id,
        String path,
        URI target
) {
}
