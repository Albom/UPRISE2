package com.albom.iion.isr.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProjectDB {

	private Connection connection = null;
	private final String properties = "properties";

	public ProjectDB(Connection connection) {
		this.connection = connection;
		createProperties();
	}

	private void createProperties() {
		try {
			Statement statement = connection.createStatement();
			statement
					.execute("CREATE TABLE IF NOT EXISTS " + properties + " (name VARCHAR PRIMARY KEY, value VARCHAR);");
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setProperty(String name, String value) {
		try {

			PreparedStatement statement = connection
					.prepareStatement("INSERT INTO " + properties + " (name, value) VALUES (?, ?);");

			statement.setString(1, name);
			statement.setString(2, value);

			statement.execute();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getProperty(String name) {
		String value = null;
		try {
			PreparedStatement statement = connection
					.prepareStatement("SELECT value FROM " + properties + " WHERE name=?;");
			statement.setString(1, name);
			ResultSet result = statement.executeQuery();
			result.next();
			value = result.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}

	public boolean checkTable(String table) {
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
			statement.close();
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

	public List<LocalDateTime> getDates(String table, int lag) {
		List<LocalDateTime> dates = new ArrayList<>();

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

	public List<Point> getTimeDependency(String table, int alt, int lag) {
		List<Point> points = new ArrayList<>();

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

	public List<Point> getHeightDependency(String table, LocalDateTime time, int lag) {
		List<Point> points = new ArrayList<>();

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
