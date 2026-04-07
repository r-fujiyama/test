package dp;

import java.time.LocalDate;

class TransactionData {
  LocalDate transactionDate;
  int amount;

  TransactionData(LocalDate transactionDate, int amount) {
    this.transactionDate = transactionDate;
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "Transaction [transactionDate=" + transactionDate + ", amount=" + amount + "]";
  }

}
