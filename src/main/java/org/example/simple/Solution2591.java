package org.example.simple;

import org.junit.Assert;

/**
 * 2591. 将钱分给最多的儿童
 *
 * 给你一个整数 money ，表示你总共有的钱数（单位为美元）和另一个整数 children ，表示你要将钱分配给多少个儿童。
 *
 * 你需要按照如下规则分配：
 *
 * 所有的钱都必须被分配。
 * 每个儿童至少获得 1 美元。
 * 没有人获得 4 美元。
 * 请你按照上述规则分配金钱，并返回 最多 有多少个儿童获得 恰好 8 美元。如果没有任何分配方案，返回 -1 。
 *
 *
 *
 * 示例 1：
 *
 * 输入：money = 20, children = 3
 * 输出：1
 * 解释：
 * 最多获得 8 美元的儿童数为 1 。一种分配方案为：
 * - 给第一个儿童分配 8 美元。
 * - 给第二个儿童分配 9 美元。
 * - 给第三个儿童分配 3 美元。
 * 没有分配方案能让获得 8 美元的儿童数超过 1 。
 *
 *
 * 示例 2：
 *
 * 输入：money = 16, children = 2
 * 输出：2
 * 解释：每个儿童都可以获得 8 美元。
 *
 *
 * 提示：
 *
 * 1 <= money <= 200
 * 2 <= children <= 30
 */
public class Solution2591 {
    public static void main(String[] args) {
        Solution2591 solution = new Solution2591();

        Assert.assertEquals(1, solution.distMoney(20, 3));
        Assert.assertEquals(2, solution.distMoney(16, 2));
    }

    public int distMoney(int money, int children) {
        //每人分1美元后，还剩余部分
        int left = money - children;
        if (left < 0) {
            return -1;
        }
        if (left < 7) {
            return 0;
        }

        //每人分7元，能分多少人
        int num = left / 7;
        if (num <= children - 2) {
            return num;
        }
        if (num > children) {
            return children - 1;
        }

        //每人分7后，还剩余多少
        int num2 = left % 7;
        if (num == children) {
            if (num2 == 0) {
                return children;
            } else {
                return children - 1;
            }
        }

        if (num == children - 1) {
            if (num2 == 3) {
                return children - 2;
            } else {
                return children - 1;
            }
        }

        return 0;
    }
}
