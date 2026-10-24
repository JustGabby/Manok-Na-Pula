package CardGame;

import java.util.ArrayList;
import java.util.Stack;

public class Blackjack extends Game implements CardValue{
	private Player player;
	private Dealer dealer;
	
	public Blackjack(double bet, Player player, Dealer dealer) {
		super(bet);
		this.player = player;
		this.dealer = dealer;
	}
	
	public Card draw() {
		return super.draw();
	}
	
	public double displayBet() {
		return super.getBet();
	}
	
	@Override
	public int getCardValue(Card card) {
		switch (card.getRank()) {
		case TWO:
			return 2;
		case THREE:
			return 3;
		case FOUR:
			return 4;
		case FIVE:
			return 5;
		case SIX: 
			return 6;
		case SEVEN: 
			return 7;
		case EIGHT:
			return 8;
		case NINE: 
			return 9;
		case TEN:
			return 10;
		case ACE:
			return 11;
		default:
			return 10;
		}	
	}
	
	public Stack<Card> getDeck() {
		return super.getDeck();
	}
	
	public boolean winner() {
		int playerScore = player.handValue(this);
		int dealerScore = dealer.handValue(this);
		
		if (playerScore > 21) {
			return false;
		}
		
		if (dealerScore > 21 || playerScore > dealerScore) {
			return true;
		}
		
		return false;
	}
	
	public void retrieveCards() {
		super.retrieve(player.getHand());
		super.retrieve(dealer.getDealerHand());
		
		player.getHand().clear();
		dealer.getHand().clear();
	}

	@Override
	public int calculateHandValue(ArrayList<Card> hand) {
		int handValue = 0;
		int aces = 0;
		
		for (Card card : hand) {
			handValue += getCardValue(card);
			
			if (card.getRank() == Rank.ACE) {
				aces++;
			}
		}
		while(handValue > 21 && aces > 0) {
			handValue -= 10;
			aces--;
		}
		
		return handValue;
	}
	
	public void dealerTurn() {
		dealer.addHand(this.draw());
		dealer.addHand(this.draw());
		
		int playerScore = calculateHandValue(player.getHand());
		int dealerScore = calculateHandValue(dealer.getHand());
		
		if (playerScore <= 21) {
			while(dealerScore <= playerScore && dealerScore < 21) {
				dealer.addHand(this.draw());
				dealerScore = calculateHandValue(dealer.getHand());
			}
		}
		
		dealer.displayHand(this);
		
		if(dealerScore > 21) {
			System.out.println("The dealer busts!");
		}
	}
	
	@Override
	public void showDown() {
		System.out.println("-------------------");
		dealerTurn();
		if (winner()) {
			System.out.println("Winner!");
		} else {
			System.out.println("Loser...");
		}
	}
}
