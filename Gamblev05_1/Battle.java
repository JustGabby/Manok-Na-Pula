package Gamev05_1;

import java.util.ArrayList;
import java.util.Scanner;

public class Battle {
	private Player player;
	private Enemy enemy;
	private boolean allInTriggered;

	public Battle(Player player, Enemy enemy) {
		this.player = player;
		this.enemy = enemy;
	}

	public boolean startBattle(Scanner sc) {
		allInTriggered = false;
		player.onBattleStart();
		while (player.getHealthpoint() > 0 && enemy.getHealthpoint() > 0) {
			System.out.println();
			enemy.displayEnemyInfo();
			System.out.println();
			System.out.println("Your ♥ HP: " + player.getHealthpoint() + "/" + player.getMaxHealthpoint() + "  |  🎲 Rerolls: " + player.getReroll() + "  |  ⚔ Bonus Damage: +" + player.getBonusDamage());
			System.out.println("");
			System.out.println("1. ⊕ Draw");
			System.out.println("2. ▣ Inventory (" + player.getInventory().size() + ")");
			System.out.println("3. ♠ View Deck (" + player.getDeck().size() + ")");
			System.out.println("4. ☠ Surrender");

			int battleChoice = Input.readInt(sc, "Choice: ");

			switch (battleChoice) {
			case 1:
				player.playerTurn(sc);
				enemy.enemyTurn();
				showdown();
				break;
			case 2:
				openInventory(sc);
				break;
			case 3:
				player.getDeck().viewDeckMenu(sc);
				break;
			case 4:
				System.out.println("\nYou surrendered. Game Over.");
				player.setHealthpoint(0);
				player.endArtifactEffects();
				return false;
			default:
				System.out.println("Invalid choice!");
				break;
			}
		}

		if (player.getHealthpoint() <= 0) {
			System.out.println("\nYou lose.");
			player.endArtifactEffects();
			return false;
		}

		System.out.println();
		showWinBanner();
		applyVictoryRewards();
		player.resetCardsToDeck();
		return true;
	}

	private void openInventory(Scanner sc) {
		ArrayList<Item> items = player.getInventory();
		if (items.isEmpty()) {
			System.out.println("Your inventory is empty!");
			return;
		}
		ArrayList<String> labels = new ArrayList<>();
		for (Item item : items) {
			labels.add(item.getItemName() + " - " + item.getDescription());
		}
		int idx = Input.pickOption(sc, "INVENTORY", "Select an item to use:", labels, "Back");
		if (idx >= 0) {
			player.useItem(idx);
		}
	}

	private void showdown() {
		int playerPoint = player.calculateHandValue(player.getShowdownHand());
		int enemyPoint = enemy.calculateHandValue(enemy.getShowdownHand());

		System.out.println();
		displayShowdown(player);
		System.out.println();
		displayShowdown(enemy);
		System.out.println();

		boolean playerBusted = playerPoint > player.getBustLimit();
		boolean enemyBusted = enemyPoint > enemy.getBustLimit();

		if (playerBusted && enemyBusted) {
			System.out.println("Both busted! Tie round.");
		} else if (!playerBusted && !enemyBusted && playerPoint == enemyPoint) {
			System.out.println("Tie! No damage dealt.");
		} else if (playerBusted || (!enemyBusted && enemyPoint > playerPoint)) {
			applyRoundDamage(enemy, player, enemyPoint, playerPoint, playerBusted);
		} else {
			applyRoundDamage(player, enemy, playerPoint, enemyPoint, enemyBusted);
		}

		player.setDoubledDown(false);
		enemy.setDoubledDown(false);
		player.clearShowdownHand();
		enemy.clearShowdownHand();
	}

	private void applyRoundDamage(Entity winner, Entity loser, int winnerPoint, int loserPoint, boolean loserBusted) {
		int baseTotal = loserBusted
				? winner.calculateBustDamage(winnerPoint)
				: winner.calculateTotalDamage(winnerPoint, loserPoint);
		int bonus = loserBusted
				? Math.min(winnerPoint, 6)
				: Math.max(0, winnerPoint - loserPoint);

		int sharpBonus = 0;
		int vampiricHeal = 0;
		int midasGold = 0;
		for (Card c : winner.getShowdownHand()) {
			if (c.getSigil() == Sigil.SHARP) {
				sharpBonus += Sigil.SHARP.getValue();
			} else if (c.getSigil() == Sigil.VAMPIRIC) {
				vampiricHeal += Sigil.VAMPIRIC.getValue();
			} else if (c.getSigil() == Sigil.MIDAS) {
				midasGold += Sigil.MIDAS.getValue();
			}
		}

		int shieldReduction = 0;
		for (Card c : loser.getShowdownHand()) {
			if (c.getSigil() == Sigil.FORTIFIED) {
				shieldReduction += Sigil.FORTIFIED.getValue();
			}
		}

		int doubleDownDamageBonus = 0;
		int doubleDownGoldBonus = 0;
		if (winner.isDoubledDown()) {
			doubleDownDamageBonus = Math.max(1, baseTotal / 2);
			if (winner == player) {
				doubleDownGoldBonus = 5;
			}
		}

		boolean playerWon = (winner == player);
		
		int looseStringBonus = 0;
		if (playerWon && player.hasLooseString() && winnerPoint() >= 21 && winnerPoint <=24) {
			looseStringBonus = 20;
		}
		
		int faceCards = 0;
		if (playerWon && player.hasHappyFaces()) {
			for (Card c : winner.getShowdownHand()) {
				Rank r : c.getRank();
				if (r ==Rank.jack || r == Rank.QUEEN || r == Rank.KING) {
					faceCards++;
				}
			}
		}
		
		double artifactMult = playerWon ? player.getDamageMultiplier() : 1.0;
		if (faceCards > 0) {
			artifactMult *=1 + 0.017 * faceCards;
		}
		
		int artifactGuard = (loser == player) ? player.getDamageReduction() : 0;
		
		boolean allInNow = false;
		if (playerWon &&p player.hasAllIn() && winnerPoint > 18 && !allInTriggered) {
			allInTriggered = true;
			allInNow = true;
		}

		HandPattern pattern = winner.getHandPattern(winner.getShowdownHand());
		int multipliedDamage = (int) Math.round((baseTotal + sharpBonus + doubleDownDamageBonus) * pattern.getMultiplier());

		int finalDamage = Math.max(1, multipliedDamage - shieldReduction);
		loser.setHealthpoint(loser.getHealthpoint() - finalDamage);

		String outcome = loserBusted
				? (loser == player ? "You busted!" : loser.getName() + " busted!")
				: (winner == player ? "Hand won!" : "Hand lost!");
		String verb = (winner == player) ? "Dealt " : "Took ";
		String bonusType = loserBusted ? " bust bonus" : "";
		String hpLabel = (loser == player) ? "HP: " : loser.getName() + " HP: ";

		String breakdown = winnerPoint + " + " + bonus + bonusType;
		if (winner.getBonusDamage() > 0) {
			breakdown += " + " + winner.getBonusDamage() + " Bonus";
		}
		if (doubleDownDamageBonus > 0) {
			breakdown += " + " + doubleDownDamageBonus + " Double Down";
		}
		if (sharpBonus > 0) {
			breakdown += " + " + sharpBonus + " Sharp";
		}
		if (looseStringBonus > 0) {
			breakdown += " + " + looseStringBonus + " Loose String";
		}
		if (pattern.hasBonus()) {
			breakdown = "(" + breakdown + ") x " + pattern.getMultiplier() + " [" + pattern.getLabel() + "]";
		}
		if (artifactMult ! = 1.0) {
			breakdown = "(" + breakdown + ") x " + fmt(artifactMult) + " [Artifacts]";
		}
		if (shieldReduction > 0) {
			breakdown += " - " + shieldReduction + " Fortified";
		}
		if (artifactGuard > 0) {
			breakdown += " - " + artifactGuard + " Artifact Guard";
		}

		System.out.println(outcome + " " + verb + finalDamage + " damage (" + breakdown + ").");
		if (vampiricHeal > 0) {
			winner.setHealthpoint(winner.getHealthpoint() + vampiricHeal);
			System.out.println("[Vampiric Sigil] " + winner.getName() + " healed " + vampiricHeal + " HP!");
		}
		int faceGold = faceCards * 2;
		int totalBonusGold = midasGold + doubleDownGoldBonus;
		if (totalBonusGold > 0 && winner == player) {
			int boostedGold = (int) Math.round(totalBonusGold * (1 + player.getGoldBonus());
			player.addGold(totalBonusGold);
			if (doubleDownGoldBonus > 0) {
				System.out.println("[Double Down] Earned +" + doubleDownGoldBonus + " bonus gold!");
			}
			if (midasGold > 0) {
				System.out.println("[Midas Sigil] Earned +" + midasGold + " bonus gold!");
			}
			if (faceGold > 0) {
				System.out.println("[Happy Faces]" + faceCards + "face card(s): " + faceGold + " bonus gold!");
			}
			if (boostedGold > totalBonusGold) {
				System.out.println("[Greed Curse] " + (boostedGold - totalBonusGold) + " extra gold!"); 
			}
		}
		if (allInNow) {
			System.out.println("[All In] Strong winning hand! Victory gold willget 50%.");
		}
		System.out.println(hpLabel + loser.getHealthpoint() + "/" + loser.getMaxHealthpoint());
	}

	private void displayShowdown(Entity entity) {
		if (!entity.getShowdownHand().isEmpty()) {
			if (entity == player) {
				System.out.println("Played Hand:");
			} else {
				System.out.println("Enemy Hand:");
			}
			for (Card card : entity.getShowdownHand()) {
				System.out.println(card.toString());
			}
			int total = entity.calculateHandValue(entity.getShowdownHand());
			HandPattern pattern = entity.getHandPattern(entity.getShowdownHand());
			String patternTag = pattern.hasBonus() ? " [" + pattern.getLabel() + " " + pattern.getMultiplier() + "x]" : "";

			if (total > entity.getBustLimit()) {
				System.out.println("Total Hand Value: " + total + " (BUST!)");
			} else {
				System.out.println("Total Hand Value: " + total + patternTag);
			}
		} else {
			System.out.println("No hand played!");
		}
	}
	
	private static String fmt(double d) {
		return d == Math.floor(d) ? String.valueOf(int) d) : String.format("%.2f", d);
	}
	
	private void showWinBanner() {
		System.out.println("\n__   _____  _   _  __        _____  _   _ \n"
				+ "\\ \\ / / _ \\| | | | \\ \\      / / _ \\| \\ | |\n"
				+ " \\ V / | | | | | |  \\ \\ /\\ / / | | |  \\| |\n"
				+ "  | || |_| | |_| |   \\ V  V /| |_| | |\\  |\n"
				+ "  |_| \\___/ \\___/     \\_/\\_/  \\___/|_| \\_|");
	}

	private void applyVictoryRewards() {
		int reward = 5 * enemy.getLevel();
		double goldMult = 1 + player.getGoldBonus();
		if (player.hasAllIn() && allInTriggered) {
			goldMult += 0.5;
		}
		int boostedReward = (int) Math.round(reward * goldMult);
		player.addGold(reward);
		player.restoreRerolls();
		System.out.println("Earned " + reward + " gold!");
		player.endArtifactEffects();
		player.healPercent(0.05);
	}
}