package Gamev02;

import java.util.LinkedList;

public abstract class Entity {
	private String name;
	private int healthpoint;
	private Deck deck;
	private int cardSlot;
	private int bustLimit;
	private LinkedList<Card> hand;
	private LinkedList<Card> discardPile;
	private LinkedList<Card> showdownHand;

	public Entity(String name, int healthpoint, int cardSlot) {
		this.name = name;
		this.healthpoint = healthpoint;
		this.deck = new Deck();
		this.cardSlot = cardSlot;
		this.bustLimit = 21;
		this.discardPile = new LinkedList<>();
		this.hand = new LinkedList<>();
		this.showdownHand = new LinkedList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHealthpoint() {
		return healthpoint;
	}

	public void setHealthpoint(int healthpoint) {
		this.healthpoint = healthpoint;
	}

	public Deck getDeck() {
		return deck;
	}

	public int getCardSlot() {
		return cardSlot;
	}

	public void setCardSlot(int cardSlot) {
		this.cardSlot = cardSlot;
	}

	public int getBustLimit() {
		return bustLimit;
	}

	public void setBustLimit(int bustLimit) {
		this.bustLimit = bustLimit;
	}

	public LinkedList<Card> getHand() {
		return hand;
	}

	public LinkedList<Card> getShowdownHand() {
		return showdownHand;
	}

	// methods
	public void drawCards() {
		while (hand.size() < cardSlot) {
			if (deck.isEmpty()) {
				addDiscardPileToDeck();
				if (deck.isEmpty()) {
					System.out.println("No more cards in discard pile.");
					break;
				}
			}
			hand.add(deck.draw());
		}
	}

	public void rerollHand() {
		System.out.println("Rerolling hand...");
		discardPile.addAll(hand);
		hand.clear();

		drawCards();
	}

	public void addDiscardPileToDeck() {
		deck.moveDiscardedCardsToDeck(discardPile);
		discardPile.clear();
	}

	public void viewHand() {
		for (int i = 0; i < hand.size(); i++) {
			System.out.println((i + 1) + ". " + hand.get(i));
		}
	}

	public void selectCards(int[] plays) {
		for (int choice : plays) {
			int index = choice - 1;
			if (index >= 0 && index < hand.size()) {
				Card chosen = hand.get(index);
				if (!showdownHand.contains(chosen)) {
					showdownHand.add(chosen);
				}
			} else {
				System.out.println("Invalid card number: " + choice);
			}
		}
		hand.removeAll(showdownHand);
	}
	
	public int calculateHandValue(LinkedList<Card> hand) {
		int handValue = 0;
		int aces = 0;
		
		for (Card card : hand) {
			handValue += card.getRank().getValue();
			
			if (card.getRank() == Rank.ACE) {
				aces++;
			}
		}
		while(handValue > getBustLimit() && aces > 0) {
			handValue -= 10;
			aces--;
		}
		return handValue;
	}

	public void clearShowdownHand() {
		discardPile.addAll(showdownHand);
		showdownHand.clear();
	}

	public abstract void playShowdownHand();
}
