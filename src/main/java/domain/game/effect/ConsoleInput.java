package domain.game.effect;

import java.util.Scanner;

public class ConsoleInput implements InputProvider {
	private static final int MIN_SHUFFLE_COUNT = 1;
	private static final int MAX_SHUFFLE_COUNT = 100;
	private final Scanner scanner;

	public ConsoleInput(Scanner scanner) {
		this.scanner = scanner;
	}

	@Override
	public int readInteger(String message, int min, int max) {
		System.out.println(message);
		while (true) {
			try {
				int value = Integer.parseInt(scanner.nextLine());
				if (value >= min && value <= max) {
					return value;
				}
				final String errorMsg = "Please enter a number between " + min
						+ " and " + max;
				System.out.println(errorMsg);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number.");
			}
		}
	}

	@Override
	public boolean readYesNo(String message) {
		System.out.println(message + " (yes/no)");
		while (true) {
			String input = scanner.nextLine().trim().toLowerCase();
			if (input.equals("yes") || input.equals("y")) {
				return true;
			} else if (input.equals("no") || input.equals("n")) {
				return false;
			}
			System.out.println("Please enter 'yes' or 'no'");
		}
	}

	public int promptForShuffleCount() {
		final String prompt = "How many times to shuffle? ("
				+ MIN_SHUFFLE_COUNT + "-" + MAX_SHUFFLE_COUNT + ")";
		return readInteger(prompt, MIN_SHUFFLE_COUNT, MAX_SHUFFLE_COUNT);
	}
}
