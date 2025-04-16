package com.bitvavo.verifier;

import java.util.Collections;
import java.util.NavigableMap;
import java.util.Queue;
import java.util.TreeMap;

/**
 * Maintains separate order books for buy and sell orders.
 */
public class OrderBook {
    private final NavigableMap<Integer, Queue<Order>> buyOrders = new TreeMap<>(Collections.reverseOrder());
    private final NavigableMap<Integer, Queue<Order>> sellOrders = new TreeMap<>();

    /**
     * Retrieves the buy orders map.
     *
     * @return NavigableMap of buy orders.
     */
    public NavigableMap<Integer, Queue<Order>> getBuyOrders() {
        return buyOrders;
    }

    /**
     * Retrieves the sell orders map.
     *
     * @return NavigableMap of sell orders.
     */
    public NavigableMap<Integer, Queue<Order>> getSellOrders() {
        return sellOrders;
    }
}