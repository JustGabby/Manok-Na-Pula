package Gamev07;

public enum Item {
	HEALTH_POTION("Health Potion", "Restores 35 HP", 10, Rarity.COMMON),
	WHETSTONE("Whetstone", "Permanently increases Bonus Damage by +4", 14, Rarity.COMMON),
	LOADED_DIE("Loaded Die", "Grants +2 Rerolls immediately", 8, Rarity.COMMON),
	VITALITY_CHARM("Vitality Charm", "Increases Max HP by +20 and heals 20 HP", 16, Rarity.COMMON),
	ACE_TOKEN("Ace Token", "Adds a temporary Ace of Spades to your current hand", 12, Rarity.COMMON),
	WATER_FLASK("Water Flask", "Blocks the next 3 heat ticks", 12, Rarity.COMMON),
	MARSH_CLOAK("Marsh Cloak", "Reduces incoming damage by 2", 14, Rarity.RARE),
	SWAMP_WARD("Swamp Ward", "Adds a temporary Fortified Ace to your current hand", 18, Rarity.LEGENDARY),
	LAVA_FLASK("Lava Flask", "Blocks the next 4 heat ticks", 16, Rarity.RARE),
	RELIC_MAP("Relic Map", "Increases gold rewards by 25%", 20, Rarity.RARE);

	private final String itemName;
	private final String description;
	private final int price;
	private final Rarity rarity;

	Item(String itemName, String description, int price, Rarity rarity) {
		this.itemName = itemName;
		this.description = description;
		this.price = price;
		this.rarity = rarity;
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

	public Rarity getRarity() {
		return rarity;
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
		case WATER_FLASK:
			player.setHeatShieldTicks(3);
			System.out.println("Used Water Flask! Heat will be blocked for " + player.getHeatShieldTicks() + " tick(s).");
			break;
		case MARSH_CLOAK:
			player.setArtifactDamageReduction(player.getArtifactDamageReduction() + 2);
			System.out.println("Used Marsh Cloak! Damage reduction is now " + player.getArtifactDamageReduction() + ".");
			break;
		case SWAMP_WARD:
			player.getHand().add(new Card(Rank.ACE, Suit.SPADES, Sigil.FORTIFIED, true));
			System.out.println("Used Swamp Ward! Added temporary Fortified Ace of Spades to your current hand.");
			break;
		case LAVA_FLASK:
			player.setHeatShieldTicks(4);
			System.out.println("Used Lava Flask! Heat will be blocked for " + player.getHeatShieldTicks() + " tick(s).");
			break;
		case RELIC_MAP:
			player.setArtifactGoldMultiplier(player.getArtifactGoldMultiplier() + 0.25);
			System.out.println("Used Relic Map! Gold rewards are now x" + player.getArtifactGoldMultiplier() + ".");
			break;
		}
	}
}
