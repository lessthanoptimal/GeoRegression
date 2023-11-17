/*
 * Copyright (C) 2022, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package georegression.fitting;

import georegression.geometry.UtilPlane3D_F64;
import georegression.geometry.UtilPoint3D_F64;
import georegression.metric.Distance3D_F64;
import georegression.struct.plane.PlaneGeneral3D_F64;
import georegression.struct.plane.PlaneNormal3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Cylinder3D_F64;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestShapeFittingRobustOps {
	Random rand = new Random(0xDEADBEEFL);

	@Test void ransac_plane_points() {
		var plane = new PlaneNormal3D_F64(1, 1, 2, 0, 0, 1);

		List<Point3D_F64> points = UtilPoint3D_F64.random(plane, 2, 100, rand);

		// add one outlier
		points.get(12).setTo(200, -2394, 93213);

		var alg = new ShapeFittingRobustOps();
		alg.configRansac(100, 0.1);
		PlaneGeneral3D_F64 found = alg.ransacPlaneFromPoints(points);

		// See if the points lie on the found plane, except for the outlier
		int count = 0;
		for (int i = 0; i < points.size(); i++) {
			if (Math.abs(UtilPlane3D_F64.evaluate(found, points.get(i))) < 1) {
				count++;
			}
		}

		assertEquals(points.size() - 1, count);
	}

	@Test void ransac_plane_normals() {
		var plane = new PlaneNormal3D_F64(1, 1, 2, 0, 0, 1);

		List<PlaneNormal3D_F64> points = UtilPoint3D_F64.randomNormals(plane, 2, 100, rand);

		// add one outlier
		points.get(12).p.setTo(200, -2394, 93213);

		var alg = new ShapeFittingRobustOps();
		alg.configRansac(100, 0.1);
		PlaneGeneral3D_F64 found = alg.ransacPlaneFromPointNormals(points);

		// See if the points lie on the found plane, except for the outlier
		int count = 0;
		for (int i = 0; i < points.size(); i++) {
			if (Math.abs(UtilPlane3D_F64.evaluate(found, points.get(i).p)) < 1) {
				count++;
			}
		}

		assertEquals(points.size() - 1, count);
	}

	@Test void lmeds_plane_points() {
		var plane = new PlaneNormal3D_F64(1, 1, 2, 0, 0, 1);

		List<Point3D_F64> points = UtilPoint3D_F64.random(plane, 2, 100, rand);

		// add one outlier
		points.get(12).setTo(200, -2394, 93213);

		var alg = new ShapeFittingRobustOps();
		alg.maxIterations = 100;
		PlaneGeneral3D_F64 found = alg.lmedsPlaneFromPoints(points);

		// See if the points lie on the found plane, except for the outlier
		int count = 0;
		for (int i = 0; i < points.size(); i++) {
			if (Math.abs(UtilPlane3D_F64.evaluate(found, points.get(i))) < 1) {
				count++;
			}
		}

		assertEquals(points.size() - 1, count);
	}

	@Test void lmeds_plane_normals() {
		var plane = new PlaneNormal3D_F64(1, 1, 2, 0, 0, 1);

		List<PlaneNormal3D_F64> points = UtilPoint3D_F64.randomNormals(plane, 2, 100, rand);

		// add one outlier
		points.get(12).p.setTo(200, -2394, 93213);

		var alg = new ShapeFittingRobustOps();
		alg.maxIterations = 100;
		PlaneGeneral3D_F64 found = alg.lmedsPlaneFromPointNormals(points);

		// See if the points lie on the found plane, except for the outlier
		int count = 0;
		for (int i = 0; i < points.size(); i++) {
			if (Math.abs(UtilPlane3D_F64.evaluate(found, points.get(i).p)) < 1) {
				count++;
			}
		}

		assertEquals(points.size() - 1, count);
	}

	@Test void ransac_cylinder_normals() {
		var shape = new Cylinder3D_F64(1, 1, 2, -0.25, 0, 1, 0.2);

		List<PlaneNormal3D_F64> points = UtilPoint3D_F64.randomNormals(shape, 2, 100, rand);

		// add one outlier
		points.get(12).p.setTo(200, -2394, 93213);

		var alg = new ShapeFittingRobustOps();
		alg.configRansac(100, 0.1);
		Cylinder3D_F64 found = alg.ransacCylinderFromPointNormals(points);

		// See if the points lie on the found plane, except for the outlier
		int count = 0;
		for (int i = 0; i < points.size(); i++) {
			if (Math.abs(Distance3D_F64.distance(found, points.get(i).p)) < 1) {
				count++;
			}
		}

		assertEquals(points.size() - 1, count);
	}

	@Test void lmeds_cylinder_normals() {
		var shape = new Cylinder3D_F64(1, 1, 2, -0.25, 0, 1, 0.2);

		List<PlaneNormal3D_F64> points = UtilPoint3D_F64.randomNormals(shape, 2, 100, rand);

		// add one outlier
		points.get(12).p.setTo(200, -2394, 93213);

		var alg = new ShapeFittingRobustOps();
		alg.maxIterations = 100;
		Cylinder3D_F64 found = alg.lmedsCylinderFromPointNormals(points);

		// See if the points lie on the found plane, except for the outlier
		int count = 0;
		for (int i = 0; i < points.size(); i++) {
			if (Math.abs(Distance3D_F64.distance(found, points.get(i).p)) < 1) {
				count++;
			}
		}

		assertEquals(points.size() - 1, count);
	}
}