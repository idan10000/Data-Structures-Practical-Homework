

public class Item{
	
	private int key;
	private String info;
	
	public Item (int key, String info){
		this.key = key;
		this.info = info;
	}
	public int getKey(){
		return key;
	}
	public String getInfo(){
		return info;
	}




	@Override
	public String toString() {
//		return "Item{" +
//				"key=" + key +
//				", info='" + info + '\'' +
//				'}';
		return "" + key;
	}
}