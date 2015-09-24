/*
 * Copyright (C) 2011-2015, Peter Abeles. All Rights Reserved.
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

import georegression.geometry.UtilPolygons2D_F32;
import georegression.metric.Intersection2D_F32;
import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F32;
import georegression.struct.shapes.Polygon2D_F32;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestAndrewMonotoneConvexHull_F32 {

	Random rand = new Random(2355);

	@Test
	public void triangle() {
		// all points are in hull
		Point2D_F32 []input = new Point2D_F32[3];
		input[0] = new Point2D_F32(2,3);
		input[1] = new Point2D_F32(7,8);
		input[2] = new Point2D_F32(8,3);

		Polygon2D_F32 output = new Polygon2D_F32();

		AndrewMonotoneConvexHull_F32 alg = new AndrewMonotoneConvexHull_F32();

		alg.process(input,input.length,output);

		containsOnceEach(input,output);

		// add a point inside
		Point2D_F32 []input2 = new Point2D_F32[4];
		System.arraycopy(input,0,input2,0,3);
		input2[3] = new Point2D_F32(4,4);

		alg.process(input2,input.length,output);
		containsOnceEach(input,output);
	}

	@Test
	public void rectangle() {
		// all points are in hull
		Point2D_F32 []input = new Point2D_F32[4];
		input[0] = new Point2D_F32(2,3);
		input[1] = new Point2D_F32(2,8);
		input[2] = new Point2D_F32(7,8);
		input[3] = new Point2D_F32(7,3);

		Polygon2D_F32 output = new Polygon2D_F32();

		AndrewMonotoneConvexHull_F32 alg = new AndrewMonotoneConvexHull_F32();

		alg.process(input,input.length,output);

		containsOnceEach(input,output);

		// add a point inside
		Point2D_F32 []input2 = new Point2D_F32[5];
		System.arraycopy(input,0,input2,0,4);
		input2[4] = new Point2D_F32(4,4);

		alg.process(input2,input.length,output);
		containsOnceEach(input,output);
	}

	@Test
	public void pentagon() {
		// all points are in hull
		Point2D_F32 []input = new Point2D_F32[4];
		input[0] = new Point2D_F32(2,3);
		input[1] = new Point2D_F32(2,8);
		input[2] = new Point2D_F32(7,8);
		input[3] = new Point2D_F32(7,3);
		input[3] = new Point2D_F32(4,10);

		Polygon2D_F32 output = new Polygon2D_F32();

		AndrewMonotoneConvexHull_F32 alg = new AndrewMonotoneConvexHull_F32();

		alg.process(input,input.length,output);

		containsOnceEach(input,output);

		// add a point inside
		Point2D_F32 []input2 = new Point2D_F32[6];
		System.arraycopy(input,0,input2,0,input.length);
		input2[5] = new Point2D_F32(4,4);

		alg.process(input2,input.length,output);
		containsOnceEach(input,output);
	}

	@Test
	public void randomPoints() {

		AndrewMonotoneConvexHull_F32 alg = new AndrewMonotoneConvexHull_F32();
		Polygon2D_F32 output = new Polygon2D_F32();

		for (int numPoints = 3; numPoints < 20; numPoints++) {

			Point2D_F32 data[] = new Point2D_F32[numPoints];
			for (int i = 0; i < numPoints; i++) {
				float x = (float)rand.nextGaussian()*5;
				float y = (float)rand.nextGaussian()*5;

				data[i] = new Point2D_F32(x,y);
			}

			alg.process(data,numPoints,output);

			// check some of the properties of the convex hull
			assertTrue(output.size() <= numPoints);
			assertTrue(output.isCCW());
			assertTrue(output.isConvex());

			for (int i = 0; i < numPoints; i++) {
				Intersection2D_F32.containConvex(output,data[i]);
			}
		}
	}

	private void containsOnceEach( Point2D_F32[] expected , Polygon2D_F32 output ) {

		assertTrue(UtilPolygons2D_F32.isConvex(output));

		int count[] = new int[expected.length];

		assertEquals(expected.length,output.size());

		for (int i = 0; i < expected.length; i++) {
			for (int j = 0; j < output.size(); j++) {
				if( expected[i].distance2(output.get(j)) <= GrlConstants.FLOAT_TEST_TOL ) {
					count[i]++;
				}
			}
		}

		for (int i = 0; i < count.length; i++) {
			assertEquals(1, count[i]);
		}
	}
}
