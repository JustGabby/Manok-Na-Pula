package Gamev07;

import java.util.ArrayList;
import java.util.Random;

public class MonsterRegistry {
	private static final ArrayList<MonsterTemplate> ALL = new ArrayList<MonsterTemplate>();

	static {
		add("Goblin", "Scavenger", "Goblin Scavenger", 1, 90, 4, 1, 0, 8, 5, false,
				Terrain.FOREST, Terrain.DESERT, Terrain.RUINS);
		add("Goblin", "Raider", "Goblin Raider", 2, 112, 4, 2, 1, 13, 3, false,
				Terrain.FOREST, Terrain.DESERT, Terrain.RUINS);
		add("Goblin", "Shaman", "Goblin Shaman", 3, 140, 5, 3, 3, 22, 1, true,
				Terrain.FOREST, Terrain.SWAMP, Terrain.RUINS);

		add("Orc", "Boy", "Orc Boy", 1, 100, 4, 1, 1, 10, 5, false,
				Terrain.FOREST, Terrain.DESERT, Terrain.RUINS);
		add("Orc", "Arrer Boy", "Arrer Boy", 2, 118, 4, 2, 2, 15, 3, false,
				Terrain.FOREST, Terrain.DESERT, Terrain.RUINS);
		add("Orc", "Black Orc", "Black Orc", 3, 158, 5, 3, 4, 26, 1, true,
				Terrain.RUINS, Terrain.DESERT);

		add("Skeleton", "Rattling", "Rattling Skeleton", 1, 92, 4, 1, 0, 9, 5, false,
				Terrain.SWAMP, Terrain.RUINS);
		add("Skeleton", "Guard", "Skeleton Guard", 2, 120, 4, 2, 1, 14, 3, false,
				Terrain.SWAMP, Terrain.RUINS);
		add("Skeleton", "Champion", "Skeleton Champion", 3, 150, 5, 3, 3, 24, 1, true,
				Terrain.SWAMP, Terrain.RUINS);

		add("Slime", "Green", "Green Slime", 1, 88, 4, 1, 0, 8, 5, false,
				Terrain.FOREST, Terrain.SWAMP);
		add("Slime", "Marsh", "Marsh Slime", 2, 116, 4, 2, 1, 13, 3, false,
				Terrain.FOREST, Terrain.SWAMP);
		add("Slime", "Acid", "Acid Slime", 3, 142, 5, 3, 3, 22, 1, true,
				Terrain.SWAMP, Terrain.DESERT);

		add("Beast", "Forest Wolf", "Forest Wolf", 1, 96, 4, 1, 1, 10, 5, false,
				Terrain.FOREST);
		add("Beast", "Dire Wolf", "Dire Wolf", 2, 124, 4, 2, 2, 16, 3, false,
				Terrain.FOREST, Terrain.SWAMP);
		add("Beast", "Alpha Wolf", "Alpha Wolf", 3, 150, 5, 3, 4, 25, 1, true,
				Terrain.FOREST, Terrain.RUINS);

		add("Insect", "Sand Scarab", "Sand Scarab", 1, 90, 4, 1, 0, 9, 5, false,
				Terrain.DESERT);
		add("Insect", "Dune Scarab", "Dune Scarab", 2, 118, 4, 2, 2, 15, 3, false,
				Terrain.DESERT);
		add("Insect", "Scarab Queen", "Scarab Queen", 3, 146, 5, 3, 3, 24, 1, true,
				Terrain.DESERT, Terrain.SWAMP);

		add("Imp", "Cinder", "Cinder Imp", 1, 94, 4, 1, 1, 10, 5, false,
				Terrain.VOLCANO);
		add("Imp", "Ash", "Ash Imp", 2, 122, 4, 2, 2, 16, 3, false,
				Terrain.VOLCANO, Terrain.DESERT);
		add("Imp", "Magma", "Magma Imp", 3, 155, 5, 3, 4, 27, 1, true,
				Terrain.VOLCANO);

		add("Golem", "Stone", "Stone Golem", 1, 110, 4, 1, 1, 12, 5, false,
				Terrain.VOLCANO, Terrain.RUINS);
		add("Golem", "Rune", "Rune Golem", 2, 132, 4, 2, 2, 18, 3, false,
				Terrain.VOLCANO, Terrain.RUINS);
		add("Golem", "Colossus Fragment", "Colossus Fragment", 3, 165, 5, 3, 5, 30, 1, true,
				Terrain.VOLCANO, Terrain.RUINS);

		add("Wraith", "Lost", "Lost Wraith", 1, 92, 4, 1, 1, 10, 5, false,
				Terrain.SWAMP, Terrain.RUINS);
		add("Wraith", "Grave", "Grave Wraith", 2, 120, 4, 2, 2, 16, 3, false,
				Terrain.SWAMP, Terrain.RUINS);
		add("Wraith", "Dread", "Dread Wraith", 3, 150, 5, 3, 4, 26, 1, true,
				Terrain.SWAMP, Terrain.RUINS);

		add("Dragon", "Whelp", "Dragon Whelp", 1, 105, 4, 1, 2, 12, 5, false,
				Terrain.FOREST, Terrain.VOLCANO, Terrain.DESERT);
		add("Dragon", "Young", "Young Dragon", 2, 135, 5, 2, 3, 20, 3, false,
				Terrain.VOLCANO, Terrain.DESERT, Terrain.FOREST);
		add("Dragon", "Elder", "Elder Dragon", 3, 175, 5, 3, 6, 35, 1, true,
				Terrain.VOLCANO, Terrain.RUINS, Terrain.DESERT);
	}

	private static void add(String family, String variant, String name, int tier, int baseHealthpoint,
			int cardSlot, int level, int bonusDamage, int goldReward, int weight, boolean bossEligible,
			Terrain... terrainArray) {
		ArrayList<Terrain> terrains = new ArrayList<Terrain>();
		for (Terrain terrain : terrainArray) {
			terrains.add(terrain);
		}
		ALL.add(new MonsterTemplate(family, variant, name, tier, baseHealthpoint, cardSlot, level,
				bonusDamage, goldReward, weight, bossEligible, terrains));
	}

	public static ArrayList<MonsterTemplate> all() {
		return new ArrayList<MonsterTemplate>(ALL);
	}

	public static MonsterTemplate findByName(String name) {
		for (MonsterTemplate template : ALL) {
			if (template.getName().equals(name)) {
				return template;
			}
		}
		return null;
	}

	public static int getFamilyCount() {
		ArrayList<String> families = new ArrayList<String>();
		for (MonsterTemplate template : ALL) {
			if (!families.contains(template.getFamily())) {
				families.add(template.getFamily());
			}
		}
		return families.size();
	}

	public static MonsterTemplate pick(Terrain terrain, int stage, boolean boss, Random random) {
		ArrayList<MonsterTemplate> pool = buildPool(terrain, stage, boss);
		if (pool.isEmpty()) {
			pool = buildPool(null, stage, boss);
		}
		if (pool.isEmpty()) {
			return ALL.get(0);
		}
		int totalWeight = 0;
		for (MonsterTemplate template : pool) {
			totalWeight += template.getWeight();
		}
		int roll = random.nextInt(totalWeight);
		for (MonsterTemplate template : pool) {
			roll -= template.getWeight();
			if (roll < 0) {
				return template;
			}
		}
		return pool.get(pool.size() - 1);
	}

	private static ArrayList<MonsterTemplate> buildPool(Terrain terrain, int stage, boolean boss) {
		ArrayList<MonsterTemplate> pool = new ArrayList<MonsterTemplate>();
		for (MonsterTemplate template : ALL) {
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
