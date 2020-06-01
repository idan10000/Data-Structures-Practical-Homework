/**
 * @author pinto - 322522111
 * @author rashtiyarden - 315384578
 */
public class Item {

    private int key;
    private String info;

    /**
     * <p>Initializes the item fields.</p>
     * Time Complexity: O(1)
     *
     * @param key  the key of the time
     * @param info the "value" of the item
     */
    public Item(int key, String info) {
        this.key = key;
        this.info = info;
    }

    //        All getters and setters run at O(1) time and are implemented defaultly (return value for getter, change it for setter)
    public int getKey() {
        return key;
    }

    public String getInfo() {
        return info;
    }
}