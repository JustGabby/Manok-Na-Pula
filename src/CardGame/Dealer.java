package CardGame;

import java.util.ArrayList;

public class Dealer extends Player{
	
	public Dealer() {
		super();
	}
	
	public ArrayList<Card> getDealerHand(){
		return super.getHand();
	}
	
	@Override
	public int handValue(Game game) {
		return game.calculateHandValue(super.getHand());
	}
	
	@Override
	public void addHand(Card card) {
		getDealerHand().add(card);
	}
	
	@Override
	public void displayHand(Blackjack blackjack) {
		System.out.print("Dealer Hand: ");
		for (Card card : super.getHand()) {
			System.out.print("[" + card.getCard() + "]");
		}
		System.out.println();
		System.out.println("Value: " + handValue(blackjack));
	}
}
