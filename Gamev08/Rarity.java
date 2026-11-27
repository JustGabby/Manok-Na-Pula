package Gamev08;

public enum Rarity {
	COMMON("Common"),
	RARE("Rare"),
	LEGENDARY("Legendary");

	private final String displayName;

	Rarity(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
