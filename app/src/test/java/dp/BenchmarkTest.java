package dp;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Subset Sum Algorithm Benchmark Test")
public class BenchmarkTest {

  @Test
  @Order(1)
  void dp10000() {
    int[] sizes = {5, 10, 15, 20, 25, 30};
    int targetAmount = 10000;

    System.out.println("\n=== DP Algorithm Test ===");
    System.out.println("\n--- Target Amount: " + targetAmount + " ---");
    System.out.printf("%-5s | %-15s | %-15s | %-15s%n", "n", "Execution(ms)", "Memory(MB)",
        "Count");
    System.out.println("------+-----------------+-----------------+------------------");

    for (int n : sizes) {
      BenchmarkResult result = runBenchmark(n, targetAmount);
      System.out.printf("%-5d | %-15.3f | %-15.2f | %-15d%n", n, result.executionTime,
          result.memoryUsed, result.resultCount);

      assertNotNull(result);
    }
  }

  @Test
  @Order(2)
  void dp100000() {
    int[] sizes = {5, 10, 15, 20, 25, 30};
    int targetAmount = 100000;

    System.out.println("\n--- Target Amount: " + targetAmount + " ---");
    System.out.printf("%-5s | %-15s | %-15s | %-15s%n", "n", "Execution(ms)", "Memory(MB)",
        "Count");
    System.out.println("------+-----------------+-----------------+------------------");

    for (int n : sizes) {
      BenchmarkResult result = runBenchmark(n, targetAmount);
      System.out.printf("%-5d | %-15.3f | %-15.2f | %-15d%n", n, result.executionTime,
          result.memoryUsed, result.resultCount);

      assertNotNull(result);
    }
  }

  @Test
  @Order(3)
  void dp1000000() {
    int[] sizes = {5, 10, 15, 20, 25, 30};
    int targetAmount = 1000000;

    System.out.println("\n--- Target Amount: " + targetAmount + " ---");
    System.out.printf("%-5s | %-15s | %-15s | %-15s%n", "n", "Execution(ms)", "Memory(MB)",
        "Count");
    System.out.println("------+-----------------+-----------------+------------------");

    for (int n : sizes) {
      BenchmarkResult result = runBenchmark(n, targetAmount);
      System.out.printf("%-5d | %-15.3f | %-15.2f | %-15d%n", n, result.executionTime,
          result.memoryUsed, result.resultCount);

      assertNotNull(result);
    }
  }

  @Test
  @Order(4)
  void dp2000000() {
    int[] sizes = {5, 10, 15, 20, 25, 30};
    int targetAmount = 2000000;

    System.out.println("\n--- Target Amount: " + targetAmount + " ---");
    System.out.printf("%-5s | %-15s | %-15s | %-15s%n", "n", "Execution(ms)", "Memory(MB)",
        "Count");
    System.out.println("------+-----------------+-----------------+------------------");

    for (int n : sizes) {
      BenchmarkResult result = runBenchmark(n, targetAmount);
      System.out.printf("%-5d | %-15.3f | %-15.2f | %-15d%n", n, result.executionTime,
          result.memoryUsed, result.resultCount);

      assertNotNull(result);
    }
  }

  static class BenchmarkResult {
    double executionTime; // ミリ秒
    double memoryUsed; // MB
    int resultCount; // 結果の件数

    BenchmarkResult(double executionTime, double memoryUsed, int resultCount) {
      this.executionTime = executionTime;
      this.memoryUsed = memoryUsed;
      this.resultCount = resultCount;
    }
  }

  static BenchmarkResult runBenchmark(int n, int targetAmount) {
    // 取引データを生成
    List<@NonNull TransactionData> transactions = generateTransactions(n);

    // ガベージコレクションを実行
    System.gc();
    long memoryBeforeL = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

    // 実行時間を計測
    long startTime = System.nanoTime();
    List<TransactionData> result = Searcher.findSubsetSumWithDP(transactions, targetAmount);
    long endTime = System.nanoTime();

    long memoryAfterL = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    double memoryUsed = (memoryAfterL - memoryBeforeL) / (1024.0 * 1024.0);

    double executionTime = (endTime - startTime) / 1_000_000.0; // ナノ秒からミリ秒へ変換
    int resultCount = (result != null) ? result.size() : 0;

    return new BenchmarkResult(executionTime, Math.max(0, memoryUsed), resultCount);
  }

  /**
   * n件の取引データを生成 ターゲット金額に到達しやすいよう、適切な金額を生成します
   */
  static List<@NonNull TransactionData> generateTransactions(int n) {
    List<@NonNull TransactionData> transactions = new ArrayList<>();
    LocalDate baseDate = LocalDate.of(2026, 4, 1);

    // ターゲット金額に到達しやすいように調整
    // nが大きいほど、個別金額は小さくなるように設定
    int baseAmount = Math.max(100, 50000 / n);

    for (int i = 0; i < n; i++) {
      // 各取引は多少のバリエーションを持つ金額
      int amount = baseAmount + (i % 10) * 100;
      transactions.add(new TransactionData(baseDate.plusDays(i), amount));
    }

    return transactions;
  }

  @Test
  @Order(5)
  void bruteForce() {
    int[] sizes = {5, 10, 15, 20, 25, 30};
    int targetAmount = 10000;

    System.out.println("\n=== Brute Force Algorithm Test ===");
    System.out.println("\n--- Target Amount: " + targetAmount + " ---");
    System.out.printf("%-5s | %-15s | %-15s | %-15s%n", "n", "Execution(ms)", "Memory(MB)",
        "Count");
    System.out.println("------+-----------------+-----------------+------------------");

    for (int n : sizes) {
      BenchmarkResultBruteForce result = runBenchmarkBruteForce(n, targetAmount);
      System.out.printf("%-5d | %-15.3f | %-15.2f | %-15d%n", n, result.executionTime,
          result.memoryUsed, result.resultCount);

      assertNotNull(result);
    }
  }

  static class BenchmarkResultBruteForce {
    double executionTime; // ミリ秒
    double memoryUsed; // MB
    int resultCount; // 結果の件数

    BenchmarkResultBruteForce(double executionTime, double memoryUsed, int resultCount) {
      this.executionTime = executionTime;
      this.memoryUsed = memoryUsed;
      this.resultCount = resultCount;
    }
  }

  static BenchmarkResultBruteForce runBenchmarkBruteForce(int n, int targetAmount) {
    // 取引データを生成
    List<@NonNull TransactionData> transactions = generateTransactions(n);

    // ガベージコレクションを実行
    System.gc();
    long memoryBeforeL = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

    // 実行時間を計測
    long startTime = System.nanoTime();
    List<TransactionData> result = Searcher.findSubsetSumWithBruteForce(transactions, targetAmount);
    long endTime = System.nanoTime();

    long memoryAfterL = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    double memoryUsed = (memoryAfterL - memoryBeforeL) / (1024.0 * 1024.0);

    double executionTime = (endTime - startTime) / 1_000_000.0; // ナノ秒からミリ秒へ変換
    int resultCount = (result != null) ? result.size() : 0;

    return new BenchmarkResultBruteForce(executionTime, Math.max(0, memoryUsed), resultCount);
  }
}
