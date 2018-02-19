package com.albom.iion.isr.data;

import java.nio.file.Path;

public class ProjectFS {

	protected ProjectDB project;
	protected String table;

	public ProjectFS(ProjectDB project, String table){
		this.project = project;
		this.table = table;
	}
	
	public int load(Path dir){
	return 0;
	}

	/**
	 * @return the project DB
	 */
	public ProjectDB getProjectDB() {
		return project;
	}
	
}
