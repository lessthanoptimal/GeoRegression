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

package georegression.geometry;

import georegression.struct.point.Point2D_I32;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilPoint2D_I32 {

	Random rand = new Random(234);

	@Test
	public void distance_int() {
		int x0 = 5;
		int y0 = 6;
		int x1 = -2;
		int x2 = 4;

		double found = UtilPoint2D_I32.distance(x0,y0,x1,x2);
		assertEquals(7.2801, found, 1e-3);
	}

	@Test
	public void distance_pts() {
		double found = UtilPoint2D_I32.distance(new Point2D_I32(5,6),new Point2D_I32(-2,4));
		assertEquals(7.2801, found, 1e-3);
	}

	@Test
	public void distanceSq_int() {
		int x0 = 5;
		int y0 = 6;
		int x1 = -2;
		int x2 = 4;

		int found = UtilPoint2D_I32.distanceSq(x0,y0,x1,x2);
		assertEquals(53, found);
	}

	@Test
	public void distanceSq_pts() {
		int found = UtilPoint2D_I32.distanceSq(new Point2D_I32(5,6),new Point2D_I32(-2,4));
		assertEquals(53, found, 1e-3);
	}

	@Test
	public void mean() {
		List<Point2D_I32> list = new ArrayList<Point2D_I32>();

		int X=0,Y=0;
		for( int i = 0; i < 20; i++ ) {
			Point2D_I32 p = new Point2D_I32();
			X += p.x = rand.nextInt(100)-50;
			Y += p.y = rand.nextInt(100)-50;

			list.add(p);
		}

		Point2D_I32 found = UtilPoint2D_I32.mean(list, null);

		assertEquals(X/20,found.x);
		assertEquals(Y/20,found.y);
	}

}
