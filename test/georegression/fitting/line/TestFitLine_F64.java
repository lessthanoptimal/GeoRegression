/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.fitting.line;

import georegression.metric.UtilAngle;
import georegression.misc.GrlConstants;
import georegression.struct.line.LinePolar2D_F64;
import georegression.struct.point.Point2D_F64;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestFitLine_F64 {
	@Test
	public void polar() {

		double r = 1.5;
		double theta = 0.75;

		List<Point2D_F64> pts = new ArrayList<Point2D_F64>();

		for( int i = 0; i < 20; i++ ) {
			Point2D_F64 p = new Point2D_F64();
			p.x = i;
			p.y = (double)( (r-p.x*Math.cos(theta))/Math.sin(theta));

			pts.add(p);
		}

		LinePolar2D_F64 found = FitLine_F64.polar(pts,null);

		assertEquals(r,found.distance, GrlConstants.DOUBLE_TEST_TOL);
		assertTrue(UtilAngle.dist(r, found.distance) <= GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void polar_weighted() {

		double weights[] = new double[20];
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

		LinePolar2D_F64 found = FitLine_F64.polar(pts,weights,null);

		assertEquals(r,found.distance, GrlConstants.DOUBLE_TEST_TOL);
		assertTrue(UtilAngle.dist(r, found.distance) <= GrlConstants.DOUBLE_TEST_TOL);
	}
}
