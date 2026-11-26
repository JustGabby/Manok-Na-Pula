package Gamev07;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class Deck {
	private ArrayList<Card> permanentCards;
	private Stack<Card> drawPile;

	public Deck() {
		this.permanentCards = new ArrayList<>();
		this.drawPile = new Stack<>();
		initializeDeck();
		resetForBattle();
	}

	private void initializeDeck() {
		for (Rank rank : Rank.values()) {
			for (Suit suit : Suit.values()) {
				permanentCards.add(new Card(rank, suit));
			}
		}
	}

	public void resetForBattle() {
		drawPile.clear();
		for (Card card : permanentCards) {
			drawPile.push(card);
		}
		shuffle();
	}

	public boolean isEmpty() {
		return drawPile.isEmpty();
	}

	public int size() {
		return permanentCards.size();
	}

	public void shuffle() {
		Rng.shuffle(drawPile);
	}

	public void moveDiscardedCardsToDeck(LinkedList<Card> discardPile) {
		for (Card card : discardPile) {
			drawPile.push(card);
		}
		shuffle();
	}

	public void addCard(Card card) {
		permanentCards.add(card);
		drawPile.push(card);
	}

	public boolean removeCard(Card card) {
		boolean removed = permanentCards.remove(card);
		if (removed) {
			drawPile.remove(card);
		}
		return removed;
	}

	public ArrayList<Card> getRandomSubset(int count) {
		ArrayList<Card> pool = new ArrayList<>(permanentCards);
		Rng.shuffle(pool);
		int limit = Math.min(count, pool.size());
		return new ArrayList<>(pool.subList(0, limit));
	}

	public static Card generateRandomCard(double sigilChance) {
		Rank[] ranks = Rank.values();
		Suit[] suits = Suit.values();
		Rank randomRank = ranks[Rng.nextInt(ranks.length)];
		Suit randomSuit = suits[Rng.nextInt(suits.length)];
		Sigil sigil = (Rng.nextDouble() < sigilChance) ? Sigil.getRandomSigil() : null;
		return new Card(randomRank, randomSuit, sigil);
	}

	public void viewDeck() {
		for (int i = 0; i < permanentCards.size(); i++) {
			System.out.println((i + 1) + ". " + permanentCards.get(i).toString());
		}
	}

	private void printDeckList(ArrayList<Card> cards) {
		for (int i = 0; i < cards.size(); i++) {
			System.out.println((i + 1) + ". " + cards.get(i).toString());
		}
	}

	public void viewDeckMenu(Scanner sc) {
		ArrayList<Card> displayList = new ArrayList<>(permanentCards);
		String sortMode = "Default";

		while (true) {
			System.out.println("\n=== YOUR DECK (" + displayList.size() + " cards | Sort: " + sortMode + ") ===");
			System.out.println();
			printDeckList(displayList);
			System.out.println();
			System.out.println("1. Sort by Rank");
			System.out.println("2. Sort by Suit");
			System.out.println("3. Sort by Sigil");
			System.out.println("0. Back");

			int choice = Input.readInt(sc, "Choice: ");

			switch (choice) {
			case 1:
				System.out.println("Before sorting:");
				printDeckList(displayList);
				displayList.sort(Comparator.comparing(Card::getRank).thenComparing(Card::getSuit));
				sortMode = "Rank";
				System.out.println("After sorting by Rank:");
				printDeckList(displayList);
				break;
			case 2:
				System.out.println("Before sorting:");
				printDeckList(displayList);
				displayList.sort(Comparator.comparing(Card::getSuit).thenComparing(Card::getRank));
				sortMode = "Suit";
				System.out.println("After sorting by Suit:");
				printDeckList(displayList);
				break;
			case 3:
				System.out.println("Before sorting:");
				printDeckList(displayList);
				displayList.sort(Comparator.comparing((Card c) -> !c.hasSigil())
						.thenComparing(c -> c.hasSigil() ? c.getSigil().getDisplayName() : "")
						.thenComparing(Card::getRank)
						.thenComparing(Card::getSuit));
				sortMode = "Sigil";
				System.out.println("After sorting by Sigil:");
				printDeckList(displayList);
				break;
			case 0:
				return;
			default:
				System.out.println("Invalid choice!");
				break;
			}
		}
	}

	public Card draw() {
		if (drawPile.isEmpty()) {
			return null;
		}
		return drawPile.pop();
	}
}