package Gamev06;

public enum HandPattern {
	NONE("", 1.0),
	PAIR("Pair", 1.2),
	TWO_PAIR("Two Pair", 1.35),
	THREE_OF_A_KIND("Three of a Kind", 1.5),
	STRAIGHT("Straight", 1.6),
	FLUSH("Flush", 1.75),
	FULL_HOUSE("Full House", 2.0),
	FOUR_OF_A_KIND("Four of a Kind", 2.2),
	FIVE_OF_A_KIND("Five of a Kind", 2.4),
	STRAIGHT_FLUSH("Straight Flush", 2.5),
	ROYAL_FLUSH("Royal Flush", 3.0);

	private final String label;
	private final double multiplier;

	HandPattern(String label, double multiplier) {
		this.label = label;
		this.multiplier = multiplier;
	}

	public String getLabel() {
		return label;
	}

	public double getMultiplier() {
		return multiplier;
	}

	public boolean hasBonus() {
		return this != NONE;
	}
}