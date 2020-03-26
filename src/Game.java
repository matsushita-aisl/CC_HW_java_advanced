import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class Game implements Serializable{
	public static final int PLAYER_SIZE = 2;
	
	public Player[] players = new Player[PLAYER_SIZE];
	private ArrayList<Card> field = new ArrayList<>();
	
	private int round;
	
	
	public Game(){
		players[0] = new Player("You");
		players[1] = new Player("Opponent");
		round = 1;
	}
	
	
	
	public class Player implements Serializable{	//inner class Player
		private String name;
		private Queue<Card> hand, acquisition;
		
		
		public Player(String name){
			this.name = name;
			hand = new ArrayDeque<>();
			acquisition = new ArrayDeque<>();
		}
		
		
		public void addHand(Card card){
			hand.add(card);
		}
		
		
		public void earn(Card card){	//Earning a card
			acquisition.add(card);
		}
		
		
		public int getScore(){
			return acquisition.size();
		}
		
		
		public int getHandNum(){
			return hand.size();
		}
		
		
		public String getName(){
			return name;
		}
	}
	
	
	public int getFieldNum(){
		return field.size();
	}
	
	
	public int getRound(){
		return round;
	}
	
	
	public void incRound(){ //increment round
		round += 1;
	}
	
	
	public void Deal(Deck deck){	//Deal deck to players
		int size = deck.cards.size();
		for(Player p: players){
			for(int i = 0; i < size/PLAYER_SIZE; i++){
				p.addHand(deck.cards.remove(0));
			}
		}
	}
	
	//Play card
	public void Play(){
		System.out.println("~~~PLAY A CARD~~~" );		
		for(Player p: players){
			System.out.println(p.name + " : " + p.hand.peek().toString());
		}
		System.out.println("~~~~~~~~~~~~~~~~~" );				
		
		int v0 = Card.rate.get(players[0].hand.peek().getNum()),
			v1 = Card.rate.get(players[1].hand.peek().getNum());

		if(v0 == v1){	//Draw
			System.out.println("Draw");
			for(Player p: players){
				field.add(p.hand.poll());
			}
		}else if(v0 > v1){	//Ally win
			System.out.println(players[0].name + " win");
			for(Player p: players){
				players[0].earn(p.hand.poll());
			}
			if(!field.isEmpty()){	//if there are stacks on field
				for(Card card: field){
					players[0].earn(card);
				}
				field.clear();
			}
		}else{	//Opponent win
			System.out.println(players[1].name + " win");
			for(Player p: players){
				players[1].earn(p.hand.poll());
			}
			if(!field.isEmpty()){
				for(Card card: field){	//if there are stacks on field
					players[1].earn(card);
				}
				field.clear();
			}
		}
	}
}
