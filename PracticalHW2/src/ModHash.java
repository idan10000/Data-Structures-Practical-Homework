import java.util.concurrent.ThreadLocalRandom;

public class ModHash {

	private long a,b, p;
	private int m;

	public ModHash(long a, long b, long p, int m) {
		this.a = a;
		this.b = b;
		this.p = p;
		this.m = m;
	}

	public static ModHash GetFunc(int m, long p){
		long a,b;
		a = ThreadLocalRandom.current().nextLong(1,p);
		b = ThreadLocalRandom.current().nextLong(0,p);
		return new ModHash(a,b,p,m);
	}
	
	public int Hash(long key) {
		return (int)((a * key + b) % p) % m;
	}
}
