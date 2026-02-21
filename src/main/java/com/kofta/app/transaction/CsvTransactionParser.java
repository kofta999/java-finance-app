package com.kofta.app.transaction;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kofta.app.common.result.Result;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CsvTransactionParser implements TransactionParser {

    @Override
    public Result<List<ParsedTransaction>, TransactionParsingError> from(
        InputStream stream
    ) {
        if (stream == null) {
            return new Result.Err<>(
                new TransactionParsingError(
                    "Input stream cannot be null",
                    new IllegalAccessException()
                )
            );
        }

        var mapper = new CsvMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        var schema = CsvSchema.builder()
            .addColumn("date")
            .addColumn("description")
            .addColumn("amount")
            .addColumn("category")
            .setUseHeader(true)
            .build();

        try (
            MappingIterator<ParsedTransaction> iterator = mapper
                .readerFor(ParsedTransaction.class)
                .with(schema)
                .readValues(stream);
        ) {
            return new Result.Ok<>(iterator.readAll());
        } catch (IOException e) {
            return new Result.Err<>(
                new TransactionParsingError(
                    "Failed to parse transactions from file",
                    e
                )
            );
        }
    }
}
