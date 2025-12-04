package domain.game.effect;

import domain.game.Player;
import domain.game.CardType;
import java.util.Scanner;

public class ConsoleInputProvider implements UserInputProvider {
	private static final int MIN_SHUFFLE_COUNT = 1;
	private static final int MAX_SHUFFLE_COUNT = 100;
	private final Scanner scanner;

	public ConsoleInputProvider(Scanner scanner) {
		this.scanner = scanner;
	}

	@Override
	public int promptForInteger(String message, int min, int max) {
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
	public boolean promptForYesNo(String message) {
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

	@Override
	public int promptForCardIndex(Player player, String message) {
		System.out.println(message);
		while (true) {
			try {
				int index = Integer.parseInt(scanner.nextLine());
				if (index >= 0 && index < player.getHandSize()) {
					return index;
				}
				final String errorMsg = "Please enter a valid card index (0-"
						+ (player.getHandSize() - 1) + ")";
				System.out.println(errorMsg);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number.");
			}
		}
	}

	@Override
	public int promptForPlayerIndex(String message, boolean excludeCurrent) {
		System.out.println(message);
		return Integer.parseInt(scanner.nextLine());
	}

	@Override
	public CardType promptForCardType(String message) {
		System.out.println(message);
		String input = scanner.nextLine().toUpperCase().replace(" ", "_");
		return CardType.valueOf(input);
	}

	@Override
	public int promptForShuffleCount() {
		final String prompt = "How many times to shuffle? ("
				+ MIN_SHUFFLE_COUNT + "-" + MAX_SHUFFLE_COUNT + ")";
		System.out.println(prompt);
		while (true) {
			try {
				int count = Integer.parseInt(scanner.nextLine());
				if (count >= MIN_SHUFFLE_COUNT && count <= MAX_SHUFFLE_COUNT) {
					return count;
				}
				final String errorMsg = "Please enter a number between "
						+ MIN_SHUFFLE_COUNT + " and " + MAX_SHUFFLE_COUNT;
				System.out.println(errorMsg);
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number.");
			}
		}
	}

	@Override
	public void displayMessage(String message) {
		System.out.println(message);
	}
}
