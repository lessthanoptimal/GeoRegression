/*
 * Copyright (C) 2026, Peter Abeles. All Rights Reserved.
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

import georegression.GeoRegressionJUnit;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineParametric3D_F64;
import georegression.struct.line.LineSegment3D_F64;
import georegression.struct.point.Point3D_F64;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtilLine3D_F64 extends GeoRegressionJUnit {
	@Test void convert_ls_lp() {
		var ls = new LineSegment3D_F64(1,2,3,6,8,10);

		LineParametric3D_F64 lp = UtilLine3D_F64.convert(ls,null);

		assertEquals(1, lp.p.x, GrlConstants.TEST_F64);
		assertEquals(2, lp.p.y, GrlConstants.TEST_F64);
		assertEquals(3, lp.p.z, GrlConstants.TEST_F64);

		assertEquals(5, lp.slope.x, GrlConstants.TEST_F64);
		assertEquals(6, lp.slope.y, GrlConstants.TEST_F64);
		assertEquals(7, lp.slope.z, GrlConstants.TEST_F64);
	}

	@Test void computeT() {
		var line = new LineParametric3D_F64(1,2,3,-4,1.5,0.23);

		double t0 = -3.4;
		double t1 = 1.2;

		Point3D_F64 p0 = line.getPointOnLine(t0);
		Point3D_F64 p1 = line.getPointOnLine(t1);

		assertEquals(t0,UtilLine3D_F64.computeT(line,p0),GrlConstants.TEST_F64);
		assertEquals(t1,UtilLine3D_F64.computeT(line,p1),GrlConstants.TEST_F64);
	}
}
