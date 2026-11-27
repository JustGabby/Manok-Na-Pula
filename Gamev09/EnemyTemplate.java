package Gamev09;

import java.util.ArrayList;

public class EnemyTemplate {
	private final String family;
	private final String variant;
	private final String name;
	private final int tier;
	private final int baseHealthpoint;
	private final int cardSlot;
	private final int level;
	private final int bonusDamage;
	private final ArrayList<Terrain> terrains;
	private final int weight;
	private final int minStage;
	private final boolean bossEligible;

	public EnemyTemplate(String family, String variant, String name, int tier, int baseHealthpoint,
			int cardSlot, int level, int bonusDamage, int weight, boolean bossEligible,
			ArrayList<Terrain> terrains) {
		this.family = family;
		this.variant = variant;
		this.name = name;
		this.tier = tier;
		this.baseHealthpoint = baseHealthpoint;
		this.cardSlot = cardSlot;
		this.level = level;
		this.bonusDamage = bonusDamage;
		this.weight = weight;
		this.bossEligible = bossEligible;
		this.terrains = new ArrayList<>(terrains);
		this.minStage = tier == 1 ? 1 : tier == 2 ? 3 : 6;
	}

	public String getFamily() {
		return family;
	}

	public String getName() {
		return name;
	}

	public String getLabel() {
		return family + " " + variant;
	}

	public ArrayList<Terrain> getTerrains() {
		return new ArrayList<>(terrains);
	}

	public int getWeight() {
		return weight;
	}

	public int getMinStage() {
		return minStage;
	}

	public boolean isBossEligible() {
		return bossEligible;
	}

	public Enemy createEnemy(int stage, boolean boss) {
		int healthpoint = baseHealthpoint + Math.max(0, stage - 1) * 10;
		int bonus = bonusDamage + stage / 5;
		String displayName = name;
		int deckTier = tier;
		if (boss) {
			healthpoint += 25 + stage * 5;
			bonus += 2;
			displayName = "BOSS: " + name;
			deckTier = 3;
		}
		Deck enemyDeck = Deck.createEnemyDeck(family, deckTier);
		return new Enemy(displayName, healthpoint, cardSlot, level, bonus, enemyDeck);
	}
}