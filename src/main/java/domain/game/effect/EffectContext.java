package domain.game.effect;

import domain.game.Game;
import domain.game.Player;
import domain.game.Deck;

public class EffectContext {
	private final Game game;
	private final Player currentPlayer;
	private final Player[] allPlayers;
	private final UserInputProvider userInput;
	private Player targetPlayer;

	public EffectContext(Game game, Player currentPlayer, Player[] allPlayers,
			UserInputProvider userInput) {
		this.game = game;
		this.currentPlayer = currentPlayer;
		this.allPlayers = allPlayers;
		this.userInput = userInput;
		this.targetPlayer = null;
	}

	public void setTargetPlayer(Player player) {
		this.targetPlayer = player;
	}

	public Game getGame() {
		return game;
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public Deck getDeck() {
		return game.getDeck();
	}

	public UserInputProvider getUserInput() {
		return userInput;
	}

	public Player getTargetPlayer() {
		return targetPlayer;
	}

	public Player[] getAllPlayers() {
		return allPlayers;
	}
}
