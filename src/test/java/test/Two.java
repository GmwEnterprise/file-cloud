package test;

/*
 * @lc app=leetcode.cn id=2 lang=java
 *
 * [2] 两数相加
 */

// @lc code=start

/**
 * Definition for singly-linked list.
 * public class ListNode {
 * int val;
 * ListNode next;
 * ListNode() {}
 * ListNode(int val) { this.val = val; }
 * ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */

class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    ListNode append(int val) {
        this.next = new ListNode(val);
        return this.next;
    }
}

public class Two {
    public static void main(String[] args) {
        ListNode l1 = new ListNode(9), l2 = new ListNode(1);
        l2.append(9).append(9).append(9).append(9).append(9).append(9).append(9).append(9).append(9);
        Two two = new Two();
        ListNode x = two.addTwoNumbers(l1, l2);
        System.out.println(x);
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        long i1 = 1, i2 = 1, num1 = 0, num2 = 0;

        // 遍历链表，组合出两个加数
        for (ListNode p = l1; p != null; p = p.next, i1 *= 10) {
            num1 += p.val * i1;
        }
        for (ListNode p = l2; p != null; p = p.next, i2 *= 10) {
            num2 += p.val * i2;
        }

        // 求和，再生成链表，从个位开始
        long sum = num1 + num2;

        if (sum == 0) {
            return new ListNode(0);
        }

        ListNode result = new ListNode();
        int digit = 1;

        for (ListNode node = result; ; ) {
            int val = digitCalculate(sum, digit++);
            if (val == -1) return result.next;
            node.next = new ListNode();
            node = node.next;
            node.val = val;
        }
    }

    // 计算位数，num为初始数，digit为位数 (1表示获取个位数，2表示获取十位数，以此类推)
    // 若返回 -1 则表示num没有该位数 (比如小于1000的整数没有千位数)
    private int digitCalculate(long num, int digit) {
        // 商为0则返回-1
        long quotient = num / (long) Math.pow(10, digit - 1);
        if (quotient == 0) return -1;
        return ((int) (quotient % 10));
    }
}
// @lc code=end

