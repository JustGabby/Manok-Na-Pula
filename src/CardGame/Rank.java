package CardGame;

public enum Rank {
	TWO("2"),
	THREE("3"),
	FOUR("4"),
	FIVE("5"),
	SIX("6"),
	SEVEN("7"),
	EIGHT("8"),
	NINE("9"),
	TEN("10"),
	ACE("A"),
	KING("K"),
	QUEEN("Q"),
	JACK("J");
	
	private String symbol;
	
	Rank(String symbol){
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return this.symbol;
	}
}
