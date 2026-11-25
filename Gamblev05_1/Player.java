package Gamev05_1;

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
	
	private ArrayList<Artifacts> activeArtifacts = new ArrayList<>();
	private double damageMultiplier = 1.0;
	private int damageReduction = 0;
	private int handSizeBonus = 0;
	private int bustLimitBonus  = 0;
	private int maxHpDelta = 0;
	private double goldBonus = 0.0;
	private boolean startHealth = false;
	private boolean resonator = false;
	private boolean looseString = false;
	private boolean allIn = false;
	private boolean happyFaces = false;
	
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
		aritfact.apply(this);
		activeArtifacts.add(artifact);
		System.out.println(" Lasts until the end of your next battle ");
		return true;
	}
	
	private static String fmt(double d) {
		return d == Math.floor(d) ? String.valueOf(int) d) : String.format("%.2f), d);
	}
	
	public void addRerolls(int amount) {
		setReroll(getReroll() + amount);
		System.out.println(" +" + amount + " rerolls (now" + getReroll() + ")";
	}

	public void addHandSize(int amount) {
		handSizeBonus += amount;
		setCardSlot(getCardSlot() + amount);
		System.out.println(" +" + amount + " playable hand (" + getCardSlot*() + " cards per draw)");
	}
	
	public void addBustLimit(int amount) {
		bustLimitBonus += amount;
		setBustLimit(getBustLimit() + amount);
		System.out.println(" Bust Limit " + (amount > 0 ? "+" : "") + amount + " (now " + getBustLimit() + ")");
	}
	
	public void multiplyDamage(double factor) {
		damageMultiplier *= factor;
		System.out.println(" FDamage multiplier x" fmt(factor) + " (total x" +fmt(damageMultiplier) + ")");
	}
	
	public void addDamageMultiplier(double amount) {
		damageMultiplier +=amount;
		System.out.println(" Damage multiplier +" fmt(amount) + " (total x" + fmt(damageMultiplier) + ")");
	
	public void addDamageReduction(int amount) {
		damageReduction += amount;
		System.out.println("Enemy damage -" + amount + "(total -" + damageReduction + ")";
	}
	
	public void changeMaxHealth(int delta) {
		int hpBefore = getHealthpoint();
		int maxBefore= getMaxHealthpoint();
		int newMax = Math.max(1, maxBefore + delta);
		int applied = newMax - maxBefore;
		setMaxHealthpoint(newMax);
		setHealthpoint(Math.max(1, hpBefore + applied));
		maxHpDelta += applied;
		System.out.println(" Max HP " + (applied >= 0 ? "+" : "") + applied + " (HP " + getHealthpoint() + "/" + getMaxHealthpoint() + ")";
	}
	
	public void multiplyMaxHealth(int factor) {
		int hpBefore = getHealthpoint();
		int maxBefore = getMaxHealthpoint();
		int newMax = maxBefore * factor);
		setMaxHealthpoint(newMax)
		setHealthpoint(hpBefore * factor);
		maxHpDelta += newMax - maxBefore;
		System.out.println("Healthpoint x" + factor + " (HP " + getHealthpoint() + "/"+ getMaxHealthpoint() + ")");
	}
	
	public void enableLooseString() {
		looseString =true;
		System.out.println(" Winning with a hand value of 21 - 24 deals +20 damage");
	}
	
	public void enableAllIn() {
		allIn = true;
		System.out.println("All in!");
	}
	
	public void enableResonator() {
		resonator = true;
		System.out.println(" Played sigils trigger twice");
	}
	
	public void enableGreedCurse() {
		startHalfHealth = true;
		goldBonus += 1.5;
		System.out.println(" Your next battle starts at 50% HP");
	}
	
	public void enableHappyFaces() {
		happyFaces = true;
		System.out.println(" Cards in winning hands: +2 gold and +1.7% damage each");
	}
	
	public double getDamageMultiplier() {
		return damageMultiplier;
	}
	
	public int getDamageReduction() {
		return damageReduction;
	}
	
	public double getGoldBonus() {
		return goldBonus;
	}
	
	public double hasLooseString() {
		return looseString;
	}
	
	public boolean hasAllIn() {
		return allIn;
	}
	
	public boolean hasHappyFaces() {
		return happyFaces;
	}
	
	public int getSigilTriggers() {
		return resonator ? 2 : 1;
	}
	
	public void onBattleStart() {
		if (activeArtifacts.isEmpty()) {
			return;
		}
		StringBuilder names = new StringBuilder();
		for (int i = 0; i < activeArtifacts.size(); i++) {
			if (i > 0 ) {
				names.append(", ");
			}
			names.append(activeArtifacts.get(i).getName());
		}
		System.out.println("\Artifacts: " + names);
		if (startHalfHealth) {
			int half = Math.max(1,getMaxHealthpoint() / 2);
			if (getHealthpoint() > half) {
				setHealthpoint(half);
				System.out.println("You got Greed Curse" + getHealthpoint() + "/" + getMaxHealthpoint() + ".");
			}
		}
	}
	
	public void endArtifactEffects() {
		if (activeArtifacts.isEmpty()) {
			return;
		}
		setBustLimit(getBustLimit() - bustLimitBonus);
		setCardSlot(getCardSlot() - handSizeBonus);
		setMaxHealthpoint(getMaxHealthpoint() - maxHpDelta);
		
		activeArtifacts.clear();
		damageMultiplier = 1.0;
		damageReduction = 0;
		handSizeBonus = 0;
		bustLimitBonus = 0;
		maxHpDelta = 0;
		goldBonus = 0;
		startHalfHealth = false;
		resonator = false;
		looseString = false;
		allIn = false;
		happyFaces = false;
		System.out.println("Artifact effects have worn off.");
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