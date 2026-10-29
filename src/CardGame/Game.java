package CardGame;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Scanner;

public abstract class Game {
	private double bet;
	private Deck deck;
	
	public Game(double bet) {
		this.bet = bet;
		this.deck = new Deck();
	}

	public double getBet() {
		return bet;
	}
	
	public Stack<Card> getDeck() {
		return this.deck.getDeck();
	}
	
	public Card draw() {
		return this.deck.draw();
	}
	
	public boolean askPlayAgain(Scanner sc) {
		sc.nextLine();
		System.out.println("Play again? (Y / N)"); 
		String askPlayer = sc.nextLine().toLowerCase();
		
		if (askPlayer.startsWith("y")) {
			System.out.println("\nPlaying again...\n");
			return true;
		}
		return false;
	}

	public abstract void showDown();
	public abstract int calculateHandValue(ArrayList<Card> card);
	
	public void retrieve(ArrayList<Card> hand){
		for (Card card : hand) {
			this.deck.getDeck().push(card);
		}
	}
}
