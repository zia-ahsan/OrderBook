package com.bitvavo.verifier.util;

import com.bitvavo.verifier.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OrderBookPrinterTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testOrderBookFormattingMatchesSpec() {
        NavigableMap<Integer, Queue<Order>> buy = new TreeMap<>(Collections.reverseOrder());
        NavigableMap<Integer, Queue<Order>> sell = new TreeMap<>();

        buy.put(99, new LinkedList<>(List.of(new Order("B1", 'B', 99, 50000))));
        buy.put(98, new LinkedList<>(List.of(new Order("B2", 'B', 98, 25500))));

        sell.put(100, new LinkedList<>(List.of(new Order("S1", 'S', 100, 10000))));
        sell.put(103, new LinkedList<>(List.of(new Order("S2", 'S', 103, 100))));
        sell.put(105, new LinkedList<>(List.of(new Order("S3", 'S', 105, 20000))));

        OrderBookPrinter.printOrderBook(buy, sell);
        String output = outContent.toString();

        String[] lines = output.split("\\n");

        assertEquals(3, lines.length, "There should be exactly 3 lines in the table");

        assertEquals("     50,000     99 |    100      10,000", lines[0]);
        assertEquals("     25,500     98 |    103         100", lines[1]);
        assertEquals("                   |    105      20,000", lines[2]);

        // Optional visual check
        System.err.println("---- Formatted Table ----");
        Arrays.stream(lines).forEach(System.err::println);
    }
}
