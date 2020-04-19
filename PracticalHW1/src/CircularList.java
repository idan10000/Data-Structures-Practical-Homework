import java.util.Arrays;

/**
 * Circular list
 * <p>
 * An implementation of a circular list with  key and info
 */

public class CircularList {

    private Item[] arr;
    private final int maxLen;
    private int len;
    private int start;


    public CircularList(int maxLen) {
        this.maxLen = maxLen;
        arr = new Item[maxLen];
        //Len, start are defaultly set to 0.
    }

    /**
     * public Item retrieve(int i)
     * <p>
     * returns the item in the ith position if it exists in the list.
     * otherwise, returns null
     */
    public Item retrieve(int i) {
        if (i >= len || i < 0)
            return null;

        return arr[(start + i) % maxLen];
    }

    /**
     * public int insert(int i, int k, String s)
     * <p>
     * inserts an item to the ith position in list  with key k and  info s.
     * returns -1 if i<0 or i>n  or n=maxLen otherwise return 0.
     */
    public int insert(int i, int k, String s) {
        if (i > len || i < 0 || len == maxLen)
            return -1;
        Item newItem = new Item(k, s);
        if (i < len - i) {
            for (int j = start; j < start + i; j++) {
                arr[(j - 1) % maxLen] = arr[j % maxLen];
            }
            start = (maxLen + start - 1) % maxLen;
        } else {
            for (int j = start + len - 1; j >= start + i; j--) {
                arr[(j + 1) % maxLen] = arr[j % maxLen];
            }
        }

        len++;
        arr[(start + i) % maxLen] = newItem;
        return 0;
    }

    /**
     * public int delete(int i)
     * <p>
     * deletes an item in the ith posittion from the list.
     * returns -1 if i<0 or i>n-1 otherwise returns 0.
     */
    public int delete(int i) {
        if (i >= len || i < 0)
            return -1;

        if (i > len - i) {
            for (int j = start + i; j < start + len - 1; j++) {
                arr[j % maxLen] = arr[(j+1) % maxLen];
            }
        } else {
            for (int j = start + i; j > 0; j--) {
                arr[j % maxLen] = arr[(j - 1) % maxLen];
            }
            start++;

        }
        if(--len == 0)
            start = -1;
        return 0;
    }


    @Override
    public String toString() {
        return "CircularList{" +
                "arr=" + Arrays.toString(arr) +
                ", maxLen=" + maxLen +
                ", len=" + len +
                ", start=" + start +
                '}';
    }
}
 
 
 
