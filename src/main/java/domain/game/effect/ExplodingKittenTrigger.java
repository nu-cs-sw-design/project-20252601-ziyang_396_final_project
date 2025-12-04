package domain.game.effect;

import domain.game.CardType;
import domain.game.Card;

public class ExplodingKittenTrigger implements DrawTrigger {

	@Override
	public DrawResult onDraw(EffectContext context) {
		// Check if player has a defuse card
		boolean hasDefuse = false;
		int defuseIndex = -1;
		
		for (int i = 0; i < context.getCurrentPlayer().getHandSize(); i++) {
			Card card = context.getCurrentPlayer().getCardAt(i);
			if (card.getCardType() == CardType.DEFUSE) {
				hasDefuse = true;
				defuseIndex = i;
				break;
			}
		}

		if (!hasDefuse) {
			final String explodedMsg = "You drew an Exploding Kitten and have no "
					+ "Defuse card! You are eliminated.";
			return DrawResult.exploded(explodedMsg);
		}

		final String defuseMsg = "You drew an Exploding Kitten but you have "
				+ "a Defuse card!";
		context.getUserInput().displayMessage(defuseMsg);
		
		// Remove defuse from hand
		context.getCurrentPlayer().removeCardFromHand(defuseIndex);
		
		// Ask where to put the kitten
		final String positionPrompt = "Where do you want to place the "
				+ "Exploding Kitten? (0 = top, "
				+ context.getDeck().getDeckSize() + " = bottom)";
		int position = context.getUserInput().promptForInteger(
			positionPrompt,
			0,
			context.getDeck().getDeckSize()
		);

		// Create new kitten card and insert it
		context.getDeck().insertExplodingKittenAtIndex(position);

		final String resultMsg = "You defused the Exploding Kitten and placed "
				+ "it at position " + position;
		return DrawResult.defused(resultMsg);
	}
}
