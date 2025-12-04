package domain.game.effect;

import domain.game.CardType;
import java.util.HashMap;
import java.util.Map;

public class CardEffectFactory {
	private final Map<CardType, CardEffect> effectRegistry;
	private final ShuffleStrategy shuffleStrategy;

	public CardEffectFactory() {
		this.effectRegistry = new HashMap<>();
		this.shuffleStrategy = new FisherYatesShuffle();
		registerEffects();
	}

	private void registerEffects() {
		// Register SHUFFLE with NOPE check
		ShuffleEffect shuffleEffect = new ShuffleEffect(shuffleStrategy);
		effectRegistry.put(CardType.SHUFFLE, new NOPEInterceptor(shuffleEffect, true));

		final int cardsToReveal = 2;
		SeeTheFutureEffect seeTheFutureEffect =
				new SeeTheFutureEffect(cardsToReveal);
		effectRegistry.put(CardType.SEE_THE_FUTURE,
				new NOPEInterceptor(seeTheFutureEffect, true));

		// NOPE and EXPLODING_KITTEN are handled differently
		// NOPE: handled by NOPEInterceptor decorator
		// EXPLODING_KITTEN: uses DrawTrigger interface, not CardEffect
	}

	public CardEffect getEffect(CardType cardType) {
		return effectRegistry.get(cardType);
	}

	public DrawTrigger getTrigger(CardType cardType) {
		if (cardType == CardType.EXPLODING_KITTEN) {
			return new ExplodingKittenTrigger();
		}
		return null;
	}

	public boolean hasEffect(CardType cardType) {
		return effectRegistry.containsKey(cardType);
	}

	public boolean hasTrigger(CardType cardType) {
		return cardType == CardType.EXPLODING_KITTEN;
	}
}
