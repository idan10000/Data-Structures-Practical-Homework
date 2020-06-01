import java.util.Random;

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
		return (hash.Hash(x) + (int)Math.pow(-1,i)*i*i) % m;
	}
}
