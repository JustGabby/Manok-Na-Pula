package Gamev09;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Input {
	public static int readInt(Scanner sc, String prompt) {
		while (true) {
			System.out.print(prompt);
			try {
				return Integer.parseInt(sc.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Invalid input!");
			} catch (NoSuchElementException | IllegalStateException e) {
				throw new IllegalStateException("Input stream is unavailable.", e);
			}
		}
	}

	public static int pickOption(Scanner sc, String title, String subtitle, List<String> options, String zeroLabel) {
		while (true) {
			System.out.println("\n=== " + title + " ===");
			if (subtitle != null && !subtitle.isEmpty()) {
				System.out.println(subtitle);
			}
			for (int i = 0; i < options.size(); i++) {
				System.out.println((i + 1) + ". " + options.get(i));
			}
			System.out.println("0. " + zeroLabel);

			int choice = readInt(sc, "Choice: ");
			if (choice == 0) {
				return -1;
			}
			if (choice >= 1 && choice <= options.size()) {
				return choice - 1;
			}
			System.out.println("Invalid selection!");
		}
	}
}