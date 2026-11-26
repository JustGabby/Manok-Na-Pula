package Gamev07;

public enum Terrain {
	FOREST("Forest", "Enemy winning hands deal +2 damage", 0, 0, 0, 2, 0),
	SWAMP("Swamp", "Your Fortified sigils are worth +1", 0, 0, 1, 0, 0),
	DESERT("Desert", "Heat strikes every 3 turns", 3, 2, 0, 0, 0),
	VOLCANO("Volcano", "Heat strikes every 2 turns for 3 damage", 2, 3, 0, 0, 0),
	RUINS("Ruins", "Winning hands grant +2 bonus gold", 0, 0, 0, 0, 2);

	private final String name;
	private final String description;
	private final int triggerInterval;
	private final int damage;
	private final int fortifiedBonus;
	private final int enemyBonusDamage;
	private final int goldBonus;

	Terrain(String name, String description, int triggerInterval, int damage, int fortifiedBonus, int enemyBonusDamage, int goldBonus) {
		this.name = name;
		this.description = description;
		this.triggerInterval = triggerInterval;
		this.damage = damage;
		this.fortifiedBonus = fortifiedBonus;
		this.enemyBonusDamage = enemyBonusDamage;
		this.goldBonus = goldBonus;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getTriggerInterval() {
		return triggerInterval;
	}

	public int getDamage() {
		return damage;
	}

	public int getFortifiedBonus() {
		return fortifiedBonus;
	}

	public int getEnemyBonusDamage() {
		return enemyBonusDamage;
	}

	public int getGoldBonus() {
		return goldBonus;
	}

	public int getTurnsUntilTrigger(int turnsTaken) {
		if (triggerInterval <= 0) {
			return 0;
		}
		return triggerInterval - (turnsTaken % triggerInterval);
	}

	public static Terrain forStage(int stage) {
		return values()[Math.floorMod(stage - 1, values().length)];
	}
}
