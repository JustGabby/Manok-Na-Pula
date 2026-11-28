package Game;

import java.util.Collections;
import java.util.LinkedList;

public abstract class Entity {
	private String name;
	private int healthpoint;
	private Deck deck;
	private int cardSlot;
	private LinkedList<Card> hand;
	private LinkedList<Card> discardedHand;
	private LinkedList<Card> playHand;

	public Entity(String name, int healthpoint, int cardSlot) {
		this.name = name;
		this.healthpoint = healthpoint;
		this.deck = new Deck();
		this.cardSlot = cardSlot;
		this.discardedHand = new LinkedList<>();
		this.hand = new LinkedList<>();
		this.playHand = new LinkedList<>();
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

	// Add special card to the deck
	public void addCard(Card specialCard) {
		this.deck.addCard(specialCard);
	}

	public void gatherCardsToPlay() {
		for (int i = 1; i <= this.cardSlot; i++) {
			if (getDeck().getDeck().isEmpty()) {
				reGatherCards();
			}

			Card draw = getDeck().draw();

			if (draw != null) {
				this.hand.add(draw);
			}
		}
	}

	public void reroll() {
		discardedHand.addAll(hand);
		hand.clear();

		gatherCardsToPlay();
	}

	public void reGatherCards() {
		for (Card card : discardedHand) {
			deck.getDeck().push(card);
		}
		discardedHand.clear();
		Collections.shuffle(deck.getDeck());
	}
	
	public LinkedList<Card> getHand(){
		return hand;
	}
	
	public void viewCards() {
		for (int i = 0; i < hand.size(); i++) {
			System.out.println((i + 1) + ". " + hand.get(i));
		}
	}
	
	public void playHand(int[] plays) {
		for (int choice : plays) {
			int index = choice - 1;
			if (index >= 0 && index < hand.size()) {
				Card chosen = hand.get(index);
				if (!playHand.contains(chosen)) {
					playHand.add(chosen);
				}
			} else {
				System.out.println("Invalid card number: " + choice);
			}
		}
		hand.removeAll(playHand);
	}
	
	public void clearPlayedHand() {
		discardedHand.addAll(playHand);
		playHand.clear();
	}
	
	public LinkedList<Card> getPlayHand(){
		return this.playHand;
	}
	
	public abstract void playHand();
}
