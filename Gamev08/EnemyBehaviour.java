package Gamev08;

import java.util.Scanner;

public abstract class EnemyBehaviour {
	public abstract String getArchetypeName();

	public abstract void playShowdownHand(Enemy enemy, Scanner sc);

	public abstract double getDoubleDownChance();

	public abstract int getHitThreshold();

	public abstract double getHitChance();

	public boolean drawsTwice() {
		return false;
	}

	public static EnemyBehaviour forLevel(int level) {
		if (level <= 1) {
			return new RecklessBehaviour();
		}
		if (level == 2) {
			return new CautiousBehaviour();
		}
		return new TacticalBehaviour();
	}

	public static class RecklessBehaviour extends EnemyBehaviour {
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

	public static class CautiousBehaviour extends EnemyBehaviour {
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

	public static class TacticalBehaviour extends EnemyBehaviour {
		@Override
		public String getArchetypeName() {
			return "Tactical";
		}

		@Override
		public void playShowdownHand(Enemy enemy, Scanner sc) {
			enemy.pickBestUntilBust();
		}

		@Override
		public double getDoubleDownChance() {
			return 0.65;
		}

		@Override
		public int getHitThreshold() {
			return 15;
		}

		@Override
		public double getHitChance() {
			return 1.0;
		}

		@Override
		public boolean drawsTwice() {
			return true;
		}
	}
}
