/*
 * Copyright (C) 2011-2020, Peter Abeles. All Rights Reserved.
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
import georegression.struct.point.Point4D_F64;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Peter Abeles
 */
public class TestUtilPoint4D_F64 {

	Random rand = new Random(234);

	@Test
	void isInfinite() {
		assertFalse(UtilPoint4D_F64.isInfiniteH(new Point4D_F64(1,2,3,4), GrlConstants.EPS ));
		assertTrue(UtilPoint4D_F64.isInfiniteH(new Point4D_F64(1,2,3,0), GrlConstants.EPS ));
	}

	@Test
	void randomN_4D() {
		Point4D_F64 center = new Point4D_F64(-0.2,1,-2.3,4);
		List<Point4D_F64> list = UtilPoint4D_F64.randomN(center,0.5,2000,rand);

		assertEquals(2000,list.size());

		double meanX=0,meanY=0,meanZ=0,meanW=0;

		for (int i = 0; i < list.size(); i++) {
			Point4D_F64 p = list.get(i);
			meanX += p.x; meanY += p.y; meanZ += p.z; meanW += p.w;
		}
		meanX /= list.size();meanY /= list.size();meanZ /= list.size();meanW /= list.size();

		double stdX=0,stdY=0,stdZ=0,stdW=0;
		for (int i = 0; i < list.size(); i++) {
			Point4D_F64 p = list.get(i);
			stdX += (p.x-meanX)*(p.x-meanX);
			stdY += (p.y-meanY)*(p.y-meanY);
			stdZ += (p.z-meanZ)*(p.z-meanZ);
			stdW += (p.w-meanW)*(p.w-meanW);
		}
		stdX = Math.sqrt(stdX/list.size());
		stdY = Math.sqrt(stdY/list.size());
		stdZ = Math.sqrt(stdZ/list.size());
		stdW = Math.sqrt(stdW/list.size());

		assertEquals(center.x,meanX,0.02);
		assertEquals(center.y,meanY,0.02);
		assertEquals(center.z,meanZ,0.02);
		assertEquals(center.w,meanW,0.02);

		assertEquals(0.5,stdX,0.02);
		assertEquals(0.5,stdY,0.02);
		assertEquals(0.5,stdZ,0.02);
		assertEquals(0.5,stdW,0.02);
	}

	@Test
	void randomN_3D() {
		Point3D_F64 center = new Point3D_F64(-0.5, 1.1, -3);
		List<Point4D_F64> list = UtilPoint4D_F64.randomN(center, 0.1, 0.5, 2000, rand);

		assertEquals(2000, list.size());

		double meanX = 0, meanY = 0, meanZ = 0;

		for (int i = 0; i < list.size(); i++) {
			Point4D_F64 p = list.get(i);
			meanX += p.x;
			meanY += p.y;
			meanZ += p.z;
			assertEquals(0.1, p.w, UtilEjml.TEST_F64);
		}
		meanX /= list.size();
		meanY /= list.size();
		meanZ /= list.size();

		double stdX = 0, stdY = 0, stdZ = 0;
		for (int i = 0; i < list.size(); i++) {
			Point4D_F64 p = list.get(i);
			stdX += (p.x - meanX) * (p.x - meanX);
			stdY += (p.y - meanY) * (p.y - meanY);
			stdZ += (p.z - meanZ) * (p.z - meanZ);
		}
		stdX = Math.sqrt(stdX/list.size());
		stdY = Math.sqrt(stdY/list.size());
		stdZ = Math.sqrt(stdZ/list.size());

		assertEquals(center.x, meanX, 0.025);
		assertEquals(center.y, meanY, 0.025);
		assertEquals(center.z, meanZ, 0.025);

		assertEquals(0.5, stdX, 0.02);
		assertEquals(0.5, stdY, 0.02);
		assertEquals(0.5, stdZ, 0.02);
	}

	@Test
	void random_min_max() {
		double lower = -2;
		double upper = 1.5;
		List<Point4D_F64> list = UtilPoint4D_F64.random(lower,upper,1000,rand);

		assertEquals(1000,list.size());

		double m = Double.MAX_VALUE;
		Point4D_F64 foundLower = new Point4D_F64(m,m,m,m);
		Point4D_F64 foundUpper = new Point4D_F64(-m,-m,-m,-m);

		for (int i = 0; i < list.size(); i++) {
			Point4D_F64 p = list.get(i);
			for (int j = 0; j < 4; j++) {
				foundLower.setIdx(j,Math.min(foundLower.getIdx(j),p.getIdx(j)));
				foundUpper.setIdx(j,Math.max(foundUpper.getIdx(j),p.getIdx(j)));
			}
		}

		for (int i = 0; i < 4; i++) {
			assertTrue(foundLower.getIdx(i)<lower+0.05);
			assertTrue(foundUpper.getIdx(i)>upper-0.05);
		}
	}

	@Test
	void h_to_e() {
		Point4D_F64 a = new Point4D_F64(1,2,3,0.5);
		Point3D_F64 b = new Point3D_F64();
		UtilPoint4D_F64.h_to_e(a,b);

		assertEquals(2, b.x, UtilEjml.TEST_F64);
		assertEquals(4, b.y, UtilEjml.TEST_F64);
		assertEquals(6, b.z, UtilEjml.TEST_F64);
	}

	@Test
	void h_to_e_out() {
		Point4D_F64 a = new Point4D_F64(1,2,3,0.5);
		Point3D_F64 b = UtilPoint4D_F64.h_to_e(a);

		assertEquals(2, b.x, UtilEjml.TEST_F64);
		assertEquals(4, b.y, UtilEjml.TEST_F64);
		assertEquals(6, b.z, UtilEjml.TEST_F64);
	}
}