package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) throws SQLException {
		FilmQueryApp app = new FilmQueryApp();
//		app.test();
		app.launch();
	}

	private void test() throws SQLException {
//		Film film = db.findFilmById(666);
//    List<Actor> actors = db.findActorsByFilmId(666);
//    for (Actor actor : actors) {
//		System.out.println(actor + " "+ film); 
//	}
		Actor actor = db.findActorById(73);
		List<Film> films = db.findFilmsByActorId(73);
		for (Film film2 : films) {
			System.out.println("Actor: " + actor.getFirstName() + " " + actor.getLastName() + " Stared in: "
					+ film2.getTitle() + " That was released in: " + film2.getReleaseYear() + " The Film was rated: "
					+ film2.getRating() + " Description of Film: " + film2.getDescription());

		}
	}

	private void launch() throws SQLException {
		Scanner input = new Scanner(System.in);
		boolean running = true;
		while (running) {
			startUserInterface(input);
			int userChoice = input.nextInt();
			input.nextLine();
			if (userChoice == 1) {
				System.out.println("Please enter in the id of the movie you wish to search:");
				int searchByFilmId = input.nextInt();
				input.nextLine();
				Film film = db.findFilmById(searchByFilmId);
				
				if (searchByFilmId < 0 || searchByFilmId > 1000) {
					System.err.println("Sorry that movie has been TERMINATED, please select again\n");
				} else {
					System.out.println("That Film's title is: " + film.getTitle() + " It was released in: "
							+ film.getReleaseYear() + " The Film was rated: " + film.getRating()
							+ " Description of Film: " + film.getDescription() + "\n");
				}
			} else if (userChoice == 2) {
				

			} else if (userChoice == 3) {
				System.out.println("GoodBye! Thanks for visiting your local Redbox please stop by again sometime and dont forget about us like you did Blockbuster");
				running = false;
			}
		}
		input.close();
	}

	private void startUserInterface(Scanner input) {

		System.out.println("Hello welcome to the local redbox, please make a selection from the below menu:");
		System.out.println("1. Look up a film by the id.");
		System.out.println("2. Look up the move by entering in a keyword.");
		System.out.println("3. Exit the redbox.");

	}

}
