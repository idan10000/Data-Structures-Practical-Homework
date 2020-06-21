public class AQPHashTable extends OAHashTable {

	private ModHash hash;
	private int m;

	public AQPHashTable(int m, long p) {
		super(m);
		hash = ModHash.GetFunc(m, p);
		this.m = m;
	}
	
	@Override
	public int Hash(long x, int i) {
		return i % 2 == 0 ? ((hash.Hash(x) + i*i) % m) : Math.floorMod(hash.Hash(x) - (i * i), m);
	}
}
