package com.jwetherell.algorithms.sorts;


/**
 * An American flag sort is an efficient, in-place variant of radix sort that distributes items into hundreds 
 * of buckets. Non-comparative sorting algorithms such as radix sort and American flag sort are typically used 
 * to sort large objects such as strings, for which comparison is not a unit-time operation.
 * Family: Bucket.
 * Space: 10 Buckets of 1 integer per radix.
 * Stable: False.
 * 
 * Average case = O(n*k)
 * Worst case = O(n*k)
 * Best case = O(n*k)
 * NOTE: n is the number of digits and k is the average bucket size
 * 
 * http://en.wikipedia.org/wiki/American_flag_sort
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class AmericanFlagSort {

    private static final int numberOfBuckets = 10;


    private AmericanFlagSort() { }

    public static Integer[] sort(Integer[] unsorted) {
        int numberOfDigits = getMaxNumberOfDigits(unsorted); //Max number of digits
        int max = 1;
        for (int i=0; i<numberOfDigits-1; i++) max *= 10; 
        sort(unsorted,0,unsorted.length,max);
        return unsorted;
    }
    
    public static void sort(Integer[] unsorted, int start, int length, int divisor) {
        //First pass - find counts
        int[] count = new int[numberOfBuckets];
        int[] offset = new int[count.length];
        int digit = 0;
        for (int i=start; i<length; i++) {
            int d = unsorted[i];
            digit = getDigit(d,divisor);
            count[digit]++;
        }
        offset[0] = start+0;
        for (int i=1; i<offset.length; i++) {
            offset[i] = count[i-1]+offset[i-1];
        }

        //Second pass - move into position
        int origin = 0;
        int from = 0;
        int num = 0;
        int to = 0;
        int temp = 0;
        for (int b = 0; b < count.length; b++) {
            while (count[b] > 0) {
               origin = offset[b];
               from = origin;
               num = unsorted[from];
               unsorted[from] = -1;
               do {
                  digit = getDigit(num,divisor);
                  to = offset[digit]++;
                  count[digit]--;
                  temp = unsorted[to];
                  unsorted[to] = num;
                  num = temp;
                  from = to;
               } while (from != origin);
            }
         }

        if (divisor>1) {
            //Sort the buckets
            for (int i=0; i<offset.length; i++) {
                int begin = (i>0)?offset[i-1]:start;
                int end = offset[i];
                if (end-begin>1) sort(unsorted, begin, end, divisor/10);
            }
        }
    }

    private static int getMaxNumberOfDigits(Integer[] unsorted) {
        int max = Integer.MIN_VALUE;
        int temp = 0;
        for (int i : unsorted) {
            temp = (int)Math.log10(i)+1;
            if (temp>max) max=temp;
        }
        return max;
    }

    private static int getDigit(int integer, int divisor) {
        return (integer / divisor) % 10;
    }
}
