package Gamev04;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Player extends Entity {
	private int gold;
	private int reroll;
	private int arc;
	private int enemiesDefeated;
	private ArrayList<Item> inventory;
	private ArrayList<Artifacts> artifacts;
	private final String id;
	private static int idCounter = 1000;

	public Player(String name) {
		super(name, 150, 4, 0);
		this.gold = 15;
		this.reroll = 3;
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
	
	public void addArtifact(Artifacts artifact) {
		if(artifact != null) {
			artifacts.add(artifact);
		}
	}
	
	public ArrayList<Artifacts> getArtifacts() {
		return artifacts;
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
				int[] selectedCards = new int[split.length];
				HashSet<Integer> uniqueChoices = new HashSet<>();
				boolean valid = true;

				for (int i = 0; i < split.length; i++) {
					if (!split[i].matches("[1-9]\\d?")) {
						System.out.println("Please only input valid card numbers.");
						valid = false;
						break;
					}
					int choice = Integer.parseInt(split[i]);
					if (choice < 1 || choice > getHand().size()) {
						System.out.println("Card number " + choice + " is out of range (1-" + getHand().size() + ").");
						valid = false;
						break;
					}
					selectedCards[i] = choice;
					uniqueChoices.add(choice);
				}

				if (valid && getHand().size() >= 2 && uniqueChoices.size() < 2) {
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
			HandPattern pattern = getHandPattern(getShowdownHand());
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
			System.out.println("2. Draw (Hit 1 card from deck)");
			if (canDoubleDown) {
				System.out.println("3. Double Down (Draw 2 cards from deck; +50% damage & +5 Gold if you win)");
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
				HandPattern finalPattern = getHandPattern(getShowdownHand());
				String finalTag = finalPattern.hasBonus() ? " [" + finalPattern.getLabel() + " " + finalPattern.getMultiplier() + "x]" : "";
				System.out.println("Final Double Down Hand Value: " + finalVal + (finalVal > getBustLimit() ? " (BUST!)" : finalTag));
				break;
			} else {
				System.out.println("Invalid action!");
			}
		}
	}

	public void playerTurn(Scanner sc) {
		setDoubledDown(false);
		this.drawCards();
		this.viewHand();
		this.playShowdownHand(sc);
		this.blackjackActionPhase(sc);
	}

	public void restoreRerolls() {
		setReroll(3);
	}
	
	public String getId() {
	    return id;
	}

	private static String generateId() {
	    idCounter++;
	    return "JKL-" + idCounter;
	}
	
	public boolean useArtifact(int index) {
		if (index < 0 || index >= artifacts.size()) {
			return false;
		}
		Artifacts artifact = artifacts.remove(index);
		System.out.println("Used " + artifact.getName() + ": " + artifact.getEffect());
		return true;
	}
	
	public void displayPlayerProfile() {
	    System.out.printf("%-18s %s%n", "Name:", super.getName());
	    System.out.printf("%-18s %d/%d%n", "HP:", super.getHealthpoint(), super.getMaxHealthpoint());
	    System.out.printf("%-18s %d%n", "Gold:", getGold());
	    System.out.printf("%-18s %+d%n", "Bonus Damage:", getBonusDamage());
	    System.out.printf("%-18s %d | Items: %d%n", "Deck Size:", getDeck().size(), inventory.size());
	    System.out.printf("%-18s %d | Enemies Defeated: %d%n", "Arc:", arc, enemiesDefeated);
	}
}