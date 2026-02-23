package com.kofta.app.api.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kofta.app.account.AccountService;
import com.kofta.app.finance.FinanceService;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.UUID;

public class AccountHandler implements ApiHandler {

    private final AccountService accountService;
    private final FinanceService financeService;
    private final ObjectMapper objectMapper;

    public AccountHandler(
        AccountService accountService,
        FinanceService financeService
    ) {
        this.accountService = accountService;
        this.financeService = financeService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            switch (exchange.getRequestMethod()) {
                case "GET" -> handleGet(exchange);
                default -> handleMethodNotImplemented(exchange);
            }
        } catch (IOException e) {
            returnInternalServerError(exchange);
        } catch (IllegalArgumentException e) {
            returnBadRequest(exchange, "Invalid ID");
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        var params = getQueryParams(exchange);
        String userId = params.get("userId");

        var pathComponents = exchange.getRequestURI().getPath().split("/");

        if (pathComponents.length == 2) {
            if (userId == null) {
                returnBadRequest(exchange, "userId is required");
            }

            var accounts = accountService.findByUserId(UUID.fromString(userId));
            var json = objectMapper.writeValueAsString(accounts);

            returnJsonResponse(exchange, 200, json);
        } else if (
            pathComponents.length == 4 && pathComponents[3].equals("balance")
        ) {
            var accountId = UUID.fromString(pathComponents[2]);
            var total = financeService.calculateTotal(accountId);

            ObjectNode root = objectMapper.createObjectNode();
            root.put("balance", total);

            returnJsonResponse(
                exchange,
                200,
                objectMapper.writeValueAsString(root)
            );
        } else {
            returnNotFound(exchange, "Page not found");
        }
    }
}
