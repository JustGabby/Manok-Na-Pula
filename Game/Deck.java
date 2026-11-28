package Game;

import java.util.Collections;
import java.util.Stack;

public class Deck {
	private Stack<Card> deck;
	private Card card;
	
	public Deck() {
		this.deck = new Stack<>();
		initializeDeck();
		Collections.shuffle(deck);
	}
	
	public void initializeDeck() {
		for (Rank rank : Rank.values()) {
			for(Suits suit: Suits.values()) {
				Card card = new Card(rank, suit);
				addCard(card);
			}
		}
	}
	
	public void addCard(Card card) {
		this.deck.push(card);
	}
	
	public Stack<Card> getDeck(){
		return this.deck;
	}
	
	public void viewDeck() {
		for(Card card: this.deck) {
			System.out.println(card.toString());
		}
	}
	
	public Card getCard() {
		return this.card;
	}
	
	public Card draw() {
		if (this.deck.isEmpty()) {
			return null;
		} else {
			return this.deck.pop();
		}
	}
}
