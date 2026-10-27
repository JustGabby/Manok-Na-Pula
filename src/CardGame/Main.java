package CardGame;

import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {

	public static void main(String[] args) {
		Player player1 = new Player();
		Dealer dealer1 = new Dealer();

		int userMenuChoice = 0;
		double bet;
		boolean appRunning = true;
		Scanner sc = new Scanner(System.in);

		while (appRunning) {
			try {
				System.out.println("1. Play Blackjack");
				System.out.println("2. Play Baccarat");
				System.out.println("3. Play Lucky-9");
				System.out.println("4. Exit");
				System.out.print("\nInput: ");
				userMenuChoice = sc.nextInt();
			} catch (InputMismatchException e) {
				System.out.println("\nError. Try Again.\n");
				sc.nextLine();
				continue;
			}

			switch (userMenuChoice) {
			case 1:
				Blackjack blackjack;
				boolean isBlackjackRunning = true;

				while (isBlackjackRunning) {
					System.out.print("Enter bet: ");
					bet = sc.nextInt();

					blackjack = new Blackjack(bet, player1, dealer1);

					System.out.println("Winrate: " + blackjack.getWinRate() + "% ( Wins: " + player1.getWins()
							+ " | Lose: " + player1.getLose() + " | Draw: " + player1.getDraw() + " )");
					System.out.println("Bet: " + blackjack.displayBet());

					blackjack.runBlackjack(sc, player1, dealer1, blackjack, isBlackjackRunning);
				}
				break;
			case 4:
				appRunning = false;
				sc.close();
				System.out.println("Thank you for playing!");
				System.out.println("( - Program Terminated - )");
				break;
			default:
				System.out.println("\nInvalid choice. Try again.\n");
			}
		}
	}
}
