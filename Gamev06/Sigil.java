package Gamev06;

public enum Sigil {
	SHARP("Sharp", 4, "bonus damage when played in a winning hand"),
	VAMPIRIC("Vampiric", 5, "HP healed when played in a winning hand"),
	MIDAS("Midas", 3, "bonus gold when played in a winning hand"),
	FORTIFIED("Fortified", 4, "damage blocked when you lose or bust"),
	LUCKY("Lucky", 2, "hand value reduction if the hand would bust");

	private final String displayName;
	private final int value;
	private final String effect;

	Sigil(String displayName, int value, String effect) {
		this.displayName = displayName;
		this.value = value;
		this.effect = effect;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getValue() {
		return value;
	}

	public String getDescription() {
		String prefix = (this == FORTIFIED || this == LUCKY) ? "-" : "+";
		return prefix + value + " " + effect;
	}

	public static Sigil getRandomSigil() {
		Sigil[] values = values();
		return values[Rng.nextInt(values.length)];
	}
}