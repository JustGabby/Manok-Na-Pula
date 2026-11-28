package Gamev09;

import java.util.ArrayList;

public enum Artifacts {
		LOOSESTRING("Loose String", 6, "+2 bust limit (21 to 23)", ArtifactType.BUST_LIMIT, 2),
		KINGOFSPARTA("King of Sparta", 6, "-10 incoming damage taken", ArtifactType.DAMAGE_REDUCTION, 10),
		HANDOFGOD("Hand of God", 6, "+2.0x damage multiplier on winning hands", ArtifactType.DAMAGE_MULTIPLIER, 2.0),
		RAGNAROK("Ragnarok", 6, "+2.0x damage multiplier on winning hands", ArtifactType.DAMAGE_MULTIPLIER, 2.0),
		GOLDENFRUIT("Golden Fruit", 6, "+40 max HP and heals 40 HP", ArtifactType.MAX_HEALTH, 40),
		CURSEDHELMET("Cursed Helmet", 5, "-5 incoming damage taken", ArtifactType.DAMAGE_REDUCTION, 5),
		JOKER8("Joker8", 5, "+1.3x damage multiplier on winning hands", ArtifactType.DAMAGE_MULTIPLIER, 1.3),
		FATEWEAVER("Fate Weaver", 5, "+2 cards drawn each turn", ArtifactType.CARD_SLOT, 2),
		GRIMOIRE("Grimoire", 5, "+1.2x damage multiplier on winning hands", ArtifactType.DAMAGE_MULTIPLIER, 1.2),
		GOLDENWIND("Golden Wind", 5, "+55% victory gold", ArtifactType.GOLD_MULTIPLIER, 1.55),
		SWORDOFDIVINITY("Sword of Divinity", 5, "+1.15x damage multiplier on winning hands", ArtifactType.DAMAGE_MULTIPLIER, 1.15),
		DRAGONHEART("Dragon Heart", 5, "+30 max HP and heals 30 HP", ArtifactType.MAX_HEALTH, 30),
		CHRONOHOURGLASS("Chrono Hourglass", 5, "+2 rerolls per battle", ArtifactType.REROLL, 2),
		AEGISPLATE("Aegis Plate", 5, "-6 incoming damage taken", ArtifactType.DAMAGE_REDUCTION, 6),
		CROWNOFAVARICE("Crown of Avarice", 5, "+45% victory gold", ArtifactType.GOLD_MULTIPLIER, 1.45),
		RECKLESSTHREAD("Reckless Thread", 5, "+1 bust limit (21 to 22)", ArtifactType.BUST_LIMIT, 1),
		PARADOX("Paradox", 4, "+1.1x damage multiplier on winning hands", ArtifactType.DAMAGE_MULTIPLIER, 1.1),
		KINGSHAND("King's Hand", 4, "+2 cards drawn each turn", ArtifactType.CARD_SLOT, 2),
		GOLDARMOR("Gold Armor", 4, "-3 incoming damage taken", ArtifactType.DAMAGE_REDUCTION, 3),
		NOCTURNE("Nocturne", 4, "+1 reroll per battle", ArtifactType.REROLL, 1),
		DESERT_CLOAK("Desert Cloak", 4, "Reduces Desert/Volcano heat damage by 2", ArtifactType.HEAT_REDUCTION, 2),
		GAMBLERSMONOCLE("Gambler's Monocle", 4, "+1 card drawn each turn", ArtifactType.CARD_SLOT, 1),
		VITALITYAMULET("Vitality Amulet", 4, "+20 max HP and heals 20 HP", ArtifactType.MAX_HEALTH, 20),
		SILVERRAPIER("Silver Rapier", 4, "+1.12x damage multiplier on winning hands", ArtifactType.DAMAGE_MULTIPLIER, 1.12),
		ROYALSIGNET("Royal Signet", 4, "+30% victory gold", ArtifactType.GOLD_MULTIPLIER, 1.30),
		FORGEDBUCKLER("Forged Buckler", 4, "-4 incoming damage taken", ArtifactType.DAMAGE_REDUCTION, 4),
		ASHENMANTLE("Ashen Mantle", 4, "Reduces Desert/Volcano heat damage by 2", ArtifactType.HEAT_REDUCTION, 2),
		FATESCOIN("Fate's Coin", 4, "+1 reroll per battle", ArtifactType.REROLL, 1),
		ECHOCRYSTAL("Echo Crystal", 4, "Winning-hand sigils trigger twice", ArtifactType.DOUBLE_SIGIL, 2),
		BOTTLEOFWHISKEY("Bottle of Whiskey", 3, "-2 incoming damage taken", ArtifactType.DAMAGE_REDUCTION, 2),
		ALLIN("All-in!", 3, "+25% victory gold", ArtifactType.GOLD_MULTIPLIER, 1.25),
		RESONATOR("Resonator", 3, "Winning-hand sigils trigger twice", ArtifactType.DOUBLE_SIGIL, 2),
		GREEDCURSE("Greed Curse", 3, "+50% victory gold", ArtifactType.GOLD_MULTIPLIER, 1.5),
		BLUNTSWORD("Blunt Sword", 3, "+1.10x damage multiplier on winning hands", ArtifactType.DAMAGE_MULTIPLIER, 1.10),
		BLACKSMITHGLOVES("Blacksmith Gloves", 3, "Reduces Desert/Volcano heat damage by 1", ArtifactType.HEAT_REDUCTION, 1),
		HAPPYFACES("Happy Faces", 3, "+15% victory gold", ArtifactType.GOLD_MULTIPLIER, 1.15),
		RUSTEDCOIN("Rusted Coin", 3, "+10% victory gold", ArtifactType.GOLD_MULTIPLIER, 1.10),
		CRACKEDSHIELD("Cracked Shield", 3, "-1 incoming damage taken", ArtifactType.DAMAGE_REDUCTION, 1),
		IRONDAGGER("Iron Dagger", 3, "+1.05x damage multiplier on winning hands", ArtifactType.DAMAGE_MULTIPLIER, 1.05),
		TRAVELERSRATIONS("Traveler's Rations", 3, "+10 max HP and heals 10 HP", ArtifactType.MAX_HEALTH, 10),
		LOADEDDIE("Loaded Die", 3, "+1 reroll per battle", ArtifactType.REROLL, 1),
		SLEEVEPOCKET("Sleeve Pocket", 3, "+1 card drawn each turn", ArtifactType.CARD_SLOT, 1),
		WATERSKIN("Waterskin", 3, "Reduces Desert/Volcano heat damage by 1", ArtifactType.HEAT_REDUCTION, 1),
		COPPERRING("Copper Ring", 3, "+1.08x damage multiplier on winning hands", ArtifactType.DAMAGE_MULTIPLIER, 1.08),
		MERCHANTSLEDGER("Merchant's Ledger", 3, "+20% victory gold", ArtifactType.GOLD_MULTIPLIER, 1.20),
		HEARTYSTEW("Hearty Stew", 3, "+15 max HP and heals 15 HP", ArtifactType.MAX_HEALTH, 15);

	public enum ArtifactType {
		DAMAGE_MULTIPLIER,
		GOLD_MULTIPLIER,
		MAX_HEALTH,
		REROLL,
		CARD_SLOT,
		BUST_LIMIT,
		DAMAGE_REDUCTION,
		DOUBLE_SIGIL,
		HEAT_REDUCTION
	}

	private final String name;
	private final int rating;
	private final String effect;
	private final ArtifactType type;
	private final double value;

	Artifacts(String name, int rating, String effect, ArtifactType type, double value) {
		this.name = name;
		this.rating = rating;
		this.effect = effect;
		this.type = type;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public int getRating() {
		return rating;
	}

	public String getEffect() {
		return effect;
	}

	public void apply(Player player) {
		switch (type) {
		case DAMAGE_MULTIPLIER:
			player.setArtifactDamageMultiplier(player.getArtifactDamageMultiplier() * value);
			break;
		case GOLD_MULTIPLIER:
			player.setArtifactGoldMultiplier(player.getArtifactGoldMultiplier() * value);
			break;
		case MAX_HEALTH:
			player.increaseMaxHealthpoint((int) value);
			break;
		case REROLL:
			player.addBonusRerolls((int) value);
			break;
		case CARD_SLOT:
			player.setCardSlot(player.getCardSlot() + (int) value);
			break;
		case BUST_LIMIT:
			player.setBustLimit(player.getBustLimit() + (int) value);
			break;
		case DAMAGE_REDUCTION:
			player.setArtifactDamageReduction(player.getArtifactDamageReduction() + (int) value);
			break;
		case DOUBLE_SIGIL:
			player.setDoubleSigils(true);
			break;
		case HEAT_REDUCTION:
			player.setArtifactHeatReduction(player.getArtifactHeatReduction() + (int) value);
			break;
		}
	}

	public static Artifacts sortByRarity(int rarity) {
		ArrayList<Artifacts> matches = new ArrayList<>();
		for (Artifacts artifact : values()) {
			if (artifact.rating == rarity) {
				matches.add(artifact);
			}
		}
		if (matches.isEmpty()) {
			return values()[0];
		}
		return matches.get(Rng.nextInt(matches.size()));
	}
}