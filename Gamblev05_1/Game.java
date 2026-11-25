package Gamev05_1;

import java.util.Scanner;

public class Game {
	private Player player;
	private Shop shop;
	private Wish wish;

	public Game(Player player) {
		this.player = player;
		this.shop = new Shop();
		this.wish = new Wish();
	}

	public Player getPlayer() {
		return player;
	}

	public void startNewRun(Scanner sc) {
		System.out.println("\n- ⚔ - Introductory Combat - ⚔ -");
		Enemy goblin = new Enemy("Goblin", 100, 4, 1, 0);
		Battle introBattle = new Battle(player, goblin);

		if (introBattle.startBattle(sc)) {
			recordVictory();
			runPostBattleLoop(sc);
		}
	}

	private void runPostBattleLoop(Scanner sc) {
		boolean inRun = true;

		while (inRun && player.getHealthpoint() > 0) {
			System.out.println("\n=== CAMP MENU ===");
			player.displayPlayerProfile();
			System.out.println();
			System.out.println("1. ⚔ Next Battle");
			System.out.println("2. ◆ Shop");
			System.out.println("3. ✦ Event");
			System.out.println("4. 🂠 View Deck");
			System.out.println("5. ♥ Player's Profile");
			System.out.println("6. ✕ End Run");

			int choice = Input.readInt(sc, "Choice: ");

			switch (choice) {
			case 1:
				int stage = player.getEnemiesDefeated() + 1;
				Enemy nextEnemy = spawnEnemyForStage(stage);
				Battle battle = new Battle(player, nextEnemy);
				if (battle.startBattle(sc)) {
					if (stage % 3 == 0) {
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
				System.out.println("♨Campfire event coming next!");
				break;
			case 5:
				player.getDeck().viewDeckMenu(sc);
				break;
			case 6:
				showArtifacts(sc);
				break;
			case 7:
				showPlayerProfile(sc);
				break;
			case 8:
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
		boolean inArtifacts = true;

		while (inArtifacts) {
			System.out.println("\n=== OWNED ARTIFACTS (" + player.getArtifacts().size() + ") ===");
			if (player.getArtifacts().isEmpty()) {
				System.out.println("No artifacts acquired yet. Visit the Wish Altar to pull!");
				inArtifacts = false;
			} else {
				for (int i = 0; i < player.getArtifacts().size(); i++) {
					Artifacts artifact = player.getArtifacts().get(i);
					System.out.println((i + 1) + ". [" + artifact.getRating() + "★] " + artifact.getName() + " - " + artifact.getEffect());
				}
				System.out.println("\n1. Use Artifact");
				System.out.println("2. Back");

				int choice = Input.readInt(sc, "Choice: ");

				switch (choice) {
				case 1:
					if (player.getArtifacts().isEmpty()) {
						System.out.println("You have no artifacts to use!");
						break;
					}
					int artifactNum = Input.readInt(sc, "Enter artifact number (0 to cancel): ");
					if (artifactNum == 0) {
						break;
					}
					if (player.useArtifact(artifactNum - 1)) {
						System.out.println("Artifact activated!");
					} else {
						System.out.println("Invalid artifact number!");
					}
					break;
				case 2:
					inArtifacts = false;
					break;
				default:
					System.out.println("Invalid choice!");
					break;
				}
			}
		}
	}

	private Enemy spawnEnemyForStage(int stage) {
		if (stage % 3 == 0) {
			return new Enemy("BOSS: Shadow Warden", 140 + (stage * 10), 4, 3, 4);
		}
		if (stage % 3 == 2) {
			return new Enemy("Skeleton Guard", 120, 4, 2, 0);
		}
		return new Enemy("Goblin Raider", 110, 4, 1, 0);
	}

	public void recordVictory() {
		player.setEnemiesDefeated(player.getEnemiesDefeated() + 1);
	}

	public void recordBossVictory() {
		player.setArc(player.getArc() + 1);
		player.setEnemiesDefeated(player.getEnemiesDefeated() + 1);
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
	    boolean inProfile = true;

	    while (inProfile) {
	        System.out.println("\n=== PLAYER'S PROFILE ===");
	        System.out.println("Player Name: " + player.getName());
	        System.out.println("Player ID: " + player.getId());

	        System.out.println("\n1.✎ Edit Name");
	        System.out.println("2. ↩ Back");

	        int choice = Input.readInt(sc, "Choice: ");

	        switch (choice) {
	        case 1:
	            renamePlayer(sc);
	            break;
	        case 2:
	            inProfile = false;
	            break;
	        default:
	            System.out.println("Invalid choice!");
	            break;
	        }
	    }
	}
}