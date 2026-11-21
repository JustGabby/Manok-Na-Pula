package Jacklore;

public class Card {
	private Rank rank;
	private Suit suit;
	private Sigil sigil;
	private boolean temporary;

	public Card(Rank rank, Suit suit) {
		this(rank, suit, null, false);
	}

	public Card(Rank rank, Suit suit, Sigil sigil) {
		this(rank, suit, sigil, false);
	}

	public Card(Rank rank, Suit suit, Sigil sigil, boolean temporary) {
		this.rank = rank;
		this.suit = suit;
		this.sigil = sigil;
		this.temporary = temporary;
	}

	public Rank getRank() {
		return rank;
	}

	public Suit getSuit() {
		return suit;
	}

	public Sigil getSigil() {
		return sigil;
	}

	public void setSigil(Sigil sigil) {
		this.sigil = sigil;
	}

	public boolean hasSigil() {
		return sigil != null;
	}

	public boolean isTemporary() {
		return temporary;
	}

	@Override
	public String toString() {
		String valueText = (rank == Rank.ACE) ? "1 / 11" : String.valueOf(rank.getValue());
		String text = rank + " of " + suit + " (" + valueText + ")";
		if (sigil != null) {
			text += " [" + sigil.getDisplayName() + "]";
		}
		if (temporary) {
			text += " (Temp)";
		}
		return text;
	}
}