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
import georegression.struct.point.*;
import georegression.struct.so.Quaternion_F64;
import georegression.struct.tuples.GeoTuple3D_F64;
import georegression.struct.tuples.GeoTuple4D_F64;
import org.ejml.UtilEjml;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.MatrixFeatures_DDRM;
import org.ejml.dense.row.RandomMatrices_DDRM;
import org.ejml.equation.Equation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGeometryMath_F64 extends GeoRegressionJUnit {
	/// Sees if crossMatrix produces a valid output
	@Test void crossMatrix_validOut() {
		double a = 1.1, b = -0.5, c = 2.2;

		var v = new Vector3D_F64(a, b, c);
		var x = new Vector3D_F64(7.6, 2.9, 0.5);

		var found0 = new Vector3D_F64();
		var found1 = new Vector3D_F64();

		GeometryMath_F64.cross(v, x, found0);
		DMatrixRMaj V = GeometryMath_F64.crossMatrix(a, b, c, null);

		GeometryMath_F64.mult(V, x, found1);

		assertEquals(found0.x, found1.x, GrlConstants.TEST_F64);
		assertEquals(found0.y, found1.y, GrlConstants.TEST_F64);
		assertEquals(found0.z, found1.z, GrlConstants.TEST_F64);
	}

	@Test void crossMatrix_scalars_output_reshaped() {
		var out = new DMatrixRMaj(1, 1);
		GeometryMath_F64.crossMatrix(1, 2, 3, out);
		assertEquals(3, out.numCols);
		assertEquals(3, out.numRows);
	}

	@Test void crossMatrix_vector_output_reshaped() {
		var out = new DMatrixRMaj(1, 1);
		GeometryMath_F64.crossMatrix(new Vector3D_F64(1, 2, 3), out);
		assertEquals(3, out.numCols);
		assertEquals(3, out.numRows);
	}

	/// Sees if both crossMatrix functions produce the same output
	@Test void crossMatrix_sameOut() {
		double a = 1.1, b = -0.5, c = 2.2;

		var v = new Vector3D_F64(a, b, c);

		DMatrixRMaj V1 = GeometryMath_F64.crossMatrix(v, null);
		DMatrixRMaj V2 = GeometryMath_F64.crossMatrix(a, b, c, null);

		assertTrue(MatrixFeatures_DDRM.isIdentical(V1, V2, GrlConstants.TEST_F64));
	}

	@Test void cross_3d_3d() {
		var a = new Vector3D_F64(1, 0, 0);
		var b = new Vector3D_F64(0, 1, 0);
		var c = new Vector3D_F64();

		GeometryMath_F64.cross(a, b, c);

		assertEquals(0, c.x, GrlConstants.TEST_F64);
		assertEquals(0, c.y, GrlConstants.TEST_F64);
		assertEquals(1, c.z, GrlConstants.TEST_F64);

		GeometryMath_F64.cross(b, a, c);

		assertEquals(0, c.x, GrlConstants.TEST_F64);
		assertEquals(0, c.y, GrlConstants.TEST_F64);
		assertEquals(-1, c.z, GrlConstants.TEST_F64);
	}

	@Test void cross_3d_3d_double() {
		var a = new Vector3D_F64(1, 2, 3);
		var b = new Vector3D_F64(0.5, 1.5, -3);
		var expected = new Vector3D_F64();
		var found = new Vector3D_F64();

		GeometryMath_F64.cross(a, b, expected);
		GeometryMath_F64.cross(a.x, a.y, a.z, b.x, b.y, b.z, found);

		assertEquals(expected.x, found.x, GrlConstants.TEST_F64);
		assertEquals(expected.y, found.y, GrlConstants.TEST_F64);
		assertEquals(expected.z, found.z, GrlConstants.TEST_F64);
	}

	@Test void cross_2d_3d() {
		var aa = new Vector2D_F64(0.75, 2);
		var a = new Vector3D_F64(0.75, 2, 1);
		var b = new Vector3D_F64(3, 0.1, 4);
		var expected = new Vector3D_F64();
		var found = new Vector3D_F64();

		GeometryMath_F64.cross(a, b, expected);
		GeometryMath_F64.cross(aa, b, found);

		assertEquals(expected.x, found.x, GrlConstants.TEST_F64);
		assertEquals(expected.y, found.y, GrlConstants.TEST_F64);
		assertEquals(expected.z, found.z, GrlConstants.TEST_F64);
	}

	@Test void cross_2d_2d() {
		var aa = new Vector2D_F64(0.75, 2);
		var a = new Vector3D_F64(0.75, 2, 1);
		var bb = new Vector2D_F64(3, 0.1);
		var b = new Vector3D_F64(3, 0.1, 1);
		var expected = new Vector3D_F64();
		var found = new Vector3D_F64();

		GeometryMath_F64.cross(a, b, expected);
		GeometryMath_F64.cross(aa, bb, found);

		assertEquals(expected.x, found.x, GrlConstants.TEST_F64);
		assertEquals(expected.y, found.y, GrlConstants.TEST_F64);
		assertEquals(expected.z, found.z, GrlConstants.TEST_F64);
	}

	@Test void add() {
		var a = new Vector3D_F64(1, 2, 3);
		var b = new Vector3D_F64(3, 1, 4);
		var c = new Vector3D_F64();

		GeometryMath_F64.add(a, b, c);

		assertEquals(4, c.getX(), GrlConstants.TEST_F64);
		assertEquals(3, c.getY(), GrlConstants.TEST_F64);
		assertEquals(7, c.getZ(), GrlConstants.TEST_F64);
	}

	@Test void add_scale() {
		var a = new Vector3D_F64(1, 2, 3);
		var b = new Vector3D_F64(3, 1, 4);
		var c = new Vector3D_F64();

		GeometryMath_F64.add(2, a, -1, b, c);

		assertEquals(-1, c.getX(), GrlConstants.TEST_F64);
		assertEquals(3, c.getY(), GrlConstants.TEST_F64);
		assertEquals(2, c.getZ(), GrlConstants.TEST_F64);
	}

	@Test void addMult() {
		var a = new Vector3D_F64(1, 2, 3);
		var b = new Vector3D_F64(2, 3, 4);
		var c = new Vector3D_F64();
		var M = new DMatrixRMaj(3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9);

		GeometryMath_F64.addMult(a, M, b, c);

		Equation eq = new Equation();
		eq.alias(M, "M");
		eq.process("expected=[1,2,3]' + M*[2;3;4]");
		DMatrixRMaj expected = eq.lookupDDRM("expected");

		assertEquals(expected.get(0), c.getX(), GrlConstants.TEST_F64);
		assertEquals(expected.get(1), c.getY(), GrlConstants.TEST_F64);
		assertEquals(expected.get(2), c.getZ(), GrlConstants.TEST_F64);

		// see if passing a twice messes up the results
		GeometryMath_F64.addMult(a, M, b, a);
		assertEquals(expected.get(0), a.getX(), GrlConstants.TEST_F64);
		assertEquals(expected.get(1), a.getY(), GrlConstants.TEST_F64);
		assertEquals(expected.get(2), a.getZ(), GrlConstants.TEST_F64);
	}

	@Test void addMultTran() {
		var a = new Vector3D_F64(1, 2, 3);
		var b = new Vector3D_F64(2, 3, 4);
		var c = new Vector3D_F64();
		var M = new DMatrixRMaj(3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9);

		GeometryMath_F64.addMultTrans(a, M, b, c);

		Equation eq = new Equation();
		eq.alias(M, "M");
		eq.process("expected=[1,2,3]' + M'*[2;3;4]");
		DMatrixRMaj expected = eq.lookupDDRM("expected");

		assertEquals(expected.get(0), c.getX(), GrlConstants.TEST_F64);
		assertEquals(expected.get(1), c.getY(), GrlConstants.TEST_F64);
		assertEquals(expected.get(2), c.getZ(), GrlConstants.TEST_F64);

		// see if passing a twice messes up the results
		GeometryMath_F64.addMultTrans(a, M, b, a);
		assertEquals(expected.get(0), a.getX(), GrlConstants.TEST_F64);
		assertEquals(expected.get(1), a.getY(), GrlConstants.TEST_F64);
		assertEquals(expected.get(2), a.getZ(), GrlConstants.TEST_F64);
	}

	@Test void sub() {
		var a = new Vector3D_F64(1, 2, 3);
		var b = new Vector3D_F64(3, 1, 4);
		var c = new Vector3D_F64();

		GeometryMath_F64.sub(a, b, c);

		assertEquals(-2, c.getX(), GrlConstants.TEST_F64);
		assertEquals(1, c.getY(), GrlConstants.TEST_F64);
		assertEquals(-1, c.getZ(), GrlConstants.TEST_F64);
	}

	@Test void rotate_2d_theta() {
		var a = new Vector2D_F64(1, 2);
		double theta = 0.6;

		var b = new Vector2D_F64();

		GeometryMath_F64.rotate(theta, a, b);

		double c = Math.cos(theta);
		double s = Math.sin(theta);


		double x = c*a.x - s*a.y;
		double y = s*a.x + c*a.y;

		assertEquals(x, b.x, GrlConstants.TEST_F64);
		assertEquals(y, b.y, GrlConstants.TEST_F64);
	}

	@Test void rotate_2d_c_s() {
		var a = new Vector2D_F64(1, 2);
		double theta = 0.6;

		var b = new Vector2D_F64();

		double c = Math.cos(theta);
		double s = Math.sin(theta);

		GeometryMath_F64.rotate(c, s, a, b);


		double x = c*a.x - s*a.y;
		double y = s*a.x + c*a.y;

		assertEquals(x, b.x, GrlConstants.TEST_F64);
		assertEquals(y, b.y, GrlConstants.TEST_F64);
	}

	@Test void mult_3d_3d() {
		var a = new Vector3D_F64(-1, 2, 3);
		var M = new DMatrixRMaj(3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9);
		var c = new Vector3D_F64();

		GeometryMath_F64.mult(M, a, c);

		assertEquals(12, c.getX(), GrlConstants.TEST_F64);
		assertEquals(24, c.getY(), GrlConstants.TEST_F64);
		assertEquals(36, c.getZ(), GrlConstants.TEST_F64);
	}

	@Test void mult4_3d_3d() {
		var a = new Vector3D_F64(-1, 2, 3);
		var aa = new Vector4D_F64(-1, 2, 3, 1);
		var M = new DMatrixRMaj(4, 4, true, 1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3, 4, 5, 6, 7);
		var c = new Vector3D_F64();
		var cc = new Vector3D_F64();

		GeometryMath_F64.mult4(M, a, c);
		GeometryMath_F64.mult(M, aa, cc);

		assertEquals(cc.x, c.getX(), GrlConstants.TEST_F64);
		assertEquals(cc.y, c.getY(), GrlConstants.TEST_F64);
		assertEquals(cc.z, c.getZ(), GrlConstants.TEST_F64);
	}

	@Test void mult_3d_2d() {
		var a = new Vector3D_F64(-1, 2, 3);
		var M = new DMatrixRMaj(3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9);
		Vector2D_F64 c = new Vector2D_F64();

		GeometryMath_F64.mult(M, a, c);

		assertEquals(12.0/36.0, c.getX(), GrlConstants.TEST_F64);
		assertEquals(24.0/36.0, c.getY(), GrlConstants.TEST_F64);
	}

	@Test void mult_2d_3d() {
		var a3 = new Vector3D_F64(-1, 2, 1);
		var M = new DMatrixRMaj(3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9);
		var expected = new Vector3D_F64();

		GeometryMath_F64.mult(M, a3, expected);

		var a2 = new Vector2D_F64(-1, 2);
		var found = new Vector3D_F64();
		GeometryMath_F64.mult(M, a2, found);

		assertEquals(expected.x, found.x, GrlConstants.TEST_F64);
		assertEquals(expected.y, found.y, GrlConstants.TEST_F64);
		assertEquals(expected.z, found.z, GrlConstants.TEST_F64);
	}

	@Test void mult_2d_2d() {
		Vector3D_F64 a3 = new Vector3D_F64(-1, 2, 1);
		var M = new DMatrixRMaj(3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9);
		var expected = new Vector3D_F64();

		GeometryMath_F64.mult(M, a3, expected);

		Vector2D_F64 a2 = new Vector2D_F64(-1, 2);
		Vector2D_F64 found = new Vector2D_F64();
		GeometryMath_F64.mult(M, a2, found);

		double z = expected.z;

		assertEquals(expected.x/z, found.x, GrlConstants.TEST_F64);
		assertEquals(expected.y/z, found.y, GrlConstants.TEST_F64);
	}

	@Test void multTran_3d_3d() {
		var a = new Vector3D_F64(-1, 2, 3);
		var M = new DMatrixRMaj(3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9);
		var c = new Vector3D_F64();

		GeometryMath_F64.multTran(M, a, c);

		assertEquals(28, c.getX(), GrlConstants.TEST_F64);
		assertEquals(32, c.getY(), GrlConstants.TEST_F64);
		assertEquals(36, c.getZ(), GrlConstants.TEST_F64);
	}

	@Test void multTran_2d_3d() {
		var a = new Vector2D_F64(-1, 2);
		var M = new DMatrixRMaj(3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9);
		var c = new Vector3D_F64();

		GeometryMath_F64.multTran(M, a, c);

		assertEquals(14, c.getX(), GrlConstants.TEST_F64);
		assertEquals(16, c.getY(), GrlConstants.TEST_F64);
		assertEquals(18, c.getZ(), GrlConstants.TEST_F64);
	}

	@Test void mult_3x4_4d_3d() {
		DMatrixRMaj P = RandomMatrices_DDRM.rectangle(3, 4, rand);
		var X = new Point4D_F64(1, 2, 3, 4);

		var Y = new Point3D_F64();
		GeometryMath_F64.mult(P, X, Y);

		var eq = new GEquation(P, "P", X, "X");
		eq.process("Y=P*X");
		checkEquals(eq, "Y", Y);
	}

	@Test void mult_3x3_4d_3d() {
		DMatrixRMaj P34 = RandomMatrices_DDRM.rectangle(3, 4, rand);
		DMatrixRMaj P33 = new DMatrixRMaj(3, 3);

		// Make P33 a subset of P34 and P34 should have all zeros in column 3
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				P33.set(row, col, P34.get(row, col));
			}
			P34.set(row, 3, 0);
		}

		var X = new Point4D_F64(1, 2, 3, 4);
		var Y = new Point3D_F64();
		GeometryMath_F64.mult(P33, X, Y);

		var eq = new GEquation(P34, "P", X, "X");
		eq.process("Y=P*X");
		checkEquals(eq, "Y", Y);
	}

	private void checkEquals( GEquation eq, String name, GeoTuple3D_F64<?> found ) {
		DMatrixRMaj expected = eq.lookupDDRM(name);

		for (int i = 0; i < 3; i++) {
			assertEquals(expected.get(i), found.get(i), UtilEjml.TEST_F64);
		}
	}

	private void checkEquals( GEquation eq, String name, GeoTuple4D_F64<?> found ) {
		DMatrixRMaj expected = eq.lookupDDRM(name);

		for (int i = 0; i < 4; i++) {
			assertEquals(expected.get(i), found.get(i), UtilEjml.TEST_F64);
		}
	}

	@Test void mult_4x4_4d_4d() {
		DMatrixRMaj P = RandomMatrices_DDRM.rectangle(4, 4, rand);
		var X = new Point4D_F64(1, 2, 3, 4);

		var Y = new Point4D_F64();
		GeometryMath_F64.mult(P, X, Y);

		var eq = new GEquation(P, "P", X, "X");
		eq.process("Y=P*X");
		checkEquals(eq, "Y", Y);
	}

	@Test void mult_3x3_4d_4d() {
		DMatrixRMaj P = RandomMatrices_DDRM.rectangle(3, 3, rand);
		var X = new Point4D_F64(1, 2, 3, 4);

		var Y = new Point4D_F64();
		GeometryMath_F64.mult(P, X, Y);

		var eq = new GEquation(P, "P", X, "X");
		eq.process("P=[[P,[0,0,0]'];[0,0,0,1]]");
		eq.process("Y=P*X");
		checkEquals(eq, "Y", Y);
	}

	@Test void multTran_4x4_4d_4d() {
		DMatrixRMaj P = RandomMatrices_DDRM.rectangle(4, 4, rand);
		var X = new Point4D_F64(1, 2, 3, 4);

		var Y = new Point4D_F64();
		GeometryMath_F64.multTran(P, X, Y);

		var eq = new GEquation(P, "P", X, "X");
		eq.process("Y=P'*X");
		checkEquals(eq, "Y", Y);
	}

	@Test void multTran_3x3_4d_4d() {
		DMatrixRMaj P = RandomMatrices_DDRM.rectangle(3, 3, rand);
		var X = new Point4D_F64(1, 2, 3, 4);

		var Y = new Point4D_F64();
		GeometryMath_F64.multTran(P, X, Y);

		var eq = new GEquation(P, "P", X, "X");
		eq.process("P=[[P,[0,0,0]'];[0,0,0,1]]");
		eq.process("Y=P'*X");
		checkEquals(eq, "Y", Y);
	}

	@Test void mult_4d_2d() {
		DMatrixRMaj P = RandomMatrices_DDRM.rectangle(3, 4, rand);
		var X = new Point4D_F64(1, 2, 3, 4);

		var Y = new Point2D_F64();
		GeometryMath_F64.mult(P, X, Y);

		var eq = new GEquation(P, "P", X, "X");
		eq.process("Y=P*X");
		DMatrixRMaj expected = eq.lookupDDRM("Y");

		for (int i = 0; i < 2; i++) {
			double z = expected.get(2);
			assertEquals(expected.get(i)/z, Y.get(i), UtilEjml.TEST_F64);
		}
	}

	@Test void multCrossA_2D() {
		var a = new Point2D_F64(3, 2);
		DMatrixRMaj b = RandomMatrices_DDRM.rectangle(3, 3, rand);

		DMatrixRMaj a_hat = GeometryMath_F64.crossMatrix(a.x, a.y, 1, null);
		var expected = new DMatrixRMaj(3, 3);
		CommonOps_DDRM.mult(a_hat, b, expected);

		DMatrixRMaj found = GeometryMath_F64.multCrossA(a, b, null);

		assertTrue(MatrixFeatures_DDRM.isIdentical(expected, found, GrlConstants.TEST_F64));
	}

	@Test void multCrossATransA_2D() {
		var a = new Point2D_F64(3, 2);
		DMatrixRMaj b = RandomMatrices_DDRM.rectangle(3, 3, rand);

		DMatrixRMaj a_hat = GeometryMath_F64.crossMatrix(a.x, a.y, 1, null);
		var expected = new DMatrixRMaj(3, 3);
		CommonOps_DDRM.multTransA(a_hat, b, expected);

		DMatrixRMaj found = GeometryMath_F64.multCrossATransA(a, b, null);

		assertTrue(MatrixFeatures_DDRM.isIdentical(expected, found, GrlConstants.TEST_F64));
	}

	@Test void multCrossA_3D() {
		var a = new Point3D_F64(1, 2, 3);
		DMatrixRMaj b = RandomMatrices_DDRM.rectangle(3, 3, rand);

		DMatrixRMaj a_hat = GeometryMath_F64.crossMatrix(a.x, a.y, a.z, null);
		var expected = new DMatrixRMaj(3, 3);
		CommonOps_DDRM.mult(a_hat, b, expected);

		DMatrixRMaj found = GeometryMath_F64.multCrossA(a, b, null);

		assertTrue(MatrixFeatures_DDRM.isIdentical(expected, found, GrlConstants.TEST_F64));
	}

	@Test void multCrossATransA_3D() {
		var a = new Point3D_F64(1, 2, 3);
		DMatrixRMaj b = RandomMatrices_DDRM.rectangle(3, 3, rand);

		DMatrixRMaj a_hat = GeometryMath_F64.crossMatrix(a.x, a.y, a.z, null);
		DMatrixRMaj expected = new DMatrixRMaj(3, 3);
		CommonOps_DDRM.multTransA(a_hat, b, expected);

		DMatrixRMaj found = GeometryMath_F64.multCrossATransA(a, b, null);

		assertTrue(MatrixFeatures_DDRM.isIdentical(expected, found, GrlConstants.TEST_F64));
	}

	@Test void innerProd_3D() {
		var a = new Vector3D_F64(2, -2, 3);
		var b = new Vector3D_F64(4, 3, 2);
		var M = new DMatrixRMaj(3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9);

		double found = GeometryMath_F64.innerProd(a, M, b);

		assertEquals(156, found, GrlConstants.TEST_F64);
	}

	@Test void innerProdTranM() {
		var a = new Vector3D_F64(2, -2, 3);
		var b = new Vector3D_F64(4, 3, 2);
		var M = new DMatrixRMaj(3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9);

		double found = GeometryMath_F64.innerProdTranM(a, M, b);

		assertEquals(126, found, GrlConstants.TEST_F64);
	}

	@Test void innerProd_2D() {
		var a = new Vector2D_F64(2, -2);
		var b = new Vector2D_F64(4, 3);
		var M = new DMatrixRMaj(3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9);

		double found = GeometryMath_F64.innerProd(a, M, b);

		assertEquals(13, found, GrlConstants.TEST_F64);
	}

	@Test void outerProd_3D() {
		var a = new Vector3D_F64(2, -2, 5);
		var b = new Vector3D_F64(4, 3, 9);
		var M = new DMatrixRMaj(3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9);

		DMatrixRMaj expected = new DMatrixRMaj(3, 3, true, 8, 6, 18, -8, -6, -18, 20, 15, 45);

		GeometryMath_F64.outerProd(a, b, M);

		assertTrue(MatrixFeatures_DDRM.isIdentical(expected, M, GrlConstants.TEST_F64));
	}

	@Test void addOuterProd_3D() {
		var a = new Vector3D_F64(2, -2, 5);
		var b = new Vector3D_F64(4, 3, 9);
		DMatrixRMaj A = new DMatrixRMaj(3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9);

		DMatrixRMaj found = new DMatrixRMaj(3, 3);

		DMatrixRMaj expected = new DMatrixRMaj(3, 3, true, -7, -4, -15, 12, 11, 24, -13, -7, -36);

		GeometryMath_F64.addOuterProd(A, -1, a, b, found);

		assertTrue(MatrixFeatures_DDRM.isIdentical(expected, found, GrlConstants.TEST_F64));
	}

	@Test void dot() {
		var a = new Vector3D_F64(2, -2, 3);
		var b = new Vector3D_F64(4, 3, 2);
		double found = GeometryMath_F64.dot(a, b);

		assertEquals(8, found, GrlConstants.TEST_F64);
	}

	@Test void scale() {
		var a = new Vector3D_F64(1, -2, 3);
		GeometryMath_F64.scale(a, 2);

		assertEquals(2, a.x, GrlConstants.TEST_F64);
		assertEquals(-4, a.y, GrlConstants.TEST_F64);
		assertEquals(6, a.z, GrlConstants.TEST_F64);
	}

	@Test void divide() {
		var a = new Vector3D_F64(1, -2, 3);
		GeometryMath_F64.divide(a, 2);

		assertEquals(0.5, a.x, GrlConstants.TEST_F64);
		assertEquals(-1.0, a.y, GrlConstants.TEST_F64);
		assertEquals(1.5, a.z, GrlConstants.TEST_F64);
	}

	@Test void changeSign() {
		var a = new Vector3D_F64(1, -2, 3);
		GeometryMath_F64.changeSign(a);

		assertEquals(-1, a.x, GrlConstants.TEST_F64);
		assertEquals(2, a.y, GrlConstants.TEST_F64);
		assertEquals(-3, a.z, GrlConstants.TEST_F64);
	}

	@Test void toMatrix() {
		var a = new Vector3D_F64(1, -2, 3);
		DMatrixRMaj found = GeometryMath_F64.toMatrix(a, null);

		assertEquals(1, found.get(0), GrlConstants.TEST_F64);
		assertEquals(-2, found.get(1), GrlConstants.TEST_F64);
		assertEquals(3, found.get(2), GrlConstants.TEST_F64);
	}

	@Test void toTuple3D() {
		var a = new DMatrixRMaj(3, 1, true, 1, -2, 3);
		var b = new Vector3D_F64();
		GeometryMath_F64.toTuple3D(a, b);

		assertEquals(b.x, a.get(0), GrlConstants.TEST_F64);
		assertEquals(b.y, a.get(1), GrlConstants.TEST_F64);
		assertEquals(b.z, a.get(2), GrlConstants.TEST_F64);
	}

	@Test void quatFromTwoVectors() {
		quatFromTwoVectors(1, 0, 0, -1, 0, 0);
		quatFromTwoVectors(0, 1, 0, -1, 0, 0);
		quatFromTwoVectors(1, 0, 0, -1, 0, 0);
		quatFromTwoVectors(0, 0, -1, 0, 0, -1);
		quatFromTwoVectors(0, 0, -1, 0, 1, 0);
	}

	void quatFromTwoVectors( double ax, double ay, double az, double bx, double by, double bz ) {
		var a = new Vector3D_F64(ax, ay, az);
		var b = new Vector3D_F64(bx, by, bz);

		Quaternion_F64 q = GeometryMath_F64.quatFromTwoVectors(a, b, null);
		DMatrixRMaj rotation = ConvertRotation3D_F64.quaternionToMatrix(q, null);

		var found = new Vector3D_F64();
		GeometryMath_F64.mult(rotation, a, found);

		assertTrue(found.isIdentical(b, GrlConstants.TEST_F64));
	}

	@Test void quatFromTwoVectorsSvd() {
		quatFromTwoVectorsSvd(1, 0, 0, -1, 0, 0);
		quatFromTwoVectorsSvd(0, 1, 0, -1, 0, 0);
		quatFromTwoVectorsSvd(1, 0, 0, -1, 0, 0);
		quatFromTwoVectorsSvd(0, 0, -1, 0, 0, -1);
		quatFromTwoVectorsSvd(0, 0, -1, 0, 1, 0);
	}

	void quatFromTwoVectorsSvd( double ax, double ay, double az, double bx, double by, double bz ) {
		var a = new Vector3D_F64(ax, ay, az);
		var b = new Vector3D_F64(bx, by, bz);

		Quaternion_F64 q = GeometryMath_F64.quatFromTwoVectorsSvd(a, b, null);
		DMatrixRMaj rotation = ConvertRotation3D_F64.quaternionToMatrix(q, null);

		var found = new Vector3D_F64();
		GeometryMath_F64.mult(rotation, a, found);

		assertTrue(found.isIdentical(b, GrlConstants.TEST_F64));
	}

	@Test void rotationFromTwoVectors() {
		rotationFromTwoVectors(1, 0, 0, -1, 0, 0);
		rotationFromTwoVectors(0, 1, 0, -1, 0, 0);
		rotationFromTwoVectors(1, 0, 0, -1, 0, 0);
		rotationFromTwoVectors(0, 0, -1, 0, 0, -1);
		rotationFromTwoVectors(0, 0, -1, 0, 1, 0);

		rotationFromTwoVectors(0, -1, 0, 0, 1, 0);
	}

	void rotationFromTwoVectors( double ax, double ay, double az, double bx, double by, double bz ) {
		var a = new Vector3D_F64(ax, ay, az);
		var b = new Vector3D_F64(bx, by, bz);

		DMatrixRMaj rotation = GeometryMath_F64.rotationFromTwoVectors(a, b, null);

		var found = new Vector3D_F64();
		GeometryMath_F64.mult(rotation, a, found);

		assertTrue(found.isIdentical(b, GrlConstants.TEST_F64));
	}

	// Test anti-parallel case with a non-trivial configuration
	@Test void rotationFromTwoVectors_antiparallel() {
		Vector3D_F64[] aValues = {
				// Branch 1: |ax| > 0.25, a not in xy-plane (so az ≠ 0 → |v| ≠ 1)
				new Vector3D_F64(0.5, 0.0, Math.sqrt(3)/2),
				// Branch 2: |ax| ≤ 0.25, |ay| > 0.25, a not in yz-plane (so ax ≠ 0 → |v| ≠ 1)
				new Vector3D_F64(0.2, 0.5, Math.sqrt(1 - 0.04 - 0.25)),
				// Branch 3: |ax| ≤ 0.25 AND |ay| ≤ 0.25, a not in xz-plane (so ay ≠ 0 → |v| ≠ 1)
				new Vector3D_F64(0.1, 0.2, Math.sqrt(1 - 0.01 - 0.04)),
		};

		for (var a : aValues) {
			var b = new Vector3D_F64(-a.x, -a.y, -a.z);
			DMatrixRMaj R = GeometryMath_F64.rotationFromTwoVectors(a, b, null);

			DMatrixRMaj RRT = new DMatrixRMaj(3, 3);
			CommonOps_DDRM.multTransB(R, R, RRT);
			assertTrue(MatrixFeatures_DDRM.isIdentity(RRT, GrlConstants.TEST_F64),
					"Not orthogonal for a=" + a.formatMap());
		}
	}

	@Test void pickPerpendicular() {
		for (int i = 0; i < 20; i++) {
			Vector3D_F64 a = new Vector3D_F64(rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian());
			Vector3D_F64 b = GeometryMath_F64.pickPerpendicular(a, null);
			assertEquals(0.0, a.dot(b), UtilEjml.TEST_F64);
		}
	}

	@Test void setColumn() {
		var a = new Vector3D_F64(1, 2, 3);
		var M = new DMatrixRMaj(3, 10);
		GeometryMath_F64.setColumn(2, a, M);
		assertEquals(1, M.get(0, 2));
		assertEquals(2, M.get(1, 2));
		assertEquals(3, M.get(2, 2));
	}

	@Test void setRow() {
		var a = new Vector3D_F64(1, 2, 3);
		var M = new DMatrixRMaj(10, 3);
		GeometryMath_F64.setRow(2, a, M);
		assertEquals(1, M.get(2, 0));
		assertEquals(2, M.get(2, 1));
		assertEquals(3, M.get(2, 2));
	}

	@Test void setColumn_beta() {
		var a = new Vector3D_F64(1, 2, 3);
		var M = new DMatrixRMaj(3, 10);
		GeometryMath_F64.setColumn(2, -0.1, a, M);
		assertEquals(-0.1, M.get(0, 2), UtilEjml.EPS);
		assertEquals(-0.2, M.get(1, 2), UtilEjml.EPS);
		assertEquals(-0.3, M.get(2, 2), UtilEjml.EPS);
	}

	@Test void setRow_beta() {
		var a = new Vector3D_F64(1, 2, 3);
		var M = new DMatrixRMaj(10, 3);
		GeometryMath_F64.setRow(2, -0.1, a, M);
		assertEquals(-0.1, M.get(2, 0), UtilEjml.EPS);
		assertEquals(-0.2, M.get(2, 1), UtilEjml.EPS);
		assertEquals(-0.3, M.get(2, 2), UtilEjml.EPS);
	}
}
