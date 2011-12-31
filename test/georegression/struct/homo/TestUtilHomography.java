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

package georegression.struct.homo;


import org.ejml.data.DenseMatrix64F;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestUtilHomography {

	@Test
	public void convert_matrix() {
		DenseMatrix64F a = new DenseMatrix64F(3,3,true,1,2,3,4,5,6,7,8,9);

		Homography2D_F64 h = UtilHomography.convert(a,null);

		assertTrue(h.a11 == a.get(0,0));
		assertTrue(h.a12 == a.get(0,1));
		assertTrue(h.a13 == a.get(0,2));
		assertTrue(h.a21 == a.get(1,0));
		assertTrue(h.a22 == a.get(1,1));
		assertTrue(h.a23 == a.get(1,2));
		assertTrue(h.a31 == a.get(2,0));
		assertTrue(h.a32 == a.get(2,1));
		assertTrue(h.a33 == a.get(2,2));
	}

	@Test
	public void convert_F64_F32() {
		Homography2D_F64 a = new Homography2D_F64(1,2,3,4,5,6,7,8,9);

		Homography2D_F32 h = UtilHomography.convert(a,null);

		assertTrue(h.a11 == (float)a.a11);
		assertTrue(h.a12 == (float)a.a12);
		assertTrue(h.a13 == (float)a.a13);
		assertTrue(h.a21 == (float)a.a21);
		assertTrue(h.a22 == (float)a.a22);
		assertTrue(h.a23 == (float)a.a23);
		assertTrue(h.a31 == (float)a.a31);
		assertTrue(h.a32 == (float)a.a32);
		assertTrue(h.a33 == (float)a.a33);
	}
}
