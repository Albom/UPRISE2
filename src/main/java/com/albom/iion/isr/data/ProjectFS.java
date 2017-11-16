package com.albom.iion.isr.data;

import java.util.ArrayList;

import com.albom.utils.Directory;

public class ProjectFS {

	private ProjectDB project;
	private String table;
	
	public ProjectFS(ProjectDB project, String table){
		this.project = project;
		this.table = table;
	}
	
	public int load(String dir){
		int result = 0;
		ArrayList<String> list = Directory.list(dir);
		for ( String name : list ){
			project.begin();
			// TODO loading each file in the directory
			project.commit();
		}
	return result;
	}
	
}
