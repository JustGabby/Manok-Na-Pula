package Gamev08;

public enum Sigil {
	SHARP("Sharp", 5, "+5 bonus damage when played in a winning hand"),
	VAMPIRIC("Vampiric", 20, "Heals 20% of dealt damage when played in a winning hand"),
	MIDAS("Midas", 5, "+5 bonus gold when played in a winning hand"),
	FORTIFIED("Fortified", 3, "-3 damage received when played in a losing or busted hand"),
	LUCKY("Lucky", 2, "-2 hand value reduction if the hand would bust");

	private final String displayName;
	private final int value;
	private final String description;

	Sigil(String displayName, int value, String description) {
		this.displayName = displayName;
		this.value = value;
		this.description = description;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}

	public static Sigil getRandomSigil() {
		Sigil[] values = values();
		return values[Rng.nextInt(values.length)];
	}
}