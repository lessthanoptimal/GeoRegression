/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
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
}
