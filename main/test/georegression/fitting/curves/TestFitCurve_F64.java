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

package georegression.fitting.curves;

import georegression.misc.GrlConstants;
import georegression.struct.curve.PolynomialCubic1D_F64;
import georegression.struct.curve.PolynomialQuadratic1D_F64;
import georegression.struct.curve.PolynomialQuadratic2D_F64;
import org.ejml.UtilEjml;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Abeles
 */
public class TestFitCurve_F64 {
	@Test
	void fitMM_quadratic1D() {

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
	void fitQRP_quadratic1D() {

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
	void fit_float_cubic1D() {
		// NOTE: Tolerances are crude because F32 case is unstable, but the math is correct. Works OK in F64

		PolynomialCubic1D_F64 expected = new PolynomialCubic1D_F64(0.1,-0.15,0.02,-0.05);

		double[] data = new double[8];

		for (int i = 0; i < 4; i++) {
			double x = i*1.1 + 0.5;
			double y = expected.evaluate(x);
			data[i*2  ] = x;
			data[i*2+1] = y;
		}

		PolynomialCubic1D_F64 found = new PolynomialCubic1D_F64();
		FitCurve_F64.fitMM(data,0,data.length,found,null);

		// it should fit all the points perfectly
		for (int i = 0; i < 4; i++) {
			double x = data[i*2  ];
			double y = data[i*2+1];
			assertEquals(y,found.evaluate(x),GrlConstants.TEST_SQ_F64);
		}

		// Now see if it can a straight line
		for (int i = 0; i < 4; i++) {
			data[i*2  ] = 2*i+1;
			data[i*2+1] = 2*i+1;
		}

		FitCurve_F64.fitMM(data,0,data.length,found,null);

		assertFalse(UtilEjml.isUncountable(found.a));
		assertFalse(UtilEjml.isUncountable(found.b));
		assertFalse(UtilEjml.isUncountable(found.c));
		assertFalse(UtilEjml.isUncountable(found.d));

		// it's a line so a2 needs to be zero
		assertEquals(0,found.c,GrlConstants.TEST_SQ_F64);
		assertEquals(0,found.d,GrlConstants.TEST_SQ_F64);

		// it should fit all the points perfectly
		for (int i = 0; i < 4; i++) {
			assertEquals(data[i*2+1],found.evaluate(data[i*2]),0.1 );
		}
	}

	@Test
	void fit_float_quadratic2D() {
		int N = 6;
		double[] data = new double[N*3];

		set3(data,0,1,1,0);
		set3(data,1,4,2,2);
		set3(data,2,-1,8,0.5);
		set3(data,3,-3,5,1.5);
		set3(data,4,2.1,6,2);
		set3(data,5,2.5,4,3);

		PolynomialQuadratic2D_F64 found = new PolynomialQuadratic2D_F64();
		assertTrue(FitCurve_F64.fit(data,0,data.length,found));

		// it should fit all the points perfectly
		for (int i = 0; i < N; i++) {
			assertEquals(data[i*3+2],found.evaluate(data[i*3],data[i*3+1]),4*GrlConstants.TEST_F64);
		}

		// Now see if it can a straight line
		for (int i = 0; i < N; i++) {
			data[i*3  ] = i+1;
			data[i*3+1] = i+1;
			data[i*3+2] = i+1;
		}

		assertTrue(FitCurve_F64.fit(data,0,data.length,found));

		assertFalse(UtilEjml.isUncountable(found.a));
		assertFalse(UtilEjml.isUncountable(found.b));
		assertFalse(UtilEjml.isUncountable(found.c));
		assertFalse(UtilEjml.isUncountable(found.d));
		assertFalse(UtilEjml.isUncountable(found.e));
		assertFalse(UtilEjml.isUncountable(found.f));

		// it's a line so these needs to be zero
		assertEquals(0,found.d,GrlConstants.TEST_F64);
		assertEquals(0,found.e,GrlConstants.TEST_F64);
		assertEquals(0,found.f,GrlConstants.TEST_F64);
		// it should fit all the points perfectly
		for (int i = 0; i < N; i++) {
			assertEquals(data[i*3+2],found.evaluate(data[i*3],data[i*3+1]),4*GrlConstants.TEST_F64);
		}
	}

	private void set3( double[] data , int which , double x , double y , double z ) {
		data[which*3  ] = x;
		data[which*3+1] = y;
		data[which*3+2] = z;
	}
}