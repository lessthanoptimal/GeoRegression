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

package georegression.struct.homography;


import org.ejml.data.DMatrixRMaj;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilHomography_F64 {

	@Test
	void convert_matrix_F64() {
		DMatrixRMaj a = new DMatrixRMaj(3,3,true,1,2,3,4,5,6,7,8,9);

		Homography2D_F64 h = UtilHomography_F64.convert(a,null);

		assertEquals(a.get(0, 0), h.a11);
		assertEquals(a.get(0, 1), h.a12);
		assertEquals(a.get(0, 2), h.a13);
		assertEquals(a.get(1, 0), h.a21);
		assertEquals(a.get(1, 1), h.a22);
		assertEquals(a.get(1, 2), h.a23);
		assertEquals(a.get(2, 0), h.a31);
		assertEquals(a.get(2, 1), h.a32);
		assertEquals(a.get(2, 2), h.a33);
	}

	@Test
	void convert_F64_matrix() {
		Homography2D_F64 h = new Homography2D_F64(1,2,3,4,5,6,7,8,9);

		DMatrixRMaj a = UtilHomography_F64.convert(h,null);

		assertEquals(a.get(0, 0), h.a11);
		assertEquals(a.get(0, 1), h.a12);
		assertEquals(a.get(0, 2), h.a13);
		assertEquals(a.get(1, 0), h.a21);
		assertEquals(a.get(1, 1), h.a22);
		assertEquals(a.get(1, 2), h.a23);
		assertEquals(a.get(2, 0), h.a31);
		assertEquals(a.get(2, 1), h.a32);
		assertEquals(a.get(2, 2), h.a33);
	}

}
