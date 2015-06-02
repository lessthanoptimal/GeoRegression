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

package georegression.fitting.line;

import georegression.metric.UtilAngle;
import georegression.misc.GrlConstants;
import georegression.struct.line.LinePolar2D_F32;
import georegression.struct.point.Point2D_F32;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestFitLine_F32 {
	@Test
	public void polar() {

		float r = 1.5f;
		float theta = 0.75f;

		List<Point2D_F32> pts = new ArrayList<Point2D_F32>();

		for( int i = 0; i < 20; i++ ) {
			Point2D_F32 p = new Point2D_F32();
			p.x = i;
			p.y = (float)( (r-p.x*Math.cos(theta))/Math.sin(theta));

			pts.add(p);
		}

		LinePolar2D_F32 found = FitLine_F32.polar(pts,null);

		assertEquals(r,found.distance, GrlConstants.FLOAT_TEST_TOL);
		assertTrue(UtilAngle.dist(r, found.distance) <= GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void polar_weighted() {

		float weights[] = new float[30];
		float r = 1.5f;
		float theta = 0.75f;

		List<Point2D_F32> pts = new ArrayList<Point2D_F32>();

		for( int i = 0; i < 20; i++ ) {
			Point2D_F32 p = new Point2D_F32();
			p.x = i;
			p.y = (float)( (r-p.x*Math.cos(theta))/Math.sin(theta));

			weights[i] = 0.5f;

			pts.add(p);
		}

		// add in data which should be ignored
		for (int i = 20; i < weights.length; i++) {
			weights[i] = 1000;
		}

		LinePolar2D_F32 found = FitLine_F32.polar(pts,weights,null);

		assertEquals(r,found.distance, GrlConstants.FLOAT_TEST_TOL);
		assertTrue(UtilAngle.dist(r, found.distance) <= GrlConstants.FLOAT_TEST_TOL);
	}

	/**
	 * Test case where sum of weight is zero
	 */
	@Test
	public void polar_weighted_zero() {
		float weights[] = new float[20];

		List<Point2D_F32> pts = new ArrayList<Point2D_F32>();
		for( int i = 0; i < 20; i++ ) {
			pts.add( new Point2D_F32());
		}

		LinePolar2D_F32 found = FitLine_F32.polar(pts,weights,null);
		assertTrue( null == found);
	}
}
