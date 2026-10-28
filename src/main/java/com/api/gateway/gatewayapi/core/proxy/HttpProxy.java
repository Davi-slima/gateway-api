package com.api.gateway.gatewayapi.core.proxy;

import java.net.URI;

public interface HttpProxy {

    ProxyResponse forward(ProxyRequest request, URI target);

}
