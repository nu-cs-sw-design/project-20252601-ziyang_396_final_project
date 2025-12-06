package domain.game.effect;

public class ConsoleOutput implements OutputProvider {

	@Override
	public void display(String message) {
		System.out.println(message);
	}
}
