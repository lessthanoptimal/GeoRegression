/*
 * Copyright (C) 2011-2016, Peter Abeles. All Rights Reserved.
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
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector2D_F64;
import georegression.struct.point.Vector3D_F64;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.MatrixFeatures;
import org.ejml.ops.RandomMatrices;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestGeometryMath_F64 {

	Random rand = new Random( 0x33 );

	/**
	 * Sees if crossMatrix produces a valid output
	 */
	@Test
	public void crossMatrix_validOut() {
		double a = 1.1, b = -0.5, c = 2.2;

		Vector3D_F64 v = new Vector3D_F64( a, b, c );

		Vector3D_F64 x = new Vector3D_F64( 7.6, 2.9, 0.5 );

		Vector3D_F64 found0 = new Vector3D_F64();
		Vector3D_F64 found1 = new Vector3D_F64();

		GeometryMath_F64.cross( v, x, found0 );
		DenseMatrix64F V = GeometryMath_F64.crossMatrix( a, b, c, null );

		GeometryMath_F64.mult( V, x, found1 );

		assertEquals( found0.x, found1.x, GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( found0.y, found1.y, GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( found0.z, found1.z, GrlConstants.DOUBLE_TEST_TOL );
	}

	/**
	 * Sees if both crossMatrix functions produce the same output
	 */
	@Test
	public void crossMatrix_sameOut() {
		double a = 1.1, b = -0.5, c = 2.2;

		Vector3D_F64 v = new Vector3D_F64( a, b, c );

		DenseMatrix64F V1 = GeometryMath_F64.crossMatrix( v, null );
		DenseMatrix64F V2 = GeometryMath_F64.crossMatrix( a, b, c, null );

		assertTrue( MatrixFeatures.isIdentical( V1 ,V2 , GrlConstants.DOUBLE_TEST_TOL ));
	}

	@Test
	public void cross_3d_3d() {
		Vector3D_F64 a = new Vector3D_F64( 1, 0, 0 );
		Vector3D_F64 b = new Vector3D_F64( 0, 1, 0 );
		Vector3D_F64 c = new Vector3D_F64();

		GeometryMath_F64.cross( a, b, c );

		assertEquals( 0, c.x, GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 0, c.y, GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 1, c.z, GrlConstants.DOUBLE_TEST_TOL );

		GeometryMath_F64.cross( b, a, c );

		assertEquals( 0, c.x, GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 0, c.y, GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( -1, c.z, GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void cross_3d_3d_double() {
		Vector3D_F64 a = new Vector3D_F64( 1, 2, 3 );
		Vector3D_F64 b = new Vector3D_F64( 0.5, 1.5, -3 );
		Vector3D_F64 expected = new Vector3D_F64();
		Vector3D_F64 found = new Vector3D_F64();

		GeometryMath_F64.cross( a, b, expected );
		GeometryMath_F64.cross( a.x, a.y, a.z, b.x, b.y, b.z, found );

		assertEquals( expected.x, found.x, GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( expected.y, found.y, GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( expected.z, found.z, GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void cross_2d_3d() {
		Vector2D_F64 aa = new Vector2D_F64( 0.75, 2 );
		Vector3D_F64 a = new Vector3D_F64( 0.75, 2, 1);
		Vector3D_F64 b = new Vector3D_F64( 3, 0.1, 4 );
		Vector3D_F64 expected = new Vector3D_F64();
		Vector3D_F64 found = new Vector3D_F64();

		GeometryMath_F64.cross( a, b, expected );
		GeometryMath_F64.cross( aa, b, found );

		assertEquals( expected.x, found.x , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( expected.y, found.y , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( expected.z, found.z , GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void cross_2d_2d() {
		Vector2D_F64 aa = new Vector2D_F64( 0.75, 2 );
		Vector3D_F64 a = new Vector3D_F64( 0.75, 2, 1);
		Vector2D_F64 bb = new Vector2D_F64( 3, 0.1);
		Vector3D_F64 b = new Vector3D_F64( 3, 0.1, 1 );
		Vector3D_F64 expected = new Vector3D_F64();
		Vector3D_F64 found = new Vector3D_F64();

		GeometryMath_F64.cross( a, b, expected );
		GeometryMath_F64.cross( aa, bb, found );

		assertEquals( expected.x, found.x , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( expected.y, found.y , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( expected.z, found.z , GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void add() {
		Vector3D_F64 a = new Vector3D_F64( 1, 2, 3 );
		Vector3D_F64 b = new Vector3D_F64( 3, 1, 4 );
		Vector3D_F64 c = new Vector3D_F64();

		GeometryMath_F64.add( a , b , c );

		assertEquals( 4 , c.getX() , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 3 , c.getY() , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 7 , c.getZ() , GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void add_scale() {
		Vector3D_F64 a = new Vector3D_F64( 1, 2, 3 );
		Vector3D_F64 b = new Vector3D_F64( 3, 1, 4 );
		Vector3D_F64 c = new Vector3D_F64();

		GeometryMath_F64.add( 2, a , -1 , b , c );

		assertEquals( -1 , c.getX() , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals(  3 , c.getY() , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals(  2 , c.getZ() , GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void addMult() {
		Vector3D_F64 a = new Vector3D_F64( 1, 2, 3 );
		Vector3D_F64 b = new Vector3D_F64( 2, 3, 4 );
		Vector3D_F64 c = new Vector3D_F64();
		DenseMatrix64F M = new DenseMatrix64F( 3,3,true,1,1,1,1,1,1,1,1,1);

		GeometryMath_F64.addMult( a , M , b , c );

		assertEquals( 10 , c.getX() , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 11 , c.getY() , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 12 , c.getZ() , GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void sub() {
		Vector3D_F64 a = new Vector3D_F64( 1, 2, 3 );
		Vector3D_F64 b = new Vector3D_F64( 3, 1, 4 );
		Vector3D_F64 c = new Vector3D_F64();

		GeometryMath_F64.sub( a , b , c );

		assertEquals( -2 , c.getX() , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 1 , c.getY() , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( -1 , c.getZ() , GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void rotate_2d_theta() {
		Vector2D_F64 a = new Vector2D_F64( 1, 2 );
		double theta = 0.6;

		Vector2D_F64 b = new Vector2D_F64();

		GeometryMath_F64.rotate( theta,a,b);

		double c = Math.cos(theta);
		double s = Math.sin(theta);


		double x = c*a.x - s*a.y;
		double y = s*a.x + c*a.y;

		assertEquals(x,b.x, GrlConstants.DOUBLE_TEST_TOL );
		assertEquals(y,b.y, GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void rotate_2d_c_s() {
		Vector2D_F64 a = new Vector2D_F64( 1, 2 );
		double theta = 0.6;

		Vector2D_F64 b = new Vector2D_F64();

		double c = Math.cos(theta);
		double s = Math.sin(theta);

		GeometryMath_F64.rotate( c,s,a,b);


		double x = c*a.x - s*a.y;
		double y = s*a.x + c*a.y;

		assertEquals(x,b.x, GrlConstants.DOUBLE_TEST_TOL );
		assertEquals(y,b.y, GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void mult_3d_3d() {
		Vector3D_F64 a = new Vector3D_F64( -1, 2, 3 );
		DenseMatrix64F M = new DenseMatrix64F(3,3,true,1,2,3,4,5,6,7,8,9);
		Vector3D_F64 c = new Vector3D_F64();

		GeometryMath_F64.mult( M , a , c );

		assertEquals( 12 , c.getX() , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 24 , c.getY() , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 36 , c.getZ() , GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void mult_3d_2d() {
		Vector3D_F64 a = new Vector3D_F64( -1, 2, 3 );
		DenseMatrix64F M = new DenseMatrix64F(3,3,true,1,2,3,4,5,6,7,8,9);
		Vector2D_F64 c = new Vector2D_F64();

		GeometryMath_F64.mult( M , a , c );

		assertEquals( 12.0/36.0 , c.getX() , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 24.0/36.0 , c.getY() , GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void mult_2d_3d() {
		Vector3D_F64 a3 = new Vector3D_F64( -1, 2, 1 );
		DenseMatrix64F M = new DenseMatrix64F(3,3,true,1,2,3,4,5,6,7,8,9);
		Vector3D_F64 expected = new Vector3D_F64();

		GeometryMath_F64.mult( M , a3 , expected );

		Vector2D_F64 a2 = new Vector2D_F64( -1, 2 );
		Vector3D_F64 found = new Vector3D_F64();
		GeometryMath_F64.mult( M , a2 , found );

		assertEquals( expected.x , found.x , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( expected.y , found.y , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( expected.z , found.z , GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void mult_2d_2d() {
		Vector3D_F64 a3 = new Vector3D_F64( -1, 2, 1 );
		DenseMatrix64F M = new DenseMatrix64F(3,3,true,1,2,3,4,5,6,7,8,9);
		Vector3D_F64 expected = new Vector3D_F64();

		GeometryMath_F64.mult( M , a3 , expected );

		Vector2D_F64 a2 = new Vector2D_F64( -1, 2 );
		Vector2D_F64 found = new Vector2D_F64();
		GeometryMath_F64.mult( M , a2 , found );

		double z = expected.z;

		assertEquals( expected.x/z , found.x , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( expected.y/z , found.y , GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void multTran_3d_3d() {
		Vector3D_F64 a = new Vector3D_F64( -1, 2, 3 );
		DenseMatrix64F M = new DenseMatrix64F(3,3,true,1,2,3,4,5,6,7,8,9);
		Vector3D_F64 c = new Vector3D_F64();

		GeometryMath_F64.multTran( M , a , c );

		assertEquals( 28 , c.getX() , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 32 , c.getY() , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 36 , c.getZ() , GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void multTran_2d_3d() {
		Vector2D_F64 a = new Vector2D_F64( -1, 2 );
		DenseMatrix64F M = new DenseMatrix64F(3,3,true,1,2,3,4,5,6,7,8,9);
		Vector3D_F64 c = new Vector3D_F64();

		GeometryMath_F64.multTran( M , a , c );

		assertEquals( 14 , c.getX() , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 16 , c.getY() , GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 18 , c.getZ() , GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void multCrossA_2D() {
		Point2D_F64 a = new Point2D_F64(3,2);
		DenseMatrix64F b = RandomMatrices.createRandom(3,3,rand);

		DenseMatrix64F a_hat = GeometryMath_F64.crossMatrix(a.x,a.y,1,null);
		DenseMatrix64F expected = new DenseMatrix64F(3,3);
		CommonOps.mult(a_hat,b,expected);

		DenseMatrix64F found = GeometryMath_F64.multCrossA(a,b,null);

		assertTrue(MatrixFeatures.isIdentical(expected,found,GrlConstants.DOUBLE_TEST_TOL));
	}

	@Test
	public void multCrossA_3D() {
		Point3D_F64 a = new Point3D_F64(1,2,3);
		DenseMatrix64F b = RandomMatrices.createRandom(3,3,rand);

		DenseMatrix64F a_hat = GeometryMath_F64.crossMatrix(a.x,a.y,a.z,null);
		DenseMatrix64F expected = new DenseMatrix64F(3,3);
		CommonOps.mult(a_hat,b,expected);

		DenseMatrix64F found = GeometryMath_F64.multCrossA(a,b,null);

		assertTrue(MatrixFeatures.isIdentical(expected,found,GrlConstants.DOUBLE_TEST_TOL));
	}

	@Test
	public void innerProd_3D() {
		Vector3D_F64 a = new Vector3D_F64( 2, -2, 3 );
		Vector3D_F64 b = new Vector3D_F64( 4, 3, 2 );
		DenseMatrix64F M = new DenseMatrix64F( 3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9 );

		double found = GeometryMath_F64.innerProd( a, M, b );

		assertEquals( 156, found, GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void innerProdTranM() {
		Vector3D_F64 a = new Vector3D_F64( 2, -2, 3 );
		Vector3D_F64 b = new Vector3D_F64( 4, 3, 2 );
		DenseMatrix64F M = new DenseMatrix64F( 3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9 );

		double found = GeometryMath_F64.innerProdTranM( a, M, b );

		assertEquals( 126, found, GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void innerProd_2D() {
		Vector2D_F64 a = new Vector2D_F64( 2, -2 );
		Vector2D_F64 b = new Vector2D_F64( 4, 3 );
		DenseMatrix64F M = new DenseMatrix64F( 3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9 );

		double found = GeometryMath_F64.innerProd( a, M, b );

		assertEquals( 13, found, GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void outerProd_3D() {
		Vector3D_F64 a = new Vector3D_F64( 2, -2 , 5);
		Vector3D_F64 b = new Vector3D_F64( 4, 3 , 9);
		DenseMatrix64F M = new DenseMatrix64F( 3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9 );

		DenseMatrix64F expected = new DenseMatrix64F(3,3,true,8 , 6 ,18 , -8 ,-6,-18,20,15,45);

		GeometryMath_F64.outerProd( a, b , M );

		assertTrue( MatrixFeatures.isIdentical(expected,M,GrlConstants.DOUBLE_TEST_TOL) );
	}

	@Test
	public void addOuterProd_3D() {
		Vector3D_F64 a = new Vector3D_F64( 2, -2 , 5);
		Vector3D_F64 b = new Vector3D_F64( 4, 3 , 9);
		DenseMatrix64F A = new DenseMatrix64F( 3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9 );

		DenseMatrix64F found = new DenseMatrix64F( 3, 3);

		DenseMatrix64F expected = new DenseMatrix64F(3,3,true,-7,-4,-15,12 , 11,24,-13,-7,-36);

		GeometryMath_F64.addOuterProd( A , -1 , a, b , found );

		assertTrue( MatrixFeatures.isIdentical(expected,found,GrlConstants.DOUBLE_TEST_TOL) );
	}

	@Test
	public void dot() {
		Vector3D_F64 a = new Vector3D_F64( 2, -2, 3 );
		Vector3D_F64 b = new Vector3D_F64( 4, 3, 2 );
		double found = GeometryMath_F64.dot( a, b );

		assertEquals( 8, found, GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void scale() {
		Vector3D_F64 a = new Vector3D_F64( 1, -2, 3 );
		GeometryMath_F64.scale( a, 2 );

		assertEquals( 2, a.x, GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( -4, a.y, GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 6, a.z, GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void divide() {
		Vector3D_F64 a = new Vector3D_F64( 1, -2, 3 );
		GeometryMath_F64.divide( a, 2 );

		assertEquals( 0.5, a.x, GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( -1.0, a.y, GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 1.5, a.z, GrlConstants.DOUBLE_TEST_TOL );
	}


	@Test
	public void changeSign() {
		Vector3D_F64 a = new Vector3D_F64( 1, -2, 3 );
		GeometryMath_F64.changeSign( a );

		assertEquals( -1, a.x, GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( 2, a.y, GrlConstants.DOUBLE_TEST_TOL );
		assertEquals( -3, a.z, GrlConstants.DOUBLE_TEST_TOL );
	}

	@Test
	public void toMatrix() {
		Vector3D_F64 a = new Vector3D_F64( 1, -2, 3 );
		DenseMatrix64F found = GeometryMath_F64.toMatrix(a,null);

		assertEquals(1,found.get(0),GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(-2,found.get(1),GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(3,found.get(2),GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void toTuple3D() {
		DenseMatrix64F a = new DenseMatrix64F(3,1,true,1,-2,3);
		Vector3D_F64 b = new Vector3D_F64();
		GeometryMath_F64.toTuple3D(a, b);

		assertEquals(b.x,a.get(0),GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(b.y,a.get(1),GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(b.z,a.get(2),GrlConstants.DOUBLE_TEST_TOL);
	}
}
