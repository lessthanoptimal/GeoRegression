/*
 * Copyright (C) 2011-2019, Peter Abeles. All Rights Reserved.
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
import georegression.struct.curve.PolynomialCubic1D_F64;
import georegression.struct.curve.PolynomialQuadratic1D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point2D_I32;
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
	public void fitMM_quadratic() {

		double[] data = new double[6];

		data[0] = 1;data[1] = 1;
		data[2] = 4;data[3] = 2;
		data[4] =-1;data[5] = 8;

		PolynomialQuadratic1D_F64 found = new PolynomialQuadratic1D_F64();
		FitCurve_F64.fitMM(data,0,data.length,found,null);

		// it should fit all the points perfectly
		for( int i = 0; i < data.length; i += 2 ) {
			double x = data[i];
			double y = data[i+1];
			assertEquals(y,found.evaluate(x),4*GrlConstants.TEST_F64);
		}

		// Now see if it can a straight line
		data[2] = 3;data[3] = 3;
		data[4] = 4;data[5] = 4;

		FitCurve_F64.fitMM(data,0,data.length,found,null);

		assertFalse(UtilEjml.isUncountable(found.a));
		assertFalse(UtilEjml.isUncountable(found.b));
		assertFalse(UtilEjml.isUncountable(found.c));

		// it's a line so a2 needs to be zero
		assertEquals(0,found.c,GrlConstants.TEST_F64);
		// it should fit all the points perfectly
		for( int i = 0; i < data.length; i += 2 ) {
			double x = data[i];
			double y = data[i+1];
			assertEquals(y,found.evaluate(x),4*GrlConstants.TEST_F64);
		}
	}

	@Test
	public void fitQRP_quadratic() {

		double[] data = new double[6];

		data[0] = 1;data[1] = 1;
		data[2] = 4;data[3] = 2;
		data[4] =-1;data[5] = 8;

		PolynomialQuadratic1D_F64 found = new PolynomialQuadratic1D_F64();
		FitCurve_F64.fitQRP(data,0,data.length,found);

		// it should fit all the points perfectly
		for( int i = 0; i < data.length; i += 2 ) {
			double x = data[i];
			double y = data[i+1];
			assertEquals(y,found.evaluate(x),4*GrlConstants.TEST_F64);
		}

		// Now see if it can a straight line
		data[2] = 3;data[3] = 3;
		data[4] = 4;data[5] = 4;

		FitCurve_F64.fitQRP(data,0,data.length,found);

		assertFalse(UtilEjml.isUncountable(found.a));
		assertFalse(UtilEjml.isUncountable(found.b));
		assertFalse(UtilEjml.isUncountable(found.c));

		// it's a line so a2 needs to be zero
		assertEquals(0,found.c,GrlConstants.TEST_F64);
		// it should fit all the points perfectly
		for( int i = 0; i < data.length; i += 2 ) {
			double x = data[i];
			double y = data[i+1];
			assertEquals(y,found.evaluate(x),4*GrlConstants.TEST_F64);
		}
	}


	@Test
	public void fit_float_cubic() {
		List<Point2D_F64> points = new ArrayList<>();

		PolynomialCubic1D_F64 expected = new PolynomialCubic1D_F64(0.1,-0.5,2,0.1);

		for (int i = 0; i < 4; i++) {
			double x = i+0.5;
			double y = expected.evaluate(x);
			points.add( new Point2D_F64(x,y) );
		}

		PolynomialCubic1D_F64 found = FitCurve_F64.fit(points,(PolynomialCubic1D_F64)null);

		// it should fit all the points perfectly
		for( Point2D_F64 p : points ) {
			assertEquals(p.y,found.evaluate(p.x),4*GrlConstants.TEST_F64);
		}

		// Now see if it can a straight line
		for (int i = 0; i < 4; i++) {
			points.get(i).set(i+1,i+1);
		}

		FitCurve_F64.fit(points,found);

		assertFalse(UtilEjml.isUncountable(found.a));
		assertFalse(UtilEjml.isUncountable(found.b));
		assertFalse(UtilEjml.isUncountable(found.c));
		assertFalse(UtilEjml.isUncountable(found.d));

		// it's a line so a2 needs to be zero
		assertEquals(0,found.c,GrlConstants.TEST_F64);
		assertEquals(0,found.d,GrlConstants.TEST_F64);

		// it should fit all the points perfectly
		for( Point2D_F64 p : points ) {
			assertEquals(p.y,found.evaluate(p.x),4*GrlConstants.TEST_F64);
		}
	}

	@Test
	public void fit_int_cubic() {
		List<Point2D_I32> points = new ArrayList<>();

		points.add( new Point2D_I32(1,1));
		points.add( new Point2D_I32(4,2));
		points.add( new Point2D_I32(-1,8));
		points.add( new Point2D_I32(-2,4));

		PolynomialCubic1D_F64 found = FitCurve_F64.fit_S32(points,(PolynomialCubic1D_F64)null);

		// it should fit all the points perfectly
		for( Point2D_I32 p : points ) {
			assertEquals(p.y,found.evaluate(p.x),4*GrlConstants.TEST_F64);
		}

		// Now see if it can a straight line
		for (int i = 0; i < 4; i++) {
			points.get(i).set(i+1,i+1);
		}

		FitCurve_F64.fit_S32(points,found);

		assertFalse(UtilEjml.isUncountable(found.a));
		assertFalse(UtilEjml.isUncountable(found.b));
		assertFalse(UtilEjml.isUncountable(found.c));
		assertFalse(UtilEjml.isUncountable(found.d));

		// it's a line so a2 needs to be zero
		assertEquals(0,found.c,GrlConstants.TEST_F64);
		assertEquals(0,found.d,GrlConstants.TEST_F64);

		// it should fit all the points perfectly
		for( Point2D_I32 p : points ) {
			assertEquals(p.y,found.evaluate(p.x),4*GrlConstants.TEST_F64);
		}
	}
}