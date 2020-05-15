/****************************
This class is for playing the War Game
Using Game, Deck and Card class

*Compile & Execution*
{path to WarGame}$ javac WG_play.java
{path to WarGame}$ java WG_play

*Notification*
Adapt path to save file to your environment (DIR, DAT and CSV in GW_play class)
****************************/

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



public class WG_play{
	private static final Path DIR = Paths.get("./saved/");	//Relative path from Eclipse project (${workspace}/WarGame/)
	private static final Path DAT = DIR.resolve("PreviousGame.dat");	//Connecting to DIR (In Eclipse, DAT's path is "${workspace}/WarGame/saved/PreviousGame.dat") 
	private static final Path CSV = DIR.resolve("EntireResult.csv");	//Same as DAT
	private static final String HEADER = "Number of Games,Number of Victories,Maximum cards you earned\n";	//Result header
	
	static Game game;
	static Scanner scanner = new Scanner(System.in);

	
	public static void main(String[] args){
		Deck deck = new Deck();
		String str;
		boolean end = false; //end flag
		
		if(!load()){	//Loading previous game data
			game = new Game();
			game.Deal(deck);
		}
		
		while(!end && (game.players[0].getHandNum() != 0 || game.players[1].getHandNum() != 0)){	//Empty hand Or End flag -> QUIT
			System.out.println("-*-*-*-Round " + game.getRound() + "-*-*-*-\n" +
					"Pool : " + game.getFieldNum() + "\n" +
					"Opponent's hand : " + game.players[1].getHandNum() + ", Acquisitions : " + game.players[1].getScore() +"\n" +
					"Your hand : " + game.players[0].getHandNum() + ", Acquisitions : " + game.players[0].getScore()
					);
			
			while(true){
				System.out.print("Do you play a card? [p/q] (p:play / q:quit) > ");
				str = scanner.next();
				if(!str.equals("q") && !str.equals("p")){	//Re-Input
					System.out.println("INVALID INPUT!! Please press the 'p' or 'q' key");
					continue;
				}else if(str.equals("p")){	//Play
					game.Play();
				}else{	//Quit
					saveGame();
					end = true;
				}
				break;
			}
			game.incRound();	//Next round
		}
		scanner.close();
		
		if(!end){	//Display result and End processing
			Game.Player winner = game.players[0].getScore() > game.players[1].getScore() ? game.players[0] : game.players[1];
			System.out.println("-*-*-*-Result " + "-*-*-*-\n" +
					"Opponent's Acquisitions : " + game.players[1].getScore() + "\n" +
					"Your Acquisitions : " + game.players[0].getScore() + "\n" +
					" WINNER : " + winner.getName()
					);
			updateResult(winner);
			//Game reach end, then delete save file of game
			if(Files.exists(DAT)){
				try{
					Files.delete(DAT);
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	
	static boolean load(){
		if(Files.exists(DAT)){
			String str;
			
			System.out.println("There is save data of previous game");
			
			while(true){
				System.out.print("Do you continue with the game? [y/n] > ");
				str = scanner.next();
				if(!str.equals("y") && !str.equals("n")){	//Re-Input
					System.out.println("INVALID INPUT!! Please press the 'y' or 'n' key");
					continue;
				}else if(str.equals("y")){	//Continue
					try(ObjectInputStream in = new ObjectInputStream(Files.newInputStream(DAT))){
						game = (Game)in.readObject();
						System.out.println("Continue from previous game");
						return true;
					}catch(IOException | ReflectiveOperationException e){
						System.out.println("Loading failed! New game start...");
						return false;
					}
				}else{	//New game
					return false;
				}
			}
		}else{
			return false;
		}
	}
	
	
	static void saveGame(){
		mkdir();
		try(ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(DAT))){
			out.writeObject(game);
			out.flush();
			System.out.println("Saved this game. See you again :)");
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("Saving failed");
		}
	}
	
	
	static void updateResult(Game.Player winner){
		List<Integer> values = null;
		mkdir();
		
		if(Files.exists(CSV)){
			try(BufferedReader br = Files.newBufferedReader(CSV)){
				br.readLine();	//Header
				String line = br.readLine(); //read past data
				String[] buff = line.split(",");
				values = Arrays.stream(buff).map(Integer::valueOf).collect(Collectors.toList());
				
			}catch(IOException e){
		      e.printStackTrace();
		    }
		}else{
			values = new ArrayList<>(Arrays.asList(0,0,0));
		}
		
		values.set(0, values.get(0) + 1);	//increment Num. of Game
		
		if(winner.equals(game.players[0])){
			values.set(1, values.get(1) + 1);	//increment Vict. of Game
			if(winner.getScore() > values.get(2)){	//Update Maximum earned
				values.set(2, winner.getScore());
			}
		}
		
		try(BufferedWriter bw = Files.newBufferedWriter(CSV)){	//write down to CSV file
			bw.write(HEADER);
			bw.write(values.stream().map(String::valueOf).collect(Collectors.joining(",","","\n")));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	static void mkdir(){
		if(!Files.exists(DIR)){	//if the save directory does not exist, create the directory
			try {
				Files.createDirectory(DIR);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
