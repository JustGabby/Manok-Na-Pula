package Gamev09;

import java.util.ArrayList;
import java.util.Random;

public class EnemyRegistry {
	private static final ArrayList<EnemyTemplate> ALL = new ArrayList<>();

	static {
		add("Goblin", "Scavenger", "Goblin Scavenger", 1, 90, 4, 1, 0, 5, false,
				Terrain.FOREST, Terrain.DESERT, Terrain.RUINS);
		add("Goblin", "Raider", "Goblin Raider", 2, 112, 4, 2, 1, 3, false,
				Terrain.FOREST, Terrain.DESERT, Terrain.RUINS);
		add("Goblin", "Shaman", "Goblin Shaman", 3, 140, 5, 3, 3, 1, true,
				Terrain.FOREST, Terrain.SWAMP, Terrain.RUINS);

		add("Orc", "Boy", "Orc Boy", 1, 100, 4, 1, 1, 5, false,
				Terrain.FOREST, Terrain.DESERT, Terrain.RUINS);
		add("Orc", "Arrer Boy", "Arrer Boy", 2, 118, 4, 2, 2, 3, false,
				Terrain.FOREST, Terrain.DESERT, Terrain.RUINS);
		add("Orc", "Black Orc", "Black Orc", 3, 158, 5, 3, 4, 1, true,
				Terrain.RUINS, Terrain.DESERT);

		add("Skeleton", "Rattling", "Rattling Skeleton", 1, 92, 4, 1, 0, 5, false,
				Terrain.SWAMP, Terrain.RUINS);
		add("Skeleton", "Guard", "Skeleton Guard", 2, 120, 4, 2, 1, 3, false,
				Terrain.SWAMP, Terrain.RUINS);
		add("Skeleton", "Champion", "Skeleton Champion", 3, 150, 5, 3, 3, 1, true,
				Terrain.SWAMP, Terrain.RUINS);

		add("Slime", "Green", "Green Slime", 1, 88, 4, 1, 0, 5, false,
				Terrain.FOREST, Terrain.SWAMP);
		add("Slime", "Marsh", "Marsh Slime", 2, 116, 4, 2, 1, 3, false,
				Terrain.FOREST, Terrain.SWAMP);
		add("Slime", "Acid", "Acid Slime", 3, 142, 5, 3, 3, 1, true,
				Terrain.SWAMP, Terrain.DESERT);

		add("Beast", "Forest Wolf", "Forest Wolf", 1, 96, 4, 1, 1, 5, false,
				Terrain.FOREST);
		add("Beast", "Dire Wolf", "Dire Wolf", 2, 124, 4, 2, 2, 3, false,
				Terrain.FOREST, Terrain.SWAMP);
		add("Beast", "Alpha Wolf", "Alpha Wolf", 3, 150, 5, 3, 4, 1, true,
				Terrain.FOREST, Terrain.RUINS);

		add("Insect", "Sand Scarab", "Sand Scarab", 1, 90, 4, 1, 0, 5, false,
				Terrain.DESERT);
		add("Insect", "Dune Scarab", "Dune Scarab", 2, 118, 4, 2, 2, 3, false,
				Terrain.DESERT);
		add("Insect", "Scarab Queen", "Scarab Queen", 3, 146, 5, 3, 3, 1, true,
				Terrain.DESERT, Terrain.SWAMP);

		add("Imp", "Cinder", "Cinder Imp", 1, 94, 4, 1, 1, 5, false,
				Terrain.VOLCANO);
		add("Imp", "Ash", "Ash Imp", 2, 122, 4, 2, 2, 3, false,
				Terrain.VOLCANO, Terrain.DESERT);
		add("Imp", "Magma", "Magma Imp", 3, 155, 5, 3, 4, 1, true,
				Terrain.VOLCANO);

		add("Golem", "Stone", "Stone Golem", 1, 110, 4, 1, 1, 5, false,
				Terrain.VOLCANO, Terrain.RUINS);
		add("Golem", "Rune", "Rune Golem", 2, 132, 4, 2, 2, 3, false,
				Terrain.VOLCANO, Terrain.RUINS);
		add("Golem", "Colossus Fragment", "Colossus Fragment", 3, 165, 5, 3, 5, 1, true,
				Terrain.VOLCANO, Terrain.RUINS);

		add("Wraith", "Lost", "Lost Wraith", 1, 92, 4, 1, 1, 5, false,
				Terrain.SWAMP, Terrain.RUINS);
		add("Wraith", "Grave", "Grave Wraith", 2, 120, 4, 2, 2, 3, false,
				Terrain.SWAMP, Terrain.RUINS);
		add("Wraith", "Dread", "Dread Wraith", 3, 150, 5, 3, 4, 1, true,
				Terrain.SWAMP, Terrain.RUINS);

		add("Dragon", "Whelp", "Dragon Whelp", 1, 105, 4, 1, 2, 5, false,
				Terrain.FOREST, Terrain.VOLCANO, Terrain.DESERT);
		add("Dragon", "Young", "Young Dragon", 2, 135, 5, 2, 3, 3, false,
				Terrain.VOLCANO, Terrain.DESERT, Terrain.FOREST);
		add("Dragon", "Elder", "Elder Dragon", 3, 175, 5, 3, 6, 1, true,
				Terrain.VOLCANO, Terrain.RUINS, Terrain.DESERT);
	}

	private static void add(String family, String variant, String name, int tier, int baseHealthpoint,
			int cardSlot, int level, int bonusDamage, int weight, boolean bossEligible,
			Terrain... terrainArray) {
		ArrayList<Terrain> terrains = new ArrayList<>();
		for (Terrain terrain : terrainArray) {
			terrains.add(terrain);
		}
		ALL.add(new EnemyTemplate(family, variant, name, tier, baseHealthpoint, cardSlot, level,
				bonusDamage, weight, bossEligible, terrains));
	}

	public static EnemyTemplate pick(Terrain terrain, int stage, boolean boss, Random random) {
		ArrayList<EnemyTemplate> pool = buildPool(terrain, stage, boss);
		if (pool.isEmpty()) {
			pool = buildPool(null, stage, boss);
		}
		if (pool.isEmpty()) {
			return ALL.get(0);
		}
		int totalWeight = 0;
		for (EnemyTemplate template : pool) {
			totalWeight += template.getWeight();
		}
		int roll = random.nextInt(totalWeight);
		for (EnemyTemplate template : pool) {
			roll -= template.getWeight();
			if (roll < 0) {
				return template;
			}
		}
		return pool.get(pool.size() - 1);
	}

	private static ArrayList<EnemyTemplate> buildPool(Terrain terrain, int stage, boolean boss) {
		ArrayList<EnemyTemplate> pool = new ArrayList<>();
		for (EnemyTemplate template : ALL) {
			if (terrain != null && !template.getTerrains().contains(terrain)) {
				continue;
			}
			if (boss) {
				if (!template.isBossEligible()) {
					continue;
				}
			} else if (stage < template.getMinStage()) {
				continue;
			}
			pool.add(template);
		}
		return pool;
	}
}