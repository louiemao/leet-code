package org.example.medium;

import org.junit.Assert;

/**
 * 某乐团的演出场地可视作 num * num 的二维矩阵 grid（左上角坐标为 [0,0])，每个位置站有一位成员。乐团共有 9 种乐器，乐器编号为 1~9，每位成员持有 1 个乐器。
 *
 * 为保证声乐混合效果，成员站位规则为：自 grid 左上角开始顺时针螺旋形向内循环以 1，2，...，9 循环重复排列。例如当 num = 5 时，站位如图所示
 *
 * image.png
 *
 * 请返回位于场地坐标 [Xpos,Ypos] 的成员所持乐器编号。
 *
 * 示例 1：
 *
 * 输入：num = 3, Xpos = 0, Ypos = 2
 *
 * 输出：3
 *
 * 解释：
 * 1 2 3
 * 8 9 4
 * 7 6 5
 *
 * 示例 2：
 *
 * 输入：num = 4, Xpos = 1, Ypos = 2
 *
 * 输出：5
 *
 * 解释：
 * 1 2 3 4
 * 3 4 5 5
 * 2 7 6 6
 * 1 9 8 7
 *
 * 1 2 3 4 5
 * 7 8 9 1 6
 * 6 6 7 2 7
 * 5 5 4 3 8
 * 4 3 2 1 9
 *
 * 提示：
 *
 * 1 <= num <= 10^9
 * 0 <= Xpos, Ypos < num
 */
public class SolutionLCP29 {

    public static void main(String[] args) {
        SolutionLCP29 solution = new SolutionLCP29();

        ////num=3
        //Assert.assertEquals(1, solution.calculateN(3, 0, 2));
        //Assert.assertEquals(1, solution.calculateN(3, 2, 2));
        //Assert.assertEquals(2, solution.calculateN(3, 1, 1));
        //
        //Assert.assertEquals(9, solution.first(1, 2));
        //
        //Assert.assertEquals(1, solution.getIndex(3, 0, 1, 1));
        //Assert.assertEquals(3, solution.getIndex(3, 1, 2, 1));
        //Assert.assertEquals(5, solution.getIndex(3, 2, 1, 1));
        //Assert.assertEquals(7, solution.getIndex(3, 1, 0, 1));
        //
        ////num=4
        //Assert.assertEquals(1, solution.calculateN(4, 0, 3));
        //Assert.assertEquals(1, solution.calculateN(4, 3, 3));
        //Assert.assertEquals(2, solution.calculateN(4, 1, 1));
        //Assert.assertEquals(2, solution.calculateN(4, 2, 2));
        //
        //Assert.assertEquals(4, solution.first(2, 2));
        //
        ////num=5
        //Assert.assertEquals(8, solution.first(3, 2));
        //Assert.assertEquals(7, solution.first(1, 3));
        //
        //Assert.assertEquals(0, solution.getIndex(4, 1, 1, 2));
        //Assert.assertEquals(1, solution.getIndex(4, 1, 2, 2));
        //Assert.assertEquals(2, solution.getIndex(4, 2, 2, 2));
        //Assert.assertEquals(3, solution.getIndex(4, 2, 1, 2));

        Assert.assertEquals(3, solution.orchestraLayout(3, 0, 2));
        Assert.assertEquals(5, solution.orchestraLayout(4, 1, 2));
        Assert.assertEquals(3, solution.orchestraLayout(2511, 1504, 2235));
        Assert.assertEquals(4, solution.orchestraLayout(7466, 7084, 2520));
        Assert.assertEquals(1, solution.orchestraLayout(449572, 209397, 306801));
    }

    public int orchestraLayout(int num, int xPos, int yPos) {
        //第几圈
        int n = calculateN(num, xPos, yPos);

        //当前圈第一个数的值
        int first = first(num, n);

        //位置在当前圈的第几个,0是第一个
        long index = getIndex(num, xPos, yPos, n);

        //返回结果
        int result = (int)((first + index) % 9);
        if (result == 0) {
            return 9;
        }
        return result;
    }

    private long getIndex(int num, int xPos, int yPos, int n) {
        //右边界
        int right = num - n;
        //左边界
        int left = n - 1;

        //在第一横行
        if (xPos == left) {
            return yPos - left;
        }

        //边长
        long size = right - left;

        //在第一竖行
        if (yPos == right) {
            return size + xPos - left;
        }

        //在第二横行
        if (xPos == right) {
            return size * 2 + right - yPos;
        }

        //在第二竖行
        return size * 3 + right - xPos;

    }

    private int first(int num, int n) {
        // if (n == 1) {
        //     return 1;
        // }

        // int result = (first(num + 2, n - 1) + (num + 1) * 4) % 9;
        // if (result == 0) {
        //     return 9;
        // }
        // return result;

        //外体积
        long out = (long)num * num;
        //内边长
        int inNum = num - 2 * (n - 1);
        //内体积
        long in = (long)inNum * inNum;
        return (int)((out - in) % 9 + 1);
    }

    private int calculateN(int num, int xPos, int yPos) {
        int min = Math.min(xPos, yPos);
        int max = Math.min(num - xPos - 1, num - yPos - 1);
        return Math.min(min, max) + 1;
    }

}
