package Gamev08;

import java.util.ArrayList;
import java.util.Scanner;

public class Player extends Entity {
	private int gold;
	private int reroll;
	private int bonusRerolls;
	private int arc;
	private int enemiesDefeated;
	private ArrayList<Item> inventory;
	private ArrayList<Artifacts> artifacts;
	private double artifactDamageMultiplier = 1.0;
	private double artifactGoldMultiplier = 1.0;
	private int artifactDamageReduction = 0;
	private int artifactHeatReduction = 0;
	private int tempBonusDamage = 0;
	private int tempDamageReduction = 0;
	private double tempGoldBonus = 0.0;
	private int heatShieldTicks = 0;
	private boolean doubleSigils = false;
	private int handWinStreak = 0;
	private final String id;
	private static int idCounter = 1000;

	public Player(String name) {
		super(name, 150, 4, 0);
		this.gold = 15;
		this.reroll = 3;
		this.bonusRerolls = 0;
		this.arc = 1;
		this.enemiesDefeated = 0;
		this.inventory = new ArrayList<>();
		this.artifacts = new ArrayList<>();
		this.id = generateId();
	}

	public int getReroll() {
		return reroll;
	}

	public void setReroll(int reroll) {
		this.reroll = reroll;
	}

	public void addBonusRerolls(int amount) {
		if (amount > 0) {
			this.bonusRerolls += amount;
			this.reroll += amount;
		}
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = Math.max(0, gold);
	}

	public void addGold(int amount) {
		if (amount > 0) {
			this.gold += amount;
		}
	}

	public int getHandWinStreak() {
		return handWinStreak;
	}

	public void increaseHandWinStreak() {
		handWinStreak++;
	}

	public void resetHandWinStreak() {
		handWinStreak = 0;
	}

	public int getArc() {
		return arc;
	}

	public void setArc(int arc) {
		this.arc = arc;
	}

	public int getEnemiesDefeated() {
		return enemiesDefeated;
	}

	public void setEnemiesDefeated(int enemiesDefeated) {
		this.enemiesDefeated = enemiesDefeated;
	}

	public ArrayList<Item> getInventory() {
		return inventory;
	}

	public void addItem(Item item) {
		if (item != null) {
			inventory.add(item);
		}
	}

	public boolean hasArtifact(Artifacts artifact) {
		return artifacts.contains(artifact);
	}

	public boolean addArtifact(Artifacts artifact) {
		if (artifact == null || artifacts.contains(artifact)) {
			return false;
		}
		artifacts.add(artifact);
		artifact.apply(this);
		return true;
	}

	public ArrayList<Artifacts> getArtifacts() {
		return artifacts;
	}

	public double getArtifactDamageMultiplier() {
		return artifactDamageMultiplier;
	}

	public void setArtifactDamageMultiplier(double artifactDamageMultiplier) {
		this.artifactDamageMultiplier = artifactDamageMultiplier;
	}

	public double getArtifactGoldMultiplier() {
		return artifactGoldMultiplier;
	}

	public void setArtifactGoldMultiplier(double artifactGoldMultiplier) {
		this.artifactGoldMultiplier = artifactGoldMultiplier;
	}

	public int getArtifactDamageReduction() {
		return artifactDamageReduction;
	}

	public void setArtifactDamageReduction(int artifactDamageReduction) {
		this.artifactDamageReduction = artifactDamageReduction;
	}

	public int getArtifactHeatReduction() {
		return artifactHeatReduction;
	}

	public void setArtifactHeatReduction(int artifactHeatReduction) {
		this.artifactHeatReduction = Math.max(0, artifactHeatReduction);
	}

	public void addTempBonusDamage(int amount) {
		if (amount > 0) {
			this.tempBonusDamage += amount;
		}
	}

	public void addTempDamageReduction(int amount) {
		if (amount > 0) {
			this.tempDamageReduction += amount;
		}
	}

	public void addTempGoldBonus(double amount) {
		if (amount > 0) {
			this.tempGoldBonus += amount;
		}
	}

	public int getTotalBonusDamage() {
		return getBonusDamage() + tempBonusDamage;
	}

	public int getTotalDamageReduction() {
		return artifactDamageReduction + tempDamageReduction;
	}

	public double getTotalGoldMultiplier() {
		return artifactGoldMultiplier + tempGoldBonus;
	}

	@Override
	public int calculateTotalDamage(int winnerScore, int loserScore) {
		int difference = Math.max(0, winnerScore - loserScore);
		return winnerScore + difference + getTotalBonusDamage();
	}

	@Override
	public int calculateBustDamage(int winnerScore) {
		int cappedBonus = Math.min(winnerScore, 6);
		return winnerScore + cappedBonus + getTotalBonusDamage();
	}

	public void clearTemporaryBattleEffects() {
		tempBonusDamage = 0;
		tempDamageReduction = 0;
		tempGoldBonus = 0.0;
		heatShieldTicks = 0;
		restoreRerolls();
	}

	public int getHeatShieldTicks() {
		return heatShieldTicks;
	}

	public void setHeatShieldTicks(int heatShieldTicks) {
		this.heatShieldTicks = Math.max(0, heatShieldTicks);
	}

	public void useHeatShieldTick() {
		if (heatShieldTicks > 0) {
			heatShieldTicks--;
		}
	}

	public boolean isDoubleSigils() {
		return doubleSigils;
	}

	public void setDoubleSigils(boolean doubleSigils) {
		this.doubleSigils = doubleSigils;
	}

	public boolean useItem(int index) {
		if (index < 0 || index >= inventory.size()) {
			return false;
		}
		Item item = inventory.remove(index);
		item.use(this);
		return true;
	}

	@Override
	public void playShowdownHand(Scanner sc) {
		while (true) {
			System.out.print("Pick at least 2 cards (r to reroll): ");
			String line = sc.nextLine().trim();

			if (line.isEmpty()) {
				System.out.println("No cards selected. Try again.");
				continue;
			}

			if (line.equalsIgnoreCase("r")) {
				if (reroll > 0) {
					super.rerollHand();
					reroll--;
					System.out.println("\n!--" + getReroll() + " reroll(s) left--!");
					viewHand();
				} else {
					System.out.println("\n!--No more rerolls left--!\n");
				}
			} else {
				String[] split = line.split("[,\\s]+");
				ArrayList<Integer> selectedCards = new ArrayList<>();
				boolean valid = true;

				for (String token : split) {
					if (!token.matches("[1-9]\\d?")) {
						System.out.println("Please only input valid card numbers.");
						valid = false;
						break;
					}
					int choice = Integer.parseInt(token);
					if (choice < 1 || choice > getHand().size()) {
						System.out.println("Card number " + choice + " is out of range (1-" + getHand().size() + ").");
						valid = false;
						break;
					}
					if (!selectedCards.contains(choice)) {
						selectedCards.add(choice);
					}
				}

				if (valid && getHand().size() >= 2 && selectedCards.size() < 2) {
					System.out.println("You must select at least 2 different cards!");
					valid = false;
				}

				if (valid) {
					selectCards(selectedCards);
					break;
				}
			}
		}
	}

	private void blackjackActionPhase(Scanner sc) {
		boolean canDoubleDown = true;

		while (true) {
			int currentVal = calculateHandValue(getShowdownHand());
			HandPattern pattern = HandEvaluator.evaluate(getShowdownHand());
			String patternTag = pattern.hasBonus() ? " [" + pattern.getLabel() + " " + pattern.getMultiplier() + "x]" : "";

			System.out.println("\n--- Selected Hand (" + currentVal + "/" + getBustLimit() + ")" + patternTag + " ---");
			for (Card card : getShowdownHand()) {
				System.out.println(card);
			}
			System.out.println();

			if (currentVal > getBustLimit()) {
				System.out.println("Total exceeds " + getBustLimit() + " (BUST)!");
				break;
			}

			System.out.println("1. Stand (Proceed to Showdown)");
			System.out.println("2. Draw (Hit 1 card from deck; 1.2x damage if you win)");
			if (canDoubleDown) {
				System.out.println("3. Double Down (Draw 2 cards from deck; 1.5x damage & +5 Gold if you win)");
			}

			int action = Input.readInt(sc, "Action: ");

			if (action == 1) {
				break;
			} else if (action == 2) {
				canDoubleDown = false;
				Card drawn = drawCardToShowdown();
				if (drawn == null) {
					break;
				}
				setDrewCard(true);
				System.out.println("\nDrew: " + drawn);
			} else if (action == 3 && canDoubleDown) {
				setDoubledDown(true);
				System.out.println("\nDOUBLE DOWN!");
				for (int i = 0; i < 2; i++) {
					Card drawn = drawCardToShowdown();
					if (drawn != null) {
						System.out.println("Drew: " + drawn);
					}
				}
				int finalVal = calculateHandValue(getShowdownHand());
				HandPattern finalPattern = HandEvaluator.evaluate(getShowdownHand());
				String finalTag = finalPattern.hasBonus() ? " [" + finalPattern.getLabel() + " " + finalPattern.getMultiplier() + "x]" : "";
				System.out.println("Final Double Down Hand Value: " + finalVal + (finalVal > getBustLimit() ? " (BUST!)" : finalTag));
				break;
			} else {
				System.out.println("Invalid action!");
			}
		}
	}

	public void playerTurn(Scanner sc) {
		setDrewCard(false);
		setDoubledDown(false);
		this.drawCards();
		this.viewHand();
		this.playShowdownHand(sc);
		this.blackjackActionPhase(sc);
	}

	public void restoreRerolls() {
		setReroll(3 + bonusRerolls);
	}

	public String getId() {
		return id;
	}

	private static String generateId() {
		idCounter++;
		return "JKL-" + idCounter;
	}

	public void displayPlayerProfile() {
		System.out.printf("%-18s %s%n", "Name:", super.getName());
		System.out.printf("%-18s %d/%d%n", "HP:", super.getHealthpoint(), super.getMaxHealthpoint());
		System.out.printf("%-18s %d%n", "Gold:", getGold());
		System.out.printf("%-18s %+d%n", "Bonus Damage:", getTotalBonusDamage());
		System.out.printf("%-18s %d | Items: %d | Artifacts: %d%n", "Deck Size:", getDeck().size(), inventory.size(), artifacts.size());
		System.out.printf("%-18s %d | Enemies Defeated: %d%n", "Arc:", arc, enemiesDefeated);
	}
}