package org.example.simple;

/**
 * 给你两个只包含 1 到 9 之间数字的数组 nums1 和 nums2 ，每个数组中的元素 互不相同 ，请你返回 最小 的数字，两个数组都 至少 包含这个数字的某个数位。
 *
 *
 * 示例 1：
 *
 * 输入：nums1 = [4,1,3], nums2 = [5,7]
 * 输出：15
 * 解释：数字 15 的数位 1 在 nums1 中出现，数位 5 在 nums2 中出现。15 是我们能得到的最小数字。
 * 示例 2：
 *
 * 输入：nums1 = [3,5,2,6], nums2 = [3,1,7]
 * 输出：3
 * 解释：数字 3 的数位 3 在两个数组中都出现了。
 *
 *
 * 提示：
 *
 * 1 <= nums1.length, nums2.length <= 9
 * 1 <= nums1[i], nums2[i] <= 9
 * 每个数组中，元素 互不相同 。
 */
public class Solution2605 {
    public static void main(String[] args) {
        int[] nums1 = new int[] {4, 1, 3};
        int[] nums2 = new int[] {5, 7};
        Solution2605 solution = new Solution2605();
        System.out.println(solution.minNumber(nums1, nums2));
    }

    public int minNumber(int[] nums1, int[] nums2) {
        //先找是否有相同的数字,如果有多个，取最小的
        Integer sameNumber = getSameNumber(nums1, nums2);
        if (sameNumber != null) {
            return sameNumber;
        }

        //分别从两个数组中找最小的数字
        return getNotSameNumber(nums1, nums2);
    }

    private int getNotSameNumber(int[] nums1, int[] nums2) {
        int minNum1 = getMinNum(nums1);
        int minNum2 = getMinNum(nums2);
        if (minNum1 <= minNum2) {
            return minNum1 * 10 + minNum2;
        }
        return minNum2 * 10 + minNum1;

    }

    private int getMinNum(int[] nums1) {
        int result = nums1[0];
        for (int i = 1; i < nums1.length; i++) {
            if (nums1[i] < result) {
                result = nums1[i];
            }
        }
        return result;
    }

    private Integer getSameNumber(int[] nums1, int[] nums2) {
        Integer result = null;
        for (int i : nums1) {
            for (int j : nums2) {
                if (i == j) {
                    if (result == null || i < result) {
                        result = i;
                    }
                }
            }
        }
        return result;
    }
}
