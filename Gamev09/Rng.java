package Gamev09;

import java.util.List;

public final class Rng {
	private static long state = 1L;

	private Rng() {
	}

	public static void setSeed(long seed) {
		state = mix(seed == 0L ? 1L : seed);
	}

	public static long getState() {
		return state;
	}

	public static void setState(long state) {
		Rng.state = state;
	}

	public static int nextInt(int bound) {
		if (bound <= 0) {
			throw new IllegalArgumentException("bound must be positive");
		}
		return (int) Math.floorMod(nextLong() >>> 33, bound);
	}

	public static int range(int min, int max) {
		if (max < min) {
			throw new IllegalArgumentException("max must be at least min");
		}
		return min + nextInt(max - min + 1);
	}

	public static double nextDouble() {
		return (nextLong() >>> 11) * (1.0 / (1L << 53));
	}

	public static <T> void shuffle(List<T> list) {
		for (int i = list.size() - 1; i > 0; i--) {
			int j = nextInt(i + 1);
			T temp = list.get(i);
			list.set(i, list.get(j));
			list.set(j, temp);
		}
	}

	private static long nextLong() {
		state = state * 6364136223846793005L + 1442695040888963407L;
		return state;
	}

	private static long mix(long value) {
		value ^= value >>> 30;
		value *= 0xBF58476D1CE4E5B9L;
		value ^= value >>> 27;
		value *= 0x94D049BB133111EBL;
		value ^= value >>> 31;
		return value;
	}
}
