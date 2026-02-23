package com.kofta.app.api.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public interface ApiHandler extends HttpHandler {
    default void returnJsonResponse(
        HttpExchange exchange,
        int statusCode,
        String json
    ) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, json.length());

        try (var os = exchange.getResponseBody()) {
            os.write(json.getBytes());
        }
    }

    default void handleMethodNotImplemented(HttpExchange exchange)
        throws IOException {
        byte[] res = "Method Not Implemented".getBytes();
        exchange.sendResponseHeaders(501, res.length);

        try (var os = exchange.getResponseBody()) {
            os.write(res);
        }
    }

    default Map<String, String> getQueryParams(HttpExchange exchange) {
        // Input: /accounts?userId=abc
        var map = new HashMap<String, String>();
        var query = exchange.getRequestURI().getQuery();

        if (query != null) {
            for (var param : query.split("&")) {
                var pair = param.split("=");
                if (pair.length == 2) {
                    map.put(pair[0], pair[1]);
                }
            }
        }

        return map;
    }

    default void returnNotFound(HttpExchange exchange, String message)
        throws IOException {
        String res = "{\"message\": \"%s\" }".formatted(message);

        returnJsonResponse(exchange, 404, res);
    }

    default void returnBadRequest(HttpExchange exchange, String message)
        throws IOException {
        String res = "{\"message\": \"%s\" }".formatted(message);

        returnJsonResponse(exchange, 400, res);
    }

    default void returnInternalServerError(HttpExchange exchange)
        throws IOException {
        String res = "{\"message\": \"Internal Server Error\" }";

        returnJsonResponse(exchange, 400, res);
    }
}
