package com.bitvavo;

/**
 * Immutable Order record.
 * - orderId, side, price are final
 * - quantity is mutable and updated directly as it's reduced during trades
 */
public record Order(String orderId, char side, int price, int quantity) {

    /**
     * Creates a new Order with updated quantity.
     * Used to simulate quantity reduction during matching.
     */
    public Order withReducedQuantity(int amount) {
        return new Order(this.orderId, this.side, this.price, this.quantity - amount);
    }
}
