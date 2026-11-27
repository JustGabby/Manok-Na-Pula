package Gamev09;

import java.util.Scanner;

public class Game {
	private final Player player;
	private final Shop shop;
	private final Wish wish;
	private final EncounterGenerator encounters;
	private final long runSeed;
	private boolean chestAvailable = false;
	private boolean campfireAvailable = false;

	public Game(Player player) {
		this(player, System.currentTimeMillis());
	}

	public Game(Player player, long seed) {
		Rng.setSeed(seed);
		Bundle.resetAllPity();
		this.player = player;
		this.player.getDeck().resetForBattle();
		this.shop = new Shop();
		this.wish = new Wish();
		this.encounters = new EncounterGenerator(seed);
		this.runSeed = seed;
	}

	public Player getPlayer() {
		return player;
	}

	public Shop getShop() {
		return shop;
	}

	public long getRunSeed() {
		return runSeed;
	}

	public boolean isChestAvailable() {
		return chestAvailable;
	}

	public void setChestAvailable(boolean chestAvailable) {
		this.chestAvailable = chestAvailable;
	}

	public boolean isCampfireAvailable() {
		return campfireAvailable;
	}

	public void setCampfireAvailable(boolean campfireAvailable) {
		this.campfireAvailable = campfireAvailable;
	}

	public void resumeRun(Scanner sc) {
		System.out.println("\n- ⚔ - Run Resumed - ⚔ -");
		System.out.println("Run seed: " + runSeed);
		player.displayPlayerProfile();
		runPostBattleLoop(sc);
	}

	public void startNewRun(Scanner sc) {
		System.out.println("\n- ⚔ - Introductory Combat - ⚔ -");
		System.out.println("Run seed: " + runSeed);
		Enemy firstEnemy = generateEncounter(1);
		Battle introBattle = new Battle(player, firstEnemy);

		if (introBattle.startBattle(sc)) {
			recordVictory();
			runPostBattleLoop(sc);
		}
	}

	private Enemy generateEncounter(int stage) {
		Enemy enemy = encounters.generate(stage);
		System.out.println("\nEncounter " + stage + ": " + encounters.getLastTemplate().getLabel()
				+ " in " + Terrain.forStage(stage).getName());
		return enemy;
	}

	private void runPostBattleLoop(Scanner sc) {
		boolean inRun = true;

		while (inRun && player.getHealthpoint() > 0) {
			System.out.println("\n=== CAMP MENU ===");
			player.displayPlayerProfile();
			System.out.println();
			System.out.println("1. ⚔ Next Battle");
			System.out.println("2. ◆ Shop");
			System.out.println("3. ✦ Wish Altar");
			System.out.println("4. ♨ Campfire" + (campfireAvailable ? "" : (player.getEnemiesDefeated() % 3 == 0 ? " (used)" : " (locked)")));
			System.out.println("5. 📦 Open Chest" + (chestAvailable ? "" : " (empty)"));
			System.out.println("6. 🂠 View Deck");
			System.out.println("7. ❖ View Artifacts");
			System.out.println("8. ♥ Player's Profile");
			System.out.println("9. ▣ Save Run");
			System.out.println("10. ✕ End Run");

			int choice = Input.readInt(sc, "Choice: ");

			switch (choice) {
			case 1:
				int stage = player.getEnemiesDefeated() + 1;
				Enemy nextEnemy = generateEncounter(stage);
				Battle battle = new Battle(player, nextEnemy);
				if (battle.startBattle(sc)) {
					if (stage % 5 == 0) {
						recordBossVictory();
					} else {
						recordVictory();
					}
					shop.restockItems();
				} else {
					inRun = false;
				}
				break;
			case 2:
				shop.enterShop(player, sc);
				break;
			case 3:
				wish.wishInterface(player, sc);
				break;
			case 4:
				openCampfire(sc);
				break;
			case 5:
				openChest();
				break;
			case 6:
				player.getDeck().viewDeckMenu(sc);
				break;
			case 7:
				showArtifacts(sc);
				break;
			case 8:
				showPlayerProfile(sc);
				break;
			case 9:
				SaveSystem.save(this);
				break;
			case 10:
				System.out.println("Run ended. Returning to main menu...\n");
				inRun = false;
				break;
			default:
				System.out.println("Invalid choice!");
				break;
			}
		}
	}

	private void showArtifacts(Scanner sc) {
		while (true) {
			System.out.println("\n=== ACTIVE PASSIVE ARTIFACTS (" + player.getArtifacts().size() + ") ===");
			if (player.getArtifacts().isEmpty()) {
				System.out.println("No passive artifacts acquired yet. Visit the Wish Altar to pull!");
				return;
			}

			for (int i = 0; i < player.getArtifacts().size(); i++) {
				Artifacts artifact = player.getArtifacts().get(i);
				System.out.println((i + 1) + ". [" + artifact.getRating() + "★] " + artifact.getName() + " - " + artifact.getEffect());
			}
			System.out.printf("%nActive Passives: Damage x%.2f | Gold x%.2f | Guard -%d | Heat -%d | Bust Limit %d | Hand Size %d | Double Sigils: %s%n",
					player.getArtifactDamageMultiplier(),
					player.getArtifactGoldMultiplier(),
					player.getArtifactDamageReduction(),
					player.getArtifactHeatReduction(),
					player.getBustLimit(),
					player.getCardSlot(),
					player.isDoubleSigils() ? "Yes" : "No");
			System.out.println("0. Back");

			int choice = Input.readInt(sc, "Choice: ");
			if (choice == 0) {
				return;
			}
			System.out.println("Invalid choice!");
		}
	}

	public void openCampfire(Scanner sc) {
		if (!campfireAvailable) {
			System.out.println("No campfire lit. Win 3 battles to light one.");
			return;
		}
		boolean inCampfire = true;
		while (inCampfire && player.getHealthpoint() > 0) {
			System.out.println("\n=== CAMPFIRE ===");
			System.out.println("1. Rest - Heal 35% max HP");
			System.out.println("2. Blacksmith - Pay 15 Gold for +2 Bonus Damage");
			System.out.println("3. Ember Wish - Pay 10 Gold for a random sigil");
			System.out.println("4. Scavenge - Gain 8 Gold, lose 5 HP");
			System.out.println("5. Leave");

			int choice = Input.readInt(sc, "Choice: ");
			switch (choice) {
			case 1:
				Campfire.rest(player);
				campfireAvailable = false;
				inCampfire = false;
				break;
			case 2:
				if (Campfire.blacksmith(player)) {
					campfireAvailable = false;
					inCampfire = false;
				}
				break;
			case 3:
				if (Campfire.emberWish(player)) {
					campfireAvailable = false;
					inCampfire = false;
				}
				break;
			case 4:
				if (Campfire.scavenge(player)) {
					campfireAvailable = false;
					inCampfire = false;
				}
				break;
			case 5:
				System.out.println("You leave the campfire.");
				inCampfire = false;
				break;
			default:
				System.out.println("Invalid choice!");
				break;
			}
		}
	}

	public void openChest() {
		if (!chestAvailable) {
			System.out.println("No chest to open. Win a battle first!");
			return;
		}
		Item item = Chest.grantRandomItem(player);
		chestAvailable = false;
		System.out.println("Chest opened! Found [" + item.getRarity().getDisplayName() + "] " + item.getItemName() + ". Added to inventory.");
	}

	private void awardCampfireIfDue() {
		if (player.getEnemiesDefeated() % 3 == 0) {
			campfireAvailable = true;
		}
	}

	public void recordVictory() {
		player.setEnemiesDefeated(player.getEnemiesDefeated() + 1);
		chestAvailable = true;
		awardCampfireIfDue();
	}

	public void recordBossVictory() {
		player.setArc(player.getArc() + 1);
		player.setEnemiesDefeated(player.getEnemiesDefeated() + 1);
		chestAvailable = true;
		awardCampfireIfDue();
		System.out.println("Boss defeated! Advancing to Arc " + player.getArc() + "!");
	}

	private void renamePlayer(Scanner sc) {
		System.out.print("\nEnter new name: ");
		String newName = sc.nextLine().trim();
		if (!newName.isEmpty()) {
			player.setName(newName);
			System.out.println("Name changed to " + newName + ".");
		}
	}

	private void showPlayerProfile(Scanner sc) {
		while (true) {
			System.out.println("\n=== PLAYER'S PROFILE ===");
			System.out.println("Player Name: " + player.getName());
			System.out.println("Player ID: " + player.getId());
			System.out.println("\n1. ✎ Edit Name");
			System.out.println("2. ↩ Back");

			int choice = Input.readInt(sc, "Choice: ");

			switch (choice) {
			case 1:
				renamePlayer(sc);
				break;
			case 2:
				return;
			default:
				System.out.println("Invalid choice!");
				break;
			}
		}
	}
}