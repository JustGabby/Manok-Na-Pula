package Game;

import java.util.Scanner;

public class Player extends Entity{
	private int gold;
	private int damage;
	public Player(String name, int healthpoint, int cardSlot) {
		super(name, healthpoint, cardSlot);
		this.gold = 5;
		this.damage = 10;
		super.getDeck();
	}
	
	public int getGold() {
		return gold;
	}
	
	public void setGold(int gold) {
		this.gold = gold;
	}
	
	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	@Override
	public void playHand() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Pick card: ");
		String line = sc.nextLine().trim();

		if (line.isEmpty()) {
			System.out.println("No cards selected.");
			return;
		}

		String[] split = line.split("[,\\s]+");
		int[] selectedCards = new int[split.length];

		for (int i = 0; i < split.length; i++) {
			selectedCards[i] = Integer.parseInt(split[i]);
		}

		this.playHand(selectedCards);
	}
}
