package domain.game.effect;

public class EffectResult {
	private final boolean success;
	private final boolean endsTurn;
	private final String message;
	private final boolean cancelled;

	private EffectResult(boolean success, boolean endsTurn, String message, boolean cancelled) {
		this.success = success;
		this.endsTurn = endsTurn;
		this.message = message;
		this.cancelled = cancelled;
	}

	public boolean isSuccess() {
		return success;
	}

	public boolean shouldEndTurn() {
		return endsTurn;
	}

	public String getMessage() {
		return message;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public static EffectResult success(String message) {
		return new EffectResult(true, false, message, false);
	}

	public static EffectResult successEndsTurn(String message) {
		return new EffectResult(true, true, message, false);
	}

	public static EffectResult cancelled(String message) {
		return new EffectResult(false, false, message, true);
	}

	public static EffectResult failed(String message) {
		return new EffectResult(false, false, message, false);
	}
}
