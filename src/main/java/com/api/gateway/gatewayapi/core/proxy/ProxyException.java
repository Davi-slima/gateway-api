package com.api.gateway.gatewayapi.core.proxy;

public class ProxyException extends RuntimeException {

    private final int statusCode;

    public ProxyException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

}
