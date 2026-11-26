package Gamev07;

import java.util.ArrayList;

public class MonsterTemplate {
	private final String family;
	private final String variant;
	private final String name;
	private final int tier;
	private final int baseHealthpoint;
	private final int cardSlot;
	private final int level;
	private final int bonusDamage;
	private final int goldReward;
	private final ArrayList<Terrain> terrains;
	private final int weight;
	private final int minStage;
	private final boolean bossEligible;

	public MonsterTemplate(String family, String variant, String name, int tier, int baseHealthpoint,
			int cardSlot, int level, int bonusDamage, int goldReward, int weight, boolean bossEligible,
			ArrayList<Terrain> terrains) {
		this.family = family;
		this.variant = variant;
		this.name = name;
		this.tier = tier;
		this.baseHealthpoint = baseHealthpoint;
		this.cardSlot = cardSlot;
		this.level = level;
		this.bonusDamage = bonusDamage;
		this.goldReward = goldReward;
		this.weight = weight;
		this.bossEligible = bossEligible;
		this.terrains = new ArrayList<Terrain>(terrains);
		this.minStage = tier == 1 ? 1 : tier == 2 ? 3 : 6;
	}

	public String getFamily() {
		return family;
	}

	public String getVariant() {
		return variant;
	}

	public String getName() {
		return name;
	}

	public String getLabel() {
		return family + " " + variant;
	}

	public int getTier() {
		return tier;
	}

	public int getBaseHealthpoint() {
		return baseHealthpoint;
	}

	public int getCardSlot() {
		return cardSlot;
	}

	public int getLevel() {
		return level;
	}

	public int getBonusDamage() {
		return bonusDamage;
	}

	public int getGoldReward() {
		return goldReward;
	}

	public ArrayList<Terrain> getTerrains() {
		return new ArrayList<Terrain>(terrains);
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
		if (boss) {
			healthpoint += 25 + stage * 5;
			bonus += 2;
			displayName = "BOSS: " + name;
		}
		return new Enemy(displayName, healthpoint, cardSlot, level, bonus);
	}
}
