package domain.game.effect;

import domain.game.Player;
import domain.game.CardType;

public interface UserInputProvider {

	int promptForInteger(String message, int min, int max);

	boolean promptForYesNo(String message);

	int promptForCardIndex(Player player, String message);

	int promptForPlayerIndex(String message, boolean excludeCurrent);

	CardType promptForCardType(String message);

	int promptForShuffleCount();

	void displayMessage(String message);
}
