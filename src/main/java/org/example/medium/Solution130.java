package org.example.medium;

import org.junit.Assert;

/**
 * 130. 被围绕的区域
 *
 * 给你一个 m x n 的矩阵 board ，由若干字符 'X' 和 'O' ，找到所有被 'X' 围绕的区域，并将这些区域里所有的 'O' 用 'X' 填充。
 *
 *
 * 示例 1：
 * "X","X","X","X"           "X","X","X","X"
 * "X","O","O","X"    -->    "X","X","X","X"
 * "X","X","O","X"           "X","X","X","X"
 * "X","O","X","X"           "X","O","X","X"
 *
 *
 * 输入：board = [["X","X","X","X"],["X","O","O","X"],["X","X","O","X"],["X","O","X","X"]]
 * 输出：[["X","X","X","X"],["X","X","X","X"],["X","X","X","X"],["X","O","X","X"]]
 * 解释：被围绕的区间不会存在于边界上，换句话说，任何边界上的 'O' 都不会被填充为 'X'。 任何不在边界上，或不与边界上的 'O' 相连的 'O' 最终都会被填充为 'X'。如果两个元素在水平或垂直方向相邻，则称它们是“相连”的。
 *
 * 示例 2：
 *
 * 输入：board = [["X"]]
 * 输出：[["X"]]
 *
 *
 * 提示：
 *
 * m == board.length
 * n == board[i].length
 * 1 <= m, n <= 200
 * board[i][j] 为 'X' 或 'O'
 */
public class Solution130 {
    public static void main(String[] args) {
        Solution130 solution = new Solution130();

        char[][] board = {{'X', 'X', 'X', 'X'}, {'X', 'O', 'O', 'X'}, {'X', 'X', 'O', 'X'}, {'X', 'O', 'X', 'X'}};
        char[][] board2 = {{'X', 'X', 'X', 'X'}, {'X', 'X', 'X', 'X'}, {'X', 'X', 'X', 'X'}, {'X', 'O', 'X', 'X'}};

        solution.solve(board);
        Assert.assertEquals(board2, board);
        System.out.println("执行完成");
    }

    // 2 ms
    public void solve(char[][] board) {
        int m = board.length;
        int n = board[0].length;

        //从最外围开始进行深度优先遍历
        for (int i = 0; i < n; i++) {
            dfs(board, 0, i);
            dfs(board, m - 1, i);
        }
        for (int i = 0; i < m; i++) {
            dfs(board, i, 0);
            dfs(board, i, n - 1);
        }

        //'O'换成'X'，'A'换成'O'
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (board[j][i] == 'O') {
                    board[j][i] = 'X';
                } else if (board[j][i] == 'A') {
                    board[j][i] = 'O';
                }
            }
        }
    }

    public void dfs(char[][] board, int x, int y) {
        if (x < 0 || x >= board.length || y < 0 || y >= board[0].length) {
            return;
        }
        if (board[x][y] == 'A' || board[x][y] == 'X') {
            return;
        }
        board[x][y] = 'A';
        dfs(board, x - 1, y);
        dfs(board, x + 1, y);
        dfs(board, x, y - 1);
        dfs(board, x, y + 1);
    }

}
