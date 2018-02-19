package com.albom.iion.isr.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ProjectDB {

	private Connection connection = null;

	public ProjectDB(Connection connection) {
		this.connection = connection;
	}

	public boolean checkTable(String table){
		int num = 0;
		try {
			PreparedStatement statement = connection
					.prepareStatement("SELECT count(*) FROM sqlite_master WHERE type='table' AND name=?;");
			statement.setString(1, table);
			ResultSet result = statement.executeQuery();
			result.next();
			num = result.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return num > 0;
	}
	
	public void createTable(String table) {

		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS " + table
					+ " (id INTEGER PRIMARY KEY AUTOINCREMENT, time DATETIME, alt INT, lag INT, value DOUBLE);");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void insert(String table, Point data) {

		try {

			PreparedStatement statement = connection
					.prepareStatement("INSERT INTO " + table + " (time, alt, lag, value) VALUES (?, ?, ?, ?);");

			statement.setTimestamp(1, Timestamp.valueOf(data.getDate()));
			statement.setInt(2, data.getAlt());
			statement.setInt(3, data.getLag());
			statement.setDouble(4, data.getValue());

			statement.execute();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void close() {

		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void begin() {

		try {
			Statement statement = connection.createStatement();
			statement.execute("BEGIN;");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void commit() {

		try {
			Statement statement = connection.createStatement();
			statement.execute("COMMIT;");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<LocalDateTime> getDates(String table, int lag) {
		ArrayList<LocalDateTime> dates = new ArrayList<>();

		try {
			PreparedStatement statement = connection
					.prepareStatement("SELECT DISTINCT time FROM " + table + " WHERE lag = ? ORDER BY time;");
			statement.setInt(1, lag);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				LocalDateTime date = result.getTimestamp(1).toLocalDateTime();
				dates.add(date);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dates;
	}

	public ArrayList<Point> getTimeDependency(String table, int alt, int lag) {
		ArrayList<Point> points = new ArrayList<>();

		try {
			PreparedStatement statement = connection
					.prepareStatement("SELECT time, value FROM " + table + " WHERE alt = ? AND lag = ? ORDER BY time;");
			statement.setInt(1, alt);
			statement.setInt(2, lag);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				LocalDateTime time = result.getTimestamp(1).toLocalDateTime();
				double value = result.getDouble(2);
				points.add(new Point(time, alt, lag, value));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return points;
	}

	public ArrayList<Point> getHeightDependency(String table, LocalDateTime time, int lag) {
		ArrayList<Point> points = new ArrayList<>();

		try {
			PreparedStatement statement = connection
					.prepareStatement("SELECT alt, value FROM " + table + " WHERE time = ? AND lag = ? ORDER BY alt;");
			statement.setTimestamp(1, Timestamp.valueOf(time));
			statement.setInt(2, lag);
			ResultSet result = statement.executeQuery();
			while (result.next()) {
				int alt = result.getInt(1);
				double value = result.getDouble(2);
				points.add(new Point(time, alt, lag, value));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return points;
	}

}
