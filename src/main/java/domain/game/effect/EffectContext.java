package domain.game.effect;

import domain.game.Game;
import domain.game.Player;
import domain.game.Deck;
import ui.io.InputProvider;
import ui.io.OutputProvider;

public class EffectContext {
	private final Game game;
	private final Player currentPlayer;
	private final Player[] allPlayers;
	private final InputProvider input;
	private final OutputProvider output;
	private Player targetPlayer;

	public EffectContext(Game game, Player currentPlayer, Player[] allPlayers,
			InputProvider input, OutputProvider output) {
		this.game = game;
		this.currentPlayer = currentPlayer;
		this.allPlayers = allPlayers;
		this.input = input;
		this.output = output;
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

	public InputProvider getInput() {
		return input;
	}

	public OutputProvider getOutput() {
		return output;
	}

	public Player getTargetPlayer() {
		return targetPlayer;
	}

	public Player[] getAllPlayers() {
		return allPlayers;
	}
}
