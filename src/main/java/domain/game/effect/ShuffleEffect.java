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

		ConsoleInput consoleInput = (ConsoleInput) context.getInput();
		int shuffleCount = consoleInput.promptForShuffleCount();
		for (int i = 0; i < shuffleCount; i++) {
			strategy.shuffle(context.getDeck());
		}
		return EffectResult.success("Deck shuffled " + shuffleCount + " times!");
	}
}
