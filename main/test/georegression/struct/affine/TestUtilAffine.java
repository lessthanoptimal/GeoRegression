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

package georegression.struct.affine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilAffine {

	@Test
	void convert_F64_F32() {
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
