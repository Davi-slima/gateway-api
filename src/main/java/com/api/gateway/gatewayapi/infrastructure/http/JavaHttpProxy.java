package com.api.gateway.gatewayapi.infrastructure.http;

import com.api.gateway.gatewayapi.core.proxy.HttpProxy;
import com.api.gateway.gatewayapi.core.proxy.ProxyException;
import com.api.gateway.gatewayapi.core.proxy.ProxyRequest;
import com.api.gateway.gatewayapi.core.proxy.ProxyResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.*;

@Component
public class JavaHttpProxy implements HttpProxy {

    private static final Set<String> EXCLUDED_HEADERS = Set.of("host", "connection",
            "content-length", "transfer-encoding", "keep-alive",
            "te", "trailer", "upgrade", "proxy-connection",
            "proxy-authenticate", "proxy-authorization", "expect"
    );

    private final HttpClient httpClient;

    public JavaHttpProxy(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public ProxyResponse forward(ProxyRequest request, URI target) {

        try {
            HttpRequest.Builder builder = HttpRequest
                    .newBuilder(target)
                    .timeout(Duration.ofSeconds(15));

            Set<String> excluded = new HashSet<>(EXCLUDED_HEADERS);

            request.headers().forEach((name, values) -> {
                if (name.equalsIgnoreCase("connection")) {
                    values.forEach(value -> {
                        Arrays.stream(value.split(","))
                                .map(String::trim)
                                .map(string -> string.toLowerCase(Locale.ROOT))
                                .forEach(excluded::add);
                    });
                }
            });

            request.headers().forEach((name, values) -> {
                if (!excluded.contains(name.toLowerCase(Locale.ROOT))) {
                    values.forEach(value -> builder.header(name, value));
                }
            });

            byte[] body = request.body() == null
                    ? new byte[0]
                    : request.body();

            builder.method(request.method(),
                    HttpRequest.BodyPublishers.ofByteArray(body));

            HttpResponse<byte[]> response =
                    httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofByteArray());

            Map<String, List<String>> headers =
                    new HashMap<>(response.headers().map());

            Set<String> excludedResponse = new HashSet<>(EXCLUDED_HEADERS);

            response.headers().allValues("connection")
                    .forEach(value ->
                            Arrays.stream(value.split(","))
                                    .map(String::trim)
                                    .map(String -> String.toLowerCase(Locale.ROOT))
                                    .forEach(excludedResponse::add));

            headers.keySet().removeIf(name ->
                    excludedResponse.contains(name.toLowerCase(Locale.ROOT)));

            return new ProxyResponse(response.statusCode(), headers, response.body());

        } catch (HttpTimeoutException e) {
            throw new ProxyException(
                    504, "Gateway Timeout", e
            );
        } catch (IOException e) {
            throw new ProxyException(
                    502, "Bad Gateway", e
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new ProxyException(
                    502, "Proxy request interrupted", e
            );

        }
    }
}