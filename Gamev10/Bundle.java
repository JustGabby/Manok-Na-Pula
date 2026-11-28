package Gamev09;

public enum Bundle {
	STANDARD("Standard Bundle", 5, 20, 50, 0, 0, 0.20, 0.10, 0.00, 0.00),
	EPIC("Epic Bundle", 15, 10, 30, 60, 0, 0.50, 0.25, 0.10, 0.00),
	LEGENDARY("Legendary Bundle", 30, 5, 10, 30, 60, 0.50, 0.30, 0.18, 0.02);

	private final String name;
	private final int cost;
	private final int pity3star;
	private final int pity4star;
	private final int pity5star;
	private final int pity6star;
	private final double rate3star;
	private final double rate4star;
	private final double rate5star;
	private final double rate6star;
	private int pity3Count = 0;
	private int pity4Count = 0;
	private int pity5Count = 0;
	private int pity6Count = 0;

	Bundle(String name, int cost, int pity3star, int pity4star, int pity5star, int pity6star,
			double rate3star, double rate4star, double rate5star, double rate6star) {
		this.name = name;
		this.cost = cost;
		this.pity3star = pity3star;
		this.pity4star = pity4star;
		this.pity5star = pity5star;
		this.pity6star = pity6star;
		this.rate3star = rate3star;
		this.rate4star = rate4star;
		this.rate5star = rate5star;
		this.rate6star = rate6star;
	}

	public String getName() {
		return name;
	}

	public int getCost() {
		return cost;
	}

	public int pull() {
		pity3Count++;
		pity4Count++;
		pity5Count++;
		pity6Count++;

		double roll = Rng.nextDouble();

		if (pity6star > 0 && pity6Count >= pity6star) {
			resetPity6();
			return 6;
		}
		if (pity5star > 0 && pity5Count >= pity5star) {
			resetPity5();
			return 5;
		}
		if (pity4star > 0 && pity4Count >= pity4star) {
			resetPity4();
			return 4;
		}
		if (pity3star > 0 && pity3Count >= pity3star) {
			resetPity3();
			return 3;
		}

		if (roll < rate6star) {
			resetPity6();
			return 6;
		}
		roll -= rate6star;
		if (roll < rate5star) {
			resetPity5();
			return 5;
		}
		roll -= rate5star;
		if (roll < rate4star) {
			resetPity4();
			return 4;
		}
		roll -= rate4star;
		if (roll < rate3star) {
			resetPity3();
			return 3;
		}
		return 1;
	}

	public int getPity3Count() {
		return pity3Count;
	}

	public int getPity4Count() {
		return pity4Count;
	}

	public int getPity5Count() {
		return pity5Count;
	}

	public int getPity6Count() {
		return pity6Count;
	}

	public void restorePity(int pity3Count, int pity4Count, int pity5Count, int pity6Count) {
		this.pity3Count = Math.max(0, pity3Count);
		this.pity4Count = Math.max(0, pity4Count);
		this.pity5Count = Math.max(0, pity5Count);
		this.pity6Count = Math.max(0, pity6Count);
	}

	public static void resetPity3() {
		for (Bundle bundle : values()) {
			bundle.pity3Count = 0;
		}
	}

	public static void resetPity4() {
		for (Bundle bundle : values()) {
			bundle.pity4Count = 0;
		}
	}

	public static void resetPity5() {
		for (Bundle bundle : values()) {
			bundle.pity5Count = 0;
		}
	}

	public static void resetPity6() {
		for (Bundle bundle : values()) {
			bundle.pity6Count = 0;
		}
	}

	public static void resetAllPity() {
		resetPity3();
		resetPity4();
		resetPity5();
		resetPity6();
	}
}