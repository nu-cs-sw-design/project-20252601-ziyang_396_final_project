package domain.game.effect;

public class ShuffleEffect implements CardEffect {
	private final ShuffleStrategy strategy;

	public ShuffleEffect(ShuffleStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public boolean canExecute(EffectContext context) {
		return context.getDeck().getDeckSize() > 0;
	}

	@Override
	public EffectResult execute(EffectContext context) {
		if (!canExecute(context)) {
			return EffectResult.failed("Cannot shuffle an empty deck");
		}

		final int minShuffleCount = 1;
		final int maxShuffleCount = 100;
		final String prompt = "How many times to shuffle? ("
				+ minShuffleCount + "-" + maxShuffleCount + ")";
		int shuffleCount = context.getInput().readInteger(
				prompt, minShuffleCount, maxShuffleCount);
		for (int i = 0; i < shuffleCount; i++) {
			strategy.shuffle(context.getDeck());
		}
		return EffectResult.success("Deck shuffled " + shuffleCount + " times!");
	}
}
