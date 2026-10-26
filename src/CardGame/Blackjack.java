package CardGame;

import java.util.ArrayList;
import java.util.Stack;

public class Blackjack extends Game implements CardValue {
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

	public int winner() {
		int playerScore = player.handValue(this);
		int dealerScore = dealer.handValue(this);

		// 1 = Tie
		// 2 = Player Win
		// 3 = Dealer Win

		if (playerScore > 21) {
			return 3;
		}
		if (dealerScore > 21) {
			return 2;
		}
		if (playerScore > dealerScore) {
			return 2;
		} else if (playerScore < dealerScore) {
			return 3;
		}
		return 1;
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
		while (handValue > 21 && aces > 0) {
			handValue -= 10;
			aces--;
		}

		return handValue;
	}

	public void dealerTurn() {
		int dealerScore = calculateHandValue(dealer.getHand());

		while (dealerScore < 17) {
			dealer.addHand(this.draw());
			dealerScore = calculateHandValue(dealer.getHand());
		}

		dealer.displayHand(this);

		if (dealerScore > 21) {
			System.out.println("The dealer busts!");
		}
	}

	@Override
	public void showDown() {
		System.out.println("-------------------");

		if (player.getHandValue() <= 21) {
			dealerTurn();
		}
		if (winner() == 1) {
			System.out.println("Draw.");
			player.setDraw(player.getDraw() + 1);
		} else if (winner() == 2) {
			System.out.println("Winner!");
			player.setWins(player.getWins() + 1);
		} else if (winner() == 3) {
			System.out.println("Lose...");
			player.setLose(player.getLose() + 1);
		}
	}

	public double getWinRate() {
		int wins = player.getWins();
		int totalGames = player.getTotalGames();

		if (totalGames == 0) {
			return 0.0;
		}
		return 100.0 * wins / totalGames;
	}
}
