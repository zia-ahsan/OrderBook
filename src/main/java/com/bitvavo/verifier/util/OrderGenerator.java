package com.bitvavo.verifier.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Generates a specified number of random orders and writes them to a file.
 */
public class OrderGenerator {

    /**
     * Main method to generate orders.
     *
     * @param args Command-line arguments: [number of orders] [output file path]
     * @throws IOException If an I/O error occurs.
     */
    public static void main(String[] args) throws IOException {
        int numOrders = 100_000; // default
        String outputPath = String.format("orders%06d.txt", numOrders); // default formatted filename

        if (args.length >= 1) {
            numOrders = Integer.parseInt(args[0]);
            outputPath = String.format("orders%06d.txt", numOrders);
        }
        if (args.length >= 2) {
            outputPath = args[1]; // override filename if provided
        }

        generateOrders(numOrders, outputPath);
        System.out.println("Generated " + numOrders + " orders at: " + outputPath);
    }

    /**
     * Generates random orders and writes them to the specified file.
     *
     * @param numOrders Number of orders to generate.
     * @param filePath  Path to the output file.
     * @throws IOException If an I/O error occurs.
     */
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

                writer.write(String.format("%d, %s, %d, %d\n", orderId, side, price, quantity));
            }
        }
    }
}
