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

package georegression.struct.affine;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilAffine {

	@Test
	public void convert_F64_F32() {
		Affine2D_F64 a = new Affine2D_F64(1,2,3,4,5,6);
		Affine2D_F32 b = UtilAffine.convert(a,null);

		assertEquals(a.a11,b.a11,1e-4);
		assertEquals(a.a12,b.a12,1e-4);
		assertEquals(a.a21,b.a21,1e-4);
		assertEquals(a.a22,b.a22,1e-4);
		assertEquals(a.tx,b.tx,1e-4);
		assertEquals(a.ty,b.ty,1e-4);
	}
}
