
/**
 * @author pinto - 322522111
 * @author rashtiyarden - 315384578
 * Tree list
 * <p>
 * An implementation of a Tree list with  key and info
 */
public class TreeList {

    private final RankTreeList tree;

    /**
     * <p>
     *     makes a new {@link RankTreeList} (all fields are default).
     * </p>
     * Time Complexity: O(1)
     */
    public TreeList(){
        tree = new RankTreeList();
    }

    /**
     * public Item retrieve(int i)
     * <p>
     * returns the item in the ith position if it exists in the list.
     * otherwise, returns null
     * Time Complexity: O(log(n))
     */
    public Item retrieve(int i) {
        return tree.retrieve(i);
    }

    /**
     * public int insert(int i, int k, String s)
     * <p>
     * inserts an item to the ith position in list  with key k and  info s.
     * returns -1 if i<0 or i>n otherwise return 0.
     * Time Complexity: O(log(n))
     */
    public int insert(int i, int k, String s) {
        return tree.insert(i, k, s);
    }

    /**
     * public int delete(int i)
     * <p>
     * deletes an item in the ith posittion from the list.
     * returns -1 if i<0 or i>n-1 otherwise returns 0.
     * Time Complexity: O(log(n))
     */
    public int delete(int i) {
        return tree.delete(i);
    }

}