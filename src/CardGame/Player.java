package CardGame;

import java.util.ArrayList;

public class Player {
	private ArrayList<Card> hand;
	private int handValue;
	private int wins = 0;
	private int lose = 0;
	private int draw = 0;

	public Player() {
		this.hand = new ArrayList<>();
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLose() {
		return lose;
	}

	public void setLose(int lose) {
		this.lose = lose;
	}
	
	public int getDraw() {
		return draw;
	}
	
	public void setDraw(int draw) {
		this.draw = draw;
	}

	public ArrayList<Card> getHand() {
		return this.hand;
	}

	public int getHandValue() {
		return handValue;
	}

	public void setHandValue(int handValue) {
		this.handValue = handValue;
	}

	public void addHand(Card card) {
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

	public int getTotalGames() {
		return draw + wins + lose;
	}

}
