package CardGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Deck{
	private Stack<Card> deck;
	private ArrayList<Card> discardedCards;
	
	public Deck() {
		this.deck = new Stack<>();
		this.discardedCards = new ArrayList<>();
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
			System.out.println(card.getCard());
		}
	}
	
	public Card draw() {
		return this.deck.pop();
	}
	
	public void discardedCard(Card card) {
		this.discardedCards.add(card);
	}
}
