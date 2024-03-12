package org.example.medium;

import org.junit.Assert;

/**
 * 5. 最长回文子串
 *
 * 给你一个字符串 s，找到 s 中最长的回文子串。
 *
 * 如果字符串的反序与原始字符串相同，则该字符串称为回文字符串。
 *
 *
 *
 * 示例 1：
 *
 * 输入：s = "babad"
 * 输出："bab"
 * 解释："aba" 同样是符合题意的答案。
 * 示例 2：
 *
 * 输入：s = "cbbd"
 * 输出："bb"
 *
 *
 * 提示：
 *
 * 1 <= s.length <= 1000
 * s 仅由数字和英文字母组成
 */
public class Solution5 {
    public static void main(String[] args) {
        Solution5 solution = new Solution5();

        Assert.assertEquals("bab", solution.longestPalindrome("babad"));
        Assert.assertEquals("bb", solution.longestPalindrome("cbbd"));

        System.out.println("执行完成");
    }

    public String longestPalindrome(String s) {
        String palindrome = "";
        for (int i = 0; i < s.length(); i++) {
            for (int j = s.length() - 1; j >= i; j--) {
                if (palindrome(s, i, j)) {
                    String str = s.substring(i, j + 1);
                    if (str.length() > palindrome.length()) {
                        palindrome = str;
                    }
                    break;
                }
            }
        }

        return palindrome;
    }

    public boolean palindrome(String s, int left, int right) {
        for (; left < right; left++, right--) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
        }
        return true;
    }

}
