package model.entities;

import java.util.ArrayList;
import java.util.List;

public abstract class User {

	private List<Card> cardsOnHand = new ArrayList<Card>();
	protected String username;
	
	public String getUsername() {
		return username;
	}

	public void addCards(Card cards) {
		cardsOnHand.add(cards);
	}
	
	public List<Card> getCardsOnHand() {
		return cardsOnHand;
	}

	public int calculateResult() {
		int total = 0;

		for (Card card : cardsOnHand) {
			if (card.getNumber().equals("K") || card.getNumber().equals("Q") || card.getNumber().equals("J")) {
				total += 10;
			} else if (card.getNumber().equals("A") && total >= 11) {
				total += 1;
			} else if (card.getNumber().equals("A") && total < 11) {
				total += 11;
			} else {
				total += Integer.parseInt(card.getNumber());
			}
		}
		return total;
	}

	

}
