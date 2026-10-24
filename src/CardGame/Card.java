package CardGame;

public class Card {
	private Rank rank;
	private Suits suit;
	
	public Card(Rank rank, Suits suit) {
		this.rank = rank;
		this.suit = suit;
	}

	public Rank getRank() {
		return rank;
	}

	public Suits getSuit() {
		return suit;
	}

	public String getCard() {
		return this.rank.getSymbol() + " of " +this.suit.getSuit();
	}
}
