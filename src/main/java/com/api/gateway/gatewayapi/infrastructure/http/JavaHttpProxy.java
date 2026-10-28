package com.api.gateway.gatewayapi.infrastructure.http;

import com.api.gateway.gatewayapi.core.proxy.HttpProxy;
import com.api.gateway.gatewayapi.core.proxy.ProxyRequest;
import com.api.gateway.gatewayapi.core.proxy.ProxyResponse;

import java.net.URI;
import java.net.http.HttpClient;

public class JavaHttpProxy implements HttpProxy {

    private final HttpClient httpClient;

    public JavaHttpProxy(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public ProxyResponse forward(
            ProxyRequest request,
            URI target) {

        throw new UnsupportedOperationException(
                "HTTP proxy not implemented yet"
        );
    }
}
