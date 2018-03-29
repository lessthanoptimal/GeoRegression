/*
 * Copyright (C) 2011-2018, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.curves;

import georegression.misc.GrlConstants;
import georegression.struct.curve.QuadraticPolynomial2D_F64;
import georegression.struct.point.Point2D_F64;
import org.ejml.UtilEjml;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author Peter Abeles
 */
public class TestFitCurve_F64 {
	@Test
	public void quadratic_polynomial_p3() {
		List<Point2D_F64> points = new ArrayList<>();

		points.add( new Point2D_F64(1,1));
		points.add( new Point2D_F64(4,2));
		points.add( new Point2D_F64(-1,8));

		QuadraticPolynomial2D_F64 found = FitCurve_F64.fit(points,null);

		// it should fit all the points perfectly
		for( Point2D_F64 p : points ) {
			assertEquals(0,found.evaluate(p.x,p.y),4*GrlConstants.TEST_F64);
		}

		// Now see if it can a straight line
		points.get(1).set(3,3);
		points.get(2).set(4,4);

		found = FitCurve_F64.fit(points,null);

		assertFalse(UtilEjml.isUncountable(found.a0));
		assertFalse(UtilEjml.isUncountable(found.a1));
		assertFalse(UtilEjml.isUncountable(found.a2));

		// it's a line so a2 needs to be zero
		assertEquals(0,found.a2,GrlConstants.TEST_F64);
		// it should fit all the points perfectly
		for( Point2D_F64 p : points ) {
			assertEquals(0,found.evaluate(p.x,p.y),4*GrlConstants.TEST_F64);
		}

		// try a vertical line
		// It can't handle this case! not a bug
//		points.get(1).set(1,3);
//		points.get(2).set(1,4);
//		found = FitCurve_F64.fit(points,null);
//
//		assertFalse(UtilEjml.isUncountable(found.a0));
//		assertFalse(UtilEjml.isUncountable(found.a1));
//		assertFalse(UtilEjml.isUncountable(found.a2));
	}


}