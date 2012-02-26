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

package georegression.geometry;

import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilTrig_F64 {

	@Test
	public void dot_2d() {
		Point2D_F64 a = new Point2D_F64(-1,2);
		Point2D_F64 b = new Point2D_F64(2,7);
		
		double found = UtilTrig_F64.dot(a,b);
		assertEquals(-2+14,found,1e-8);
	}

	@Test
	public void dot_3d() {
		Point3D_F64 a = new Point3D_F64(-1,2,3);
		Point3D_F64 b = new Point3D_F64(2,7,0.5);

		double found = UtilTrig_F64.dot(a,b);
		assertEquals(-2+14+1.5,found,1e-8);
	}
}
