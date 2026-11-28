package Gamev09;

public enum Item {
	HEALTH_POTION("Health Potion", "Restores 35 HP", 10, Rarity.COMMON),
	WHETSTONE("Whetstone", "Increases Bonus Damage by +4 for this battle", 14, Rarity.COMMON),
	LOADED_DIE("Loaded Die", "Grants +2 Rerolls for this battle", 8, Rarity.COMMON),
	VITALITY_CHARM("Vitality Charm", "Restores 25 HP and reduces damage taken by 1 this battle", 16, Rarity.COMMON),
	ACE_TOKEN("Ace Token", "Adds a temporary Ace of Spades to your hand this turn", 12, Rarity.COMMON),
	WATER_FLASK("Water Flask", "Blocks the next 3 heat ticks this battle", 12, Rarity.COMMON),
	MARSH_CLOAK("Marsh Cloak", "Reduces incoming damage by 3 for this battle", 14, Rarity.RARE),
	SWAMP_WARD("Swamp Ward", "Adds a temporary Fortified Ace to your hand this turn", 18, Rarity.LEGENDARY),
	LAVA_FLASK("Lava Flask", "Blocks the next 4 heat ticks this battle", 16, Rarity.RARE),
	RELIC_MAP("Relic Map", "Increases victory gold by +25% for this battle", 20, Rarity.RARE),
	BANDAGE("Bandage", "Restores 15 HP", 6, Rarity.COMMON),
	RUSTED_BLADE("Rusted Blade", "Increases Bonus Damage by +2 for this battle", 8, Rarity.COMMON),
	SMOKE_BOMB("Smoke Bomb", "Grants +1 Reroll and reduces damage taken by 1 this battle", 9, Rarity.COMMON),
	LUCKY_FIVE("Lucky Five", "Adds a temporary 5 of Hearts to your hand this turn", 10, Rarity.COMMON),
	TEN_CHIP("Ten Chip", "Adds a temporary 10 of Diamonds to your hand this turn", 11, Rarity.COMMON),
	COPPER_COIN("Copper Coin", "Increases victory gold by +15% for this battle", 10, Rarity.COMMON),
	GREATER_POTION("Greater Potion", "Restores 60 HP", 18, Rarity.RARE),
	SHARPENING_OIL("Sharpening Oil", "Increases Bonus Damage by +7 for this battle", 20, Rarity.RARE),
	GAMBLERS_DECK("Gambler's Deck", "Grants +4 Rerolls for this battle", 16, Rarity.RARE),
	IRON_SKIN_ELIXIR("Iron Skin Elixir", "Reduces incoming damage by 5 for this battle", 22, Rarity.RARE),
	FROST_VIAL("Frost Vial", "Blocks the next 6 heat ticks this battle", 20, Rarity.RARE),
	PHOENIX_DRAUGHT("Phoenix Draught", "Restores 100 HP and reduces damage taken by 3 this battle", 30, Rarity.LEGENDARY),
	DRAGON_FANG("Dragon Fang", "Increases Bonus Damage by +12 for this battle", 28, Rarity.LEGENDARY),
	MIDAS_CHALICE("Midas Chalice", "Increases victory gold by +50% and grants +2 Rerolls this battle", 26, Rarity.LEGENDARY);

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
			player.addTempBonusDamage(4);
			System.out.println("Used Whetstone! Bonus damage is now +" + player.getTotalBonusDamage() + " for this battle.");
			break;
		case LOADED_DIE:
			player.setReroll(player.getReroll() + 2);
			System.out.println("Used Loaded Die! Rerolls: " + player.getReroll() + " for this battle.");
			break;
		case VITALITY_CHARM:
			int hpBefore = player.getHealthpoint();
			player.setHealthpoint(player.getHealthpoint() + 25);
			player.addTempDamageReduction(1);
			System.out.println("Used Vitality Charm! Recovered " + (player.getHealthpoint() - hpBefore) + " HP and gained -1 damage taken for this battle.");
			break;
		case ACE_TOKEN:
			player.getHand().add(new Card(Rank.ACE, Suit.SPADES, null, true));
			System.out.println("Used Ace Token! Added temporary Ace of Spades (1 / 11) to your current hand.");
			break;
		case WATER_FLASK:
			player.setHeatShieldTicks(3);
			System.out.println("Used Water Flask! Heat will be blocked for " + player.getHeatShieldTicks() + " tick(s) this battle.");
			break;
		case MARSH_CLOAK:
			player.addTempDamageReduction(3);
			System.out.println("Used Marsh Cloak! Damage reduction is now -" + player.getTotalDamageReduction() + " for this battle.");
			break;
		case SWAMP_WARD:
			player.getHand().add(new Card(Rank.ACE, Suit.SPADES, Sigil.FORTIFIED, true));
			System.out.println("Used Swamp Ward! Added temporary Fortified Ace of Spades to your current hand.");
			break;
		case LAVA_FLASK:
			player.setHeatShieldTicks(4);
			System.out.println("Used Lava Flask! Heat will be blocked for " + player.getHeatShieldTicks() + " tick(s) this battle.");
			break;
		case RELIC_MAP:
			player.addTempGoldBonus(0.25);
			System.out.printf("Used Relic Map! Victory gold multiplier is now x%.2f for this battle.%n", player.getTotalGoldMultiplier());
			break;
		case BANDAGE:
			int bandageBefore = player.getHealthpoint();
			player.setHealthpoint(player.getHealthpoint() + 15);
			System.out.println("Used Bandage! Recovered " + (player.getHealthpoint() - bandageBefore) + " HP (" + player.getHealthpoint() + "/" + player.getMaxHealthpoint() + ").");
			break;
		case RUSTED_BLADE:
			player.addTempBonusDamage(2);
			System.out.println("Used Rusted Blade! Bonus damage is now +" + player.getTotalBonusDamage() + " for this battle.");
			break;
		case SMOKE_BOMB:
			player.setReroll(player.getReroll() + 1);
			player.addTempDamageReduction(1);
			System.out.println("Used Smoke Bomb! Rerolls: " + player.getReroll() + " and gained -1 damage taken for this battle.");
			break;
		case LUCKY_FIVE:
			player.getHand().add(new Card(Rank.FIVE, Suit.HEARTS, null, true));
			System.out.println("Used Lucky Five! Added temporary 5 of Hearts to your current hand.");
			break;
		case TEN_CHIP:
			player.getHand().add(new Card(Rank.TEN, Suit.DIAMONDS, null, true));
			System.out.println("Used Ten Chip! Added temporary 10 of Diamonds to your current hand.");
			break;
		case COPPER_COIN:
			player.addTempGoldBonus(0.15);
			System.out.printf("Used Copper Coin! Victory gold multiplier is now x%.2f for this battle.%n", player.getTotalGoldMultiplier());
			break;
		case GREATER_POTION:
			int greaterBefore = player.getHealthpoint();
			player.setHealthpoint(player.getHealthpoint() + 60);
			System.out.println("Used Greater Potion! Recovered " + (player.getHealthpoint() - greaterBefore) + " HP (" + player.getHealthpoint() + "/" + player.getMaxHealthpoint() + ").");
			break;
		case SHARPENING_OIL:
			player.addTempBonusDamage(7);
			System.out.println("Used Sharpening Oil! Bonus damage is now +" + player.getTotalBonusDamage() + " for this battle.");
			break;
		case GAMBLERS_DECK:
			player.setReroll(player.getReroll() + 4);
			System.out.println("Used Gambler's Deck! Rerolls: " + player.getReroll() + " for this battle.");
			break;
		case IRON_SKIN_ELIXIR:
			player.addTempDamageReduction(5);
			System.out.println("Used Iron Skin Elixir! Damage reduction is now -" + player.getTotalDamageReduction() + " for this battle.");
			break;
		case FROST_VIAL:
			player.setHeatShieldTicks(6);
			System.out.println("Used Frost Vial! Heat will be blocked for " + player.getHeatShieldTicks() + " tick(s) this battle.");
			break;
		case PHOENIX_DRAUGHT:
			int phoenixBefore = player.getHealthpoint();
			player.setHealthpoint(player.getHealthpoint() + 100);
			player.addTempDamageReduction(3);
			System.out.println("Used Phoenix Draught! Recovered " + (player.getHealthpoint() - phoenixBefore) + " HP and gained -3 damage taken for this battle.");
			break;
		case DRAGON_FANG:
			player.addTempBonusDamage(12);
			System.out.println("Used Dragon Fang! Bonus damage is now +" + player.getTotalBonusDamage() + " for this battle.");
			break;
		case MIDAS_CHALICE:
			player.addTempGoldBonus(0.50);
			player.setReroll(player.getReroll() + 2);
			System.out.printf("Used Midas Chalice! Victory gold multiplier is now x%.2f and Rerolls: %d for this battle.%n", player.getTotalGoldMultiplier(), player.getReroll());
			break;
		}
	}
}