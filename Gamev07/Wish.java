package Gamev07;

import java.util.Scanner;

public class Wish {

	public void wishInterface(Player player, Scanner sc) {
		boolean wishRunning = true;

		while (wishRunning) {
			System.out.println("\n==== WISH ALTAR ====");
			System.out.println("Gold: " + player.getGold() + " | Deck Size: " + player.getDeck().size() + " | Inventory: " + player.getInventory().size());
			System.out.println("1. " + Bundle.STANDARD.getName());
			System.out.println("2. " + Bundle.EPIC.getName());
			System.out.println("3. " + Bundle.LEGENDARY.getName());
			System.out.println("4. Back");

			int wishChoice = Input.readInt(sc, "Choice: ");

			switch (wishChoice) {
			case 1:
				bundleInterface(player, sc, Bundle.STANDARD);
				break;
			case 2:
				bundleInterface(player, sc, Bundle.EPIC);
				break;
			case 3:
				bundleInterface(player, sc, Bundle.LEGENDARY);
				break;
			case 4:
				wishRunning = false;
				break;
			default:
				System.out.println("Invalid choice!");
				break;
			}
		}
	}

	public void bundleInterface(Player player, Scanner sc, Bundle bundle) {
		boolean inBundle = true;

		while (inBundle) {
			int singleCost = bundle.getCost();
			int tenPullCost = singleCost * 10;

			System.out.println("\n=== " + bundle.getName().toUpperCase() + " ===");
			System.out.println("Your Gold: " + player.getGold() + "G");
			System.out.println("1. 1x pull (" + singleCost + "G)");
			System.out.println("2. 10x pull (" + tenPullCost + "G)");
			System.out.println("3. Back");

			int pullChoice = Input.readInt(sc, "Choice: ");

			switch (pullChoice) {
			case 1:
				executeWish(player, bundle, 1, singleCost);
				break;
			case 2:
				executeWish(player, bundle, 10, tenPullCost);
				break;
			case 3:
				inBundle = false;
				break;
			default:
				System.out.println("Invalid choice!");
				break;
			}
		}
	}

	private void executeWish(Player player, Bundle bundle, int count, int totalCost) {
		if (player.getGold() < totalCost) {
			System.out.println("Not enough Gold! You need " + totalCost + "G.");
			return;
		}

		player.setGold(player.getGold() - totalCost);
		System.out.println("\n✧ Wishing " + count + "x on " + bundle.getName() + " ✧");

		for (int i = 0; i < count; i++) {
			int rarity = bundle.pull();
			grantReward(player, rarity);
		}
	}

	private void grantReward(Player player, int rarity) {
		if (rarity == 1) {
			Rank[] ranks = Rank.values();
			Suit[] suits = Suit.values();

			Rank randomRank = ranks[Rng.range(0, ranks.length - 1)];
			Suit randomSuit = suits[Rng.range(0, suits.length - 1)];
			Sigil sigil = (Rng.nextDouble() < 0.15) ? Sigil.LUCKY : null;

			Card pulledCard = new Card(randomRank, randomSuit, sigil, false);
			player.getDeck().addCard(pulledCard);
			System.out.println("★ [1-Star Card] Added to Deck: " + pulledCard);
		} else {
			
			Artifacts pulledArtifact = Artifacts.sortByRarity(rarity);
			player.addArtifact(pulledArtifact);
			
			System.out.println("★ [" + rarity + "-Star Artifact] Added to Artifacts: " + pulledArtifact.getName() + " (" + pulledArtifact.getEffect() + ")");
		}
	}
}