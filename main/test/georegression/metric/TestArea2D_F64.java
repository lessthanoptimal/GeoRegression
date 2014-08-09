/*
 * Copyright (C) 2011-2014, Peter Abeles. All Rights Reserved.
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

package georegression.metric;

import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Quadrilateral_F64;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestArea2D_F64 {

	@Test
	public void triangle() {
		Point2D_F64 a = new Point2D_F64(0,0);
		Point2D_F64 b = new Point2D_F64(5,0);
		Point2D_F64 c = new Point2D_F64(0,3);

		double expected = 0.5*5*3;

		double found = Area2D_F64.triangle(a,b,c);

		assertEquals(expected,found,GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void quadrilateral() {
		Quadrilateral_F64 q = new Quadrilateral_F64(0,0,2,0,2,3,0,3);

		assertEquals(2*3,Area2D_F64.quadrilateral(q), GrlConstants.DOUBLE_TEST_TOL);
	}

}
