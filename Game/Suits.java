package Game;

public enum Suits {
	DIAMONDS("Diamonds"),
	HEARTS("Hearts"),
	SPADES("Spades"),
	CLUBS("Clubs");
	
	private String suit;
	
	Suits(String suit){
		this.suit = suit;
	}
	
	public String getSuit() {
		return this.suit;
	}
}
