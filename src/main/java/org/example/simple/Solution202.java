package org.example.simple;

import java.util.HashSet;
import java.util.Set;

/**
 * 编写一个算法来判断一个数 n 是不是快乐数。
 *
 * 「快乐数」定义为：
 *
 * 对于一个正整数，每一次将该数替换为它每个位置上的数字的平方和。
 * 然后重复这个过程直到这个数变为 1，也可能是 无限循环 但始终变不到 1。
 * 如果这个过程 结果为1，那么这个数就是快乐数。
 * 如果 n 是 快乐数 就返回 true ；不是，则返回 false 。
 *
 *
 * 示例 1：
 * 输入：n = 19
 * 输出：true
 * 解释：
 * 1² + 9² = 82
 * 8² + 2² = 68
 * 6² + 8² = 100
 * 1² + 0² + 0² = 1
 *
 * 示例 2：
 * 输入：n = 2
 * 输出：false
 *
 *
 * 提示：
 *
 * 1 <= n <= 2^31 - 1
 *
 * @author 苍韧
 */
public class Solution202 {
    public static void main(String[] args) {
        System.out.println(isHappy(2));
    }

    public static boolean isHappy(int n) {
        Set<Integer> set = new HashSet<>();
        while (n != 1 && !set.contains(n)) {
            set.add(n);
            n = getNext(n);
        }
        System.out.println(n);
        System.out.println(set);
        return n == 1;
    }

    private static int getNext(int n) {
        int result = 0;
        while (n > 0) {
            //取最后一位
            int m = n % 10;
            n = n / 10;
            result += m * m;
        }
        return result;
    }
}
