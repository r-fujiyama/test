package dp;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Searcher {

  /**
   * 動的計画法を使用して部分集合和問題を解く 時間計算量: O(n × targetAmount), 空間計算量: O(n × targetAmount)
   *
   * @param transactions 取引データのリスト
   * @param targetAmount 目標金額
   * @return 目標金額に合致する取引のリスト、見つからない場合はnull
   */
  public static List<TransactionData> findSubsetSumWithDP(List<TransactionData> transactions,
      int targetAmount) {
    int n = transactions.size();

    // DPテーブル: dp[i][j] = i番目までの要素で金額jを作れるか
    boolean[][] dp = new boolean[n + 1][targetAmount + 1];
    // 選択テーブル: selected[i][j] = i番目の要素を選んだか
    boolean[][] selected = new boolean[n + 1][targetAmount + 1];

    // ベースケース: 金額0は常に作成可能（何も選ばない）
    dp[0][0] = true;

    // DPテーブルを埋める
    for (int i = 1; i <= n; i++) {
      int amount = transactions.get(i - 1).amount;

      for (int sum = 0; sum <= targetAmount; sum++) {
        // 場合1: この要素を選ばない場合、前の行の値をコピー
        if (dp[i - 1][sum]) {
          dp[i][sum] = true;
        }

        // 場合2: この要素を選ぶ場合、(sum - amount)から遷移可能か確認
        if (sum >= amount && dp[i - 1][sum - amount]) {
          dp[i][sum] = true;
          selected[i][sum] = true;
        }
      }
    }

    // 目標金額に到達できなかった場合
    if (!dp[n][targetAmount]) {
      return null;
    }

    // 復元処理: どの要素を選んだかを遡って確認
    List<TransactionData> result = new ArrayList<>();
    int sum = targetAmount;

    // 逆向きに復元：最後の要素から始まる最初の要素まで遡る
    for (int i = n; i > 0; i--) {
      if (selected[i][sum]) {
        TransactionData tx = transactions.get(i - 1);
        result.add(tx);
        sum -= tx.amount;
      }
    }

    return result;
  }

  /**
   * 全件探索（ブルートフォース）を使用して部分集合和問題を解く すべての部分集合を試す最もシンプルなアプローチ 時間計算量: O(2^n), 空間計算量: O(n)
   *
   * @param transactions 取引データのリスト
   * @param targetAmount 目標金額
   * @return 目標金額に合致する取引のリスト、見つからない場合はnull
   */
  public static List<TransactionData> findSubsetSumWithBruteForce(
      List<TransactionData> transactions, int targetAmount) {
    List<TransactionData> current = new ArrayList<>();
    return findSubsetSumRecursive(transactions, targetAmount, 0, 0, current);
  }

  /**
   * 再帰的に部分集合を探索 各要素に対して「選ぶ」か「選ばない」の2つの選択肢を探索
   *
   * @param transactions 取引データのリスト
   * @param targetAmount 目標金額
   * @param index 現在探索中のインデックス
   * @param currentSum 現在までの合計金額
   * @param current 現在までに選んだ取引のリスト
   * @return 条件を満たす取引リスト、見つからない場合はnull
   */
  private static List<TransactionData> findSubsetSumRecursive(List<TransactionData> transactions,
      int targetAmount, int index, int currentSum, List<TransactionData> current) {
    // 終了条件1: 目標金額に到達した場合
    if (currentSum == targetAmount) {
      return new ArrayList<>(current);
    }

    // 終了条件2: すべての要素を確認した場合
    if (index >= transactions.size()) {
      return null;
    }

    TransactionData tx = transactions.get(index);

    // 選択肢1: 現在の要素を選ぶ場合
    current.add(tx);
    List<TransactionData> result = findSubsetSumRecursive(transactions, targetAmount, index + 1,
        currentSum + tx.amount, current);
    if (result != null) {
      return result;
    }
    // 要素を削除して別の選択肢を試す（バックトラック）
    current.remove(current.size() - 1);

    // 選択肢2: 現在の要素を選ばない場合
    return findSubsetSumRecursive(transactions, targetAmount, index + 1, currentSum, current);
  }

  /**
   * BitSetを使用して部分集合和問題を解く（メモリ効率的） boolean[][]の代わりにBitSetを使用することでメモリ使用量を大幅に削減 時間計算量: O(n ×
   * targetAmount), 空間計算量: O(n × targetAmount / 8)
   *
   * @param transactions 取引データのリスト
   * @param targetAmount 目標金額
   * @return 目標金額に合致する取引のリスト、見つからない場合はnull
   */
  public static List<TransactionData> findSubsetSumWithBitSet(List<TransactionData> transactions,
      int targetAmount) {
    int n = transactions.size();

    // BitSetを使用したDPテーブル: 各行が一つのBitSetで、i番目までの要素で金額jを作れるか
    BitSet[] dp = new BitSet[n + 1];
    // 選択テーブル: ビットが立っている＝その要素を選んだ
    BitSet[] selected = new BitSet[n + 1];

    for (int i = 0; i <= n; i++) {
      dp[i] = new BitSet(targetAmount + 1);
      selected[i] = new BitSet(targetAmount + 1);
    }

    // ベースケース: 金額0は常に作成可能（何も選ばない）
    dp[0].set(0);

    // DPテーブルを埋める
    for (int i = 1; i <= n; i++) {
      int amount = transactions.get(i - 1).amount;

      // 前の行をコピー（選ばない場合）
      dp[i].or(dp[i - 1]);

      // 選ぶ場合の処理: dp[i-1]のすべて立っているビットに対して、
      // amount分だけ左にシフトした位置に立てる
      for (int j = dp[i - 1].nextSetBit(0); j >= 0 && j <= targetAmount; j =
          dp[i - 1].nextSetBit(j + 1)) {
        if (j + amount <= targetAmount) {
          dp[i].set(j + amount);
          selected[i].set(j + amount);
        }
      }
    }

    // 目標金額に到達できなかった場合
    if (!dp[n].get(targetAmount)) {
      return null;
    }

    // 復元処理: どの要素を選んだかを遡って確認
    List<TransactionData> result = new ArrayList<>();
    int sum = targetAmount;

    // 逆向きに復元
    for (int i = n; i > 0; i--) {
      if (selected[i].get(sum)) {
        TransactionData tx = transactions.get(i - 1);
        result.add(tx);
        sum -= tx.amount;
      }
    }

    return result;
  }
}
