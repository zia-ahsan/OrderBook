package com.bitvavo.verifier;

/**
 * Represents an individual order in the order book.
 *
 * @param orderId  Unique identifier for the order.
 * @param side     Side of the order: 'B' for Buy, 'S' for Sell.
 * @param price    Price per unit for the order.
 * @param quantity Quantity of units in the order.
 */
public record Order(String orderId, char side, int price, int quantity) {

    /**
     * Creates a new Order with updated quantity.
     * Used to simulate quantity reduction during matching.
     */
    public Order withReducedQuantity(final int amount) {
        return new Order(this.orderId, this.side, this.price, this.quantity - amount);
    }
}
