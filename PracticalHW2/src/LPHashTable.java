import java.util.Random;

public class LPHashTable extends OAHashTable {

    private ModHash hash;
    private int m;

    public LPHashTable(int m, long p) {
        super(m);
        hash = ModHash.GetFunc(m, p);
        this.m = m;
    }

    @Override
    public int Hash(long x, int i) {
        return (hash.Hash(x) + i) % m;
    }

}
