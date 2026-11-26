package Gamev07;

import java.util.Scanner;

public class RecklessBehavior extends EnemyBehavior {
	@Override
	public String getArchetypeName() {
		return "Reckless";
	}

	@Override
	public void playShowdownHand(Enemy enemy, Scanner sc) {
		enemy.pickRandom();
	}

	@Override
	public double getDoubleDownChance() {
		return 0.25;
	}

	@Override
	public int getHitThreshold() {
		return 12;
	}

	@Override
	public double getHitChance() {
		return 0.50;
	}
}
