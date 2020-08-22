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

package georegression.fitting.line;

import georegression.metric.UtilAngle;
import georegression.misc.GrlConstants;
import georegression.struct.line.LinePolar2D_F64;
import georegression.struct.point.Point2D_F64;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Peter Abeles
 */
public class TestFitLine_F64 {
	@Test
	void polar() {

		double r = 1.5;
		double theta = 0.75;

		List<Point2D_F64> pts = new ArrayList<>();

		for( int i = 0; i < 20; i++ ) {
			Point2D_F64 p = new Point2D_F64();
			p.x = i;
			p.y = (double)( (r-p.x*Math.cos(theta))/Math.sin(theta));

			pts.add(p);
		}

		LinePolar2D_F64 found = FitLine_F64.polar(pts,null);

		assertEquals(r,found.distance, GrlConstants.TEST_F64);
		assertTrue(UtilAngle.dist(theta, found.angle) <= GrlConstants.TEST_F64);
	}

	@Test
	void polar_weighted() {

		double weights[] = new double[30];
		double r = 1.5;
		double theta = 0.75;

		List<Point2D_F64> pts = new ArrayList<Point2D_F64>();

		for( int i = 0; i < 20; i++ ) {
			Point2D_F64 p = new Point2D_F64();
			p.x = i;
			p.y = (double)( (r-p.x*Math.cos(theta))/Math.sin(theta));

			weights[i] = 0.5;

			pts.add(p);
		}

		// add in data which should be ignored
		for (int i = 20; i < weights.length; i++) {
			weights[i] = 1000;
		}

		LinePolar2D_F64 found = FitLine_F64.polar(pts,weights,null);

		assertNotNull(found);
		assertEquals(r,found.distance, GrlConstants.TEST_F64);
		assertTrue(UtilAngle.dist(theta, found.angle) <= GrlConstants.TEST_F64);
	}

	/**
	 * Test case where sum of weight is zero
	 */
	@Test
	void polar_weighted_zero() {
		double weights[] = new double[20];

		List<Point2D_F64> pts = new ArrayList<Point2D_F64>();
		for( int i = 0; i < 20; i++ ) {
			pts.add( new Point2D_F64());
		}

		LinePolar2D_F64 found = FitLine_F64.polar(pts,weights,null);
		assertTrue( null == found);
	}
}
