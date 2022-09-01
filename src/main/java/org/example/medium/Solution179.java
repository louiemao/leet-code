package org.example.medium;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 给定一组非负整数 nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数。
 * 注意：输出结果可能非常大，所以你需要返回一个字符串而不是整数。
 *
 * 示例 1：
 * 输入：nums = [10,2]
 * 输出："210"
 *
 * 示例 2：
 * 输入：nums = [3,30,34,5,9]
 * 输出："9534330"
 *
 * @author 苍韧
 * @date 2022/9/1
 */
public class Solution179 {
    public static void main(String[] args) {
        System.out.println(largestNumber(new int[] {10, 2}));
    }

    public static String largestNumber(int[] nums) {
        List<String> list = Arrays.stream(nums)
            .mapToObj(var -> String.valueOf(var))
            .sorted((a, b) -> {
                Integer left = Integer.valueOf(a);
                Integer right = Integer.valueOf(b);
                double ab = left * Math.pow(10, b.length()) + right;
                double ba = right * Math.pow(10, a.length()) + left;
                //倒序
                return Double.compare(ba, ab);
            })
            .collect(Collectors.toList());

        if ("0".equals(list.get(0))) {
            return "0";
        }

        return list.stream().collect(Collectors.joining());
    }
}
