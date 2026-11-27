package Gamev08;

import java.util.Scanner;

public abstract class EnemyBehavior {
	public abstract String getArchetypeName();

	public abstract void playShowdownHand(Enemy enemy, Scanner sc);

	public abstract double getDoubleDownChance();

	public abstract int getHitThreshold();

	public abstract double getHitChance();

	public boolean drawsTwice() {
		return false;
	}

	public static EnemyBehavior forLevel(int level) {
		if (level <= 1) {
			return new RecklessBehavior();
		}
		if (level == 2) {
			return new CautiousBehavior();
		}
		return new TacticalBehavior();
	}
}
