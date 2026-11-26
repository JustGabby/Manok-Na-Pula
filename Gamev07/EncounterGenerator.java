package Gamev07;

import java.util.Random;

public class EncounterGenerator {
	private final long seed;
	private final Random random;
	private MonsterTemplate lastTemplate;

	public EncounterGenerator(long seed) {
		this.seed = seed;
		this.random = new Random(seed);
	}

	public Enemy generate(int stage) {
		return generate(stage, Terrain.forStage(stage));
	}

	public Enemy generate(int stage, Terrain terrain) {
		boolean boss = stage % 5 == 0;
		lastTemplate = MonsterRegistry.pick(terrain, stage, boss, random);
		return lastTemplate.createEnemy(stage, boss);
	}

	public MonsterTemplate getLastTemplate() {
		return lastTemplate;
	}

	public long getSeed() {
		return seed;
	}
}
