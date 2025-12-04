package domain.game;

import domain.game.effect.CardEffect;
import domain.game.effect.DrawTrigger;

public class Card {
	private CardType cardType;
	private boolean isMarked;
	private boolean isFacedUp;
	private CardEffect useEffect;
	private DrawTrigger drawTrigger;

	public Card(domain.game.CardType cardType) {
		this.isMarked = false;
		this.cardType = cardType;
		this.isFacedUp = false;
		this.useEffect = null;
		this.drawTrigger = null;
	}

	public domain.game.CardType getCardType() {
		return cardType;
	}

	public void markCard() {
		isMarked = true;
	}

	public boolean checkIfMarked() {
		return isMarked;
	}

	public void setFacedUp() {
		isFacedUp = true;
	}

	public boolean checkIfFacedUp() {
		return isFacedUp;
	}

	public void setUseEffect(CardEffect effect) {
		this.useEffect = effect;
	}

	public CardEffect getUseEffect() {
		return useEffect;
	}

	public boolean hasUseEffect() {
		return useEffect != null;
	}

	public void setDrawTrigger(DrawTrigger trigger) {
		this.drawTrigger = trigger;
	}

	public DrawTrigger getDrawTrigger() {
		return drawTrigger;
	}

	public boolean hasDrawTrigger() {
		return drawTrigger != null;
	}
}

