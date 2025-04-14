package com.bitvavo;

import java.util.*;

public class OrderBook {
    private final NavigableMap<Integer, Queue<Order>> buyOrders = new TreeMap<>(Collections.reverseOrder());
    private final NavigableMap<Integer, Queue<Order>> sellOrders = new TreeMap<>();

    public NavigableMap<Integer, Queue<Order>> getBuyOrders() {
        return buyOrders;
    }

    public NavigableMap<Integer, Queue<Order>> getSellOrders() {
        return sellOrders;
    }
}