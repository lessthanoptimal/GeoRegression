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

package georegression.struct.point;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestPoint2D_I32 {

	@Test
	public void distance2() {
		Point2D_I32 a = new Point2D_I32(1,2);
		Point2D_I32 b = new Point2D_I32(3,5);

		assertEquals(4+9,a.distance2(b));
	}

	@Test
	public void copy() {
		Point2D_I32 a = new Point2D_I32(1,2);
		Point2D_I32 b = a.copy();

		assertEquals(a.x,b.x);
		assertEquals(a.y,b.y);
	}
}
