package com.bitvavo.verifier;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the OrderProcessor class.
 */
class OrderProcessorTest {

    @Test
    void testSimpleTrade() {
        OrderProcessor processor = new OrderProcessor();
        processor.processOrder(new Order("1", 'B', 100, 1000));
        processor.processOrder(new Order("2", 'S', 100, 1000));

        // Redirect stdout to test output
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        processor.printOrderBook();

        System.setOut(originalOut);
        String output = out.toString();

        assertFalse(output.contains("100")); // no remaining orders expected
    }

    @Test
    void testUnmatchedOrdersGoToBook() {
        OrderProcessor processor = new OrderProcessor();
        processor.processOrder(new Order("1", 'B', 100, 5000));
        processor.processOrder(new Order("2", 'S', 105, 3000));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        processor.printOrderBook();
        System.setOut(System.out);

        String output = out.toString();
        assertTrue(output.contains("105")); // Sell side present
        assertTrue(output.contains("100")); // Buy side present
    }

    @Test
    void testSellOrderPartiallyMatchesBuyAndRequeues() {
        OrderProcessor processor = new OrderProcessor();

        // Step 1: Add a BUY order of quantity 1000
        processor.processOrder(new Order("B1", 'B', 100, 1000));

        // Step 2: Add a SELL order of quantity 400 (partial match)
        processor.processOrder(new Order("S1", 'S', 100, 400));

        // - B1 is partially matched (600 remaining)
        // - It gets re-added to the book

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        processor.printOrderBook();

        System.setOut(originalOut);
        String output = out.toString();

        assertTrue(output.contains("     600")); // Remaining 600 from B1 should be in book
        assertTrue(output.contains("100"));     // Price level 100 should exist
    }

}
