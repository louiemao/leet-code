package org.example.simple;

/**
 * 给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。
 *
 *
 * 示例 1：
 *
 * 输入：head = [1,2,3,4,5]
 * 输出：[5,4,3,2,1]
 *
 *
 * 示例 2：
 *
 * 输入：head = [1,2]
 * 输出：[2,1]
 *
 *
 * 示例 3：
 *
 * 输入：head = []
 * 输出：[]
 *
 *
 * 提示：
 *
 * 链表中节点的数目范围是 [0, 5000]
 * -5000 <= Node.val <= 5000
 *
 *
 * 进阶：链表可以选用迭代或递归方式完成反转。你能否用两种方法解决这道题？
 */
public class Solution206 {
    public static void main(String[] args) {
        Solution206 solution = new Solution206();

        ListNode head = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5)))));
        ListNode result = solution.reverseList(head);
        System.out.println("执行完成");
        //Assert.assertEquals(3, solution.majorityElement(new int[] {3, 2, 3}));
        //Assert.assertEquals(2, solution.majorityElement(new int[] {2, 2, 1, 1, 1, 2, 2}));
    }

    public ListNode reverseList(ListNode head) {
        ListNode result = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = result;
            result = head;
            head = next;
        }
        return result;
    }

    ///**
    // * 递归
    // * @param head
    // * @return
    // */
    //public ListNode reverseList(ListNode head) {
    //    if (head == null || head.next == null) {
    //        return head;
    //    }
    //    ListNode result = reverseList(head.next);
    //    head.next.next = head;
    //    head.next = null;
    //    return result;
    //}

    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {}

        ListNode(int val) {this.val = val;}

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }

    }
}
