package org.example.medium;

import org.junit.Assert;

/**
 * 3. 无重复字符的最长子串
 *
 * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
 *
 *
 *
 * 示例 1:
 *
 * 输入: s = "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * 示例 2:
 *
 * 输入: s = "bbbbb"
 * 输出: 1
 * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 * 示例 3:
 *
 * 输入: s = "pwwkew"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 * 请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 *
 *
 * 提示：
 *
 * 0 <= s.length <= 5 * 104
 * s 由英文字母、数字、符号和空格组成
 */
public class Solution3 {
    public static void main(String[] args) {
        Solution3 solution = new Solution3();

        Assert.assertEquals(3, solution.lengthOfLongestSubstring("aabaab!bb"));
        Assert.assertEquals(2, solution.lengthOfLongestSubstring("aab"));

        Assert.assertEquals(3, solution.lengthOfLongestSubstring("abcabcbb"));
        Assert.assertEquals(1, solution.lengthOfLongestSubstring("bbbbb"));
        Assert.assertEquals(3, solution.lengthOfLongestSubstring("pwwkew"));
        System.out.println("执行完成");
    }

    // 2 ms
    public int lengthOfLongestSubstring(String s) {
        int n = s.length();
        int result = 0;
        int[] index = new int[128]; // 用于存储字符的索引，初始值为0

        for (int i = 0, j = 0; j < n; j++) {
            i = Math.max(index[s.charAt(j)], i); // 更新起始位置i
            result = Math.max(result, j - i + 1); // 更新最长子串的长度
            index[s.charAt(j)] = j + 1; // 将字符的索引更新为当前位置+1，避免重复字符
        }

        return result;
    }

    //6 ms
    //public int lengthOfLongestSubstring(String s) {
    //    int n = s.length();
    //    int i = 0, j = 0;
    //    int maxLength = 0;
    //    Set<Character> set = new HashSet<>();
    //
    //    while (i < n && j < n) {
    //        if (!set.contains(s.charAt(j))) {
    //            set.add(s.charAt(j++));
    //            maxLength = Math.max(maxLength, j - i);
    //        } else {
    //            set.remove(s.charAt(i++));
    //        }
    //    }
    //
    //    return maxLength;
    //}

    //19 ms
    //public int lengthOfLongestSubstring(String s) {
    //    if (s.length() < 2) {
    //        return s.length();
    //    }
    //    int result = 0;
    //    Queue<Character> queue = new LinkedList<Character>();
    //    for (int i = 0; i < s.length(); i++) {
    //        char c = s.charAt(i);
    //        if (!queue.contains(c)) {
    //            queue.offer(c);
    //            if (queue.size() > result) {
    //                result = queue.size();
    //            }
    //        } else {
    //            do {
    //                queue.poll();
    //            } while (queue.contains(c));
    //            queue.offer(c);
    //        }
    //    }
    //    return result;
    //}

    //2797ms
    //public int lengthOfLongestSubstring(String s) {
    //    if (s.length() < 2) {
    //        return s.length();
    //    }
    //    int result = 0;
    //    List<Character> queue = new LinkedList<Character>();
    //    for (int i = 0; i < s.length(); i++) {
    //        char c = s.charAt(i);
    //        if (!queue.contains(c)) {
    //            queue.add(c);
    //            if (queue.size() > result) {
    //                result = queue.size();
    //            }
    //        } else {
    //            int indexOf = queue.indexOf(c);
    //            if (indexOf < queue.size() - 1) {
    //                queue = queue.subList(indexOf + 1, queue.size());
    //            } else {
    //                queue.clear();
    //            }
    //            queue.add(c);
    //        }
    //    }
    //    return result;
    //}
}
