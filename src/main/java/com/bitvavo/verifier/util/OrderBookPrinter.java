package com.bitvavo.verifier.util;

import com.bitvavo.verifier.Order;

import java.util.*;

public class OrderBookPrinter {

    public static void printOrderBook(NavigableMap<Integer, Queue<Order>> buyBook,
                                      NavigableMap<Integer, Queue<Order>> sellBook) {

        // Flatten buy and sell sides into ordered lists
        List<String[]> buyLines = new ArrayList<>();
        List<String[]> sellLines = new ArrayList<>();

        for (var entry : buyBook.entrySet()) {
            for (Order order : entry.getValue()) {
                buyLines.add(new String[] {
                        formatQty(order.quantity()),
                        formatPrice(order.price())
                });
            }
        }

        for (var entry : sellBook.entrySet()) {
            for (Order order : entry.getValue()) {
                sellLines.add(new String[] {
                        formatPrice(order.price()),
                        formatQty(order.quantity())
                });
            }
        }

        // Align lines
        int maxLines = Math.max(buyLines.size(), sellLines.size());
        for (int i = 0; i < maxLines; i++) {
            String[] buy = i < buyLines.size() ? buyLines.get(i) : new String[]{"", ""};
            String[] sell = i < sellLines.size() ? sellLines.get(i) : new String[]{"", ""};

            System.out.printf("%11s %6s | %6s %11s\n",
                    buy[0], buy[1],
                    sell[0], sell[1]);
        }
    }


    private static String formatQty(int qty) {
        return String.format(Locale.US, "%,11d", qty); // 11 chars, comma-separated, space-padded
    }

    private static String formatPrice(int price) {
        return String.format(Locale.US, "%,6d", price); // 6 chars, comma-separated, space-padded
    }
}
