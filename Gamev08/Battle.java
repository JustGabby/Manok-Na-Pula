package Gamev08;

import java.util.ArrayList;
import java.util.Scanner;

public class Battle {
	private Player player;
	private Enemy enemy;
	private final Terrain terrain;
	private int turnsTaken = 0;

	public Battle(Player player, Enemy enemy) {
		this.player = player;
		this.enemy = enemy;
		this.terrain = Terrain.forStage(player.getEnemiesDefeated() + 1);
	}

	public boolean startBattle(Scanner sc) {
		System.out.println("Terrain: " + terrain.getName() + " - " + terrain.getDescription());
		while (player.getHealthpoint() > 0 && enemy.getHealthpoint() > 0) {
			System.out.println();
			enemy.displayEnemyInfo();
			System.out.println();
			String terrainStatus;
			if (terrain == Terrain.SWAMP) {
				terrainStatus = "🌿 Fortified sigil value +" + terrain.getFortifiedBonus();
			} else if (terrain == Terrain.FOREST) {
				terrainStatus = "🌲 Enemy winning hands deal +" + terrain.getEnemyBonusDamage() + " damage";
			} else if (terrain == Terrain.RUINS) {
				terrainStatus = "🏆 Winning hands grant +" + terrain.getGoldBonus() + " gold";
			} else if (player.getHeatShieldTicks() > 0) {
				terrainStatus = "☀ Heat blocked: " + player.getHeatShieldTicks() + " tick(s)";
			} else {
				terrainStatus = "☀ Heat triggers in " + terrain.getTurnsUntilTrigger(turnsTaken) + " turn(s)";
			}
			System.out.println("Your ♥ HP: " + player.getHealthpoint() + "/" + player.getMaxHealthpoint() + "  |  🎲 Rerolls: " + player.getReroll() + "  |  ⚔ Bonus Damage: +" + player.getTotalBonusDamage() + "  |  🔥 Win Streak: " + player.getHandWinStreak() + "  |  " + terrainStatus);
			System.out.println();
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
				if (player.getHealthpoint() > 0 && enemy.getHealthpoint() > 0) {
					applyTerrainEffect();
				}
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
				player.clearTemporaryBattleEffects();
				return false;
			default:
				System.out.println("Invalid choice!");
				break;
			}
		}

		if (player.getHealthpoint() <= 0) {
			System.out.println("\nYou lose.");
			player.clearTemporaryBattleEffects();
			return false;
		}

		showWinBanner();
		applyVictoryRewards();
		player.clearTemporaryBattleEffects();
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
			labels.add("[" + item.getRarity().getDisplayName() + "] " + item.getItemName() + " - " + item.getDescription());
		}
		int idx = Input.pickOption(sc, "INVENTORY", "Select an item to use (lasts this battle):", labels, "Back");
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
			player.resetHandWinStreak();
		} else if (!playerBusted && !enemyBusted && playerPoint == enemyPoint) {
			System.out.println("Tie! No damage dealt.");
			player.resetHandWinStreak();
		} else if (playerBusted || (!enemyBusted && enemyPoint > playerPoint)) {
			applyRoundDamage(enemy, player, enemyPoint, playerPoint, playerBusted);
		} else {
			applyRoundDamage(player, enemy, playerPoint, enemyPoint, enemyBusted);
		}

		player.setDrewCard(false);
		enemy.setDrewCard(false);
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
		int activeBonusDamage = (winner == player) ? player.getTotalBonusDamage() : winner.getBonusDamage();

		int sharpBonus = 0;
		int vampiricPercent = 0;
		int midasGold = 0;
		int sigilMultiplier = (winner == player && player.isDoubleSigils()) ? 2 : 1;
		for (Card c : winner.getShowdownHand()) {
			if (c.getSigil() == Sigil.SHARP) {
				sharpBonus += Sigil.SHARP.getValue() * sigilMultiplier;
			} else if (c.getSigil() == Sigil.VAMPIRIC) {
				vampiricPercent += Sigil.VAMPIRIC.getValue() * sigilMultiplier;
			} else if (c.getSigil() == Sigil.MIDAS) {
				midasGold += Sigil.MIDAS.getValue() * sigilMultiplier;
			}
		}

		int shieldReduction = 0;
		int swampFortifiedBonus = 0;
		for (Card c : loser.getShowdownHand()) {
			if (c.getSigil() == Sigil.FORTIFIED) {
				int fortifiedValue = Sigil.FORTIFIED.getValue();
				if (loser == player) {
					fortifiedValue += terrain.getFortifiedBonus();
					swampFortifiedBonus += terrain.getFortifiedBonus();
				}
				shieldReduction += fortifiedValue;
			}
		}

		double actionMultiplier = 1.0;
		if (winner.isDrewCard()) {
			actionMultiplier *= 1.2;
		}
		if (winner.isDoubledDown()) {
			actionMultiplier *= 1.5;
		}
		int doubleDownGoldBonus = (winner.isDoubledDown() && winner == player) ? 5 : 0;

		HandPattern pattern = HandEvaluator.evaluate(winner.getShowdownHand());
		double combinedMultiplier = pattern.getMultiplier() * actionMultiplier;
		if (winner == player) {
			combinedMultiplier *= player.getArtifactDamageMultiplier();
		}

		int multipliedDamage = (int) Math.round((baseTotal + sharpBonus) * combinedMultiplier);
		int playerGuard = (loser == player) ? player.getTotalDamageReduction() : 0;
		int forestEnemyBonus = (terrain == Terrain.FOREST && winner == enemy) ? terrain.getEnemyBonusDamage() : 0;
		int finalDamage = Math.max(1, multipliedDamage + forestEnemyBonus - shieldReduction - playerGuard);
		loser.setHealthpoint(loser.getHealthpoint() - finalDamage);

		int streakGold = 0;
		if (winner == player) {
			player.increaseHandWinStreak();
			streakGold = Math.min(5, player.getHandWinStreak());
			player.addGold(streakGold);
		} else if (loser == player) {
			player.resetHandWinStreak();
		}

		String outcome = loserBusted
				? (loser == player ? "You busted!" : loser.getName() + " busted!")
				: (winner == player ? "Hand won!" : "Hand lost!");
		String verb = (winner == player) ? "Dealt " : "Took ";
		String bonusType = loserBusted ? " bust bonus" : "";
		String hpLabel = (loser == player) ? "HP: " : loser.getName() + " HP: ";

		String breakdown = winnerPoint + " + " + bonus + bonusType;
		if (activeBonusDamage > 0) {
			breakdown += " + " + activeBonusDamage + " Bonus";
		}
		if (sharpBonus > 0) {
			breakdown += " + " + sharpBonus + " Sharp";
		}
		if (pattern.hasBonus()) {
			breakdown = "(" + breakdown + ") x " + pattern.getMultiplier() + " [" + pattern.getLabel() + "]";
		}
		if (winner.isDrewCard()) {
			breakdown = "(" + breakdown + ") x 1.2 [Draw]";
		}
		if (winner.isDoubledDown()) {
			breakdown = "(" + breakdown + ") x 1.5 [Double Down]";
		}
		if (winner == player && player.getArtifactDamageMultiplier() > 1.0) {
			breakdown = "(" + breakdown + ") x " + String.format("%.2f", player.getArtifactDamageMultiplier()) + " [Artifacts]";
		}
		if (forestEnemyBonus > 0) {
			breakdown += " + " + forestEnemyBonus + " Forest";
		}
		if (shieldReduction > 0) {
			breakdown += " - " + shieldReduction + " Fortified" + (swampFortifiedBonus > 0 ? " (+" + swampFortifiedBonus + " Swamp)" : "");
		}
		if (playerGuard > 0) {
			breakdown += " - " + playerGuard + " Guard";
		}

		if (forestEnemyBonus > 0) {
			System.out.println("[Forest] Enemy winning hands deal +" + forestEnemyBonus + " damage.");
		}
		if (swampFortifiedBonus > 0) {
			System.out.println("[Swamp] Fortified sigils worth +1 each. Bonus blocked " + swampFortifiedBonus + ".");
		}
		System.out.println(outcome + " " + verb + finalDamage + " damage (" + breakdown + ").");
		if (vampiricPercent > 0) {
			int healAmount = Math.max(1, (int) Math.round(finalDamage * (vampiricPercent / 100.0)));
			int beforeHp = winner.getHealthpoint();
			winner.setHealthpoint(winner.getHealthpoint() + healAmount);
			System.out.println("[Vampiric Sigil] " + winner.getName() + " healed " + (winner.getHealthpoint() - beforeHp) + " HP (" + vampiricPercent + "% of damage dealt)!");
		}
		int ruinsGold = (terrain == Terrain.RUINS && winner == player) ? terrain.getGoldBonus() : 0;
		int totalBonusGold = midasGold + doubleDownGoldBonus + ruinsGold;
		if (totalBonusGold > 0 && winner == player) {
			player.addGold(totalBonusGold);
			if (doubleDownGoldBonus > 0) {
				System.out.println("[Double Down] Earned +" + doubleDownGoldBonus + " bonus gold!");
			}
			if (midasGold > 0) {
				System.out.println("[Midas Sigil] Earned +" + midasGold + " bonus gold!");
			}
			if (ruinsGold > 0) {
				System.out.println("[Ruins] Earned +" + ruinsGold + " bonus gold!");
			}
		}
		if (streakGold > 0) {
			System.out.println("[Win Streak] Earned +" + streakGold + " bonus gold! (" + player.getHandWinStreak() + " in a row)");
		}
		System.out.println(hpLabel + loser.getHealthpoint() + "/" + loser.getMaxHealthpoint());
	}

	private void applyTerrainEffect() {
		turnsTaken++;
		if (terrain.getTriggerInterval() <= 0 || turnsTaken % terrain.getTriggerInterval() != 0) {
			return;
		}
		if (player.getHeatShieldTicks() > 0) {
			player.useHeatShieldTick();
			System.out.println("[Heat Shield] Heat blocked! (" + player.getHeatShieldTicks() + " block(s) left).");
			return;
		}
		int damage = Math.max(0, terrain.getDamage() - player.getArtifactHeatReduction());
		if (damage == 0) {
			System.out.println("[Desert Cloak] Heat fully blocked!");
			return;
		}
		if (player.getArtifactHeatReduction() > 0) {
			System.out.println("[Desert Cloak] Heat reduced to " + damage + " damage.");
		}
		player.setHealthpoint(player.getHealthpoint() - damage);
		System.out.println("[Heat] You took " + damage + " damage! HP: " + player.getHealthpoint() + "/" + player.getMaxHealthpoint());
	}

	private void displayShowdown(Entity entity) {
		if (!entity.getShowdownHand().isEmpty()) {
			if (entity == player) {
				System.out.println("Played Hand:");
			} else {
				System.out.println("Enemy Hand:");
			}
			for (Card card : entity.getShowdownHand()) {
				System.out.println(card);
			}
			int total = entity.calculateHandValue(entity.getShowdownHand());
			HandPattern pattern = HandEvaluator.evaluate(entity.getShowdownHand());
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

	private void showWinBanner() {
		System.out.println("\n__   _____  _   _  __        _____  _   _ \n"
				+ "\\ \\ / / _ \\| | | | \\ \\      / / _ \\| \\ | |\n"
				+ " \\ V / | | | | | |  \\ \\ /\\ / / | | |  \\| |\n"
				+ "  | || |_| | |_| |   \\ V  V /| |_| | |\\  |\n"
				+ "  |_| \\___/ \\___/     \\_/\\_/  \\___/|_| \\_|");
	}

	private void applyVictoryRewards() {
		int reward = Math.max(1, (int) Math.round(8 * enemy.getLevel() * player.getTotalGoldMultiplier()));
		player.addGold(reward);
		System.out.println("Earned " + reward + " gold!");
		player.healPercent(0.05);
	}
}