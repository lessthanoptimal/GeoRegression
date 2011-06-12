/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.struct.point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 *
 */
public class UtilPoint3D_F32 {


	public static List<Point3D_F32> copy(List<Point3D_F32> pts) {
		List<Point3D_F32> ret = new ArrayList<Point3D_F32>();

		for (Point3D_F32 p : pts) {
			ret.add(p.copy());
		}

		return ret;
	}

	public static void noiseNormal(List<Point3D_F32> pts, float sigma, Random rand) {
		for (Point3D_F32 p : pts) {
			p.x += (float) rand.nextGaussian() * sigma;
			p.y += (float) rand.nextGaussian() * sigma;
			p.z += (float) rand.nextGaussian() * sigma;
		}
	}

	public static List<Point3D_F32> random(float min, float max, int num, Random rand) {
		List<Point3D_F32> ret = new ArrayList<Point3D_F32>();

		float d = max - min;

		for (int i = 0; i < num; i++) {
			Point3D_F32 p = new Point3D_F32();
			p.x = rand.nextFloat() * d + min;
			p.y = rand.nextFloat() * d + min;
			p.z = rand.nextFloat() * d + min;

			ret.add(p);
		}

		return ret;
	}

	public static Point3D_F32 mean(List<Point3D_F32> points) {
		Point3D_F32 mean = new Point3D_F32();

		float x = 0, y = 0, z = 0;

		for (Point3D_F32 p : points) {
			x += p.x;
			y += p.y;
			z += p.z;
		}

		mean.x = x / points.size();
		mean.y = y / points.size();
		mean.z = z / points.size();

		return mean;
	}
}
