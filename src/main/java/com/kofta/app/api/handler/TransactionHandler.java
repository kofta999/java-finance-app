package com.kofta.app.api.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kofta.app.transaction.Category;
import com.kofta.app.transaction.Transaction;
import com.kofta.app.transaction.TransactionDto;
import com.kofta.app.transaction.TransactionFilter;
import com.kofta.app.transaction.TransactionService;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class TransactionHandler implements ApiHandler {

    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;

    public TransactionHandler(TransactionService transactionService) {
        this.transactionService = transactionService;
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
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
            e.printStackTrace();
            returnInternalServerError(exchange);
        } catch (IllegalArgumentException e) {
            returnBadRequest(exchange, "Invalid UUID");
        }
    }

    void handleGet(HttpExchange exchange) throws IOException {
        var params = getQueryParams(exchange);
        var accountId = params.get("accountId");

        System.out.println(accountId);

        var filter = new TransactionFilter(
            accountId != null ? UUID.fromString(accountId) : null,
            Category.fromString(params.get("category")).orElse(null)
        );
        List<Transaction> Transactions = transactionService.findAll(filter);
        var json = objectMapper.writeValueAsString(Transactions);
        returnJsonResponse(exchange, 200, json);
    }

    void handlePost(HttpExchange exchange) throws IOException {
        var transactionDto = objectMapper.readValue(
            exchange.getRequestBody(),
            TransactionDto.class
        );

        Transaction newTransaction = transactionService.create(transactionDto);
        var json = objectMapper.writeValueAsString(newTransaction);

        returnJsonResponse(exchange, 201, json);
    }
}
