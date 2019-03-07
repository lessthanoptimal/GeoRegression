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
import georegression.struct.curve.PolynomialQuadratic2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point2D_I32;
import georegression.struct.point.Point3D_F64;
import org.ejml.UtilEjml;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Peter Abeles
 */
public class TestFitCurve_F64 {
	@Test
	public void fit_float_quadratic1D() {
		List<Point2D_F64> points = new ArrayList<>();

		points.add( new Point2D_F64(1,1));
		points.add( new Point2D_F64(4,2));
		points.add( new Point2D_F64(-1,8));

		PolynomialQuadratic1D_F64 found = FitCurve_F64.fit(points,(PolynomialQuadratic1D_F64)null);

		// it should fit all the points perfectly
		for( Point2D_F64 p : points ) {
			assertEquals(p.y,found.evaluate(p.x),4*GrlConstants.TEST_F64);
		}

		// Now see if it can a straight line
		points.get(1).set(3,3);
		points.get(2).set(4,4);

		FitCurve_F64.fit(points,found);

		assertFalse(UtilEjml.isUncountable(found.a));
		assertFalse(UtilEjml.isUncountable(found.b));
		assertFalse(UtilEjml.isUncountable(found.c));

		// it's a line so a2 needs to be zero
		assertEquals(0,found.c,GrlConstants.TEST_F64);
		// it should fit all the points perfectly
		for( Point2D_F64 p : points ) {
			assertEquals(p.y,found.evaluate(p.x),4*GrlConstants.TEST_F64);
		}
	}

	@Test
	public void fit_int_quadratic1D() {
		List<Point2D_I32> points = new ArrayList<>();

		points.add( new Point2D_I32(1,1));
		points.add( new Point2D_I32(4,2));
		points.add( new Point2D_I32(-1,8));

		PolynomialQuadratic1D_F64 found = FitCurve_F64.fit_S32(points,(PolynomialQuadratic1D_F64)null);

		// it should fit all the points perfectly
		for( Point2D_I32 p : points ) {
			assertEquals(p.y,found.evaluate(p.x),4*GrlConstants.TEST_F64);
		}

		// Now see if it can a straight line
		points.get(1).set(3,3);
		points.get(2).set(4,4);

		FitCurve_F64.fit_S32(points,found);

		assertFalse(UtilEjml.isUncountable(found.a));
		assertFalse(UtilEjml.isUncountable(found.b));
		assertFalse(UtilEjml.isUncountable(found.c));

		// it's a line so a2 needs to be zero
		assertEquals(0,found.c,GrlConstants.TEST_F64);
		// it should fit all the points perfectly
		for( Point2D_I32 p : points ) {
			assertEquals(p.y,found.evaluate(p.x),4*GrlConstants.TEST_F64);
		}
	}

	@Test
	public void fit_float_quadratic2D() {
		List<Point3D_F64> points = new ArrayList<>();

		points.add( new Point3D_F64(1,1,0));
		points.add( new Point3D_F64(4,2,2));
		points.add( new Point3D_F64(-1,8,0.5));
		points.add( new Point3D_F64(-3,5,1.5));
		points.add( new Point3D_F64(2.1,6,2));
		points.add( new Point3D_F64(2.5,4,3));

		PolynomialQuadratic2D_F64 found = new PolynomialQuadratic2D_F64();
		assertTrue(FitCurve_F64.fit(points,found));

		// it should fit all the points perfectly
		for( Point3D_F64 p : points ) {
			assertEquals(p.z,found.evaluate(p.x,p.y),4*GrlConstants.TEST_F64);
		}

		// Now see if it can a straight line
		for (int i = 0; i < 6; i++) {
			points.get(i).set(i+1,i+1,i-1);
		}

		assertTrue(FitCurve_F64.fit(points,found));

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
		for( Point3D_F64 p : points ) {
			assertEquals(p.z,found.evaluate(p.x,p.y),4*GrlConstants.TEST_F64);
		}
	}

	@Test
	public void fit_float_cubic1D() {
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
	public void fit_int_cubic1D() {
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