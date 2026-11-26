package Gamev06;

public enum Terrain {
	DESERT("Desert", "Heat strikes every 3 turns", 3, 2, 0),
	SWAMP("Swamp", "Your Fortified sigils are worth +1", 0, 0, 1);

	private final String name;
	private final String description;
	private final int triggerInterval;
	private final int damage;
	private final int fortifiedBonus;

	Terrain(String name, String description, int triggerInterval, int damage, int fortifiedBonus) {
		this.name = name;
		this.description = description;
		this.triggerInterval = triggerInterval;
		this.damage = damage;
		this.fortifiedBonus = fortifiedBonus;
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
