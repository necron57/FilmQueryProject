package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=US/Mountain";
	private static final String userName = "student";
	private static final String password = "student";

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Film findFilmById(int filmId) throws SQLException {
		Film film = null;
		String sql = "SELECT * FROM film WHERE id = ?";

		Connection conn = DriverManager.getConnection(URL, userName, password);

		PreparedStatement pstmt = conn.prepareStatement(sql);

		pstmt.setInt(1, filmId);

		ResultSet filmResults = pstmt.executeQuery();

		if (filmResults.next()) {
			film = new Film(filmResults.getInt("id"), filmResults.getString("title"),
					filmResults.getString("description"), filmResults.getShort("release_year"),
					filmResults.getInt("language_id"), filmResults.getInt("rental_duration"),
					filmResults.getDouble("rental_rate"), filmResults.getInt("length"),
					filmResults.getDouble("replacement_cost"), filmResults.getString("rating"),
					filmResults.getString("special_features"));
			
		}

		filmResults.close();
		pstmt.close();
		conn.close();
		return film;
	}

	@Override
	public Actor findActorById(int actorId) throws SQLException {
		Actor actor = null;
		String sql = "SELECT * FROM actor WHERE id = ?";
		// 1) connect to the db
		Connection conn = DriverManager.getConnection(URL, userName, password);

		// 2) prepare the SQL statement (compile / optimize)
		PreparedStatement pstmt = conn.prepareStatement(sql);

		// 3) bind the value, actorid, to the bind variable ?
		pstmt.setInt(1, actorId);

		// 4) run the SQL
		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
//			actor = new Actor(); // Create the object
			// Here is our mapping of query columns to our object fields:
//			actor.setId(rs.getInt("id"));
//			actor.setFirstName(rs.getString("first_name"));
//			actor.setLastName(rs.getString("last_name"));
			// another way of doing the above code
			actor = new Actor(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"));

			actor.setFilms(findFilmsByActorId(actorId));
		}
		rs.close();
		pstmt.close();
		conn.close();
		return actor;
	}

	@Override
	public List<Film> findFilmsByActorId(int actorId) {
		List<Film> films = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(URL, userName, password);
			String sql = "SELECT film.* "
					+ " FROM film JOIN film_actor ON film.id = film_actor.film_id WHERE actor_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, actorId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int filmId = rs.getInt("id");
				String title = rs.getString("title");
				String desc = rs.getString("description");
				short releaseYear = rs.getShort("release_year");
				int langId = rs.getInt("language_id");
				int rentDur = rs.getInt("rental_duration");
				double rate = rs.getDouble("rental_rate");
				int length = rs.getInt("length");
				double repCost = rs.getDouble("replacement_cost");
				String rating = rs.getString("rating");
				String features = rs.getString("special_features");
				Film film = new Film(filmId, title, desc, releaseYear, langId, rentDur, rate, length, repCost, rating,
						features);
				films.add(film);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return films;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actors = new ArrayList<>();

		try {
			Connection conn = DriverManager.getConnection(URL, userName, password);
			String sql = "SELECT film.id, actor.first_name, actor.last_name FROM film JOIN film_actor ON film.id = film_actor.film_id JOIN actor ON film_actor.actor_id = actor_id WHERE actor_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Actor actor = new Actor(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"));
				actors.add(actor);

			}
			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actors;
	}

}
