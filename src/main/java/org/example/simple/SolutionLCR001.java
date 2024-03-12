package org.example.simple;

/**
 * @author 苍韧
 * @date 2023/11/2
 */
public class SolutionLCR001 {
    public static void main(String[] args) {
        SolutionLCR001 solution = new SolutionLCR001();
        //System.out.println(solution.divide(-2147483648, -1));

        System.out.println(solution.divide(-2147483648, 1)+" 预期：-2147483648");

        System.out.println(solution.divide(-1010369383, -2147483648) + " 预期：0");
    }

    public int divide(int a, int b) {
        int result = 0;
        boolean negative = (a > 0 && b < 0) || (a < 0 && b > 0);

        a = Math.abs(a);
        b = Math.abs(b);

        //判断b的最高位
        int maxIndex = 32;
        for (; maxIndex > 0; maxIndex--) {
            int temp = 1 << maxIndex - 1;
            //System.out.println(Integer.toBinaryString(temp));
            if ((b & temp) == temp) {
                break;
            }
        }

        for (int i = 31 - maxIndex; i >= 0; i--) {
            int temp = b << i;
            if (temp > a && a != Integer.MIN_VALUE) {
                continue;
            }
            a -= temp;
            result += 1 << i;
        }

        if (negative) {
            if (a == b) {
                return -(result + 1);
            }
            return -result;
        }
        if (a == b && result != Integer.MAX_VALUE) {
            return result + 1;
        }
        return result;
    }

}
