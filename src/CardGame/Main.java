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
				System.out.println("2. Play Texas Hold'em");
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
				boolean isBlackjackRunning = true;

				while (isBlackjackRunning) {
					System.out.print("Enter bet: ");
					bet = sc.nextInt();

					Blackjack blackjack = new Blackjack(bet, player1, dealer1);

					System.out.println("Winrate: " + blackjack.getWinRate() + "% ( Wins: " + player1.getWins()
							+ " | Lose: " + player1.getLose() + " | Draw: " + player1.getDraw() + " )");
					System.out.println("Bet: " + blackjack.displayBet());

					isBlackjackRunning = blackjack.runBlackjack(sc);
				}
				break;
			case 2:
				boolean isPokerRunning = true;

				while (isPokerRunning) {
					System.out.print("Enter bet: ");
					bet = sc.nextInt();

					Poker poker = new Poker(bet, player1, dealer1);

					System.out.println("Winrate: " + poker.getWinRate() + "% ( Wins: " + player1.getWins()
							+ " | Lose: " + player1.getLose() + " | Draw: " + player1.getDraw() + " )");
					System.out.println("Bet: " + poker.displayBet());

					isPokerRunning = poker.runPoker(sc);
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
