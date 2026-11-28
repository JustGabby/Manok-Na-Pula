package Gamev02;

public class Game {
	private Player player;
	private Enemy enemy;

	public Game(Player player, Enemy enemy) {
		this.player = player;
		this.enemy = enemy;
	}

	public Player getPlayer() {
		return player;
	}

	public Enemy getEnemy() {
		return enemy;
	}

	public void displayShowdown(Entity entity) {
		if (!entity.getShowdownHand().isEmpty()) {
			if (entity == player) {
				System.out.println("Played Hand:");
			} else {
				System.out.println("Enemy Hand:");
			}
			for (Card card : entity.getShowdownHand()) {
				System.out.println(card.toString());
			}

			System.out.println("Total Hand Value: " + entity.calculateHandValue(entity.getShowdownHand()));
		} else {
			System.out.println("No hand played!");
		}
		entity.clearShowdownHand();
	}
	
	public void playerLoss() {
		int updatedHealthPoint = this.player.getHealthpoint() - this.enemy.getDamage();
		player.setHealthpoint(updatedHealthPoint);
	}
	
	public void enemyLoss() {
		int updatedHealthPoint = this.enemy.getHealthpoint() - this.player.getDamage();
		enemy.setHealthpoint(updatedHealthPoint);
	}
	
	public void playerWon() {
		int presentGold = this.player.getGold();
		presentGold *= this.enemy.getLevel();
		
		this.player.setGold(presentGold);
	}
	
	public void showdown() {
		int playerPoint = this.player.calculateHandValue(player.getShowdownHand());
		int enemyPoint = this.enemy.calculateHandValue(enemy.getShowdownHand());
		
		displayShowdown(this.player);
		displayShowdown(this.enemy);
		
		if (playerPoint > player.getBustLimit()) {
			System.out.println("Hand lost!");
			playerLoss();
			System.out.println("HP: " + this.player.getHealthpoint());
		} else if(enemyPoint > enemy.getBustLimit() || playerPoint >= enemyPoint) {
			System.out.println("Hand won!");
			enemyLoss();
			System.out.println("Enemy HP: " + this.enemy.getHealthpoint());
		} else {
			System.out.println("Hand lost!");
			playerLoss();
		}
	}
}
