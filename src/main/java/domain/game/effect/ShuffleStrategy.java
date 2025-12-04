package domain.game.effect;

import domain.game.Deck;

public interface ShuffleStrategy {
	void shuffle(Deck deck);
}

class FisherYatesShuffle implements ShuffleStrategy {

	public FisherYatesShuffle() {
	}

	@Override
	public void shuffle(Deck deck) {
		// Delegate to Deck's existing shuffle method
		deck.shuffleDeck();
	}
}
