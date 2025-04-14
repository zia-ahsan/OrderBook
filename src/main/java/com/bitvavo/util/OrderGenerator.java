package com.bitvavo.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class OrderGenerator {

    public static void main(String[] args) throws IOException {
        int numOrders = 100_000; // default
        String outputPath = "orders_100k.txt"; // default

        // Allow custom args
        if (args.length >= 1) {
            numOrders = Integer.parseInt(args[0]);
        }
        if (args.length >= 2) {
            outputPath = args[1];
        }

        generateOrders(numOrders, outputPath);
        System.out.println("Generated " + numOrders + " orders at: " + outputPath);
    }

    public static void generateOrders(int numOrders, String filePath) throws IOException {
        Random rand = new Random();
        int startId = 10000;
        int minPrice = 90;
        int maxPrice = 110;
        int minQty = 100;
        int maxQty = 10000;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (int i = 0; i < numOrders; i++) {
                int orderId = startId + i;
                String side = rand.nextBoolean() ? "B" : "S";
                int price = rand.nextInt(maxPrice - minPrice + 1) + minPrice;
                int quantity = rand.nextInt(maxQty - minQty + 1) + minQty;

                writer.write(String.format("%d, %s, %d, %d%n", orderId, side, price, quantity));
            }
        }
    }
}
