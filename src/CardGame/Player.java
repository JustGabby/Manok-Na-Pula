package CardGame;

import java.util.ArrayList;

public class Player {
	private ArrayList<Card> hand;
	private int handValue;
	
	public Player() {
		this.hand = new ArrayList<>();
	}
	
	public ArrayList<Card> getHand(){
		return this.hand;
	}
	
	public int getHandValue() {
		return handValue;
	}
	
	public void addHand(Card card){
		getHand().add(card);
	}
	
	public int handValue(Game game) {
		return game.calculateHandValue(hand);
	}
	
	public void displayHand(Blackjack blackjack) {
		System.out.print("Player Hand: ");
		for (Card card : hand) {
			System.out.print("[" + card.getCard() + "]");
		}
		System.out.println();
		System.out.println("Value: " + handValue(blackjack));
	}
}
