package Gamev07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class Shop {
	private static final int REMOVE_CARD_COST = 8;
	private static final int SIGIL_COST = 10;
	private static final int PACK_COST = 14;

	private ArrayList<Item> itemStock;
	private HashMap<Item, Integer> itemPrices;

	public Shop() {
		this.itemStock = new ArrayList<>();
		this.itemPrices = new HashMap<>();
		for (Item item : Item.values()) {
			itemPrices.put(item, item.getPrice());
		}
		restockItems();
	}

	private int incrementPrice(int price) {
		return price + 1 + (int) (price * 0.20);
	}

	public void restockItems() {
		itemStock.clear();
		ArrayList<Item> pool = new ArrayList<>();
		Collections.addAll(pool, Item.values());
		Rng.shuffle(pool);
		for (int i = 0; i < 3 && i < pool.size(); i++) {
			itemStock.add(pool.get(i));
		}
	}

	public void enterShop(Player player, Scanner sc) {
		boolean shopping = true;

		while (shopping) {
			System.out.println("\n================ MERCHANT SHOP ================");
			System.out.println("Gold: " + player.getGold() + " | HP: " + player.getHealthpoint() + "/" + player.getMaxHealthpoint() + " | Deck Size: " + player.getDeck().size());
			System.out.println("1. Permanently Remove 1 of 5 Random Cards (" + REMOVE_CARD_COST + " Gold)");
			System.out.println("2. Inscribe Sigil on 1 of 5 Random Cards (" + SIGIL_COST + " Gold)");
			System.out.println("3. Buy Card Pack - Pick 1 of 3 Random Cards (" + PACK_COST + " Gold)");
			System.out.println("4. Buy Items");
			System.out.println("5. View Current Deck");
			System.out.println("6. Leave Shop");

			int choice = Input.readInt(sc, "Choice: ");

			switch (choice) {
			case 1:
				removeCardPermanently(player, sc);
				break;
			case 2:
				inscribeSigilMenu(player, sc);
				break;
			case 3:
				buyCardPack(player, sc);
				break;
			case 4:
				buyItemsMenu(player, sc);
				break;
			case 5:
				player.getDeck().viewDeckMenu(sc);
				break;
			case 6:
				System.out.println("You leave the shop.");
				shopping = false;
				break;
			default:
				System.out.println("Invalid choice!");
				break;
			}
		}
	}

	private void removeCardPermanently(Player player, Scanner sc) {
		Deck deck = player.getDeck();
		if (deck.size() <= 10) {
			System.out.println("Your deck cannot go below 10 cards!");
			return;
		}
		if (player.getGold() < REMOVE_CARD_COST) {
			System.out.println("Not enough gold!");
			return;
		}

		ArrayList<Card> candidates = deck.getRandomSubset(5);
		ArrayList<String> labels = new ArrayList<>();
		for (Card c : candidates) {
			labels.add(c.toString());
		}

		int idx = Input.pickOption(sc, "CARD PURGE", "Choose 1 of 5 random cards from your deck to permanently remove:", labels, "Cancel");
		if (idx < 0) {
			return;
		}

		Card chosen = candidates.get(idx);
		if (deck.removeCard(chosen)) {
			player.setGold(player.getGold() - REMOVE_CARD_COST);
			System.out.println("Permanently removed " + chosen + " from your deck!");
		}
	}

	private void inscribeSigilMenu(Player player, Scanner sc) {
		Deck deck = player.getDeck();
		if (deck.isEmpty()) {
			System.out.println("Your deck is empty!");
			return;
		}
		if (player.getGold() < SIGIL_COST) {
			System.out.println("Not enough gold!");
			return;
		}

		ArrayList<Card> candidates = deck.getRandomSubset(5);
		Sigil offeredSigil = Sigil.getRandomSigil();
		ArrayList<String> labels = new ArrayList<>();
		for (Card c : candidates) {
			if (c.hasSigil()) {
				labels.add(c.toString() + " (Replaces [" + c.getSigil().getDisplayName() + "])");
			} else {
				labels.add(c.toString());
			}
		}

		String subtitle = "Sigil Offered: [" + offeredSigil.getDisplayName() + "] - " + offeredSigil.getDescription()
				+ "\nChoose 1 of 5 random cards from your deck to inscribe:";
		int idx = Input.pickOption(sc, "SIGIL ALTAR", subtitle, labels, "Cancel");
		if (idx < 0) {
			return;
		}

		Card chosen = candidates.get(idx);
		Sigil previous = chosen.getSigil();
		chosen.setSigil(offeredSigil);
		player.setGold(player.getGold() - SIGIL_COST);

		if (previous != null) {
			System.out.println("Replaced [" + previous.getDisplayName() + "] with [" + offeredSigil.getDisplayName() + "] on " + chosen + "!");
		} else {
			System.out.println("Inscribed [" + offeredSigil.getDisplayName() + "] onto " + chosen + "!");
		}
	}

	private void buyCardPack(Player player, Scanner sc) {
		if (player.getGold() < PACK_COST) {
			System.out.println("Not enough gold!");
			return;
		}
		player.setGold(player.getGold() - PACK_COST);

		ArrayList<Card> pack = new ArrayList<>();
		ArrayList<String> labels = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			Card pulled = Deck.generateRandomCard(0.35);
			pack.add(pulled);
			labels.add(pulled.toString());
		}

		int idx = Input.pickOption(sc, "OPENED CARD PACK", "Pick 1 card to add to your deck:", labels, "Skip");
		if (idx < 0) {
			System.out.println("Skipped card pack selection.");
			return;
		}

		Card chosen = pack.get(idx);
		player.getDeck().addCard(chosen);
		player.getDeck().shuffle();
		System.out.println("Added " + chosen + " to your deck!");
	}

	private void buyItemsMenu(Player player, Scanner sc) {
		if (itemStock.isEmpty()) {
			System.out.println("No items left in stock!");
			return;
		}

		ArrayList<String> labels = new ArrayList<>();
		for (Item item : itemStock) {
			int price = itemPrices.get(item);
			labels.add("[" + item.getRarity().getDisplayName() + "] " + item.getItemName() + " (" + price + " Gold) - " + item.getDescription());
		}

		int idx = Input.pickOption(sc, "SHOP ITEMS", "Gold: " + player.getGold(), labels, "Back");
		if (idx < 0) {
			return;
		}

		Item selected = itemStock.get(idx);
		int price = itemPrices.get(selected);
		if (player.getGold() < price) {
			System.out.println("Not enough gold!");
			return;
		}

		player.setGold(player.getGold() - price);
		player.addItem(selected);
		itemPrices.put(selected, incrementPrice(price));
		itemStock.remove(idx);
		System.out.println("Bought [" + selected.getRarity().getDisplayName() + "] " + selected.getItemName() + "! Added to inventory.");
	}
}