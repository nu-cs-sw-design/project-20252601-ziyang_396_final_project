package domain.game.effect;

import domain.game.Player;
import domain.game.Card;
import domain.game.CardType;

public class NOPEInterceptor implements CardEffect {
	private final CardEffect wrappedEffect;
	private final boolean requiresNOPECheck;

	public NOPEInterceptor(CardEffect wrappedEffect, boolean requiresNOPECheck) {
		this.wrappedEffect = wrappedEffect;
		this.requiresNOPECheck = requiresNOPECheck;
	}

	@Override
	public boolean canExecute(EffectContext context) {
		return wrappedEffect.canExecute(context);
	}

	@Override
	public EffectResult execute(EffectContext context) {
		if (!requiresNOPECheck) {
			return wrappedEffect.execute(context);
		}

		// Check for NOPE cards from all players
		boolean cancelled = checkForNOPE(context);

		if (cancelled) {
			return EffectResult.cancelled("The effect was cancelled by a NOPE card");
		}

		return wrappedEffect.execute(context);
	}

	private boolean checkForNOPE(EffectContext context) {
		Player[] players = context.getAllPlayers();
		Player currentPlayer = context.getCurrentPlayer();
		boolean cancelled = false;

		// Ask each other player if they want to play NOPE
		for (Player player : players) {
			if (player == currentPlayer || player.getIsDead()) {
				continue;
			}

			// Check if player has NOPE card
			int nopeIndex = findNOPECard(player);
			if (nopeIndex == -1) {
				continue;
			}

			final String hasNopeMsg = "Player " + player.getPlayerID()
					+ " has a NOPE card!";
			context.getOutput().display(hasNopeMsg);
			
			final String playNopePrompt = "Player " + player.getPlayerID()
					+ ", do you want to play NOPE?";
			boolean wantsToNope = context.getInput()
					.readYesNo(playNopePrompt);

			if (wantsToNope) {
				player.removeCardFromHand(nopeIndex);
				final String playedNopeMsg = "Player " + player.getPlayerID()
						+ " played NOPE!";
				context.getOutput().display(playedNopeMsg);
				cancelled = !cancelled;
				
				boolean nopeOfNope = checkForNOPE(context);
				if (nopeOfNope) {
					cancelled = !cancelled;
				}
			}
		}

		return cancelled;
	}

	private int findNOPECard(Player player) {
		for (int i = 0; i < player.getHandSize(); i++) {
			Card card = player.getCardAt(i);
			if (card.getCardType() == CardType.NOPE) {
				return i;
			}
		}
		return -1;
	}
}
