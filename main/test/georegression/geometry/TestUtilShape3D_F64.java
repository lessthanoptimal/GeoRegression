/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

package georegression.geometry;

import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se3_F64;
import georegression.struct.shapes.Polygon2D_F64;
import org.ddogleg.struct.DogArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestUtilShape3D_F64 {
	@Test
	void polygon2Dto3D() {

		Polygon2D_F64 p = new Polygon2D_F64(3);
		p.set(0,1,2);
		p.set(1,5,5);
		p.set(2,-2,-8);

		Se3_F64 se = new Se3_F64();
		se.T.setTo(1,2,3);

		DogArray<Point3D_F64> output = new DogArray<>(Point3D_F64::new);
		UtilShape3D_F64.polygon2Dto3D(p,se,output);

		assertEquals(3,output.size);
		assertTrue(output.get(0).distance(2,4,3) <= GrlConstants.TEST_F64);
		assertTrue(output.get(1).distance(6,7,3) <= GrlConstants.TEST_F64);
		assertTrue(output.get(2).distance(-1,-6,3) <= GrlConstants.TEST_F64);
	}
}