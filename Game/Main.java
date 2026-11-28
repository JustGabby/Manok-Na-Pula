package Game;

public class Main {
	public static void main(String[] args) {
		Player player = new Player("James", 150, 6);
		Enemy enemy = new Enemy("Goblin", 100, 5, 1);
		Game game = new Game(player, enemy);

		player.gatherCardsToPlay();

		System.out.println("=== Your Hand ===");
		player.viewCards();

		System.out.println();
		player.playHand();

		System.out.println("\n=== Showdown ===");
		game.getPlayerPoints();

		System.out.println("\n=== Remaining Cards in Hand ===");
		player.viewCards();
		
		
	}
}
