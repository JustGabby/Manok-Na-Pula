package Gamev06;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class HandEvaluator {
	public static HandPattern evaluate(List<Card> cards) {
		if (cards == null || cards.size() < 2) {
			return HandPattern.NONE;
		}
		boolean flush = isFlush(cards);
		boolean straight = isStraight(cards);
		ArrayList<Integer> freq = getSortedRankCounts(cards);
		int maxCount = freq.get(0);
		int secondCount = freq.size() > 1 ? freq.get(1) : 0;

		if (isRoyalFlush(cards)) {
			return HandPattern.ROYAL_FLUSH;
		}
		if (flush && straight) {
			return HandPattern.STRAIGHT_FLUSH;
		}
		if (maxCount >= 5) {
			return HandPattern.FIVE_OF_A_KIND;
		}
		if (maxCount == 4) {
			return HandPattern.FOUR_OF_A_KIND;
		}
		if (maxCount == 3 && secondCount >= 2) {
			return HandPattern.FULL_HOUSE;
		}
		if (flush) {
			return HandPattern.FLUSH;
		}
		if (straight) {
			return HandPattern.STRAIGHT;
		}
		if (maxCount == 3) {
			return HandPattern.THREE_OF_A_KIND;
		}
		if (maxCount == 2 && secondCount == 2) {
			return HandPattern.TWO_PAIR;
		}
		if (maxCount == 2) {
			return HandPattern.PAIR;
		}
		return HandPattern.NONE;
	}

	public static boolean isFlush(List<Card> cards) {
		if (cards.size() < 3) {
			return false;
		}
		Suit firstSuit = cards.get(0).getSuit();
		for (int i = 1; i < cards.size(); i++) {
			if (cards.get(i).getSuit() != firstSuit) {
				return false;
			}
		}
		return true;
	}

	public static boolean isStraight(List<Card> cards) {
		if (cards.size() < 3) {
			return false;
		}
		HashSet<Integer> unique = new HashSet<>();
		ArrayList<Integer> ranks = new ArrayList<>();
		for (Card card : cards) {
			int order = getRankOrder(card.getRank());
			if (!unique.add(order)) {
				return false;
			}
			ranks.add(order);
		}
		if (isConsecutive(ranks)) {
			return true;
		}
		if (ranks.contains(1)) {
			ArrayList<Integer> aceHighRanks = new ArrayList<>();
			for (int r : ranks) {
				aceHighRanks.add(r == 1 ? 14 : r);
			}
			if (isConsecutive(aceHighRanks)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isRoyalFlush(List<Card> cards) {
		if (cards.size() != 5 || !isFlush(cards)) {
			return false;
		}
		HashSet<Rank> ranks = new HashSet<>();
		for (Card card : cards) {
			ranks.add(card.getRank());
		}
		return ranks.contains(Rank.TEN)
				&& ranks.contains(Rank.JACK)
				&& ranks.contains(Rank.QUEEN)
				&& ranks.contains(Rank.KING)
				&& ranks.contains(Rank.ACE);
	}

	private static boolean isConsecutive(ArrayList<Integer> ranks) {
		Collections.sort(ranks);
		for (int i = 1; i < ranks.size(); i++) {
			if (ranks.get(i) != ranks.get(i - 1) + 1) {
				return false;
			}
		}
		return true;
	}

	private static int getRankOrder(Rank rank) {
		switch (rank) {
		case ACE:
			return 1;
		case TWO:
			return 2;
		case THREE:
			return 3;
		case FOUR:
			return 4;
		case FIVE:
			return 5;
		case SIX:
			return 6;
		case SEVEN:
			return 7;
		case EIGHT:
			return 8;
		case NINE:
			return 9;
		case TEN:
			return 10;
		case JACK:
			return 11;
		case QUEEN:
			return 12;
		case KING:
			return 13;
		default:
			return rank.getValue();
		}
	}

	private static ArrayList<Integer> getSortedRankCounts(List<Card> cards) {
		HashMap<Rank, Integer> counts = new HashMap<>();
		for (Card card : cards) {
			counts.put(card.getRank(), counts.getOrDefault(card.getRank(), 0) + 1);
		}
		ArrayList<Integer> freq = new ArrayList<>(counts.values());
		Collections.sort(freq, Collections.reverseOrder());
		return freq;
	}
}