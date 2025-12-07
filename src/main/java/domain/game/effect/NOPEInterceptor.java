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

		// Check for NOPE cards from all players except the one who played the card
		boolean cancelled = checkForNOPE(context, context.getCurrentPlayer());

		if (cancelled) {
			return EffectResult.cancelled("The effect was cancelled by a NOPE card");
		}

		return wrappedEffect.execute(context);
	}

	private boolean checkForNOPE(EffectContext context, Player playedByPlayer) {
		Player[] players = context.getAllPlayers();
		boolean cancelled = false;

		// Ask each player if they want to play NOPE (except the one who played the card)
		for (Player player : players) {
			if (player == playedByPlayer || player.getIsDead()) {
				continue;
			}

			// Check if player has NOPE card
			int nopeIndex = findNOPECard(player);
			if (nopeIndex == -1) {
				continue;
			}

			final String hasNopeMsg = "Player " + player.getPlayerID()
					+ " has a Nope Card.";
			context.getOutput().display(hasNopeMsg);
			
			final String playNopePrompt = "Would you like to play it?";
			context.getOutput().display(playNopePrompt);
			
			final int minChoice = 1;
			final int maxChoice = 2;
			context.getOutput().display("1. Yes");
			context.getOutput().display("2. No");
			
			int choice = context.getInput().readInteger("", minChoice, maxChoice);

			if (choice == 1) {
				final String decidedMsg = "Player " + player.getPlayerID()
						+ " decided to play a Nope Card.";
				context.getOutput().display(decidedMsg);
				
				player.removeCardFromHand(nopeIndex);
				
				final String playedNopeMsg = "Player " + player.getPlayerID()
						+ " successfully played a Nope Card.";
				context.getOutput().display(playedNopeMsg);
				cancelled = !cancelled;
				
				boolean nopeOfNope = checkForNOPE(context, player);
				if (nopeOfNope) {
					cancelled = !cancelled;
				}
			} else {
				final String didNotPlayMsg = "Player " + player.getPlayerID()
						+ " did not play a Nope Card.";
				context.getOutput().display(didNotPlayMsg);
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
