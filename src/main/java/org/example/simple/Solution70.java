package org.example.simple;

import com.alibaba.fastjson.JSON;

/**
 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
 *
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
 *
 *
 *
 * 示例 1：
 *
 * 输入：n = 2
 * 输出：2
 * 解释：有两种方法可以爬到楼顶。
 * 1. 1 阶 + 1 阶
 * 2. 2 阶
 * 示例 2：
 *
 * 输入：n = 3
 * 输出：3
 * 解释：有三种方法可以爬到楼顶。
 * 1. 1 阶 + 1 阶 + 1 阶
 * 2. 1 阶 + 2 阶
 * 3. 2 阶 + 1 阶
 *
 *
 * 提示：
 *
 * 1 <= n <= 45
 *
 * @author 苍韧
 */
public class Solution70 {
    public static Integer[] num = new Integer[45];

    public static void main(String[] args) {
        Solution70 solution = new Solution70();
        solution.climbStairs(45);
        System.out.println(JSON.toJSONString(num));
        //Assert.assertEquals(2, solution.climbStairs(2));
        //Assert.assertEquals(3, solution.climbStairs(3));
        //Assert.assertEquals(3, solution.climbStairs(45));
    }

    public int climbStairs(int n) {
        if (n == 1) {
            return 1;
        }
        if (n == 2) {
            return 2;
        }
        if (num[n - 1] != null) {
            return num[n - 1];
        }
        int result = climbStairs(n - 2) + climbStairs(n - 1);
        num[n - 1] = result;
        return result;
    }
}
