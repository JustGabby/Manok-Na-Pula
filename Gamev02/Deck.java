package Gamev02;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

public class Deck {
	private Stack<Card> deck;
	
	public Deck() {
		this.deck = new Stack<>();
		initializeDeck();
		shuffle();
	}
	
	private void initializeDeck() {
		for (Rank rank : Rank.values()) {
			for(Suit suit: Suit.values()) {
				addCard(new Card(rank, suit));
			}
		}
	}
	
	public boolean isEmpty() {
		return deck.isEmpty();
	}
	
	public void shuffle() {
		Collections.shuffle(deck);
	}
	public void moveDiscardedCardsToDeck(LinkedList<Card> discardPile)  {
		for (Card card : discardPile) {
			deck.push(card);
		}
		shuffle();
	}
	public void addCard(Card card) {
		deck.push(card);
	}
	
	public void viewDeck() {
		for(Card card: deck) {
			System.out.println(card.toString());
		}
	}
	
	public Card draw() {
		if (deck.isEmpty()) {
			return null;
		} else {
			return deck.pop();
		}
	}
}
