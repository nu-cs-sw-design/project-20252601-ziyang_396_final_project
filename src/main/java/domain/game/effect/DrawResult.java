package domain.game.effect;

public class DrawResult {
	private final boolean survived;
	private final String message;
	private final boolean shouldKeepCard;

	private DrawResult(boolean survived, String message, boolean shouldKeepCard) {
		this.survived = survived;
		this.message = message;
		this.shouldKeepCard = shouldKeepCard;
	}

	public boolean didSurvive() {
		return survived;
	}

	public String getMessage() {
		return message;
	}

	public boolean shouldKeepCard() {
		return shouldKeepCard;
	}

	public static DrawResult survived(String message) {
		return new DrawResult(true, message, true);
	}

	public static DrawResult exploded(String message) {
		return new DrawResult(false, message, false);
	}

	public static DrawResult defused(String message) {
		return new DrawResult(true, message, false);
	}
}
