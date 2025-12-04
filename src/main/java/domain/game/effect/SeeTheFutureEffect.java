package domain.game.effect;

import domain.game.Card;

public class SeeTheFutureEffect implements CardEffect {
	private final int cardsToReveal;

	public SeeTheFutureEffect(int cardsToReveal) {
		this.cardsToReveal = cardsToReveal;
	}

	@Override
	public boolean canExecute(EffectContext context) {
		return context.getDeck().getDeckSize() > 0;
	}

	@Override
	public EffectResult execute(EffectContext context) {
		if (!canExecute(context)) {
			return EffectResult.failed("Deck is empty, cannot see the future");
		}

		int cardsAvailable = Math.min(cardsToReveal, context.getDeck().getDeckSize());
		StringBuilder message = new StringBuilder("Top " + cardsAvailable + " cards: ");
		
		int deckSize = context.getDeck().getDeckSize();
		for (int i = deckSize - 1; i >= deckSize - cardsAvailable; i--) {
			Card card = context.getDeck().getCardAtIndex(i);
			message.append(card.getCardType());
			if (i > context.getDeck().getDeckSize() - cardsAvailable) {
				message.append(", ");
			}
		}

		return EffectResult.success(message.toString());
	}
}
