package com.albom.iion.isr.data.kharkiv;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.albom.iion.isr.data.Point;
import com.albom.iion.isr.data.ProjectDB;
import com.albom.iion.isr.data.ProjectFS;
import com.albom.iion.isr.processing.iv.Acf;
import com.albom.utils.Directory;

public class IvProjectFS extends ProjectFS {

	public IvProjectFS(ProjectDB project, String table) {
		super(project, table);
	}

	@Override
	public int load(Path dir) {
		int result = 0;
		List<String> list = Directory.list(dir.toString());
		for (String name : list) {
			System.out.println(name);
			project.begin();
			IvFile iv = IvFileFS.load(Paths.get(name));
			List<Point> points = Acf.calc(iv);
			for (Point p : points) {
				project.insert(table, p);
				result++;
			}
			project.commit();
		}
		return result;
	}

}
