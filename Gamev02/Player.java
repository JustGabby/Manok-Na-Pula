package Gamev02;

import java.util.Scanner;

public class Player extends Entity {
	private static final Scanner SC = new Scanner(System.in);
	private int gold;
	private int damage;
	private int reroll;

	public Player(String name, int healthpoint, int cardSlot) {
		super(name, healthpoint, cardSlot);
		this.gold = 5;
		this.damage = 50;
		this.reroll = 3;
	}

	public int getReroll() {
		return reroll;
	}

	public void setReroll(int reroll) {
		this.reroll = reroll;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	@Override
	public void playShowdownHand() {
		while (true) {
			System.out.print("Pick card (r to reroll): ");
			String line = SC.nextLine().trim();

			if (line.isEmpty()) {
				System.out.println("No cards selected.");
				return;
			}

			if (line.equalsIgnoreCase("r")) {
				if(reroll != 0) {
					super.rerollHand();
					reroll--;
					System.out.println();
					System.out.println("!--" + getReroll() + " reroll(s) left--!");
					System.out.println();
					viewHand();
				} else {
					System.out.println();
					System.out.println("!--No more rerolls left--!");
					System.out.println();
				}
			} else {
				String[] split = line.split("[,\\s]+");
				int[] selectedCards = new int[split.length];
				boolean valid = true;

				for (int i = 0; i < split.length; i++) {
					if (!split[i].matches("[1-9]\\d?")) {
						System.out.println("Please only input available card numbers.");
						valid = false;
						break;
					}
					selectedCards[i] = Integer.parseInt(split[i]);
				}

				if (valid) {
					selectCards(selectedCards);
					break;
				}
			}
		}
	}

	public void playerTurn() {
		this.drawCards();
		this.viewHand();
		this.playShowdownHand();
	}
	
	public void restoreRerolls() {
		setReroll(this.reroll);
	}
	
	public void displayPlayerProfile() {
		System.out.println("Name: " + super.getName());
		System.out.println("HP: " + super.getHealthpoint());
		System.out.println("Gold: " + getGold());
		System.out.println("Damage: " + getDamage());
	}
}
