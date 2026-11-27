package Gamev08;

import java.util.Random;

public class EncounterGenerator {
	private final Random random;
	private EnemyTemplate lastTemplate;

	public EncounterGenerator(long seed) {
		this.random = new Random(seed);
	}

	public Enemy generate(int stage) {
		return generate(stage, Terrain.forStage(stage));
	}

	public Enemy generate(int stage, Terrain terrain) {
		boolean boss = stage % 5 == 0;
		lastTemplate = EnemyRegistry.pick(terrain, stage, boss, random);
		return lastTemplate.createEnemy(stage, boss);
	}

	public EnemyTemplate getLastTemplate() {
		return lastTemplate;
	}
}