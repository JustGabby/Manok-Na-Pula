package Gamev08;

import java.util.ArrayList;

public class Campfire {
	private static final int BLACKSMITH_COST = 15;
	private static final int BLACKSMITH_BONUS = 2;
	private static final int BLACKSMITH_CAP = 10;
	private static final int EMBER_COST = 10;
	private static final int SCAVENGE_GOLD = 8;
	private static final int SCAVENGE_HP_COST = 5;

	public static void rest(Player player) {
		player.healPercent(0.35);
	}

	public static boolean blacksmith(Player player) {
		if (player.getGold() < BLACKSMITH_COST) {
			System.out.println("The Blacksmith needs " + BLACKSMITH_COST + " Gold.");
			return false;
		}
		if (player.getBonusDamage() >= BLACKSMITH_CAP) {
			System.out.println("Bonus Damage is already at +" + BLACKSMITH_CAP + ".");
			return false;
		}
		player.setGold(player.getGold() - BLACKSMITH_COST);
		player.setBonusDamage(player.getBonusDamage() + BLACKSMITH_BONUS);
		System.out.println("Blacksmith sharpened your edge. Bonus Damage is now +" + player.getBonusDamage() + ".");
		return true;
	}

	public static boolean emberWish(Player player) {
		if (player.getGold() < EMBER_COST) {
			System.out.println("The Ember Wish needs " + EMBER_COST + " Gold.");
			return false;
		}
		ArrayList<Card> cards = player.getDeck().getRandomSubset(1);
		if (cards.isEmpty()) {
			System.out.println("No cards are available for an Ember Wish.");
			return false;
		}
		Sigil sigil = Sigil.getRandomSigil();
		cards.get(0).setSigil(sigil);
		player.setGold(player.getGold() - EMBER_COST);
		System.out.println("Ember Wish placed " + sigil.getDisplayName() + " on " + cards.get(0) + ".");
		return true;
	}

	public static boolean scavenge(Player player) {
		if (player.getHealthpoint() <= SCAVENGE_HP_COST) {
			System.out.println("You are too weak to scavenge.");
			return false;
		}
		player.setGold(player.getGold() + SCAVENGE_GOLD);
		player.setHealthpoint(player.getHealthpoint() - SCAVENGE_HP_COST);
		System.out.println("You scavenged +" + SCAVENGE_GOLD + " Gold and lost " + SCAVENGE_HP_COST + " HP.");
		return true;
	}
}
