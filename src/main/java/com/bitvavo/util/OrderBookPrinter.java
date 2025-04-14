package com.bitvavo.util;

import com.bitvavo.Order;

import java.util.*;

public class OrderBookPrinter {

    public static void printOrderBook(NavigableMap<Integer, Queue<Order>> buyBook,
                                      NavigableMap<Integer, Queue<Order>> sellBook) {

        Iterator<Map.Entry<Integer, Queue<Order>>> buyIter = buyBook.entrySet().iterator();
        Iterator<Map.Entry<Integer, Queue<Order>>> sellIter = sellBook.entrySet().iterator();

        while (buyIter.hasNext() || sellIter.hasNext()) {
            Integer buyPrice = null, sellPrice = null;
            Integer buyQty = null, sellQty = null;

            if (buyIter.hasNext()) {
                var entry = buyIter.next();
                buyPrice = entry.getKey();
                buyQty = entry.getValue().stream().mapToInt(o -> o.quantity()).sum();
            }

            if (sellIter.hasNext()) {
                var entry = sellIter.next();
                sellPrice = entry.getKey();
                sellQty = entry.getValue().stream().mapToInt(o -> o.quantity()).sum();
            }

            printLine(buyPrice, buyQty, sellPrice, sellQty);
        }
    }

    private static String formatQty(int qty) {
        return String.format(Locale.US, "%,11d", qty); // 11 chars, comma-separated, space-padded
    }

    private static String formatPrice(int price) {
        return String.format(Locale.US, "%,6d", price); // 6 chars, comma-separated, space-padded
    }

    private static void printLine(Integer buyPrice, Integer buyQty,
                                  Integer sellPrice, Integer sellQty) {

        String left = (buyQty != null && buyPrice != null)
                ? String.format("%s %s", formatQty(buyQty), formatPrice(buyPrice))
                : String.format("%18s", "");

        String right = (sellQty != null && sellPrice != null)
                ? String.format(" %s %s", formatPrice(sellPrice), formatQty(sellQty))
                : "";

        System.out.printf("%s |%s\n", left, right);
    }
}
