package org.example.medium;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

/**
 * 166. 分数到小数
 *
 * 给定两个整数，分别表示分数的分子 numerator 和分母 denominator，以 字符串形式返回小数 。
 *
 * 如果小数部分为循环小数，则将循环的部分括在括号内。
 *
 * 如果存在多个答案，只需返回 任意一个 。
 *
 * 对于所有给定的输入，保证 答案字符串的长度小于 10^4 。
 *
 *
 * 示例 1：
 * 输入：numerator = 1, denominator = 2
 * 输出："0.5"
 *
 * 示例 2：
 * 输入：numerator = 2, denominator = 1
 * 输出："2"
 *
 * 示例 3：
 * 输入：numerator = 4, denominator = 333
 * 输出："0.(012)"
 *
 *
 * 提示：
 *
 * -2^31 <= numerator, denominator <= 2^31 - 1
 * denominator != 0
 */
public class Solution166 {
    public static void main(String[] args) {
        Solution166 solution = new Solution166();

        Assert.assertEquals("0.5", solution.fractionToDecimal(1, 2));
        Assert.assertEquals("2", solution.fractionToDecimal(2, 1));
        Assert.assertEquals("0.(012)", solution.fractionToDecimal(4, 333));
        Assert.assertEquals("0.(003)", solution.fractionToDecimal(1, 333));
        Assert.assertEquals("-6.25", solution.fractionToDecimal(-50, 8));
        Assert.assertEquals("-0.58(3)", solution.fractionToDecimal(7, -12));
        Assert.assertEquals("-0.58(3)", solution.fractionToDecimal(7, -12));
        Assert.assertEquals("0.0000000004656612873077392578125", solution.fractionToDecimal(-1, -2147483648));
        Assert.assertEquals("2147483648", solution.fractionToDecimal(-2147483648, -1));

        System.out.println("执行完成");
    }

    public String fractionToDecimal(int numerator, int denominator) {
        //整数部分
        long a = (long)numerator / (long)denominator;
        //余数
        long b = Math.abs(numerator % denominator);
        if (b == 0) {
            return String.valueOf(a);
        }

        StringBuilder result = new StringBuilder();
        //正负判断
        if ((numerator > 0 && denominator < 0) || (numerator < 0 && denominator > 0)) {
            result.append('-');
        }
        result.append(Math.abs(a)).append('.');
        int length = result.length();

        //计算小数部分
        long c = Math.abs((long)denominator);
        //被除数，用于标记循环
        Map<Long, Integer> map = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            if (map.containsKey(b)) {
                int index = map.get(b);
                result.insert(length + index, '(');
                result.append(')');
                return result.toString();
            }
            map.put(b, i);
            b = b * 10;
            result.append(b / c);
            b = b % c;
            if (b == 0) {
                return result.toString();
            }
        }
        return result.toString();
    }

    /**
     * 官方题解
     *
     * @param numerator
     * @param denominator
     * @return
     */
    public String fractionToDecimalV2(int numerator, int denominator) {
        long numeratorLong = (long)numerator;
        long denominatorLong = (long)denominator;
        if (numeratorLong % denominatorLong == 0) {
            return String.valueOf(numeratorLong / denominatorLong);
        }

        StringBuffer sb = new StringBuffer();
        if (numeratorLong < 0 ^ denominatorLong < 0) {
            sb.append('-');
        }

        // 整数部分
        numeratorLong = Math.abs(numeratorLong);
        denominatorLong = Math.abs(denominatorLong);
        long integerPart = numeratorLong / denominatorLong;
        sb.append(integerPart);
        sb.append('.');

        // 小数部分
        StringBuffer fractionPart = new StringBuffer();
        Map<Long, Integer> remainderIndexMap = new HashMap<Long, Integer>();
        long remainder = numeratorLong % denominatorLong;
        int index = 0;
        while (remainder != 0 && !remainderIndexMap.containsKey(remainder)) {
            remainderIndexMap.put(remainder, index);
            remainder *= 10;
            fractionPart.append(remainder / denominatorLong);
            remainder %= denominatorLong;
            index++;
        }
        if (remainder != 0) { // 有循环节
            int insertIndex = remainderIndexMap.get(remainder);
            fractionPart.insert(insertIndex, '(');
            fractionPart.append(')');
        }
        sb.append(fractionPart.toString());

        return sb.toString();
    }

}
