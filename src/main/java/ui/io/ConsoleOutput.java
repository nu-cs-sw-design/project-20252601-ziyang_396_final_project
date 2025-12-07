package ui.io;

public class ConsoleOutput implements OutputProvider {

	@Override
	public void display(String message) {
		System.out.println(message);
	}
}
