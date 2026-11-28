package Game;

public class Enemy extends Entity{
	private int level;
	public Enemy(String name, int healthpoint, int cardSlot, int level) {
		super(name, healthpoint, cardSlot);
		this.level = level;
		super.getDeck();
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public void playHand() {
		// TODO Auto-generated method stub
		
	}
}