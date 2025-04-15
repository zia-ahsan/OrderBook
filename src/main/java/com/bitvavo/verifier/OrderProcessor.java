package com.bitvavo.verifier;

import com.bitvavo.verifier.util.OrderBookPrinter;

import java.util.*;

public class OrderProcessor {
    private final OrderBook orderBook = new OrderBook();

    public void processOrder(Order order) {
        if (order.side() == 'B') {
            order = matchBuyOrder(order);
            if (order.quantity() > 0) addOrder(orderBook.getBuyOrders(), order);
        } else {
            order = matchSellOrder(order);
            if (order.quantity() > 0) addOrder(orderBook.getSellOrders(), order);
        }
    }

    private Order matchBuyOrder(Order buyOrder) {
        NavigableMap<Integer, Queue<Order>> sellOrders = orderBook.getSellOrders();
        Iterator<Map.Entry<Integer, Queue<Order>>> it = sellOrders.entrySet().iterator();

        while (it.hasNext() && buyOrder.quantity() > 0) {
            Map.Entry<Integer, Queue<Order>> entry = it.next();
            if (entry.getKey() > buyOrder.price()) break;

            Queue<Order> queue = entry.getValue();
            while (!queue.isEmpty() && buyOrder.quantity() > 0) {
                Order sellOrder = queue.peek();
                int tradedQty = Math.min(buyOrder.quantity(), sellOrder.quantity());

                System.out.printf("trade %s, %s, %d, %d\n", buyOrder.orderId(), sellOrder.orderId(), sellOrder.price(), tradedQty);

                buyOrder = buyOrder.withReducedQuantity(tradedQty);
                Order updatedSell = sellOrder.withReducedQuantity(tradedQty);

                if (updatedSell.quantity() == 0) {
                    queue.poll(); // fully matched
                } else {
                    queue.poll();
                    queue.add(updatedSell); // update at front
                }
            }
            if (queue.isEmpty()) it.remove();
        }
        return buyOrder;
    }

    private Order matchSellOrder(Order sellOrder) {
        NavigableMap<Integer, Queue<Order>> buyOrders = orderBook.getBuyOrders();
        Iterator<Map.Entry<Integer, Queue<Order>>> it = buyOrders.entrySet().iterator();

        while (it.hasNext() && sellOrder.quantity() > 0) {
            Map.Entry<Integer, Queue<Order>> entry = it.next();
            if (entry.getKey() < sellOrder.price()) break;

            Queue<Order> queue = entry.getValue();
            while (!queue.isEmpty() && sellOrder.quantity() > 0) {
                Order buyOrder = queue.peek();
                int tradedQty = Math.min(sellOrder.quantity(), buyOrder.quantity());

                System.out.printf("trade %s, %s, %d, %d%n", sellOrder.orderId(), buyOrder.orderId(), buyOrder.price(), tradedQty);

                sellOrder = sellOrder.withReducedQuantity(tradedQty);
                Order updatedBuy = buyOrder.withReducedQuantity(tradedQty);

                if (updatedBuy.quantity() == 0) {
                    queue.poll();
                } else {
                    queue.poll();
                    queue.add(updatedBuy);
                }
            }
            if (queue.isEmpty()) it.remove();
        }
        return sellOrder;
    }

    private void addOrder(NavigableMap<Integer, Queue<Order>> book, Order order) {
        book.computeIfAbsent(order.price(), k -> new LinkedList<>()).add(order);
    }

    public void printOrderBook() {
        NavigableMap<Integer, Queue<Order>> buy = orderBook.getBuyOrders();
        NavigableMap<Integer, Queue<Order>> sell = orderBook.getSellOrders();
        OrderBookPrinter.printOrderBook(buy, sell);
    }
}
