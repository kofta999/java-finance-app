package com.kofta.app.transaction;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class TransactionParser {

    public static List<Transaction> fromCsvFile(Path filePath)
        throws TransactionParsingException {
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
            MappingIterator<Transaction> iterator = mapper
                .readerFor(Transaction.class)
                .with(schema)
                .readValues(filePath.toFile());
        ) {
            return iterator.readAll();
        } catch (IOException e) {
            throw new TransactionParsingException(
                "Failed to parse transactions from: " + filePath,
                e
            );
        }
    }
}
