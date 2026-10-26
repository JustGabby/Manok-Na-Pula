package CardGame;
import java.util.Collections;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		Player player1 = new Player();
		Dealer dealer1 = new Dealer();
		Blackjack blackjack;
		int userChoice;
		int inGameChoice;
		double bet;
		boolean running = true;
		Scanner sc = new Scanner(System.in);
		
		while (running) {
			System.out.println("1. Draw");
			System.out.println("2. Exit");
			System.out.print("Input: ");
			userChoice = sc.nextInt();
			
			switch(userChoice) {
			case 1:
				System.out.print("Enter bet: ");
				bet = sc.nextInt();
				
				blackjack = new Blackjack(bet, player1, dealer1);
				
				System.out.println("Winrate: "+ blackjack.getWinRate() +"% ( Wins: "+ player1.getWins()+" | Lose: "+ player1.getLose() +" | Draw: "+ player1.getDraw()+" )");
				System.out.println("Bet: " + blackjack.displayBet());
				
				Collections.shuffle(blackjack.getDeck());
				Collections.shuffle(blackjack.getDeck());

				
				player1.addHand(blackjack.draw());
				player1.addHand(blackjack.draw());
				dealer1.addHand(blackjack.draw());
				
				player1.displayHand(blackjack);
				dealer1.displayHand(blackjack);
				

				
				while(true) {
					if (player1.handValue(blackjack) == 21) {
						blackjack.showDown();
						break;
					}
					System.out.println("1. Hit");
					System.out.println("2. Stand");
					System.out.print("Input: ");
					inGameChoice = sc.nextInt();
					
					if (inGameChoice == 1) {
						player1.addHand(blackjack.draw());
						player1.displayHand(blackjack);
							if (player1.handValue(blackjack) > 21) {
								blackjack.showDown();
								break;
							} else if(player1.handValue(blackjack) == 21) {
								blackjack.showDown();
								break;
							}
							continue;
					} else if(inGameChoice == 2) {
						blackjack.showDown();
						break;
					}
					break;
				}
				
				blackjack.retrieveCards();
				break;
			case 2:
				System.out.println("Thank you for playing!");
				running = false;
				break;
			}
			
		}
		sc.close();
	}
	
	
}
