package Game;

import java.util.LinkedList;

public class Game {
	private Player player; 
	private Enemy enemy;
	
	public Game(Player player, Enemy enemy) {
		this.player = player;
		this.enemy = enemy;
	}

	public Player getPlayer() {
		return player;
	}

	public Enemy getEnemy() {
		return enemy;
	}

	public void playerTurn() {
		
	}
	
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
	
	public int calculateHandValue(LinkedList<Card> hand) {
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
	
	public void getPlayerPoints() {
		if (!player.getPlayHand().isEmpty()) {
			System.out.println("Played Hand:");
			for (Card card : player.getPlayHand()) {
				System.out.println(card.toString());
			}
			
			int points = calculateHandValue(player.getPlayHand());
			System.out.println("Points: " + points);
			
			player.clearPlayedHand();
			
		} else {
			System.out.println("No hand played!");
		}
	}
}
