package Gamev06;

import java.util.ArrayList;

public enum Artifacts {
	HANDOFGOD("Hand of God", 6, "*10 damage multiplier on winning hands", ArtifactType.DAMAGE_MULTIPLIER, 10),
	RAGNAROK("Ragnarok", 6, "*12 damage multiplier on winning hands", ArtifactType.DAMAGE_MULTIPLIER, 12),
	GOLDENFRUIT("Golden Fruit", 6, "+50 max HP and heals 50 HP", ArtifactType.MAX_HEALTH, 50),
	JOKER8("Joker8", 5, "*3 damage multiplier on winning hands", ArtifactType.DAMAGE_MULTIPLIER, 3),
	FATEWEAVER("Fate Weaver", 5, "+4 cards drawn each turn", ArtifactType.CARD_SLOT, 4),
	GRIMOIRE("Grimoire", 5, "*5 damage multiplier on winning hands", ArtifactType.DAMAGE_MULTIPLIER, 5),
	PARADOX("Paradox", 4, "*2 damage multiplier on winning hands", ArtifactType.DAMAGE_MULTIPLIER, 2),
	KINGSHAND("King's Hand", 4, "+4 cards drawn each turn", ArtifactType.CARD_SLOT, 4),
	NOCTURNE("Nocturne", 4, "*2 damage multiplier on winning hands", ArtifactType.DAMAGE_MULTIPLIER, 2),
	BOTTLEOFWHISKEY("Bottle of Whiskey", 3, "*3 damage multiplier on winning hands", ArtifactType.DAMAGE_MULTIPLIER, 3),
	LOOSESTRING("Loose String", 3, "+3 bust limit", ArtifactType.BUST_LIMIT, 3),
	ALLIN("All-in!", 3, "+50% victory gold", ArtifactType.GOLD_MULTIPLIER, 1.5),
	RESONATOR("Resonator", 3, "Played sigils trigger twice", ArtifactType.DOUBLE_SIGIL, 2),
	GREEDCURSE("Greed Curse", 3, "+150% victory gold", ArtifactType.GOLD_MULTIPLIER, 2.5),
	DESERT_CLOAK("Desert Cloak", 4, "Reduces Desert heat damage by 2", ArtifactType.HEAT_REDUCTION, 2),
	HAPPYFACES("Happy Faces", 3, "+20% victory gold", ArtifactType.GOLD_MULTIPLIER, 1.2);

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

	public ArtifactType getType() {
		return type;
	}

	public double getValue() {
		return value;
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
			player.setReroll(player.getReroll() + (int) value);
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
		default:
			break;
		}
	}

	public static Artifacts sortByRarity(int rarity) {
		ArrayList<Artifacts> set = new ArrayList<>();

		for (Artifacts data : values()) {
			if (data.rating == rarity) {
				set.add(data);
			}
		}

		if (set.isEmpty()) {
			return values()[0];
		}

		return set.get(Rng.range(0, set.size() - 1));
	}
}
