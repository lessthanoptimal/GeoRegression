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
import georegression.struct.curve.ConicGeneral_F64;
import georegression.struct.curve.ParabolaGeneral_F64;
import georegression.struct.curve.ParabolaParametric_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import org.ejml.UtilEjml;
import org.ejml.data.DMatrixRMaj;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtilCurves_F64 extends GeoRegressionJUnit {
	@Test void matrix_to_conic() {
		ConicGeneral_F64 conic = new ConicGeneral_F64(1.5,0.1,0.9,3,2,-217.5);
		DMatrixRMaj C = UtilCurves_F64.convert(conic,(DMatrixRMaj)null);
		ConicGeneral_F64 found = UtilCurves_F64.convert(C,(ConicGeneral_F64)null);

		assertEquals(conic.a,found.a, UtilEjml.TEST_F64);
		assertEquals(conic.b,found.b, UtilEjml.TEST_F64);
		assertEquals(conic.c,found.c, UtilEjml.TEST_F64);
		assertEquals(conic.d,found.d, UtilEjml.TEST_F64);
		assertEquals(conic.e,found.e, UtilEjml.TEST_F64);
		assertEquals(conic.f,found.f, UtilEjml.TEST_F64);
	}

	@Test void conic_to_matrix() {
		// hand constructed conic with a known point on the conic
		ConicGeneral_F64 conic = new ConicGeneral_F64(1.5,0.1,0.9,3,2,-217.5);

		DMatrixRMaj C = UtilCurves_F64.convert(conic,(DMatrixRMaj)null);

		// since the point is on the conic it should equal zero
		Point3D_F64 P = new Point3D_F64(10,5,1);
		assertEquals(0,GeometryMath_F64.innerProd(P,C,P) , UtilEjml.TEST_F64);
	}

	@Test void fmatrix_to_conic() {
		ConicGeneral_F64 conic = new ConicGeneral_F64(1.5,0.1,0.9,3,2,-217.5);
		DMatrixRMaj C = UtilCurves_F64.convert(conic,(DMatrixRMaj)null);
		ConicGeneral_F64 found = UtilCurves_F64.convert(C,(ConicGeneral_F64)null);

		assertEquals(conic.a,found.a, UtilEjml.TEST_F64);
		assertEquals(conic.b,found.b, UtilEjml.TEST_F64);
		assertEquals(conic.c,found.c, UtilEjml.TEST_F64);
		assertEquals(conic.d,found.d, UtilEjml.TEST_F64);
		assertEquals(conic.e,found.e, UtilEjml.TEST_F64);
		assertEquals(conic.f,found.f, UtilEjml.TEST_F64);
	}

	@Test void conic_tof_matrix() {
		// hand constructed conic with a known point on the conic
		ConicGeneral_F64 conic = new ConicGeneral_F64(1.5,0.1,0.9,3,2,-217.5);

		DMatrixRMaj C = UtilCurves_F64.convert(conic,(DMatrixRMaj)null);

		// since the point is on the conic it should equal zero
		Point3D_F64 P = new Point3D_F64(10,5,1);
		assertEquals(0,GeometryMath_F64.innerProd(P,C,P) , UtilEjml.TEST_F64);
	}

	/**
	 * The conic is a parabola This is an easy case
	 */
	@Test void conic_to_parabola_4AC_is_B() {
		double A = 1.5,C=0.9;
		double B = Math.sqrt(4*A*C);
		ConicGeneral_F64 conic = new ConicGeneral_F64(1.5,B,0.9,3,2,-328.6895003862225);
		// sanity check
		assertEquals(0,conic.evaluate(10,5), UtilEjml.TEST_F64);

		ParabolaGeneral_F64 parabola = new ParabolaGeneral_F64();
		UtilCurves_F64.convert(conic,parabola);
		assertEquals(0,conic.evaluate(10,5), UtilEjml.TEST_F64);
		// make sure it's not all zeros
		assertTrue(conic.f != 0 );

		// go the reverse direction now
		ConicGeneral_F64 reversed = new ConicGeneral_F64();
		UtilCurves_F64.convert(parabola,reversed);

		assertEquals(conic.a,reversed.a, UtilEjml.TEST_F64);
		assertEquals(conic.b,reversed.b, UtilEjml.TEST_F64);
		assertEquals(conic.c,reversed.c, UtilEjml.TEST_F64);
		assertEquals(conic.d,reversed.d, UtilEjml.TEST_F64);
		assertEquals(conic.e,reversed.e, UtilEjml.TEST_F64);
		assertEquals(conic.f,reversed.f, UtilEjml.TEST_F64);
	}

	/**
	 * B is not zero in this case. When converted it will be forced ot be zero
	 */
	@Test void conic_to_parabola_4AC_Not_B() {
		ConicGeneral_F64 conic = new ConicGeneral_F64(1.5,2,0.9,3,2,-212.5);
		ParabolaGeneral_F64 parabola = new ParabolaGeneral_F64();
		UtilCurves_F64.convert(conic,parabola);

		assertEquals(Math.sqrt(conic.a),parabola.a, UtilEjml.TEST_F64);
		assertEquals(Math.sqrt(conic.c),parabola.c, UtilEjml.TEST_F64);
		assertEquals(conic.d,parabola.d, UtilEjml.TEST_F64);
		assertEquals(conic.e,parabola.e, UtilEjml.TEST_F64);
		assertEquals(conic.f,parabola.f, UtilEjml.TEST_F64);
	}

	@Test void parabola_generic_to_parametric() {
		ParabolaGeneral_F64 general = new ParabolaGeneral_F64(1.1,0.5,0.4,-0.1,2.1);
		ParabolaParametric_F64 parametric = new ParabolaParametric_F64();
		ParabolaGeneral_F64 found = new ParabolaGeneral_F64();

		UtilCurves_F64.convert(general,parametric);

		// See if the parametric equation generates points that lie on the general equation
		Point2D_F64 p = new Point2D_F64();
		for (int i = 0; i < 20; i++) {
			double t = i/10.0;
			parametric.evaluate(t,p);
			assertEquals(0,general.evaluate(p.x,p.y), UtilEjml.TEST_F64);
		}

		// see if the reverse works
		UtilCurves_F64.convert(parametric,found);

		// only unique up to a scale factor
		assertTrue(general.isEquivalent(found, GrlConstants.TEST_F64));
	}


}