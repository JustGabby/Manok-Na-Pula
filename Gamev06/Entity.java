package Gamev06;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public abstract class Entity {
	private String name;
	private int maxHealthpoint;
	private int healthpoint;
	private Deck deck;
	private int cardSlot;
	private int bustLimit;
	private int bonusDamage;
	private boolean doubledDown;
	private LinkedList<Card> hand;
	private LinkedList<Card> discardPile;
	private LinkedList<Card> showdownHand;

	public Entity(String name, int healthpoint, int cardSlot, int bonusDamage) {
		this.name = name;
		setMaxHealthpoint(healthpoint);
		setHealthpoint(healthpoint);
		this.deck = new Deck();
		this.cardSlot = cardSlot;
		this.bustLimit = 21;
		setBonusDamage(bonusDamage);
		this.doubledDown = false;
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

	public int getMaxHealthpoint() {
		return maxHealthpoint;
	}

	public void setMaxHealthpoint(int maxHealthpoint) {
		if (maxHealthpoint < 1) {
			maxHealthpoint = 1;
		}
		this.maxHealthpoint = maxHealthpoint;
		if (this.healthpoint > this.maxHealthpoint) {
			this.healthpoint = this.maxHealthpoint;
		}
	}

	public void increaseMaxHealthpoint(int amount) {
		if (amount > 0) {
			this.maxHealthpoint += amount;
			setHealthpoint(this.healthpoint + amount);
		}
	}

	public int getHealthpoint() {
		return healthpoint;
	}

	public void setHealthpoint(int healthpoint) {
		if (healthpoint < 0) {
			this.healthpoint = 0;
		} else if (healthpoint > this.maxHealthpoint) {
			this.healthpoint = this.maxHealthpoint;
		} else {
			this.healthpoint = healthpoint;
		}
	}

	public void healPercent(double percent) {
		int healAmount = (int) Math.round(this.maxHealthpoint * percent);
		if (healAmount < 1) {
			healAmount = 1;
		}
		int before = this.healthpoint;
		setHealthpoint(this.healthpoint + healAmount);
		System.out.println(this.name + " healed " + (this.healthpoint - before) + " HP (" + this.healthpoint + "/" + this.maxHealthpoint + ").");
	}

	public void increaseDamagePercent(double percent) {
		int bonus = (int) Math.round(this.bonusDamage * percent);
		if (bonus < 2) {
			bonus = 2;
		}
		this.bonusDamage += bonus;
		System.out.println(this.name + "'s bonus damage increased by +" + bonus + " to +" + this.bonusDamage + "!");
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

	public int getBonusDamage() {
		return bonusDamage;
	}

	public void setBonusDamage(int bonusDamage) {
		this.bonusDamage = Math.max(0, bonusDamage);
	}

	public boolean isDoubledDown() {
		return doubledDown;
	}

	public void setDoubledDown(boolean doubledDown) {
		this.doubledDown = doubledDown;
	}

	public LinkedList<Card> getHand() {
		return hand;
	}

	public LinkedList<Card> getShowdownHand() {
		return showdownHand;
	}

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

	public Card drawCardToShowdown() {
		if (deck.isEmpty()) {
			addDiscardPileToDeck();
			if (deck.isEmpty()) {
				System.out.println("No more cards left in deck or discard pile.");
				return null;
			}
		}
		Card drawn = deck.draw();
		if (drawn != null) {
			showdownHand.add(drawn);
		}
		return drawn;
	}

	public void rerollHand() {
		System.out.println("\nRerolling hand...");
		discardPile.addAll(hand);
		hand.clear();
		drawCards();
	}

	public void addDiscardPileToDeck() {
		deck.moveDiscardedCardsToDeck(discardPile);
		discardPile.clear();
	}

	public void resetCardsToDeck() {
		hand.clear();
		discardPile.clear();
		showdownHand.clear();
		deck.resetForBattle();
	}

	public void viewHand() {
		System.out.println();
		for (int i = 0; i < hand.size(); i++) {
			System.out.println((i + 1) + ". " + hand.get(i));
		}
		System.out.println();
	}

	public void discardHand() {
		discardPile.addAll(hand);
		hand.clear();
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
		discardHand();
	}

	public void selectCards(List<Integer> plays) {
		for (int choice : plays) {
			int index = choice - 1;
			if (index >= 0 && index < hand.size()) {
				Card chosen = hand.get(index);
				if (!showdownHand.contains(chosen)) {
					showdownHand.add(chosen);
				}
			}
		}
		hand.removeAll(showdownHand);
		discardHand();
	}

	public int calculateHandValue(LinkedList<Card> cards) {
		int handValue = 0;
		int aces = 0;
		int luckyCards = 0;

		for (Card card : cards) {
			handValue += card.getRank().getValue();
			if (card.getRank() == Rank.ACE) {
				aces++;
			}
			if (card.getSigil() == Sigil.LUCKY) {
				luckyCards++;
			}
		}
		while (handValue > getBustLimit() && aces > 0) {
			handValue -= 10;
			aces--;
		}
		while (handValue > getBustLimit() && luckyCards > 0) {
			handValue -= Sigil.LUCKY.getValue();
			luckyCards--;
		}
		return handValue;
	}

	public HandPattern getHandPattern(LinkedList<Card> cards) {
		return HandEvaluator.evaluate(cards);
	}

	public int calculateTotalDamage(int winnerScore, int loserScore) {
		int difference = Math.max(0, winnerScore - loserScore);
		return winnerScore + difference + this.bonusDamage;
	}

	public int calculateBustDamage(int winnerScore) {
		int cappedBonus = Math.min(winnerScore, 6);
		return winnerScore + cappedBonus + this.bonusDamage;
	}

	public void clearShowdownHand() {
		discardPile.addAll(showdownHand);
		showdownHand.clear();
	}

	public abstract void playShowdownHand(Scanner sc);
}