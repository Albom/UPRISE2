package com.albom.iion.isr.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ProjectFactory {

	public ProjectFactory() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// TODO change String to Path
	public ProjectDB getProject(String fileName) {

		Connection connection = null;

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);
		} catch (SQLException e) {
			return null;
		}

		return new ProjectDB(connection);
	}

}
