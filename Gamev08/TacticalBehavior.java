package Gamev08;

import java.util.Scanner;

public class TacticalBehavior extends EnemyBehavior {
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
