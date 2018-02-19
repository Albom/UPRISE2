package com.albom.iion.isr.data.mu;

import java.nio.file.Path;
import java.util.LinkedList;
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

	@Override
	public int load(Path dir) {
		int result = 0;
		List<String> list = Directory.list(dir.toString());
		for (String name : list) {
			System.out.println(name);
			int i = 0;
			MuSession s = null;
			project.begin();
			while ((s = MuSessionFS.load(name, i)) != null) {
				LinkedList<Point> points = Acf.calc(s);
				for (Point p : points) {
					project.insert(table, p);
					result++;
				}
				i++;
			}
			project.commit();
		}
		return result;
	}

}
