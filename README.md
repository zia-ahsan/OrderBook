# Bitvavo Order Book – Low Latency Matching Engine

A simplified, high-performance order matching engine implementation designed for the Bitvavo backend low-latency assignment.

---

## 📌 Overview

This Java application reads a list of orders from standard input (or a file), simulates a central limit order book, performs continuous matching based on price-time priority, prints trade executions, and finally outputs the state of the order book.

---

## ✅ Features

- Supports **limit orders** for both **buy (B)** and **sell (S)** sides
- Matching follows **price-time priority** rules
- **Continuous matching**: trades happen immediately upon order arrival
- Final order book output in strict tabular format (space-padded, comma-separated)
- Capable of handling **100,000+ orders** efficiently
- **Performance metrics**: total time, average time per order, and memory usage
- **Code coverage reporting** via JaCoCo with 80% enforcement

---

## 🧱 Design Decisions

### 🔄 Data Structures

- `TreeMap<Integer, Queue<Order>>` is used to store:
  - **Buy orders** in descending price order
  - **Sell orders** in ascending price order
- This allows:
  - Efficient retrieval of best matching price
  - FIFO order preservation within each price level

### 🔁 Matching Logic

- Orders are processed sequentially
- Each incoming order is matched against the best available opposite-side order(s)
- Partially filled orders are reinserted into the book
- Fully filled orders are discarded

---

## ⚙️ Concurrency and Thread Safety

### ❓ Is `processOrder(Order)` thread-safe?

Yes – **by design**:
- In this implementation, orders are processed sequentially from input (stdin or file)
- There is **no concurrency** involved in invoking `processOrder(...)`
- Therefore, there is **no need for synchronization** or thread-safe data structures

### 🧠 Real-world Insight

In production exchanges:
- Each **order book (per symbol)** is processed by a **dedicated thread**
- Orders are fed via **lock-free queues** (e.g., LMAX Disruptor)
- This design eliminates shared state, allowing lock-free matching logic

---

## 🚀 How to Run

### 🖥 Requirements

- Java 17+
- Maven 3.8+

### 🏗 Build

```bash
mvn clean package
```

### ▶️ Run with input redirection

```bash
java -cp target/OrderBook-1.0-SNAPSHOT.jar com.bitvavo.Main < src/test/resources/test-cases/input/test1.txt
```

---

## 📊 Performance Metrics

After processing, the application prints to `stderr`:

- **Total time (ms)**
- **Average time per order (microseconds)**
- **Approximate memory used (MB)**

---

## 🧪 Testing

- Integration tests provided in `IntegrationTest.java`
- Sample test cases under `src/test/resources/test-cases/`
- Run from IntelliJ or via Maven:

```bash
mvn test
```

---

## ✅ Code Coverage with JaCoCo

Code coverage is enforced using the JaCoCo Maven plugin.

### Configuration:
- Minimum line coverage: **80%**
- Enforced during `verify` phase
- HTML report available after test run

### Run full test and coverage report:

```bash
mvn clean test verify
```

### View the coverage report:

Open the file:

```
target/site/jacoco/index.html
```

You can also set build to fail if coverage drops below 80%.

---

## 🛠 Utilities

### Generate Test Orders

```bash
java -cp target/OrderBook-1.0-SNAPSHOT.jar com.bitvavo.OrderGenerator 100000 orders_100k.txt
```

---

## 📁 File Structure

```text
src/
  main/
    java/com/bitvavo/
      Main.java
      Order.java
      OrderBook.java
      OrderProcessor.java
    java/com/bitvavo/util/
      OrderBookPrinter.java
      OrderGenerator.java
  test/
    java/com/bitvavo/
      IntegrationTest.java
      AbstractIntegrationTest.java
    java/com/bitvavo/util/
      OrderBookPrinterTest.java
    resources/test-cases/
      input/
      output/
```

---

## 👨‍💻 Author

Zia Ahsan – [GitHub](https://github.com/zia-ahsan)

---

## 🧠 Notes

- Do **not** modify `IntegrationTest.java` or `AbstractIntegrationTest.java`
- Output must match exactly, including whitespace and commas
- This project is intentionally single-threaded for determinism and testability
