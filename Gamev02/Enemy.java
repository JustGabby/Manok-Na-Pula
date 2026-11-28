package Gamev02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class Enemy extends Entity{
	private static final Random RANDOM = new Random();
	private int level;
	private int damage;
	
	public Enemy(String name, int healthpoint, int cardSlot, int level, int damage) {
		super(name, healthpoint, cardSlot);
		this.level = level;
		this.damage = damage;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public void playShowdownHand() {
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
		Collections.shuffle(indices);
		int count = RANDOM.nextInt(Math.min(3, size)) + 1;
		int[] chosen = new int[count];
		for (int i = 0; i < count; i++) {
			chosen[i] = indices.get(i);
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
			if (calculateHandValue(candidate) >= getBustLimit() - 5) {
				break;
			}
		}

		int[] chosen = new int[chosenIndices.size()];
		for (int i = 0; i < chosenIndices.size(); i++) {
			chosen[i] = chosenIndices.get(i);
		}
		selectCards(chosen);
	}

	private void pickBestUntilBust() {
		LinkedList<Card> hand = getHand();
		int n = hand.size();
		if (n == 0) {
			return;
		}

		int bestScore = -1;
		ArrayList<Integer> bestIndices = new ArrayList<>();

		int totalSubsets = 1 << n;
		for (int mask = 1; mask < totalSubsets; mask++) {
			LinkedList<Card> subset = new LinkedList<>();
			ArrayList<Integer> currentIndices = new ArrayList<>();

			for (int i = 0; i < n; i++) {
				if ((mask & (1 << i)) != 0) {
					subset.add(hand.get(i));
					currentIndices.add(i + 1);
				}
			}

			int score = calculateHandValue(subset);
			if (score <= getBustLimit() && score > bestScore) {
				bestScore = score;
				bestIndices = currentIndices;
			}
		}

		if (bestIndices.isEmpty()) {
			selectCards(new int[] { 1 });
			return;
		}

		int[] chosen = new int[bestIndices.size()];
		for (int i = 0; i < bestIndices.size(); i++) {
			chosen[i] = bestIndices.get(i);
		}
		selectCards(chosen);
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public void levelUp(int level) {
		this.setLevel(level);
	}
	
	public void displayEnemyInfo() {
		System.out.println("Name: " + getName());
		System.out.println("HP: " + super.getHealthpoint());
		System.out.println("Level: " + getLevel());
	}
	
	public void enemyTurn() {
		drawCards();
		playShowdownHand();
	}
}