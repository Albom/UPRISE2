package com.albom.iion.isr.data;

import java.sql.Connection;

public class ProjectDB {

	private Connection connection = null;
	private ProjectProperties properties = null;

	public ProjectDB(Connection connection) {
		this.connection = connection;
	}

	public ProjectProperties getProperties() {
		return properties;
	}

	public void setProperties(ProjectProperties properties) {
		this.properties = properties;
	}

}
