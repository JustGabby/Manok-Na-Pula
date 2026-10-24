package CardGame;

public enum Suits {
	DIAMOND("Diamond"),
	SPADES("Spades"),
	HEARTS("Hearts"),
	CLUBS("Clubs");
	
	private String suit;
	
	Suits(String suit){
		this.suit = suit;
	}
	
	public String getSuit() {
		return this.suit;
	}
}
