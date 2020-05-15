import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



public class Card implements Serializable{
	//Range of value and suit the Card can take
	public static final String[] av_num = 
			{"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
	public static final String[] av_suit = {"Diamond", "Spade"};
	
	private String num, suit;
	
	//The Map described Rating of Cards
	public static final Map<String, Integer> rate;
	static{
		Map<String, Integer> map = new HashMap<>();
		map.put("2", 1);
		map.put("3", 2);
		map.put("4", 3);
		map.put("5", 4);
		map.put("6", 5);
		map.put("7", 6);
		map.put("8", 7);
		map.put("9", 8);
		map.put("10", 9);
		map.put("J", 10);
		map.put("Q", 11);
		map.put("K", 12);
		map.put("A", 13);
		rate = Collections.unmodifiableMap(map);
	};
	
	//Constructor
	public Card(String n, String s){
		num = n;
		suit = s;
	}
	
	//getter functions
	public String getNum(){
		return this.num;
	}
	
	//Override the Object.toString()
	public String toString(){
		return (num + " of " + suit);
	}
}
