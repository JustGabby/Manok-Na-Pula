package Gamev04;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Rng {
	private static final Random RANDOM = new Random();

	public static int nextInt(int bound) {
		return RANDOM.nextInt(bound);
	}

	public static int range(int min, int max) {
		return min + RANDOM.nextInt(max - min + 1);
	}

	public static double nextDouble() {
		return RANDOM.nextDouble();
	}

	public static <T> void shuffle(List<T> list) {
		Collections.shuffle(list, RANDOM);
	}
}