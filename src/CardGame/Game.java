package CardGame;

import java.util.ArrayList;
import java.util.Stack;

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

	public abstract void showDown();
	public abstract int calculateHandValue(ArrayList<Card> card);
	
	public void retrieve(ArrayList<Card> hand){
		for (Card card : hand) {
			this.deck.getDeck().push(card);
		}
	}
}
