package model.entities;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Player extends User {

	
	private String password;
	private int money;
	private int bet;
	
	private List<Card> cardsOnHand2 = new ArrayList<Card>();

	public Player(String username, String password, int money) {
		this.username = username;
		this.password = password;
		this.money = money;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getBet() {
		return bet;
	}
	
	public void setBet(int bet) {
		this.bet = bet;
	}
	
	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
	
	public void addCards(Card cards, List<Card> deck) {
		deck.add(cards);
	}
	
	public List<Card> getCardsOnHand(int deckIndex) {
		if (deckIndex == 0) {
			return super.getCardsOnHand();
		}
		return cardsOnHand2;
	}
	
	public void splitHands() {
		System.out.println(super.getCardsOnHand().get(1));
		cardsOnHand2.add(super.getCardsOnHand().get(1));
		super.getCardsOnHand().remove(1);
	}
	
	public String toString() {
		return String.format("Player: [Username: %s | Money: %d]", username, money);
	}
	
	public int calculateResult(int deck) {
		int total = 0;

		for (Card card : getCardsOnHand(deck)) {
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
	
	public static String passwordEncryption(String password) {
		MessageDigest digest;
		byte[] hash = null;
		String passwordEncrypted = "";
		try {
			digest = MessageDigest.getInstance("MD5");
			hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		for (byte hsh : hash) {
			passwordEncrypted += hsh;
		}
		return passwordEncrypted;
	}

}
