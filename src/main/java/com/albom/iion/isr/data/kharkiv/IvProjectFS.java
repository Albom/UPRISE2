package com.albom.iion.isr.data.kharkiv;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.albom.iion.isr.data.Point;
import com.albom.iion.isr.data.ProjectDB;
import com.albom.iion.isr.data.ProjectFS;
import com.albom.iion.isr.processing.iv.Acf;
import com.albom.util.Directory;

public class IvProjectFS extends ProjectFS {

	public IvProjectFS(ProjectDB project, String table) {
		super(project, table);
	}

	private boolean readProperties(String fileName) {
		IvFile iv = IvFileFS.load(Paths.get(fileName));
		if (!project.checkTable("properties")) {
			return false;
		}
		project.setProperty("nh", String.valueOf(iv.getNumPoint()));
		return true;
	}

	@Override
	public int load(Path dir) {
		int result = 0;
		List<String> list = Directory.list(dir.toString());
		if (!list.isEmpty()) {
			readProperties(list.get(0));
		}
		for (String name : list) {
			System.out.println(name);
			IvFile iv = IvFileFS.load(Paths.get(name));
			List<Point> points = Acf.calc(iv);
			project.insert(table, points);
			result += points.size();
		}
		return result;
	}

}
