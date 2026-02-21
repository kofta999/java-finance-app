package com.kofta.app.common.result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ResultTest {

    @Test
    @DisplayName("Ok result should behave correctly")
    void testOk() throws Throwable {
        Result<String, RuntimeException> result = new Result.Ok<>("value");

        assertTrue(result.isOk());
        assertFalse(result.isErr());
        assertEquals("value", result.unwrap());
        assertEquals("value", result.unwrapOrElse("other"));
    }

    @Test
    @DisplayName("Err result should behave correctly")
    void testErr() {
        RuntimeException ex = new RuntimeException("oops");
        Result<String, RuntimeException> result = new Result.Err<>(ex);

        assertFalse(result.isOk());
        assertTrue(result.isErr());

        RuntimeException thrown = assertThrows(RuntimeException.class, result::unwrap);
        assertEquals(ex, thrown);

        assertEquals("other", result.unwrapOrElse("other"));
    }
}
