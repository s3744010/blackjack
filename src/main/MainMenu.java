package main;

import java.util.Scanner;

import model.GameMainFrame;

public class MainMenu {

	private Scanner sc = new Scanner(System.in);
	private String input, username = null, password;
	private GameMainFrame gameMainFrame;

	public MainMenu() {
		gameMainFrame = new GameMainFrame();

		while (true) {
			System.out.printf("%s%n%-15s %s%n%-15s %s%n%-15s %s%n%-15s %s%n%-15s %s%n%-15s ", "*****MAIN MENU*****",
					"NEW PLAYER", "N", "LOAD PLAYER", "L", "LIST PLAYERS", "P", "REMOVE PLAYER", "R", "EXIT GAME", "E",
					"Command:");
			input = sc.nextLine();

			if (input.equalsIgnoreCase("N")) {
				addPlayer();
			}

			if (input.equalsIgnoreCase("L")) {
				loadPlayer();
			}

			if (input.equalsIgnoreCase("P")) {
				listPlayers();
			}

			if (input.equalsIgnoreCase("R")) {
				removePlayer();
			}

			if (input.equalsIgnoreCase("E")) {
				return;
			}

			if (input.equals("")) {
				System.out.println("");
			}
		}

	}

	private void addPlayer() {
		while (username == null || username.isEmpty()) {
			System.out.printf("%nEnter a Username: ");
			username = sc.nextLine();
			if (username.equals("")) {
				return;
			}
			if (gameMainFrame.playerExists(username)) {
				username = null;
				System.out.println("Error: Username is invalid.");
			}
		}
		
		System.out.printf("Enter a Password: ");
		password = sc.nextLine();
		if (password.equals("")) {
			return;
		}
		gameMainFrame.addPlayer(username, password);
		username = null;
		password = null;
	}

	private void listPlayers() {
		System.out.printf("%n%s%n", gameMainFrame.listPlayers());
	}

	private void loadPlayer() {
		while (username == null || username.isEmpty()) {
			System.out.printf("%nEnter a Username: ");
			username = sc.nextLine();
			if (username.equals("")) {
				return;
			}
			if (!gameMainFrame.playerExists(username)) {
				System.out.println("Error: Username is invalid.");
				username = null;
			}
		}
		
		while (password == null || password.isEmpty()) {
			System.out.printf("Enter a Password: ");
			password = sc.nextLine();
			if (password.equals("")) {
				return;
			}
			if (!gameMainFrame.passwordMatch(username, password)) {
				System.out.println("Error: Password mismatched.");
				password = null;
			}
		}
		
		gameMainFrame.loadPlayer(username);
		username = null;
		password = null;
	}

	private void removePlayer() { 
		while (username == null || username.isEmpty()) {
			System.out.printf("%nEnter a Username: ");
			username = sc.nextLine();
			if (username.equals("")) {
				return;
			}
			if (!gameMainFrame.playerExists(username)) {
				System.out.println("Error: Username Invalid.");
				username = null;
			}
		}
		
		while (password == null || password.isEmpty()) {
			System.out.printf("Enter a Password: ");
			password = sc.nextLine();
			if (username.equals("")) {
				return;
			}
			if (!gameMainFrame.passwordMatch(username, password)) {
				System.out.println("Error: Password mismatched.");
				password = null;
			}
		}
		
		gameMainFrame.removePlayer(username);
		System.out.printf("%s%s%s%n%n", "The player ", username, " has been removed.");
		username = null;
		password = null;
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		MainMenu mainMenu = new MainMenu();
	}

}
