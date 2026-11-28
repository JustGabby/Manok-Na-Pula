package Gamev09;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;

public final class SaveSystem {
	private static final String FILE_NAME = "parakayrene-v09-save.properties";
	private static final String MAGIC = "ParaKayRene";
	private static final int VERSION = 2;

	private SaveSystem() {
	}

	public static void save(Game game) {
		if (game == null) {
			System.out.println("Save failed: no active run.");
			return;
		}

		try {
			Properties data = new Properties();
			data.setProperty("save.magic", MAGIC);
			data.setProperty("save.version", String.valueOf(VERSION));
			data.setProperty("rng.state", Long.toString(Rng.getState()));
			data.setProperty("run.seed", Long.toString(game.getRunSeed()));
			data.setProperty("game.chestAvailable", Boolean.toString(game.isChestAvailable()));
			data.setProperty("game.campfireAvailable", Boolean.toString(game.isCampfireAvailable()));
			addPlayer(game.getPlayer(), data);
			addShop(game.getShop(), data);
			addBundle(data);
			data.setProperty("save.checksum", checksum(data));
			write(data);
			System.out.println("Run saved to " + FILE_NAME + ".");
		} catch (IOException | RuntimeException e) {
			System.out.println("Save failed: " + e.getMessage());
		}
	}

	public static Game load() {
		Path path = Paths.get(FILE_NAME);
		if (!Files.exists(path)) {
			System.out.println("No saved run found.");
			return null;
		}

		try {
			Properties data = new Properties();
			try (InputStream in = Files.newInputStream(path)) {
				data.load(in);
			}
			validate(data);

			String playerName = required(data, "player.name");
			String playerId = required(data, "player.id");
			int idCounter = intValue(data, "player.idCounter", 1000, 1000000);
			int maxHealthpoint = intValue(data, "player.maxHealthpoint", 1, 100000);
			int healthpoint = intValue(data, "player.healthpoint", 0, maxHealthpoint);
			int gold = intValue(data, "player.gold", 0, 1000000);
			int reroll = intValue(data, "player.reroll", 0, 100);
			int bonusRerolls = intValue(data, "player.bonusRerolls", 0, 100);
			int arc = intValue(data, "player.arc", 1, 10000);
			int enemiesDefeated = intValue(data, "player.enemiesDefeated", 0, 100000);
			int cardSlot = intValue(data, "player.cardSlot", 1, 20);
			int bustLimit = intValue(data, "player.bustLimit", 1, 100);
			int bonusDamage = intValue(data, "player.bonusDamage", 0, 10000);
			double artifactDamageMultiplier = doubleValue(data, "player.artifactDamageMultiplier", 0.0, 1000.0);
			double artifactGoldMultiplier = doubleValue(data, "player.artifactGoldMultiplier", 0.0, 1000.0);
			int artifactDamageReduction = intValue(data, "player.artifactDamageReduction", 0, 10000);
			int artifactHeatReduction = intValue(data, "player.artifactHeatReduction", 0, 10000);
			int tempBonusDamage = intValue(data, "player.tempBonusDamage", 0, 10000);
			int tempDamageReduction = intValue(data, "player.tempDamageReduction", 0, 10000);
			double tempGoldBonus = doubleValue(data, "player.tempGoldBonus", 0.0, 1000.0);
			int heatShieldTicks = intValue(data, "player.heatShieldTicks", 0, 10000);
			boolean doubleSigils = boolValue(data, "player.doubleSigils");
			int handWinStreak = intValue(data, "player.handWinStreak", 0, 1000);
			String deckName = required(data, "player.deck.name");
			ArrayList<Card> permanent = parseCardList(data, "player.deck.permanent");
			ArrayList<Card> drawOrder = parseCardList(data, "player.deck.drawPile");
			validateDeck(permanent, drawOrder);
			ArrayList<Item> inventory = parseEnumList(data, "player.inventory", Item.class);
			ArrayList<Artifacts> artifacts = parseEnumList(data, "player.artifacts", Artifacts.class);
			ArrayList<Item> stock = parseEnumList(data, "shop.stock", Item.class);
			HashMap<Item, Integer> prices = parsePrices(data);
			if (new HashSet<>(stock).size() != stock.size()) {
				throw new SaveException("Duplicate shop stock item");
			}

			int[][] pity = parsePity(data);
			boolean chestAvailable = boolValue(data, "game.chestAvailable");
			boolean campfireAvailable = boolValue(data, "game.campfireAvailable");
			long seed = longValue(data, "run.seed", Long.MIN_VALUE, Long.MAX_VALUE);
			long rngState = longValue(data, "rng.state", Long.MIN_VALUE, Long.MAX_VALUE);

			return build(playerName, playerId, idCounter, maxHealthpoint, healthpoint, gold, reroll,
					bonusRerolls, arc, enemiesDefeated, cardSlot, bustLimit, bonusDamage,
					artifactDamageMultiplier, artifactGoldMultiplier, artifactDamageReduction,
					artifactHeatReduction, tempBonusDamage, tempDamageReduction, tempGoldBonus,
					heatShieldTicks, doubleSigils, handWinStreak, deckName, permanent, drawOrder,
					inventory, artifacts, stock, prices, pity, chestAvailable, campfireAvailable,
					seed, rngState);
		} catch (SaveException | IOException | RuntimeException e) {
			System.out.println("Load failed: " + e.getMessage());
			return null;
		}
	}

	private static Game build(String playerName, String playerId, int idCounter, int maxHealthpoint,
			int healthpoint, int gold, int reroll, int bonusRerolls, int arc, int enemiesDefeated,
			int cardSlot, int bustLimit, int bonusDamage, double artifactDamageMultiplier,
			double artifactGoldMultiplier, int artifactDamageReduction, int artifactHeatReduction,
			int tempBonusDamage, int tempDamageReduction, double tempGoldBonus, int heatShieldTicks,
			boolean doubleSigils, int handWinStreak, String deckName, ArrayList<Card> permanent,
			ArrayList<Card> drawOrder, ArrayList<Item> inventory, ArrayList<Artifacts> artifacts,
			ArrayList<Item> stock, HashMap<Item, Integer> prices, int[][] pity,
			boolean chestAvailable, boolean campfireAvailable, long seed, long rngState) {
		Player player = new Player(playerName);
		Game game = new Game(player, seed);

		player.restoreId(playerId);
		Player.restoreIdCounter(idCounter);
		player.setMaxHealthpoint(maxHealthpoint);
		player.setHealthpoint(healthpoint);
		player.setGold(gold);
		player.setReroll(reroll);
		player.setBonusRerolls(bonusRerolls);
		player.setArc(arc);
		player.setEnemiesDefeated(enemiesDefeated);
		player.setCardSlot(cardSlot);
		player.setBustLimit(bustLimit);
		player.setBonusDamage(bonusDamage);
		player.setArtifactDamageMultiplier(artifactDamageMultiplier);
		player.setArtifactGoldMultiplier(artifactGoldMultiplier);
		player.setArtifactDamageReduction(artifactDamageReduction);
		player.setArtifactHeatReduction(artifactHeatReduction);
		player.setTempBonusDamage(tempBonusDamage);
		player.setTempDamageReduction(tempDamageReduction);
		player.setTempGoldBonus(tempGoldBonus);
		player.setHeatShieldTicks(heatShieldTicks);
		player.setDoubleSigils(doubleSigils);
		player.setHandWinStreak(handWinStreak);
		player.setDeck(new Deck(deckName, permanent, drawOrder));

		for (Item item : inventory) {
			player.addItem(item);
		}
		for (Artifacts artifact : artifacts) {
			player.restoreArtifact(artifact);
		}

		game.setChestAvailable(chestAvailable);
		game.setCampfireAvailable(campfireAvailable);
		game.getShop().restore(stock, prices);
		for (Bundle bundle : Bundle.values()) {
			int index = bundle.ordinal();
			bundle.restorePity(pity[index][0], pity[index][1], pity[index][2], pity[index][3]);
		}
		Rng.setState(rngState);
		return game;
	}

	private static void addPlayer(Player player, Properties data) {
		data.setProperty("player.name", player.getName());
		data.setProperty("player.id", player.getId());
		data.setProperty("player.idCounter", String.valueOf(nextIdCounter(player.getId())));
		data.setProperty("player.maxHealthpoint", String.valueOf(player.getMaxHealthpoint()));
		data.setProperty("player.healthpoint", String.valueOf(player.getHealthpoint()));
		data.setProperty("player.gold", String.valueOf(player.getGold()));
		data.setProperty("player.reroll", String.valueOf(player.getReroll()));
		data.setProperty("player.bonusRerolls", String.valueOf(player.getBonusRerolls()));
		data.setProperty("player.arc", String.valueOf(player.getArc()));
		data.setProperty("player.enemiesDefeated", String.valueOf(player.getEnemiesDefeated()));
		data.setProperty("player.cardSlot", String.valueOf(player.getCardSlot()));
		data.setProperty("player.bustLimit", String.valueOf(player.getBustLimit()));
		data.setProperty("player.bonusDamage", String.valueOf(player.getBonusDamage()));
		data.setProperty("player.artifactDamageMultiplier", String.valueOf(player.getArtifactDamageMultiplier()));
		data.setProperty("player.artifactGoldMultiplier", String.valueOf(player.getArtifactGoldMultiplier()));
		data.setProperty("player.artifactDamageReduction", String.valueOf(player.getArtifactDamageReduction()));
		data.setProperty("player.artifactHeatReduction", String.valueOf(player.getArtifactHeatReduction()));
		data.setProperty("player.tempBonusDamage", String.valueOf(player.getTempBonusDamage()));
		data.setProperty("player.tempDamageReduction", String.valueOf(player.getTempDamageReduction()));
		data.setProperty("player.tempGoldBonus", String.valueOf(player.getTempGoldBonus()));
		data.setProperty("player.heatShieldTicks", String.valueOf(player.getHeatShieldTicks()));
		data.setProperty("player.doubleSigils", Boolean.toString(player.isDoubleSigils()));
		data.setProperty("player.handWinStreak", String.valueOf(player.getHandWinStreak()));

		Deck deck = player.getDeck();
		data.setProperty("player.deck.name", deck.getDeckName());
		writeCardList(data, "player.deck.permanent", deck.getPermanentCards());
		writeCardList(data, "player.deck.drawPile", deck.getDrawPile());
		writeEnumList(data, "player.inventory", player.getInventory());
		writeEnumList(data, "player.artifacts", player.getArtifacts());
	}

	private static int nextIdCounter(String id) {
		try {
			String[] parts = id.split("-");
			return Math.max(1000, Integer.parseInt(parts[parts.length - 1]));
		} catch (RuntimeException e) {
			return 1000;
		}
	}

	private static void addShop(Shop shop, Properties data) {
		writeEnumList(data, "shop.stock", shop.getItemStock());
		HashMap<Item, Integer> prices = shop.getItemPrices();
		for (Map.Entry<Item, Integer> entry : prices.entrySet()) {
			data.setProperty("shop.price." + entry.getKey().name(), String.valueOf(entry.getValue()));
		}
	}

	private static void addBundle(Properties data) {
		for (Bundle bundle : Bundle.values()) {
			int[] values = pityValues(bundle);
			for (int i = 0; i < values.length; i++) {
				data.setProperty("bundle.pity." + bundle.name() + "." + i, String.valueOf(values[i]));
			}
		}
	}

	private static int[] pityValues(Bundle bundle) {
		return new int[] { bundle.getPity3Count(), bundle.getPity4Count(),
				bundle.getPity5Count(), bundle.getPity6Count() };
	}

	private static int[][] parsePity(Properties data) throws SaveException {
		int[][] result = new int[Bundle.values().length][4];
		for (Bundle bundle : Bundle.values()) {
			for (int i = 0; i < 4; i++) {
				result[bundle.ordinal()][i] = intValue(data,
						"bundle.pity." + bundle.name() + "." + i, 0, 1000);
			}
		}
		return result;
	}

	private static HashMap<Item, Integer> parsePrices(Properties data) throws SaveException {
		HashMap<Item, Integer> prices = new HashMap<>();
		for (Item item : Item.values()) {
			prices.put(item, intValue(data, "shop.price." + item.name(), 1, 1000000));
		}
		return prices;
	}

	private static void writeCardList(Properties data, String prefix, List<Card> cards) {
		data.setProperty(prefix + ".count", String.valueOf(cards.size()));
		for (int i = 0; i < cards.size(); i++) {
			data.setProperty(prefix + "." + i, encodeCard(cards.get(i)));
		}
	}

	private static ArrayList<Card> parseCardList(Properties data, String prefix) throws SaveException {
		int count = intValue(data, prefix + ".count", 0, 1000);
		ArrayList<Card> cards = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			cards.add(decodeCard(required(data, prefix + "." + i)));
		}
		return cards;
	}

	private static String encodeCard(Card card) {
		StringJoiner joiner = new StringJoiner("|");
		joiner.add(card.getRank().name());
		joiner.add(card.getSuit().name());
		joiner.add(Boolean.toString(card.isTemporary()));
		joiner.add(card.hasSigil() ? card.getSigil().name() : "NONE");
		return joiner.toString();
	}

	private static Card decodeCard(String text) throws SaveException {
		String[] parts = text.split("\\|", -1);
		if (parts.length != 4) {
			throw new SaveException("Malformed card record");
		}
		Rank rank = enumValue(Rank.class, parts[0]);
		Suit suit = enumValue(Suit.class, parts[1]);
		boolean temporary = boolText(parts[2], "card temporary flag");
		Sigil sigil = "NONE".equals(parts[3]) ? null : enumValue(Sigil.class, parts[3]);
		return new Card(rank, suit, sigil, temporary);
	}

	private static void validateDeck(ArrayList<Card> permanent, ArrayList<Card> drawOrder) throws SaveException {
		if (permanent.size() < 10 || permanent.size() > 1000) {
			throw new SaveException("Saved deck size is invalid");
		}
		if (permanent.size() != drawOrder.size()) {
			throw new SaveException("Saved deck and draw pile sizes differ");
		}
		ArrayList<String> permanentKeys = new ArrayList<>();
		ArrayList<String> drawKeys = new ArrayList<>();
		for (Card card : permanent) {
			permanentKeys.add(encodeCard(card));
		}
		for (Card card : drawOrder) {
			drawKeys.add(encodeCard(card));
		}
		Collections.sort(permanentKeys);
		Collections.sort(drawKeys);
		if (!permanentKeys.equals(drawKeys)) {
			throw new SaveException("Saved deck and draw pile contents differ");
		}
	}

	private static <E extends Enum<E>> void writeEnumList(Properties data, String prefix, List<E> values) {
		data.setProperty(prefix + ".count", String.valueOf(values.size()));
		for (int i = 0; i < values.size(); i++) {
			data.setProperty(prefix + "." + i, values.get(i).name());
		}
	}

	private static <E extends Enum<E>> ArrayList<E> parseEnumList(Properties data, String prefix, Class<E> type)
			throws SaveException {
		int count = intValue(data, prefix + ".count", 0, 1000);
		ArrayList<E> values = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			values.add(enumValue(type, required(data, prefix + "." + i)));
		}
		return values;
	}

	private static <E extends Enum<E>> E enumValue(Class<E> type, String text) throws SaveException {
		try {
			return Enum.valueOf(type, text);
		} catch (IllegalArgumentException e) {
			throw new SaveException("Invalid " + type.getSimpleName() + ": " + text);
		}
	}

	private static void validate(Properties data) throws SaveException {
		if (!MAGIC.equals(required(data, "save.magic"))) {
			throw new SaveException("Not a ParaKayRene save file");
		}
		int version = intValue(data, "save.version", 1, VERSION);
		if (version != VERSION) {
			throw new SaveException("Unsupported save version: " + version);
		}
		String expected = required(data, "save.checksum");
		if (!expected.equals(checksum(data))) {
			throw new SaveException("Checksum mismatch; save may be corrupted");
		}
	}

	private static String checksum(Properties data) {
		ArrayList<String> keys = new ArrayList<>(data.stringPropertyNames());
		Collections.sort(keys);
		StringJoiner joiner = new StringJoiner("|");
		for (String key : keys) {
			if (!"save.checksum".equals(key)) {
				joiner.add(key + "=" + data.getProperty(key));
			}
		}
		return Integer.toHexString(joiner.toString().hashCode());
	}

	private static void write(Properties data) throws IOException {
		Path target = Paths.get(FILE_NAME);
		Path temp = Paths.get(FILE_NAME + ".tmp");
		try (OutputStream out = Files.newOutputStream(temp)) {
			data.store(out, "ParaKayRene checkpoint");
		}
		try {
			Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
		} catch (AtomicMoveNotSupportedException e) {
			Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			Files.deleteIfExists(temp);
			throw e;
		}
	}

	private static String required(Properties data, String key) throws SaveException {
		String value = data.getProperty(key);
		if (value == null || value.isEmpty()) {
			throw new SaveException("Missing or empty save field: " + key);
		}
		return value;
	}

	private static int intValue(Properties data, String key, int min, int max) throws SaveException {
		String text = required(data, key);
		try {
			int value = Integer.parseInt(text);
			if (value < min || value > max) {
				throw new SaveException("Out-of-range value for " + key);
			}
			return value;
		} catch (NumberFormatException e) {
			throw new SaveException("Invalid integer for " + key);
		}
	}

	private static long longValue(Properties data, String key, long min, long max) throws SaveException {
		String text = required(data, key);
		try {
			long value = Long.parseLong(text);
			if (value < min || value > max) {
				throw new SaveException("Out-of-range value for " + key);
			}
			return value;
		} catch (NumberFormatException e) {
			throw new SaveException("Invalid long for " + key);
		}
	}

	private static double doubleValue(Properties data, String key, double min, double max) throws SaveException {
		String text = required(data, key);
		try {
			double value = Double.parseDouble(text);
			if (!Double.isFinite(value) || value < min || value > max) {
				throw new SaveException("Out-of-range value for " + key);
			}
			return value;
		} catch (NumberFormatException e) {
			throw new SaveException("Invalid decimal for " + key);
		}
	}

	private static boolean boolValue(Properties data, String key) throws SaveException {
		return boolText(required(data, key), key);
	}

	private static boolean boolText(String text, String label) throws SaveException {
		if ("true".equalsIgnoreCase(text)) {
			return true;
		}
		if ("false".equalsIgnoreCase(text)) {
			return false;
		}
		throw new SaveException("Invalid boolean for " + label);
	}

	private static class SaveException extends Exception {
		private static final long serialVersionUID = 1L;

		SaveException(String message) {
			super(message);
		}
	}
}
