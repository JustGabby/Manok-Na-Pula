package Jacklore;

public enum Item {
	HEALTH_POTION("Health Potion", "Restores 35 HP", 10),
	WHETSTONE("Whetstone", "Permanently increases Bonus Damage by +4", 14),
	LOADED_DIE("Loaded Die", "Grants +2 Rerolls immediately", 8),
	VITALITY_CHARM("Vitality Charm", "Increases Max HP by +20 and heals 20 HP", 16),
	ACE_TOKEN("Ace Token", "Adds a temporary Ace of Spades to your current hand", 12);

	private final String itemName;
	private final String description;
	private final int price;

	Item(String itemName, String description, int price) {
		this.itemName = itemName;
		this.description = description;
		this.price = price;
	}

	public String getItemName() {
		return itemName;
	}

	public String getDescription() {
		return description;
	}

	public int getPrice() {
		return price;
	}

	public void use(Player player) {
		switch (this) {
		case HEALTH_POTION:
			int before = player.getHealthpoint();
			player.setHealthpoint(player.getHealthpoint() + 35);
			System.out.println("Used Health Potion! Recovered " + (player.getHealthpoint() - before) + " HP (" + player.getHealthpoint() + "/" + player.getMaxHealthpoint() + ").");
			break;
		case WHETSTONE:
			player.setBonusDamage(player.getBonusDamage() + 4);
			System.out.println("Used Whetstone! Bonus damage is now +" + player.getBonusDamage() + ".");
			break;
		case LOADED_DIE:
			player.setReroll(player.getReroll() + 2);
			System.out.println("Used Loaded Die! Rerolls: " + player.getReroll() + ".");
			break;
		case VITALITY_CHARM:
			player.increaseMaxHealthpoint(20);
			System.out.println("Used Vitality Charm! Max HP is now " + player.getMaxHealthpoint() + " (" + player.getHealthpoint() + "/" + player.getMaxHealthpoint() + ").");
			break;
		case ACE_TOKEN:
			player.getHand().add(new Card(Rank.ACE, Suit.SPADES, null, true));
			System.out.println("Used Ace Token! Added temporary Ace of Spades (1 / 11) to your current hand.");
			break;
		}
	}
}