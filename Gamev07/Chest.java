package Gamev07;

import java.util.ArrayList;

public class Chest {
	private static final int COMMON_WEIGHT = 60;
	private static final int RARE_WEIGHT = 30;

	public static Item randomItem() {
		ArrayList<Item> candidates = new ArrayList<>();
		int roll = Rng.nextInt(100);
		Rarity target;
		if (roll < COMMON_WEIGHT) {
			target = Rarity.COMMON;
		} else if (roll < COMMON_WEIGHT + RARE_WEIGHT) {
			target = Rarity.RARE;
		} else {
			target = Rarity.LEGENDARY;
		}
		for (Item item : Item.values()) {
			if (item.getRarity() == target) {
				candidates.add(item);
			}
		}
		if (candidates.isEmpty()) {
			return Item.HEALTH_POTION;
		}
		return candidates.get(Rng.nextInt(candidates.size()));
	}

	public static Item grantRandomItem(Player player) {
		Item item = randomItem();
		player.addItem(item);
		return item;
	}
}
