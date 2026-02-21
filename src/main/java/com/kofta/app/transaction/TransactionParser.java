package com.kofta.app.transaction;

import com.kofta.app.common.result.Result;
import java.io.InputStream;
import java.util.List;

public interface TransactionParser {
    public Result<List<ParsedTransaction>, TransactionParsingError> from(
        InputStream stream
    );
}
