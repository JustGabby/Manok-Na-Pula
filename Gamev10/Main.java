package Gamev09;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean running = true;

		System.out.println("     _           _    _    ____ _  ___     ___  ____  _____\n"
				+ "    / \\         | |  / \\  / ___| |/ / |   / _ \\|  _ \\| ____|\n"
				+ "  _/   \\_    _  | | / _ \\| |   | ' /| |  | | | | |_) |  _|\n"
				+ " (_  |  _)  | |_| |/ ___ \\ |___| . \\| |__| |_| |  _ <| |___\n"
				+ "    /_\\      \\___//_/   \\_\\____|_|\\_\\_____\\___/|_| \\_\\_____|");

		try {
			while (running) {
				System.out.println();
				System.out.println("1. ⚔ Start Game");
				System.out.println("2. ↻ Load Game");
				System.out.println("3. ⏻ About");
				System.out.println("4. ⇥ Exit");

				int choice = Input.readInt(sc, "Choice: ");

				switch (choice) {
				case 1:
					startNewRunMenu(sc);
					break;
				case 2:
					loadSavedRunMenu(sc);
					break;
				case 3:
					displayAbout();
					break;
				case 4:
					System.out.println("Thank you for playing!");
					running = false;
					break;
				default:
					System.out.println("Invalid choice!");
					break;
				}
			}
		} catch (IllegalStateException e) {
			System.out.println("Input is no longer available. Ending the program safely.");
		} catch (RuntimeException e) {
			System.out.println("Unexpected error: " + e.getMessage());
		} finally {
			sc.close();
		}
	}

	private static void startNewRunMenu(Scanner sc) {
		System.out.print("\nEnter player name: ");
		String name = sc.nextLine().trim();
		if (name.isEmpty()) {
			System.out.println("Player name cannot be empty.");
			return;
		}
		Bundle.resetAllPity();
		Player player = new Player(name);
		System.out.println("\n✧- - - Run Started - - -✧\n");
		player.displayPlayerProfile();

		Game game = new Game(player);
		game.playInstructions();
		System.out.println("Press 'Enter' to continue...");
		sc.nextLine();
		game.startNewRun(sc);
	}

	private static void loadSavedRunMenu(Scanner sc) {
		Game game = SaveSystem.load();
		if (game != null) {
			game.resumeRun(sc);
		}
	}

	private static void displayAbout() {
		System.out.println("\n=== ABOUT JACKLORE ===");
		System.out.println("A roguelike card-clash RPG combining Blackjack, Balatro, and Inscryption mechanics.");
		System.out.println();
		System.out.println("HOW TO PLAY");
		System.out.println("- Pick at least 2 cards and get close to 21 without going over (BUST).");
		System.out.println("- The higher total wins the round and damages the enemy.");
		System.out.println("- After picking, Stand, Draw another card, or Double Down for bonus damage and gold.");
		System.out.println("- Poker hands like pairs and flushes boost your damage.");
		System.out.println("- Reduce the enemy's HP to 0 to win. Don't let yours hit 0!");
		System.out.println("- Items are one-time consumables bought in the Shop or found in Chests.");
		System.out.println("- Artifacts are permanent run-wide passives obtained at the Wish Altar.");
		System.out.println("- Win battles to unlock a camp chest; win every 3rd battle to light a campfire.");
		System.out.println("- Terrain: Desert/Volcano deal heat; Swamp boosts Fortified; Forest boosts enemy damage; Ruins grants win gold.");
		System.out.println();
		System.out.println("Developed by OnlyGamble");
	}
}