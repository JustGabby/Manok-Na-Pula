package Gamev06;

public enum Bundle {
	STANDARD("Standard Bundle", 10, 20, 50, 0, 0, 0.70, 0.20, 0.10, 0.00, 0.00),
	EPIC("Epic Bundle", 25, 10, 30, 60, 0, 0.15, 0.50, 0.25, 0.10, 0.00),
	LEGENDARY("Legendary Bundle", 50, 5, 10, 50, 80, 0.00, 0.50, 0.30, 0.18, 0.02);

	private String name;
	private int cost;
	private int pity3star;
	private int pity4star;
	private int pity5star;
	private int pity6star;
	private double rate1star;
	private double rate3star;
	private double rate4star;
	private double rate5star;
	private double rate6star;
	private int pity3Count = 0;
	private int pity4Count = 0;
	private int pity5Count = 0;
	private int pity6Count = 0;

	private Bundle(String name, int cost, int pity3star, int pity4star, int pity5star, int pity6star,
			double rate1star, double rate3star, double rate4star, double rate5star, double rate6star) {
		this.name = name;
		this.cost = cost;
		this.pity3star = pity3star;
		this.pity4star = pity4star;
		this.pity5star = pity5star;
		this.pity6star = pity6star;
		this.rate1star = rate1star;
		this.rate3star = rate3star;
		this.rate4star = rate4star;
		this.rate5star = rate5star;
		this.rate6star = rate6star;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getPity3star() {
		return pity3star;
	}

	public void setPity3star(int pity3star) {
		this.pity3star = pity3star;
	}

	public int getPity4star() {
		return pity4star;
	}

	public void setPity4star(int pity4star) {
		this.pity4star = pity4star;
	}

	public int getPity5star() {
		return pity5star;
	}

	public void setPity5star(int pity5star) {
		this.pity5star = pity5star;
	}

	public int getPity6star() {
		return pity6star;
	}

	public void setPity6star(int pity6star) {
		this.pity6star = pity6star;
	}

	public double getRate1star() {
		return rate1star;
	}

	public void setRate1star(double rate1star) {
		this.rate1star = rate1star;
	}

	public double getRate3star() {
		return rate3star;
	}

	public void setRate3star(double rate3star) {
		this.rate3star = rate3star;
	}

	public double getRate4star() {
		return rate4star;
	}

	public void setRate4star(double rate4star) {
		this.rate4star = rate4star;
	}

	public double getRate5star() {
		return rate5star;
	}

	public void setRate5star(double rate5star) {
		this.rate5star = rate5star;
	}

	public double getRate6star() {
		return rate6star;
	}

	public void setRate6star(double rate6star) {
		this.rate6star = rate6star;
	}

	public double getRateSum() {
		return rate1star + rate3star + rate4star + rate5star + rate6star;
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
		} else if (pity5star > 0 && pity5Count >= pity5star) {
			resetPity5();
			return 5;
		} else if (pity4star > 0 && pity4Count >= pity4star) {
			resetPity4();
			return 4;
		} else if (pity3star > 0 && pity3Count >= pity3star) {
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

	public static void seed(long seed) {
		Rng.setSeed(seed);
		resetAllPity();
	}

	public static void resetPity3() {
		for (Bundle data : values()) {
			data.pity3Count = 0;
		}
	}

	public static void resetPity4() {
		for (Bundle data : values()) {
			data.pity4Count = 0;
		}
	}

	public static void resetPity5() {
		for (Bundle data : values()) {
			data.pity5Count = 0;
		}
	}

	public static void resetPity6() {
		for (Bundle data : values()) {
			data.pity6Count = 0;
		}
	}

	public static void resetAllPity() {
		resetPity3();
		resetPity4();
		resetPity5();
		resetPity6();
	}
}
