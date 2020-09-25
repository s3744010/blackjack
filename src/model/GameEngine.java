package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import model.entities.Card;
import model.entities.Dealer;
import model.entities.Player;
import model.enumeration.CardSuit;

public class GameEngine {

	private List<Card> cards = new ArrayList<Card>();
	private List<Card> deckCards = new ArrayList<Card>();

	private Dealer dealer;
	private Player currentPlayer;

	public GameEngine(Player player) {
		this.dealer = new Dealer();
		this.currentPlayer = player;
		int enumOrdinal = 0;
		String[] cardNumber = new String[] { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
		for (int index = 0; index < 4; index++) {
			CardSuit cardSuit = CardSuit.values()[enumOrdinal++ % CardSuit.values().length];
			for (String cardNum : cardNumber) {
				cards.add(new Card(cardNum, cardSuit));
			}
		}
		deckCards.addAll(this.cards);
		Collections.shuffle(deckCards);
	}

	public void deckCardsShuffle() {
		if (deckCards.isEmpty()) {
			deckCards.addAll(this.cards);
			Collections.shuffle(deckCards);
		}
	}

	public boolean placeBet(int bet) {
		if (bet <= currentPlayer.getMoney()) {
			currentPlayer.setMoney(currentPlayer.getMoney() - bet);
			currentPlayer.setBet(bet);
			return false;
		}
		return true;
	}

	public boolean dealCards() {
		for (int index = 0; index < 2; index++) {
			deckCardsShuffle();
			currentPlayer.addCards(deckCards.get(0));
			deckCards.remove(0);
			deckCardsShuffle();
			dealer.addCards(deckCards.get(0));
			deckCards.remove(0);
		}
		printResult();
		if (dealer.calculateResult() == 21 || currentPlayer.calculateResult() == 21) {
			applyWinLoss();
			return true;
		}
		return false;
	}

	public boolean hit(Player player) {
		deckCardsShuffle();
		player.addCards(deckCards.get(0));
		deckCards.remove(0);
		printResult();
		if (player.calculateResult() > 21) {
			System.out.println(player.getUsername() + " has BURST!");
			applyWinLoss();
			return false;
		}
		return true;
	}

	public void stay() {
		while (dealer.calculateResult() <= 16) {
			deckCardsShuffle();
			dealer.addCards(deckCards.get(0));
			deckCards.remove(0);
		}
		printResult();
		applyWinLoss();
	}

	public void doubleDown() {
		currentPlayer.setMoney(currentPlayer.getMoney() - currentPlayer.getBet());
		currentPlayer.setBet(currentPlayer.getBet() * 2);
		hit(currentPlayer);
		stay();
	}

	public void split() {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		boolean repeat;
		String input;
		
		currentPlayer.setMoney(currentPlayer.getMoney() - currentPlayer.getBet());
		currentPlayer.splitHands();
		for (int index  = 0; index < 2; index++) {
			repeat  = true;
			while (repeat) {
				System.out.println("Dealer: " + dealer.getCardsOnHand().toString() + " Total: " + dealer.calculateResult());
				System.out.println("Player: " + currentPlayer.getCardsOnHand(index).toString() + " Total: " + currentPlayer.calculateResult(index));
				System.out.printf("%n%s%n%-14s %s%n%-14s %s%n%-14s ", "******Game******", "HIT", "H", "STAY", "S",
						"Command:");
				input = sc.nextLine();
				if (input.equalsIgnoreCase("H")) {
					deckCardsShuffle();
					currentPlayer.addCards(deckCards.get(0), currentPlayer.getCardsOnHand(index));
					deckCards.remove(0);
					if (currentPlayer.calculateResult(index) > 21) {
						System.out.println(currentPlayer.getUsername() + " has BURST!");
						System.out.println("Dealer: " + dealer.getCardsOnHand().toString() + " Total: " + dealer.calculateResult());
						System.out.println("Player: " + currentPlayer.getCardsOnHand(index).toString() + " Total: " + currentPlayer.calculateResult(index));
						System.out.println("");
						repeat = false;
						continue;
					}
					
					repeat = true;
				}

				if (input.equalsIgnoreCase("S")) {
					System.out.println("Dealer: " + dealer.getCardsOnHand().toString() + " Total: " + dealer.calculateResult());
					System.out.println("Player: " + currentPlayer.getCardsOnHand(index).toString() + " Total: " + currentPlayer.calculateResult(index));
					System.out.println("");
					if (index == 1) {
						while (dealer.calculateResult() <= 16) {
							deckCardsShuffle();
							dealer.addCards(deckCards.get(0));
							deckCards.remove(0);
						}
					}
					repeat = false;
				}
			}
		}
		applyWinLossSplitDeck();
	}

	public void surrender() {
		currentPlayer.setMoney(currentPlayer.getMoney() + currentPlayer.getBet() / 2);
		System.out.println("You've surrendered.");
		System.out.println(currentPlayer.toString());
	}

	public void printResult() {
		System.out.println("Dealer: " + dealer.getCardsOnHand().toString() + " Total: " + dealer.calculateResult());
		System.out.println("Player: " + currentPlayer.getCardsOnHand().toString() + " Total: " + currentPlayer.calculateResult());
	}
	
	
	/**
	* Settles the bet made by player considering all possible conditions win loss 
	* between the player and the dealer.
	* <p>
	* Considering blackjack has been checked initially, the conditions for blackjack
	* is redundant. This leaves us with a draw aka. "Push", "Win" and "Loss". The 
	* additional condition where the player lose by "Bust" was also added.
	* <p>
	* WIN: Return the bet + bet
	* LOSE: No changes - Initial bet has been taken from Player:[int money]
	*
	* @param  	void
	* @return	void
	* @see 		Dealer
	* @see		Player
	*/
	public void applyWinLoss() {
		
		if ( currentPlayer.calculateResult() > 21 ) {
			System.out.println("You have busted");
		} else if ( currentPlayer.calculateResult() == dealer.calculateResult() ) {
			System.out.println("You have pushed");
			currentPlayer.setMoney(currentPlayer.getMoney() + currentPlayer.getBet());
		} else if ( currentPlayer.calculateResult() < dealer.calculateResult() && dealer.calculateResult() <= 21 ) {
			System.out.println("You have lost");
		} else {
			System.out.println("You have won");
			currentPlayer.setMoney(currentPlayer.getMoney() + currentPlayer.getBet() * 2);
		}
		
		System.out.println(currentPlayer.toString());
		emptyHands();
	}
	
	public void applyWinLossSplitDeck() {
		for (int index = 0; index < 2; index++) {
			System.out.println("Dealer: " + dealer.getCardsOnHand().toString() + " Total: " + dealer.calculateResult());
			System.out.println("Player: " + currentPlayer.getCardsOnHand(index).toString() + " Total: " + currentPlayer.calculateResult(index));
			if (currentPlayer.calculateResult(index) == dealer.calculateResult()) {
				System.out.println("Push!" + "- Hand " + (index + 1));
				currentPlayer.setMoney(currentPlayer.getMoney() + currentPlayer.getBet());
				System.out.println(currentPlayer.toString());
				continue;
			}

			if (currentPlayer.calculateResult(index) == 21 && currentPlayer.getCardsOnHand(index).size() == 2) {
				currentPlayer.setMoney(currentPlayer.getMoney() + currentPlayer.getBet() * 3 / 2);
				System.out.println("BLACKJACK! You WON!");
			}

			if (dealer.calculateResult() == 21 && dealer.getCardsOnHand().size() == 2) {
				System.out.println("Dealer BLACKJACK! You LOST!");
			}

			if (currentPlayer.calculateResult(index) > dealer.calculateResult() && currentPlayer.calculateResult(index) <= 21
					|| dealer.calculateResult() > 21) {
				currentPlayer.setMoney(currentPlayer.getMoney() + currentPlayer.getBet() * 2);
				System.out.println("You WON!" + "- Hand " + (index + 1));
				System.out.println(currentPlayer.toString());
			} else if (currentPlayer.calculateResult(index) < dealer.calculateResult() || currentPlayer.calculateResult(index) > 21) {
				System.out.println("You Lost!" + "- Hand " + (index + 1));
				System.out.println(currentPlayer.toString());
			}
		}
		emptyHands();
	}

	public void emptyHands() {
		currentPlayer.getCardsOnHand(0).clear();
		currentPlayer.getCardsOnHand(1).clear();
		dealer.getCardsOnHand().clear();
	}
}
