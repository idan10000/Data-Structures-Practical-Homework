public class ModHash {

	private final long a,b, p;
	private final int m;

	public ModHash(long a, long b, long p, int m) {
		this.a = a;
		this.b = b;
		this.p = p;
		this.m = m;
	}

	public static ModHash GetFunc(int m, long p){
		long a,b;
		a = 1 + (long) (Math.random() * (p - 1)); // Generate a random long number between 1 and p
		b = (long) (Math.random() * p); // Generate a random long number between 0 and p
		return new ModHash(a,b,p,m);
	}
	
	public int Hash(long key) {
		return (int)Math.floorMod((Math.floorMod(a * key + b, p)),m); // Return base hash value
	}
}
