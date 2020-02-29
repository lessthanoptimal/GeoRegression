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

import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point2D_I32;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilPoint2D_I32 {

	Random rand = new Random(234);

	@Test
	void distance_int() {
		int x0 = 5;
		int y0 = 6;
		int x1 = -2;
		int x2 = 4;

		double found = UtilPoint2D_I32.distance(x0,y0,x1,x2);
		assertEquals(7.2801, found, 1e-3);
	}

	@Test
	void distance_pts() {
		double found = UtilPoint2D_I32.distance(new Point2D_I32(5,6),new Point2D_I32(-2,4));
		assertEquals(7.2801, found, 1e-3);
	}

	@Test
	void distanceSq_int() {
		int x0 = 5;
		int y0 = 6;
		int x1 = -2;
		int x2 = 4;

		int found = UtilPoint2D_I32.distanceSq(x0,y0,x1,x2);
		assertEquals(53, found);
	}

	@Test
	void distanceSq_pts() {
		int found = UtilPoint2D_I32.distanceSq(new Point2D_I32(5,6),new Point2D_I32(-2,4));
		assertEquals(53, found, 1e-3);
	}

	@Test
	void mean_I32() {
		List<Point2D_I32> list = new ArrayList<Point2D_I32>();

		int X=0,Y=0;
		for( int i = 0; i < 20; i++ ) {
			Point2D_I32 p = new Point2D_I32();
			X += p.x = rand.nextInt(100)-50;
			Y += p.y = rand.nextInt(100)-50;

			list.add(p);
		}

		Point2D_I32 found = UtilPoint2D_I32.mean(list, (Point2D_I32)null);

		assertEquals(X/20,found.x);
		assertEquals(Y/20,found.y);
	}

	@Test
	void mean_F32() {
		List<Point2D_I32> list = new ArrayList<Point2D_I32>();

		int X=0,Y=0;
		for( int i = 0; i < 20; i++ ) {
			Point2D_I32 p = new Point2D_I32();
			X += p.x = rand.nextInt(100)-50;
			Y += p.y = rand.nextInt(100)-50;

			list.add(p);
		}

		Point2D_F32 found = UtilPoint2D_I32.mean(list, (Point2D_F32)null);

		assertEquals(X/20.0,found.x, UtilEjml.TEST_F32);
		assertEquals(Y/20.0,found.y, UtilEjml.TEST_F32);
	}

	@Test
	void mean_F64() {
		List<Point2D_I32> list = new ArrayList<Point2D_I32>();

		int X=0,Y=0;
		for( int i = 0; i < 20; i++ ) {
			Point2D_I32 p = new Point2D_I32();
			X += p.x = rand.nextInt(100)-50;
			Y += p.y = rand.nextInt(100)-50;

			list.add(p);
		}

		Point2D_F64 found = UtilPoint2D_I32.mean(list, (Point2D_F64)null);

		assertEquals(X/20.0,found.x, UtilEjml.TEST_F64);
		assertEquals(Y/20.0,found.y, UtilEjml.TEST_F64);
	}

}
