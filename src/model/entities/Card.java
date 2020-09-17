package model.entities;

import model.enumeration.CardSuit;

public class Card {
	
	private String number; 
	private CardSuit suit;
	
	public Card (String number, CardSuit suit) {
		this.number = number;
		this.suit = suit;
	}
	
	public String getNumber() {
		return number;
	}
	
	public String toString() {
		return "Card: (Number: " + number + ", Suit: " + suit + ")";
	}
}
