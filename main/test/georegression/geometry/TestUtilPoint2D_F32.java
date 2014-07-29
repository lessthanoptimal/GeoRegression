/*
 * Copyright (C) 2011-2014, Peter Abeles. All Rights Reserved.
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

import georegression.metric.Intersection2D_F32;
import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F32;
import georegression.struct.shapes.Rectangle2D_F32;
import georegression.struct.shapes.RectangleLength2D_F32;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestUtilPoint2D_F32 {

	Random rand = new Random(234);

	@Test
	public void mean() {
		List<Point2D_F32> list = new ArrayList<Point2D_F32>();

		float X=0,Y=0;
		for( int i = 0; i < 20; i++ ) {
			Point2D_F32 p = new Point2D_F32();
			X += p.x = rand.nextFloat()*100-50;
			Y += p.y = rand.nextFloat()*100-50;

			list.add(p);
		}

		Point2D_F32 found = UtilPoint2D_F32.mean(list, null);

		assertEquals(X/20, found.x , GrlConstants.FLOAT_TEST_TOL);
		assertEquals(Y/20, found.y , GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void bounding_length() {
		List<Point2D_F32> list = new ArrayList<Point2D_F32>();

		for( int i = 0; i < 20; i++ ) {
			Point2D_F32 p = new Point2D_F32();
			p.x = rand.nextFloat()*100-50;
			p.y = rand.nextFloat()*100-50;

			list.add(p);
		}

		RectangleLength2D_F32 bounding = UtilPoint2D_F32.bounding(list,(RectangleLength2D_F32)null);

		for( int i = 0; i < list.size(); i++ ) {
			Point2D_F32 p = list.get(i);

			assertTrue(Intersection2D_F32.contains2(bounding, p.x, p.y));
		}
	}

	@Test
	public void bounding() {
		List<Point2D_F32> list = new ArrayList<Point2D_F32>();

		for( int i = 0; i < 20; i++ ) {
			Point2D_F32 p = new Point2D_F32();
			p.x = rand.nextFloat()*100-50;
			p.y = rand.nextFloat()*100-50;

			list.add(p);
		}

		Rectangle2D_F32 bounding = UtilPoint2D_F32.bounding(list,(Rectangle2D_F32)null);

		for( int i = 0; i < list.size(); i++ ) {
			Point2D_F32 p = list.get(i);

			assertTrue(Intersection2D_F32.contains2(bounding, p.x, p.y));
		}
	}

	@Test
	public void orderCCW() {
		List<Point2D_F32> input = new ArrayList<Point2D_F32>();
		input.add(new Point2D_F32(2,-3));
		input.add(new Point2D_F32(2,1));
		input.add(new Point2D_F32(-2,-3));
		input.add(new Point2D_F32(-2,1));

		List<Point2D_F32> found = UtilPoint2D_F32.orderCCW(input);

		assertTrue(found.get(0) == input.get(2));
		assertTrue(found.get(1) == input.get(0));
		assertTrue(found.get(2) == input.get(1));
		assertTrue(found.get(3) == input.get(3));
	}

}
