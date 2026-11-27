package Gamev08;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Deck {
	private ArrayList<Card> permanentCards;
	private Stack<Card> drawPile;
	private String deckName;

	public Deck() {
		this.permanentCards = new ArrayList<>();
		this.drawPile = new Stack<>();
		this.deckName = "Standard Deck";
		initializeDeck();
		resetForBattle();
	}

	public Deck(String deckName, List<Card> customCards) {
		this.permanentCards = new ArrayList<>(customCards);
		this.drawPile = new Stack<>();
		this.deckName = (deckName == null || deckName.isEmpty()) ? "Custom Deck" : deckName;
		if (this.permanentCards.isEmpty()) {
			initializeDeck();
		}
		resetForBattle();
	}

	public String getDeckName() {
		return deckName;
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

	public static Deck createEnemyDeck(String familyOrName, int tier) {
		String key = (familyOrName == null) ? "" : familyOrName.trim().toLowerCase();
		ArrayList<Card> cards = new ArrayList<>();
		double sigilChance = (tier <= 1) ? 0.15 : (tier == 2) ? 0.30 : 0.45;

		String label;
		Rank[] ranks;
		Suit[] suits;
		Sigil[] sigilPool;
		int copies;

		if (key.contains("goblin")) {
			label = "Goblin Scrap Deck";
			ranks = new Rank[] { Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE, Rank.SIX, Rank.SEVEN, Rank.EIGHT, Rank.JACK };
			suits = new Suit[] { Suit.SPADES, Suit.CLUBS, Suit.DIAMONDS };
			sigilPool = new Sigil[] { Sigil.MIDAS, Sigil.SHARP };
			copies = 1;
		} else if (key.contains("orc")) {
			label = "Orc Warband Deck";
			ranks = new Rank[] { Rank.SIX, Rank.SEVEN, Rank.EIGHT, Rank.NINE, Rank.TEN, Rank.JACK, Rank.QUEEN, Rank.KING };
			suits = new Suit[] { Suit.CLUBS, Suit.SPADES, Suit.HEARTS };
			sigilPool = new Sigil[] { Sigil.SHARP, Sigil.FORTIFIED };
			copies = 1;
		} else if (key.contains("skeleton")) {
			label = "Bone Legion Deck";
			ranks = new Rank[] { Rank.TWO, Rank.FOUR, Rank.SIX, Rank.EIGHT, Rank.TEN, Rank.QUEEN, Rank.ACE };
			suits = new Suit[] { Suit.SPADES, Suit.CLUBS };
			sigilPool = new Sigil[] { Sigil.FORTIFIED, Sigil.LUCKY };
			copies = 2;
		} else if (key.contains("slime")) {
			label = "Ooze Swarm Deck";
			ranks = new Rank[] { Rank.ACE, Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE, Rank.SIX };
			suits = Suit.values();
			sigilPool = new Sigil[] { Sigil.LUCKY, Sigil.VAMPIRIC };
			copies = 1;
		} else if (key.contains("beast") || key.contains("wolf")) {
			label = "Feral Pack Deck";
			ranks = new Rank[] { Rank.THREE, Rank.FIVE, Rank.SEVEN, Rank.NINE, Rank.JACK, Rank.KING };
			suits = new Suit[] { Suit.HEARTS, Suit.CLUBS, Suit.SPADES };
			sigilPool = new Sigil[] { Sigil.VAMPIRIC, Sigil.SHARP };
			copies = 1;
		} else if (key.contains("insect") || key.contains("scarab")) {
			label = "Chitin Hive Deck";
			ranks = new Rank[] { Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE, Rank.SEVEN, Rank.EIGHT };
			suits = new Suit[] { Suit.DIAMONDS, Suit.CLUBS };
			sigilPool = new Sigil[] { Sigil.FORTIFIED, Sigil.LUCKY };
			copies = 2;
		} else if (key.contains("imp")) {
			label = "Hellfire Trickster Deck";
			ranks = new Rank[] { Rank.ACE, Rank.TWO, Rank.THREE, Rank.SEVEN, Rank.NINE, Rank.JACK, Rank.QUEEN, Rank.KING };
			suits = new Suit[] { Suit.HEARTS, Suit.DIAMONDS };
			sigilPool = new Sigil[] { Sigil.LUCKY, Sigil.SHARP };
			copies = 2;
		} else if (key.contains("golem") || key.contains("colossus")) {
			label = "Runestone Bulwark Deck";
			ranks = new Rank[] { Rank.FIVE, Rank.SEVEN, Rank.EIGHT, Rank.NINE, Rank.TEN, Rank.KING };
			suits = new Suit[] { Suit.CLUBS, Suit.DIAMONDS };
			sigilPool = new Sigil[] { Sigil.FORTIFIED };
			copies = 2;
		} else if (key.contains("wraith")) {
			label = "Soulreaper Deck";
			ranks = new Rank[] { Rank.ACE, Rank.FOUR, Rank.SIX, Rank.EIGHT, Rank.TEN, Rank.QUEEN, Rank.KING };
			suits = new Suit[] { Suit.SPADES, Suit.HEARTS };
			sigilPool = new Sigil[] { Sigil.VAMPIRIC, Sigil.LUCKY };
			copies = 2;
		} else if (key.contains("dragon")) {
			label = "Draconic Hoard Deck";
			ranks = new Rank[] { Rank.SEVEN, Rank.EIGHT, Rank.NINE, Rank.TEN, Rank.JACK, Rank.QUEEN, Rank.KING, Rank.ACE };
			suits = new Suit[] { Suit.HEARTS, Suit.DIAMONDS, Suit.SPADES };
			sigilPool = new Sigil[] { Sigil.SHARP, Sigil.MIDAS, Sigil.FORTIFIED };
			copies = 1;
		} else {
			label = "Wild Deck";
			ranks = Rank.values();
			suits = Suit.values();
			sigilPool = Sigil.values();
			copies = 1;
		}

		for (int c = 0; c < copies; c++) {
			for (Rank rank : ranks) {
				for (Suit suit : suits) {
					Sigil sigil = (Rng.nextDouble() < sigilChance)
							? sigilPool[Rng.nextInt(sigilPool.length)]
							: null;
					cards.add(new Card(rank, suit, sigil));
				}
			}
		}

		return new Deck(label, cards);
	}

	private void printDeckList(ArrayList<Card> cards) {
		for (int i = 0; i < cards.size(); i++) {
			System.out.println((i + 1) + ". " + cards.get(i));
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