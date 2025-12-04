package domain.game.effect;

public interface CardEffect {

	EffectResult execute(EffectContext context);

	boolean canExecute(EffectContext context);
}
