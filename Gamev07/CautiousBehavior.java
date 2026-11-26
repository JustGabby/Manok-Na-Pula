package Gamev07;

import java.util.Scanner;

public class CautiousBehavior extends EnemyBehavior {
	@Override
	public String getArchetypeName() {
		return "Cautious";
	}

	@Override
	public void playShowdownHand(Enemy enemy, Scanner sc) {
		enemy.pickUnderTarget();
	}

	@Override
	public double getDoubleDownChance() {
		return 0.45;
	}

	@Override
	public int getHitThreshold() {
		return 14;
	}

	@Override
	public double getHitChance() {
		return 0.80;
	}
}
