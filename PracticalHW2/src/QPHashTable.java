public class QPHashTable extends OAHashTable {

	private ModHash hash;
	private int m;

	public QPHashTable(int m, long p) {
		super(m);
		hash = ModHash.GetFunc(m, p);
		this.m = m;
	}

	
	@Override
	public int Hash(long x, int i) {
		return (hash.Hash(x) + i*i) % m;
	}
}
