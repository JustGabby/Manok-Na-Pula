package CardGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Poker extends Game {
	private Player player;
	private Dealer dealer;
	private ArrayList<Card> board = new ArrayList<>();

	private static final String[] HAND_NAMES = { "", "High Card", "One Pair", "Two Pair", "Three of a Kind",
			"Straight", "Flush", "Full House", "Four of a Kind", "Straight Flush" };

	public Poker(double bet, Player player, Dealer dealer) {
		super(bet);
		this.player = player;
		this.dealer = dealer;
	}

	public boolean runPoker(Scanner sc) {
		player.getHand().clear(); dealer.getHand().clear(); board.clear();
		Collections.shuffle(getDeck());
		for (int i = 0; i < 2; i++) {
			player.addHand(draw());
			dealer.addHand(draw());
		}
		System.out.println("\n=== TEXAS HOLD'EM ===");
		show("Your hole cards", player.getHand());
		System.out.println("Dealer hole cards: [Hidden] [Hidden]");
		String[] street = { "Pre-Flop", "Flop", "Turn", "River" };
		int[] deal = { 0, 3, 1, 1 };
		boolean folded = false;
		for (int i = 0; i < street.length && !folded; i++) {
			for (int j = 0; j < deal[i]; j++) {
				board.add(draw());
			}
			System.out.println("\n--- " + street[i] + " ---");
			if (board.size() >= 3) {
				show("Community", board);
				System.out.println("Your best hand: " + name(bestSignature(withBoard(player.getHand()))));
			}
			System.out.println("1. Check\n2. Fold\nInput: ");
			if (sc.nextInt() == 2) {
				System.out.println("You folded. Dealer wins the hand.");
				player.setLose(player.getLose() + 1);
				folded = true;
			}
		}
		if (!folded) {
			System.out.println("\n=== SHOWDOWN ===");
			show("Community", board);
			show("Dealer hole cards", dealer.getHand());
			showDown();
		}
		retrieveCards();
		return askPlayAgain(sc);
	}

	@Override
	public void showDown() {
		int[] mine = bestSignature(withBoard(player.getHand()));
		int[] theirs = bestSignature(withBoard(dealer.getHand()));
		System.out.println("Your hand:   " + name(mine));
		System.out.println("Dealer hand: " + name(theirs));
		int result = Arrays.compare(mine, theirs);
		if (result > 0) {
			System.out.println("Winner!");
			player.setWins(player.getWins() + 1);
		} else if (result < 0) {
			System.out.println("Lose...");
			player.setLose(player.getLose() + 1);
		} else {
			System.out.println("Draw.");
			player.setDraw(player.getDraw() + 1);
		}
	}

	@Override
	public int calculateHandValue(ArrayList<Card> hand) {
		return bestSignature(withBoard(hand))[0];
	}

	private ArrayList<Card> withBoard(ArrayList<Card> hand) {
		ArrayList<Card> all = new ArrayList<>(hand);
		all.addAll(board);
		return all;
	}

	private int[] bestSignature(ArrayList<Card> all) {
		int n = all.size();
		int[] best = new int[6];
		for (int a = 0; a < n; a++) {
			for (int b = a + 1; b < n; b++) {
				for (int c = b + 1; c < n; c++) {
					for (int d = c + 1; d < n; d++) {
						for (int e = d + 1; e < n; e++) {
							int[] sig = signature(all.get(a), all.get(b), all.get(c), all.get(d), all.get(e));
							if (Arrays.compare(sig, best) > 0) best = sig;
						}
					}
				}
			}
		}
		return best;
	}

	private int[] signature(Card... five) {
		int[] count = new int[15];
		int suit = five[0].getSuit().ordinal();
		boolean flush = true;
		for (Card card : five) {
			count[card.getRank().ordinal() + 2]++;
			if (card.getSuit().ordinal() != suit) {
				flush = false;
			}
		}
		int four = 0, three = 0, pairs = 0;
		for (int r = 14; r >= 2; r--) {
			if (count[r] == 4) four = r;
			else if (count[r] == 3) three = r;
			else if (count[r] == 2) pairs++;
		}
		int straight = straightHigh(count);
		int category;
		if (straight > 0 && flush) category = 9;
		else if (four > 0) category = 8;
		else if (three > 0 && pairs > 0) category = 7;
		else if (flush) category = 6;
		else if (straight > 0) category = 5;
		else if (three > 0) category = 4;
		else if (pairs > 1) category = 3;
		else if (pairs == 1) category = 2;
		else category = 1;

		int[] result = new int[6];
		result[0] = category;
		if (category == 5 || category == 9) {
			result[1] = straight;
		} else {
			int[] ordered = order(count);
			for (int i = 0; i < 5; i++) result[i + 1] = ordered[i];
		}
		return result;
	}

	private int straightHigh(int[] count) {
		for (int r = 14; r >= 5; r--) {
			if (count[r] > 0 && count[r - 1] > 0 && count[r - 2] > 0 && count[r - 3] > 0 && count[r - 4] > 0) {
				return r;
			}
		}
		if (count[14] > 0 && count[2] > 0 && count[3] > 0 && count[4] > 0 && count[5] > 0) {
			return 5;
		}
		return 0;
	}

	private int[] order(int[] count) {
		int[] result = new int[5];
		int index = 0;
		for (int size = 4; size >= 1; size--) {
			for (int r = 14; r >= 2; r--) {
				if (count[r] != size) continue;
				for (int i = 0; i < size && index < 5; i++) result[index++] = r;
			}
		}
		return result;
	}

	private String name(int[] sig) {
		if (sig[0] == 9 && sig[1] == 14) return "Royal Flush";
		return HAND_NAMES[sig[0]];
	}

	private void show(String label, ArrayList<Card> cards) {
		System.out.print(label + ": ");
		for (Card card : cards) {
			System.out.print("[" + card.getCard() + "]");
		}
		System.out.println();
	}

	public double getWinRate() {
		int total = player.getTotalGames();
		return total == 0 ? 0.0 : 100.0 * player.getWins() / total;
	}

	public double displayBet() {
		return getBet();
	}

	private void retrieveCards() {
		super.retrieve(player.getHand());
		super.retrieve(dealer.getHand());
		super.retrieve(board);
		player.getHand().clear(); dealer.getHand().clear(); board.clear();
	}
}
