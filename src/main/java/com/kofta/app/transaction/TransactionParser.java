package com.kofta.app.transaction;

import java.io.InputStream;
import java.util.List;

public interface TransactionParser {
    public List<ParsedTransaction> from(InputStream stream);
}
