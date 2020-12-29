/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

package georegression.geometry;

import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F64;
import georegression.struct.shapes.Box3D_F64;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilPoint3D_F64 {

	Random rand = new Random(234);

	@Test void findClosestIdx() {
		List<Point3D_F64> list = new ArrayList<>();
		assertEquals(-1, UtilPoint3D_F64.findClosestIdx(1,2,0,list,2));

		list.add(new Point3D_F64(5,2,0));
		assertEquals(-1, UtilPoint3D_F64.findClosestIdx(1,2,0,list,2));
		assertEquals(0, UtilPoint3D_F64.findClosestIdx(1,2,0,list,4));

		list.add(new Point3D_F64(5,2,0));
		list.add(new Point3D_F64(6,2,0));
		list.add(new Point3D_F64(8,2,0));

		assertEquals(2, UtilPoint3D_F64.findClosestIdx(6,2.1,0,list,2));
		assertEquals(-1, UtilPoint3D_F64.findClosestIdx(100,2,0,list,2));
	}

	@Test
	void noiseNormal_single() {
		Point3D_F64 mean = new Point3D_F64(3,4,5);
		double sx=1,sy=0.5,sz=0.25;

		List<Point3D_F64> points = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			points.add( UtilPoint3D_F64.noiseNormal(mean,sx,sy,sz,rand,null));
		}

		Point3D_F64 found = UtilPoint3D_F64.mean(points,null);

		assertEquals(mean.x,found.x,0.01);
		assertEquals(mean.y,found.y,0.01);
		assertEquals(mean.z,found.z,0.01);

		double stdevX=0,stdevY=0,stdevZ=0;

		for (int i = 0; i < points.size(); i++) {
			Point3D_F64 p = points.get(i);
			double dx = p.x-found.x;
			double dy = p.y-found.y;
			double dz = p.z-found.z;

			stdevX += dx*dx;
			stdevY += dy*dy;
			stdevZ += dz*dz;
		}

		assertEquals(sx,Math.sqrt(stdevX/points.size()),sx/20);
		assertEquals(sy,Math.sqrt(stdevY/points.size()),sy/20);
		assertEquals(sz,Math.sqrt(stdevZ/points.size()),sz/20);
	}

	@Test
	void distance() {
		double found = UtilPoint3D_F64.distance(1,2,3,4,-3,-4);
		assertEquals(9.1104,found,1e-3);
	}

	@Test
	void distanceSq() {
		double found = UtilPoint3D_F64.distanceSq(1, 2, 3, 4, -3, -4);
		assertEquals(83, found, GrlConstants.TEST_F64);
	}

	@Test
	void boundingBox() {
		List<Point3D_F64> list = new ArrayList<Point3D_F64>();

		list.add( new Point3D_F64(1,1,1));
		list.add( new Point3D_F64(2,3,1));
		list.add( new Point3D_F64(1.5,2,5));

		Box3D_F64 cube = new Box3D_F64();
		UtilPoint3D_F64.boundingBox(list, cube);

		assertEquals(0,cube.getP0().distance(list.get(0)),GrlConstants.TEST_F64);
		assertEquals(0,cube.getP1().distance(new Point3D_F64(2,3,5)),1e-8);
	}

	@Test
	void axisLargestAbs() {
		assertEquals(0, UtilPoint3D_F64.axisLargestAbs(new Point3D_F64(3,2,1)));
		assertEquals(0, UtilPoint3D_F64.axisLargestAbs(new Point3D_F64(-3,2,1)));
		assertEquals(0, UtilPoint3D_F64.axisLargestAbs(new Point3D_F64(-3,-2,-1)));

		assertEquals(1, UtilPoint3D_F64.axisLargestAbs(new Point3D_F64(2,3,1)));
		assertEquals(1, UtilPoint3D_F64.axisLargestAbs(new Point3D_F64(2,-3,1)));
		assertEquals(1, UtilPoint3D_F64.axisLargestAbs(new Point3D_F64(-2,3,-1)));

		assertEquals(2, UtilPoint3D_F64.axisLargestAbs(new Point3D_F64(2,1,3)));
		assertEquals(2, UtilPoint3D_F64.axisLargestAbs(new Point3D_F64(2,1,-3)));
		assertEquals(2, UtilPoint3D_F64.axisLargestAbs(new Point3D_F64(-2,-1,3)));
	}
}
