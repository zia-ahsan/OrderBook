package com.bitvavo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Application {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        long startTime = System.nanoTime();
        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        OrderProcessor processor = new OrderProcessor();

        String line;
        while (true) {
            try {
                if (!((line = reader.readLine()) != null && !line.isEmpty())) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String[] parts = line.split(",");
            if (parts.length != 4) continue;

            String orderId = parts[0].trim();
            char side = parts[1].trim().charAt(0);
            int price = Integer.parseInt(parts[2].trim());
            int quantity = Integer.parseInt(parts[3].trim());

            Order order = new Order(orderId, side, price, quantity);
            processor.processOrder(order);
        }

        processor.printOrderBook();

        long endTime = System.nanoTime();
        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        System.err.println("Total time (ms): " + (endTime - startTime) / 1_000_000);
        System.err.println("Average time per order (micro seconds): " + (endTime - startTime) / 100_000);
        System.err.println("Approx. memory used (MB): " + (endMemory - startMemory) / (1024 * 1024));
    }
}