package com.kofta.app.api.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kofta.app.user.User;
import com.kofta.app.user.UserDto;
import com.kofta.app.user.UserService;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;

public class UserHandler implements ApiHandler {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    public UserHandler(UserService userService) {
        this.userService = userService;
        this.objectMapper = new ObjectMapper();
    }

    // TODO:DELETE

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            switch (exchange.getRequestMethod()) {
                case "GET" -> handleGet(exchange);
                case "POST" -> handlePost(exchange);
                default -> handleMethodNotImplemented(exchange);
            }
        } catch (IOException e) {
            returnInternalServerError(exchange);
        }
    }

    void handleGet(HttpExchange exchange) throws IOException {
        List<User> users = userService.findAll();
        var json = objectMapper.writeValueAsString(users);
        returnJsonResponse(exchange, 200, json);
    }

    void handlePost(HttpExchange exchange) throws IOException {
        var userDto = objectMapper.readValue(
            exchange.getRequestBody(),
            UserDto.class
        );

        User newUser = userService.create(userDto);
        var json = objectMapper.writeValueAsString(newUser);

        returnJsonResponse(exchange, 201, json);
    }
}
