package Jacklore;

import java.util.Scanner;

public class Menu {
	private Scanner sc;

	public Menu() {
		this.sc = new Scanner(System.in);
	}

	public void start() {
		boolean running = true;

		System.out.println("      _    _    ____ _  ___     ___  ____  _____ \n"
				+ "     | |  / \\  / ___| |/ / |   / _ \\|  _ \\| ____|\n"
				+ "  _  | | / _ \\| |   | ' /| |  | | | | |_) |  _|  \n"
				+ " | |_| |/ ___ \\ |___| . \\| |__| |_| |  _ <| |___ \n"
				+ "  \\___//_/   \\_\\____|_|\\_\\_____\\___/|_| \\_\\_____|");

		while (running) {
			System.out.println();
			System.out.println("1. Start Game");
			System.out.println("2. About");
			System.out.println("3. Exit");

			int choice = Input.readInt(sc, "Choice: ");

			switch (choice) {
			case 1:
				startNewRunMenu();
				break;
			case 2:
				displayAbout();
				break;
			case 3:
				System.out.println("Thank you for playing!");
				running = false;
				break;
			default:
				System.out.println("Invalid choice!\n");
				break;
			}
		}
	}

	private void startNewRunMenu() {
		System.out.print("\nEnter player name: ");
		String name = sc.nextLine().trim();
		if (name.isEmpty()) {
			System.out.println("Player name cannot be empty.\n");
			return;
		}
		Player player = new Player(name);
		System.out.println("\n!---Run Started---!");
		player.displayPlayerProfile();

		Game game = new Game(player);
		game.startNewRun(sc);
	}

	private void displayAbout() {
		System.out.println("\n=== ABOUT JACKLORE ===");
		System.out.println("A roguelike card-clash RPG combining Blackjack, Balatro, and Inscryption mechanics.");
		System.out.println("Developed by OnlyGamble\n");
	}
}