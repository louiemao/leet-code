package org.example.medium;

import java.util.Arrays;

import org.junit.Assert;

/**
 * 215. 数组中的第K个最大元素
 *
 * 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
 *
 * 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
 *
 * 你必须设计并实现时间复杂度为 O(n) 的算法解决此问题。
 *
 *
 *
 * 示例 1:
 *
 * 输入: [3,2,1,5,6,4], k = 2
 * 输出: 5
 *
 *
 * 示例 2:
 *
 * 输入: [3,2,3,1,2,4,5,5,6], k = 4
 * 输出: 4
 *
 *
 * 提示：
 *
 * 1 <= k <= nums.length <= 105
 * -104 <= nums[i] <= 104
 */
public class Solution215 {

    public static void main(String[] args) {
        Solution215 solution = new Solution215();

        Assert.assertEquals(5, solution.findKthLargest(new int[] {3, 2, 1, 5, 6, 4}, 2));
        Assert.assertEquals(4, solution.findKthLargest(new int[] {3, 2, 3, 1, 2, 4, 5, 5, 6}, 4));
    }

    //public int findKthLargest(int[] nums, int k) {
    //    if (nums.length == 1) {
    //        return nums[0];
    //    }
    //
    //    Arrays.sort(nums);
    //
    //    return nums[nums.length - k];
    //}

    public int findKthLargest(int[] nums, int k) {
        if (nums.length == 1) {
            return nums[0];
        }

        Arrays.sort(nums);

        return nums[nums.length - k];
    }

}
