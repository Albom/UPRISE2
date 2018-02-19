package com.albom.iion.isr.data.mu;

import java.nio.file.Path;
import java.util.List;

import com.albom.iion.isr.data.Point;
import com.albom.iion.isr.data.ProjectDB;
import com.albom.iion.isr.data.ProjectFS;
import com.albom.iion.isr.data.mu.MuSession;
import com.albom.iion.isr.data.mu.MuSessionFS;
import com.albom.iion.isr.processing.mu.Acf;
import com.albom.utils.Directory;

public class MuProjectFS extends ProjectFS {

	public MuProjectFS(ProjectDB project, String table) {
		super(project, table);
	}

	private boolean readProperties(String fileName) {

		MuSession test = MuSessionFS.load(fileName, 0);
		if (test == null) {
			return false;
		}

		if (!project.checkTable("properties")) {
			return false;
		}

		MuHeader header = test.getHead();

		project.setProperty("nh", String.valueOf(header.getNhigh()));
		project.setProperty("start", String.valueOf(header.getJstart()));
		project.setProperty("sampling", String.valueOf(header.getJsint()));
		project.setProperty("zenith", String.valueOf(new MuDirection(header.getIbeam()[0]).getZenith()));

		return true;
	}

	@Override
	public int load(Path dir) {
		int result = 0;
		List<String> list = Directory.list(dir.toString());
		readProperties(list.get(0));
		for (String name : list) {
			System.out.println(name); /*
			int i = 0;
			MuSession s = null;
			project.begin();
			while ((s = MuSessionFS.load(name, i)) != null) {
				List<Point> points = Acf.calc(s);
				for (Point p : points) {
					project.insert(table, p);
					result++;
				}
				i++;
			}
			project.commit(); */
		}
		return result;
	}

}
