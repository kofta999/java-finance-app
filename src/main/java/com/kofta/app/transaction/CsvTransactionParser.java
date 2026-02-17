package com.kofta.app.transaction;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CsvTransactionParser implements TransactionParser {

    @Override
    public List<ParsedTransaction> from(InputStream stream) {
        if (stream == null) {
            throw new IllegalArgumentException("Input stream cannot be null");
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
            return iterator.readAll();
        } catch (IOException e) {
            throw new TransactionParsingException(
                "Failed to parse transactions from file",
                e
            );
        }
    }
}
