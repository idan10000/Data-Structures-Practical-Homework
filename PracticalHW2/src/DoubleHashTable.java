import java.util.Random;

public class DoubleHashTable extends OAHashTable {

	private ModHash hash1,hash2;
	private int m;

	public DoubleHashTable(int m, long p) {
		super(m);
		hash1 = ModHash.GetFunc(m, p);
		hash2 = ModHash.GetFunc(m-1, p);
		this.m = m;
	}

	
	@Override
	public int Hash(long x, int i) {
		return (hash1.Hash(x) + i * (hash2.Hash(x)+1)) % m;
	}
	
}
