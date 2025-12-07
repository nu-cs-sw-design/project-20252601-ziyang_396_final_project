package ui.io;

import java.util.Scanner;

public class ConsoleInput implements InputProvider {
	private final Scanner scanner;

	public ConsoleInput(Scanner scanner) {
		this.scanner = scanner;
	}

	@Override
	public int readInteger(String message, int min, int max) {
		if (message != null && !message.isEmpty()) {
			System.out.println(message);
		}
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
}
