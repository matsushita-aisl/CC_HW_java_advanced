import java.util.ArrayList;
import java.util.Collections;



public class Deck{
	
	public ArrayList<Card> cards;
	
	//Constructor
	public Deck(){
		cards = new ArrayList<Card>();
		for(String suit: Card.av_suit){
			for(String num: Card.av_num){
				cards.add(new Card(num, suit));
			}
		}
		Collections.shuffle(cards);
	}	
}