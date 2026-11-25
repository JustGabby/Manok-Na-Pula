package Gamev05_1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Enemy extends Entity {
	private static final int MAX_SUBSET_CARDS = 5;
	private int level;

	public Enemy(String name, int healthpoint, int cardSlot, int level) {
		this(name, healthpoint, cardSlot, level, 0);
	}

	public Enemy(String name, int healthpoint, int cardSlot, int level, int bonusDamage) {
		super(name, healthpoint, cardSlot, bonusDamage);
		this.level = level;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public void playShowdownHand(Scanner sc) {
		switch (level) {
		case 1:
			pickRandom();
			break;
		case 2:
			pickUnderTarget();
			break;
		default:
			pickBestUntilBust();
			break;
		}
	}

	private void pickRandom() {
		int size = getHand().size();
		if (size == 0) {
			return;
		}
		ArrayList<Integer> indices = new ArrayList<>();
		for (int i = 1; i <= size; i++) {
			indices.add(i);
		}
		Rng.shuffle(indices);
		int minCards = Math.min(2, size);
		int maxCards = Math.min(3, size);
		int count = Rng.range(minCards, maxCards);
		ArrayList<Integer> chosen = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			chosen.add(indices.get(i));
		}
		selectCards(chosen);
	}

	private void pickUnderTarget() {
		LinkedList<Card> hand = getHand();
		if (hand.isEmpty()) {
			return;
		}
		LinkedList<Card> candidate = new LinkedList<>();
		ArrayList<Integer> chosenIndices = new ArrayList<>();

		for (int i = 0; i < hand.size(); i++) {
			candidate.add(hand.get(i));
			chosenIndices.add(i + 1);
			if (candidate.size() >= 2 && calculateHandValue(candidate) >= getBustLimit() - 5) {
				break;
			}
		}

		selectCards(chosenIndices);
	}

	private void pickBestUntilBust() {
		LinkedList<Card> hand = getHand();
		int n = hand.size();
		if (n == 0) {
			return;
		}

		int minCards = Math.min(2, n);
		int maxCards = Math.min(MAX_SUBSET_CARDS, n);
		int bestEffectiveScore = -1;
		ArrayList<Integer> bestIndices = new ArrayList<>();

		int totalSubsets = 1 << n;
		for (int mask = 1; mask < totalSubsets; mask++) {
			int bitCount = Integer.bitCount(mask);
			if (bitCount < minCards || bitCount > maxCards) {
				continue;
			}
			LinkedList<Card> subset = new LinkedList<>();
			ArrayList<Integer> currentIndices = new ArrayList<>();

			for (int i = 0; i < n; i++) {
				if ((mask & (1 << i)) != 0) {
					subset.add(hand.get(i));
					currentIndices.add(i + 1);
				}
			}

			int score = calculateHandValue(subset);
			if (score <= getBustLimit()) {
				double mult = getHandPattern(subset).getMultiplier();
				int effectiveScore = (int) Math.round((score + getBonusDamage()) * mult);
				if (effectiveScore > bestEffectiveScore) {
					bestEffectiveScore = effectiveScore;
					bestIndices = currentIndices;
				}
			}
		}

		if (bestIndices.isEmpty()) {
			if (n >= 2) {
				selectCards(new int[] { 1, 2 });
			} else {
				selectCards(new int[] { 1 });
			}
			return;
		}

		selectCards(bestIndices);
	}

	private void enemyBlackjackActionPhase() {
		int currentVal = calculateHandValue(getShowdownHand());
		if (currentVal >= getBustLimit() - 5) {
			return;
		}

		double doubleDownChance = (level == 1) ? 0.25 : (level == 2) ? 0.45 : 0.65;
		if (currentVal >= 9 && currentVal <= 11 && Rng.nextDouble() < doubleDownChance) {
			setDoubledDown(true);
			System.out.println("\n" + getName() + " DOUBLES DOWN!");
			for (int i = 0; i < 2; i++) {
				drawCardToShowdown();
			}
			return;
		}

		int hitThreshold = (level == 1) ? 12 : (level == 2) ? 14 : 15;
		double hitChance = (level == 1) ? 0.50 : (level == 2) ? 0.80 : 1.0;

		if (currentVal <= hitThreshold && Rng.nextDouble() < hitChance) {
			System.out.println("\n" + getName() + " draws from the deck!");
			drawCardToShowdown();
			if (level >= 2 && calculateHandValue(getShowdownHand()) <= hitThreshold - 2) {
				System.out.println(getName() + " draws another card!");
				drawCardToShowdown();
			}
		}
	}

	public void levelUp(int level) {
		this.setLevel(level);
	}

	public void displayEnemyInfo() {
	    System.out.printf("%-18s %s%n", "Enemy:", getName());
	    System.out.printf("%-18s %d/%d%n", "HP:", super.getHealthpoint(), super.getMaxHealthpoint());
	    System.out.printf("%-18s %d%s%n", "Level:", getLevel(),(getBonusDamage() > 0 ? " | Bonus Damage: +" + getBonusDamage() : ""));
	}

	public void enemyTurn() {
		setDoubledDown(false);
		drawCards();
		playShowdownHand(null);
		enemyBlackjackActionPhase();
	}
}