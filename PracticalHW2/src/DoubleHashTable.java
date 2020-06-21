
public class DoubleHashTable extends OAHashTable {

    private ModHash hash1; // Returns values between 0 to m-1
    private ModHash hash2; // Returns values between 0 to m-2
    private int m;

    public DoubleHashTable(int m, long p) {
        super(m);
        hash1 = ModHash.GetFunc(m, p); // Returns values between 0 to m-1
        hash2 = ModHash.GetFunc(m - 1, p); // Returns values between 0 to m-2
        this.m = m;
    }


    @Override
    public int Hash(long x, int i) {
        // We want h1 to return values between 0 to m-1, and h2 to return values between 1 to m-1, thus we add 1 to h2 returned value
        return Math.floorMod(hash1.Hash(x) + i * (hash2.Hash(x) + 1), m);
    }

}
