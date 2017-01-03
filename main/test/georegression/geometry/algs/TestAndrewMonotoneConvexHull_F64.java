/*
 * Copyright (C) 2011-2017, Peter Abeles. All Rights Reserved.
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

package georegression.geometry.algs;

import georegression.geometry.UtilPolygons2D_F64;
import georegression.metric.Intersection2D_F64;
import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestAndrewMonotoneConvexHull_F64 {

	Random rand = new Random(2355);

	@Test
	public void triangle() {
		// all points are in hull
		Point2D_F64 []input = new Point2D_F64[3];
		input[0] = new Point2D_F64(2,3);
		input[1] = new Point2D_F64(7,8);
		input[2] = new Point2D_F64(8,3);

		Polygon2D_F64 output = new Polygon2D_F64();

		AndrewMonotoneConvexHull_F64 alg = new AndrewMonotoneConvexHull_F64();

		alg.process(input,input.length,output);

		containsOnceEach(input,output);

		// add a point inside
		Point2D_F64 []input2 = new Point2D_F64[4];
		System.arraycopy(input,0,input2,0,3);
		input2[3] = new Point2D_F64(4,4);

		alg.process(input2,input.length,output);
		containsOnceEach(input,output);
	}

	@Test
	public void rectangle() {
		// all points are in hull
		Point2D_F64 []input = new Point2D_F64[4];
		input[0] = new Point2D_F64(2,3);
		input[1] = new Point2D_F64(2,8);
		input[2] = new Point2D_F64(7,8);
		input[3] = new Point2D_F64(7,3);

		Polygon2D_F64 output = new Polygon2D_F64();

		AndrewMonotoneConvexHull_F64 alg = new AndrewMonotoneConvexHull_F64();

		alg.process(input,input.length,output);

		containsOnceEach(input,output);

		// add a point inside
		Point2D_F64 []input2 = new Point2D_F64[5];
		System.arraycopy(input,0,input2,0,4);
		input2[4] = new Point2D_F64(4,4);

		alg.process(input2,input.length,output);
		containsOnceEach(input,output);
	}

	@Test
	public void pentagon() {
		// all points are in hull
		Point2D_F64 []input = new Point2D_F64[4];
		input[0] = new Point2D_F64(2,3);
		input[1] = new Point2D_F64(2,8);
		input[2] = new Point2D_F64(7,8);
		input[3] = new Point2D_F64(7,3);
		input[3] = new Point2D_F64(4,10);

		Polygon2D_F64 output = new Polygon2D_F64();

		AndrewMonotoneConvexHull_F64 alg = new AndrewMonotoneConvexHull_F64();

		alg.process(input,input.length,output);

		containsOnceEach(input,output);

		// add a point inside
		Point2D_F64 []input2 = new Point2D_F64[6];
		System.arraycopy(input,0,input2,0,input.length);
		input2[5] = new Point2D_F64(4,4);

		alg.process(input2,input.length,output);
		containsOnceEach(input,output);
	}

	@Test
	public void randomPoints() {

		AndrewMonotoneConvexHull_F64 alg = new AndrewMonotoneConvexHull_F64();
		Polygon2D_F64 output = new Polygon2D_F64();

		for (int numPoints = 10; numPoints < 20; numPoints++) {

			Point2D_F64 data[] = new Point2D_F64[numPoints];
			for (int i = 0; i < numPoints; i++) {
				double x = rand.nextGaussian()*5;
				double y = rand.nextGaussian()*5;

				data[i] = new Point2D_F64(x,y);
			}

			alg.process(data,numPoints,output);

			// check some of the properties of the convex hull
			assertTrue(output.size() <= numPoints);
			assertTrue(output.isCCW());
			assertTrue(output.isConvex());

			for (int i = 0; i < numPoints; i++) {
				Intersection2D_F64.containConvex(output,data[i]);
			}
		}
	}

	@Test
	public void grid() {

		AndrewMonotoneConvexHull_F64 alg = new AndrewMonotoneConvexHull_F64();
		Polygon2D_F64 output = new Polygon2D_F64();

		int numRows = 5;
		int numCols = 6;

		double w = 1.2;

		Point2D_F64 points[] = new Point2D_F64[numRows*numCols];

		for (int row = 0; row < numRows; row++) {
			double y = row*w - 2.1;
			for (int col = 0; col < numCols; col++) {
				double x = col*w - 2.6;

				points[ row*numCols + col ] = new Point2D_F64(x,y);
			}
		}
		alg.process(points,points.length,output);

		// check some of the properties of the convex hull
		assertTrue(output.size() <= points.length);
		assertTrue(output.isCCW());
		assertTrue(output.isConvex());

		for (int i = 0; i < points.length; i++) {
			Intersection2D_F64.containConvex(output,points[i]);
		}
	}

	private void containsOnceEach( Point2D_F64[] expected , Polygon2D_F64 output ) {

		assertTrue(UtilPolygons2D_F64.isConvex(output));

		int count[] = new int[expected.length];

		assertEquals(expected.length,output.size());

		for (int i = 0; i < expected.length; i++) {
			for (int j = 0; j < output.size(); j++) {
				if( expected[i].distance2(output.get(j)) <= GrlConstants.TEST_F64) {
					count[i]++;
				}
			}
		}

		for (int i = 0; i < count.length; i++) {
			assertEquals(1, count[i]);
		}
	}
}
