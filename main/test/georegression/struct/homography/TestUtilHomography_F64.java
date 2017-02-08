/*
 * Copyright (C) 2011-2017, Peter Abeles. All Rights Reserved.
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

package georegression.struct.homography;


import org.ejml.data.DMatrixRMaj;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestUtilHomography_F64 {

	@Test
	public void convert_matrix_F64() {
		DMatrixRMaj a = new DMatrixRMaj(3,3,true,1,2,3,4,5,6,7,8,9);

		Homography2D_F64 h = UtilHomography_F64.convert(a,null);

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
	public void convert_F64_matrix() {
		Homography2D_F64 h = new Homography2D_F64(1,2,3,4,5,6,7,8,9);

		DMatrixRMaj a = UtilHomography_F64.convert(h,null);

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

}
