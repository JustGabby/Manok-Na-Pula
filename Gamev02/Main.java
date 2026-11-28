package Gamev02;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		Player player;
		Enemy enemy;
		Game game;
		int choice;
		Scanner sc = new Scanner(System.in);
		boolean running = true;

		System.out.println("      _    _    ____ _  ___     ___  ____  _____ \n"
				+ "     | |  / \\  / ___| |/ / |   / _ \\|  _ \\| ____|\n"
				+ "  _  | | / _ \\| |   | ' /| |  | | | | |_) |  _|  \n"
				+ " | |_| |/ ___ \\ |___| . \\| |__| |_| |  _ <| |___ \n"
				+ "  \\___//_/   \\_\\____|_|\\_\\_____\\___/|_| \\_\\_____|");
		System.out.println();
		while (running) {
			System.out.println("1. Play");
			System.out.println("2. Quit");
			System.out.print("Choice: ");
			if (sc.hasNextInt()) {
				choice = sc.nextInt();
				sc.nextLine();

				switch (choice) {
				case 1:
					System.out.print("Enter player name: ");
					String name = sc.nextLine();
					player = new Player(name, 150, 6);
					System.out.println("!---Player Created---!");
					System.out.println();
					player.displayPlayerProfile();
					System.out.println();

					boolean inGame = true;
					System.out.println("Greetings, " + player.getName());
					while (inGame) {
						enemy = new Enemy("Goblin", 150, 5, 1, 10);
						System.out.println("1. Battle");
						System.out.println("2. Inventory");
						System.out.println("3. Shop");
						System.out.println("4. Quit");
						System.out.print("Choice: ");
						if (sc.hasNextInt()) {
							int inGameChoice = sc.nextInt();
							sc.nextLine();

							switch (inGameChoice) {
							case 1:
								game = new Game(player, enemy);

								while (player.getHealthpoint() != 0 && enemy.getHealthpoint() != 0) {
									enemy.displayEnemyInfo();
									System.out.println();
									System.out.println("1. Draw");
									System.out.println("2. Inventory");
									System.out.println("3. Surrender");
									System.out.print("Choice: ");

									if (sc.hasNextInt()) {
										int battleChoice = sc.nextInt();
										sc.nextLine();

										switch (battleChoice) {
										case 1:
											player.playerTurn();
											enemy.enemyTurn();
											game.showdown();
											break;

										case 3:
											System.out.println("You surrendered. Game Over.");
											inGame = false;

										default:
											break;
										}
									} else {
										System.out.println("Invalid input!");
										sc.nextLine();
									}
								}
								if (player.getHealthpoint() == 0) {
									System.out.println("You lose.");
									running = false;
								} else {
									System.out.println("You win!");

								}
							case 4:
								System.out.println("Thank you for playing!");
								inGame = false;
								running = false;
							}
						} else {
							System.out.println("Invalid input!");
							sc.nextLine();
							System.out.println();
						}
					}

					break;
				}

				running = false;

			} else {
				System.out.println("invalid input!");
				sc.nextLine();
				System.out.println();
			}
		}
	}
}
