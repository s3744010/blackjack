package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.entities.Player;

public class GameMainFrame {

	private Scanner sc = new Scanner(System.in);
	private List<Player> players;

	public GameMainFrame() {
		players = new ArrayList<Player>();
	}

	public List<Player> getAllPlayers() {
		return players;
	}

	public void addPlayer(String username, String password) {
		Player player = new Player(username, Player.passwordEncryption(password), 1000);
		players.add(player);
		runGame(player);
	}

	public void loadPlayer(String username) {
		for (Player player : players) {
			if (player.getUsername().equals(username)) {
				runGame(player);
			}
		}
	}

	public String listPlayers() {
		String output = "";
		if (players.size() == 0) {
			return String.format("%s%n", "There are no players that was previously saved.");
		}

		for (Player player : players) {
			output += String.format("%s%n", player.toString());
		}
		return output;
	}

	public void removePlayer(String username) {
		for (int index = 0; index < players.size(); index++) {
			if (players.get(index).getUsername().equals(username)) {
				players.remove(index);
			}
		}
		players.toString();
	}

	public boolean playerExists(String username) {
		for (Player player : players) {
			if (player.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}

	public boolean passwordMatch(String username, String password) {
		for (Player player : players) {
			if (player.getUsername().equals(username)) {
				if (player.getPassword().equals(Player.passwordEncryption(password))) {
					return true;
				}
			}
		}
		return false;
	}

	public void overwriteData(List<Player> players) {
		this.players.addAll(players);
	}

	public void runGame(Player player) {
		GameEngine gameEngine = new GameEngine(player);
		String input = null;

		while (true) {
			System.out.printf("%nPlace a bet: ");
			input = sc.nextLine();
			if (input.equals("")) {
				System.out.println("");
				return;
			}
			if (!input.matches("[0-9]+")) {
				System.out.println("Error: Please insert a number.");
				continue;
			}
			if (gameEngine.placeBet(Integer.parseInt(input))) {
				System.out.println("Bet is larger than the current amount that you have.");
				continue;
			}
			if (gameEngine.dealCards()) {
				continue;
			}

			if (player.getCardsOnHand().get(0).getNumber().equals(player.getCardsOnHand().get(1).getNumber())) {
				System.out.printf("%n%s%n%-14s %s%n%-14s %s%n%-14s %s%n%-14s %s%n%-14s %s%n%-14s ", "******Game******",
						"HIT", "H", "STAY", "S", "DOUBLE", "D", "SPLIT", "T", "SURRENDER", "R", "Command:");
			} else {
				System.out.printf("%n%s%n%-14s %s%n%-14s %s%n%-14s %s%n%-14s %s%n%-14s ", "******Game******", "HIT",
						"H", "STAY", "S", "DOUBLE", "D", "SURRENDER", "R", "Command:");
			}

			boolean repeat = true;
			input = sc.nextLine();

			if (input.equalsIgnoreCase("H")) {
				repeat = gameEngine.hit(player);

				while (repeat) {
					System.out.printf("%n%s%n%-14s %s%n%-14s %s%n%-14s ", "******Game******", "HIT", "H", "STAY", "S",
							"Command:");
					input = sc.nextLine();
					if (input.equalsIgnoreCase("H")) {
						repeat = gameEngine.hit(player);
					}

					if (input.equalsIgnoreCase("S")) {
						gameEngine.stay();
						repeat = false;
					}
				}
				continue;
			}

			if (input.equalsIgnoreCase("S")) {
				gameEngine.stay();
			}

			if (input.equalsIgnoreCase("D")) {
				gameEngine.doubleDown();
			}

			if (input.equalsIgnoreCase("T")) {
				gameEngine.split();
			}

			if (input.equalsIgnoreCase("R")) {
				gameEngine.surrender();
			}
		}
	}
}
