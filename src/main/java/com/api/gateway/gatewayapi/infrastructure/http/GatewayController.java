package com.api.gateway.gatewayapi.infrastructure.http;

import com.api.gateway.gatewayapi.application.GatewayService;
import com.api.gateway.gatewayapi.core.proxy.ProxyException;
import com.api.gateway.gatewayapi.core.proxy.ProxyRequest;
import com.api.gateway.gatewayapi.core.proxy.ProxyResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class GatewayController {

    private final GatewayService service;

    public GatewayController(GatewayService service) {
        this.service = service;
    }

    @RequestMapping("/api/**")
    public ResponseEntity<?> handle(HttpServletRequest servletRequest) throws IOException {
        String path = servletRequest.getRequestURI();

        if (servletRequest.getQueryString() != null) {
            path += "?" + servletRequest.getQueryString();
        }

        Map<String, List<String>> headers =
                Collections.list(servletRequest.getHeaderNames())
                        .stream()
                        .collect(Collectors.toMap(name -> name,
                                name -> Collections.list(servletRequest.getHeaders(name))));

        ProxyRequest request = new ProxyRequest(servletRequest.getMethod(), path,
                headers, servletRequest.getInputStream().readAllBytes());

        try {
            ProxyResponse response = service.process(request);
            HttpHeaders responseHeaders = new HttpHeaders();

            response.headers().forEach((name, values) -> values.forEach(
                    value -> responseHeaders.add(name, value)
            ));

            return ResponseEntity
                    .status(response.statusCode())
                    .headers(responseHeaders)
                    .body(response.body());

        } catch (ProxyException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getMessage());
        }

    }

}
