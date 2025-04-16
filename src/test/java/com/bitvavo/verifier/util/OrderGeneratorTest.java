package com.bitvavo.verifier.util;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the OrderGenerator class.
 */
class OrderGeneratorTest {

    /**
     * Tests that the generateOrders method creates the correct number of orders.
     *
     * @throws Exception If an error occurs during the test.
     */
    @Test
    void testGenerateOrdersCreatesCorrectNumberOfLines() throws Exception {
        int numOrders = 10;
        File tempFile = File.createTempFile("orders_test_", ".txt");
        tempFile.deleteOnExit();

        OrderGenerator.generateOrders(numOrders, tempFile.getAbsolutePath());

        int lineCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile))) {
            while (reader.readLine() != null) {
                lineCount++;
            }
        }

        assertEquals(numOrders, lineCount, "Number of generated orders should match input count");
    }
}
