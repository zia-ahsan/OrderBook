package com.bitvavo.verifier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final InputStream originalIn = System.in;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        System.setIn(originalIn);
    }

    @Test
    void testApplicationMainProcessesOrders() {
        String input = String.join("\n",
                "10000, B, 98, 25500",
                "10005, S, 105, 20000",
                "10001, S, 100, 500",
                "10002, S, 100, 10000",
                "10003, B, 99, 50000",
                "10004, S, 103, 100",
                "10006, B, 105, 16000"
        );

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Act
        Application.main(new String[0]);

        String stdout = outContent.toString();
        String stderr = errContent.toString();

        // Assert some expected trades and order book entries
        assertTrue(stdout.contains("trade 10006, 10001, 100, 500"));
        assertTrue(stdout.contains("trade 10006, 10005, 105, 5400"));
        assertTrue(stdout.contains("     50,000")); // Buy quantity (space-padded)
        assertTrue(stderr.contains("Total time")); // Performance output
    }

    @Test
    void testApplicationHandlesIOExceptionGracefully() {
        System.setIn(new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("forced failure");
            }
        });

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            Application.main(new String[0]);
        });

        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("forced failure", thrown.getCause().getMessage());
    }
}
