package com.bitvavo.verifier;

import com.bitvavo.verifier.util.OrderBookPrinter;

import java.util.*;

/**
 * Processes incoming orders by matching them against existing orders in the order book.
 */
public class OrderProcessor {
    private final OrderBook orderBook = new OrderBook();

    /**
     * Processes a new order by attempting to match it with existing orders.
     * If not fully matched, adds the remaining order to the appropriate order book.
     *
     * @param incomingOrder The incoming order to process.
     */
    public void processOrder(final Order incomingOrder) {
        final Order processedOrder = incomingOrder.side() == 'B'
                ? matchBuyOrder(incomingOrder)
                : matchSellOrder(incomingOrder);

        if (processedOrder.quantity() > 0) {
            if (processedOrder.side() == 'B') {
                addOrder(orderBook.getBuyOrders(), processedOrder);
            } else {
                addOrder(orderBook.getSellOrders(), processedOrder);
            }
        }
    }

    /**
     * Attempts to match a buy order with available sell orders.
     * Matches occur if the sell price is less than or equal to the buy price.
     * Fully or partially matched sell orders are removed or updated.
     *
     * @param buyOrder The incoming buy order.
     * @return The remaining portion of the buy order after matching.
     */
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

    /**
     * Attempts to match a sell order with available buy orders.
     * Matches occur if the buy price is greater than or equal to the sell price.
     * Fully or partially matched buy orders are removed or updated.
     *
     * @param sellOrder The incoming sell order.
     * @return The remaining portion of the sell order after matching.
     */
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

    /**
     * Adds an unmatched order to the appropriate order book queue.
     *
     * @param book  The price-level map (buy or sell side).
     * @param order The remaining unmatched order to add.
     */
    private void addOrder(final NavigableMap<Integer, Queue<Order>> book, final Order order) {
        book.computeIfAbsent(order.price(), k -> new LinkedList<>()).add(order);
    }

    public void printOrderBook() {
        NavigableMap<Integer, Queue<Order>> buy = orderBook.getBuyOrders();
        NavigableMap<Integer, Queue<Order>> sell = orderBook.getSellOrders();
        OrderBookPrinter.printOrderBook(buy, sell);
    }
}
