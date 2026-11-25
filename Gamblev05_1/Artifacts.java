package Gamev05_1;

import java.util.LinkedList;
import java.util.Random;

public enum Artifacts {
	HANDOFGOD("Hand of God", 6, "+5 on playable hand, *10 damage multiplier, +3 rerolls"),
	RAGNAROK("Ragnarok", 6, "+3 on playable hand, *12 damage multiplier, + 4 rerolls, increase bust limit by 15, -10 opponent's damage"),
	GOLDENFRUIT("Golden Fruit", 6, "*3 Healthpoint, -5 opponent's damage, + 2 rerolls,"),
	JOKER8("Joker8", 5, "*3 damage multiplier, + 2 rerolls"),
	FATEWEAVER("Fate Weaver", 5, "+ 4 playable hand, + 2 damage multiplier, + 2 rerolls"),
	GRIMOIRE("Grimoire", 5, "*5 damage multiplier, -50 healthpoints, + 2 rerolls,"), 
	PARADOX("Paradox", 4, "*2 damage multiplier, +50 healthpoints, + 1 rerolls"),
	KINGSHAND("King's Hand", 4, "+ 4 playable hand, + 2 rerolls, *2 damage multiplier"),
	NOCTURNE("Nocturne", 4, "decrease bust limit by 5, -2 opponent's damage, *2 damage multiplier"),
	BOTTLEOFWHISKEY("Bottle of Whiskey", 3, "-70 healthpoints, + 5 playable hand, *3 damage multiplier"),
	LOOSESTRING("Loose String", 3, "+ 3 bust limit, handvalue is equal to 21-24 can do + 20 damage"),
	ALLIN("All-in!", 3, "If won hand is greater than 18, gold acquired + 50%"),
	RESONATOR("Resonator", 3, "Played sigils will trigger twice"),
	GREEDCURSE("Greed Curse", 3, "Start battle with 50% of health, gold won + 150%"),
	HAPPYFACES("Happy Faces", 3, "Face cards played will give +2 coins and damage multiplier will increase by 1.7%");
	
	private final String name;
	private final int rating;
	private final String effect;

	Artifacts(String name, int rating, String effect) {
		this.name = name;
		this.rating = rating;
		this.effect = effect;
	}

	public String getName() {
		return name;
	}

	public int getRating() {
		return rating;
	}
	
	public String getEffect() {
		return effect;	
	}
	
	public void apply(Player player) {
		switch (this) {
			case HANDOFGOD:
				player.addHandSize(6);
				player.multiplyDamage(10);
				player.addRerolls(3);
				break;
			case RAGNAROK:
				player.addHandSize(3);
				player.multiplyDamage(12);
				player.addRerolls(4);
				player.addBustLimit(15)
				player.addDamageReduction(10);
				break;
			case GOLDENFRUIT:
				player.multiplyMaxHealth(3);
				player.addDamageReduction(5);
				player.addRerolls(2);
				break;
			case JOKER8:
				player.multiplyDamage(3);
				player.addRerolls(2);
				break;
			case FATEWEAVER:
				player.addHandSize(4);
				player.addDamageMultiplier(2);
				player.addRerolls(2);
				break;
			case GRIMOIRE:
				player.multiplyDamage(5);
				player.changeMaxHealth(-50);
				player.addRerolls(2);
				break;
			case PARADOX:
				player.multiplyDamage(2);
				player.changeMaxHealth(-50);
				player.addRerolls(1);
				break;
			case KINGSHAND:
				player.addHandSize(4);
				player.addRerolls(2);
				player.multiplyDamage(2);
				break;
			case NOCTURNE:
				player.addBustLimit(-5);
				player.addDamageReduction(2);
				player.multiplyDamage(2);
				break;
			case BOTTLEOFWHISKEY:
				player.changeMaxHealth(-70);
				player.addHandSize(5);
				player.multiplyDamage(3);
				break;
			case LOOSESTRING:
				player.addBustLimit(3);
				player.enableLooseString();
				break;
			case ALLIN:
				player.enableAllIn();
				break;
			case RESONATOR:
				player.enableResonator();
				break;
			case GREEDCURSE:
				player.enableGreedCurse();
				break;
			case HAPPYFACES:
				player.enableHappyFaces();
				break;
		}
	}
	
	public static Artifacts sortByRarity(int rarity) {
		LinkedList<Artifacts> set = new LinkedList<>();
		Random random = new Random();
		
		for (Artifacts data : values()) {
			if(data.rating == rarity) {
				set.add(data);
			}
		}
		
		return set.get(random.nextInt(set.size()));
	}
}