/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.geometry;

import georegression.misc.GrlConstants;
import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector2D_F32;
import georegression.struct.point.Vector3D_F32;
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
public class TestGeometryMath_F32 {

	Random rand = new Random( 0x33 );

	/**
	 * Sees if crossMatrix produces a valid output
	 */
	@Test
	public void crossMatrix_validOut() {
		float a = 1.1f, b = -0.5f, c = 2.2f;

		Vector3D_F32 v = new Vector3D_F32( a, b, c );

		Vector3D_F32 x = new Vector3D_F32( 7.6f, 2.9f, 0.5f );

		Vector3D_F32 found0 = new Vector3D_F32();
		Vector3D_F32 found1 = new Vector3D_F32();

		GeometryMath_F32.cross( v, x, found0 );
		DenseMatrix64F V = GeometryMath_F32.crossMatrix( a, b, c, null );

		GeometryMath_F32.mult( V, x, found1 );

		assertEquals( found0.x, found1.x, GrlConstants.FLOAT_TEST_TOL );
		assertEquals( found0.y, found1.y, GrlConstants.FLOAT_TEST_TOL );
		assertEquals( found0.z, found1.z, GrlConstants.FLOAT_TEST_TOL );
	}

	/**
	 * Sees if both crossMatrix functions produce the same output
	 */
	@Test
	public void crossMatrix_sameOut() {
		float a = 1.1f, b = -0.5f, c = 2.2f;

		Vector3D_F32 v = new Vector3D_F32( a, b, c );

		DenseMatrix64F V1 = GeometryMath_F32.crossMatrix( v, null );
		DenseMatrix64F V2 = GeometryMath_F32.crossMatrix( a, b, c, null );

		assertTrue( MatrixFeatures.isIdentical( V1 ,V2 , 1e-8 ));
	}

	@Test
	public void cross_3d_3d() {
		Vector3D_F32 a = new Vector3D_F32( 1, 0, 0 );
		Vector3D_F32 b = new Vector3D_F32( 0, 1, 0 );
		Vector3D_F32 c = new Vector3D_F32();

		GeometryMath_F32.cross( a, b, c );

		assertEquals( 0, c.x, GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 0, c.y, GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 1, c.z, GrlConstants.FLOAT_TEST_TOL );

		GeometryMath_F32.cross( b, a, c );

		assertEquals( 0, c.x, GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 0, c.y, GrlConstants.FLOAT_TEST_TOL );
		assertEquals( -1, c.z, GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void cross_3d_3d_float() {
		Vector3D_F32 a = new Vector3D_F32( 1, 2, 3 );
		Vector3D_F32 b = new Vector3D_F32( 0.5f, 1.5f, -3 );
		Vector3D_F32 expected = new Vector3D_F32();
		Vector3D_F32 found = new Vector3D_F32();

		GeometryMath_F32.cross( a, b, expected );
		GeometryMath_F32.cross( a.x, a.y, a.z, b.x, b.y, b.z, found );

		assertEquals( expected.x, found.x, GrlConstants.FLOAT_TEST_TOL );
		assertEquals( expected.y, found.y, GrlConstants.FLOAT_TEST_TOL );
		assertEquals( expected.z, found.z, GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void cross_2d_3d() {
		Vector2D_F32 aa = new Vector2D_F32( 0.75f, 2 );
		Vector3D_F32 a = new Vector3D_F32( 0.75f, 2, 1);
		Vector3D_F32 b = new Vector3D_F32( 3, 0.1f, 4 );
		Vector3D_F32 expected = new Vector3D_F32();
		Vector3D_F32 found = new Vector3D_F32();

		GeometryMath_F32.cross( a, b, expected );
		GeometryMath_F32.cross( aa, b, found );

		assertEquals( expected.x, found.x , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( expected.y, found.y , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( expected.z, found.z , GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void cross_2d_2d() {
		Vector2D_F32 aa = new Vector2D_F32( 0.75f, 2 );
		Vector3D_F32 a = new Vector3D_F32( 0.75f, 2, 1);
		Vector2D_F32 bb = new Vector2D_F32( 3, 0.1f);
		Vector3D_F32 b = new Vector3D_F32( 3, 0.1f, 1 );
		Vector3D_F32 expected = new Vector3D_F32();
		Vector3D_F32 found = new Vector3D_F32();

		GeometryMath_F32.cross( a, b, expected );
		GeometryMath_F32.cross( aa, bb, found );

		assertEquals( expected.x, found.x , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( expected.y, found.y , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( expected.z, found.z , GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void add() {
		Vector3D_F32 a = new Vector3D_F32( 1, 2, 3 );
		Vector3D_F32 b = new Vector3D_F32( 3, 1, 4 );
		Vector3D_F32 c = new Vector3D_F32();

		GeometryMath_F32.add( a , b , c );

		assertEquals( 4 , c.getX() , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 3 , c.getY() , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 7 , c.getZ() , GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void add_scale() {
		Vector3D_F32 a = new Vector3D_F32( 1, 2, 3 );
		Vector3D_F32 b = new Vector3D_F32( 3, 1, 4 );
		Vector3D_F32 c = new Vector3D_F32();

		GeometryMath_F32.add( 2, a , -1 , b , c );

		assertEquals( -1 , c.getX() , GrlConstants.FLOAT_TEST_TOL );
		assertEquals(  3 , c.getY() , GrlConstants.FLOAT_TEST_TOL );
		assertEquals(  2 , c.getZ() , GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void addMult() {
		Vector3D_F32 a = new Vector3D_F32( 1, 2, 3 );
		Vector3D_F32 b = new Vector3D_F32( 2, 3, 4 );
		Vector3D_F32 c = new Vector3D_F32();
		DenseMatrix64F M = new DenseMatrix64F( 3,3,true,1,1,1,1,1,1,1,1,1);

		GeometryMath_F32.addMult( a , M , b , c );

		assertEquals( 10 , c.getX() , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 11 , c.getY() , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 12 , c.getZ() , GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void sub() {
		Vector3D_F32 a = new Vector3D_F32( 1, 2, 3 );
		Vector3D_F32 b = new Vector3D_F32( 3, 1, 4 );
		Vector3D_F32 c = new Vector3D_F32();

		GeometryMath_F32.sub( a , b , c );

		assertEquals( -2 , c.getX() , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 1 , c.getY() , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( -1 , c.getZ() , GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void rotate_2d_theta() {
		Vector2D_F32 a = new Vector2D_F32( 1, 2 );
		float theta = 0.6f;

		Vector2D_F32 b = new Vector2D_F32();

		GeometryMath_F32.rotate( theta,a,b);

		float c = (float)Math.cos(theta);
		float s = (float)Math.sin(theta);


		float x = c*a.x - s*a.y;
		float y = s*a.x + c*a.y;

		assertEquals(x,b.x, GrlConstants.FLOAT_TEST_TOL );
		assertEquals(y,b.y, GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void rotate_2d_c_s() {
		Vector2D_F32 a = new Vector2D_F32( 1, 2 );
		float theta = 0.6f;

		Vector2D_F32 b = new Vector2D_F32();

		float c = (float)Math.cos(theta);
		float s = (float)Math.sin(theta);

		GeometryMath_F32.rotate( c,s,a,b);


		float x = c*a.x - s*a.y;
		float y = s*a.x + c*a.y;

		assertEquals(x,b.x, GrlConstants.FLOAT_TEST_TOL );
		assertEquals(y,b.y, GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void mult_3d_3d() {
		Vector3D_F32 a = new Vector3D_F32( -1, 2, 3 );
		DenseMatrix64F M = new DenseMatrix64F(3,3,true,1,2,3,4,5,6,7,8,9);
		Vector3D_F32 c = new Vector3D_F32();

		GeometryMath_F32.mult( M , a , c );

		assertEquals( 12 , c.getX() , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 24 , c.getY() , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 36 , c.getZ() , GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void mult_3d_2d() {
		Vector3D_F32 a = new Vector3D_F32( -1, 2, 3 );
		DenseMatrix64F M = new DenseMatrix64F(3,3,true,1,2,3,4,5,6,7,8,9);
		Vector2D_F32 c = new Vector2D_F32();

		GeometryMath_F32.mult( M , a , c );

		assertEquals( 12.0f/36.0f , c.getX() , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 24.0f/36.0f , c.getY() , GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void mult_2d_3d() {
		Vector3D_F32 a3 = new Vector3D_F32( -1, 2, 1 );
		DenseMatrix64F M = new DenseMatrix64F(3,3,true,1,2,3,4,5,6,7,8,9);
		Vector3D_F32 expected = new Vector3D_F32();

		GeometryMath_F32.mult( M , a3 , expected );

		Vector2D_F32 a2 = new Vector2D_F32( -1, 2 );
		Vector3D_F32 found = new Vector3D_F32();
		GeometryMath_F32.mult( M , a2 , found );

		assertEquals( expected.x , found.x , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( expected.y , found.y , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( expected.z , found.z , GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void mult_2d_2d() {
		Vector3D_F32 a3 = new Vector3D_F32( -1, 2, 1 );
		DenseMatrix64F M = new DenseMatrix64F(3,3,true,1,2,3,4,5,6,7,8,9);
		Vector3D_F32 expected = new Vector3D_F32();

		GeometryMath_F32.mult( M , a3 , expected );

		Vector2D_F32 a2 = new Vector2D_F32( -1, 2 );
		Vector2D_F32 found = new Vector2D_F32();
		GeometryMath_F32.mult( M , a2 , found );

		float z = expected.z;

		assertEquals( expected.x/z , found.x , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( expected.y/z , found.y , GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void multTran_3d_3d() {
		Vector3D_F32 a = new Vector3D_F32( -1, 2, 3 );
		DenseMatrix64F M = new DenseMatrix64F(3,3,true,1,2,3,4,5,6,7,8,9);
		Vector3D_F32 c = new Vector3D_F32();

		GeometryMath_F32.multTran( M , a , c );

		assertEquals( 28 , c.getX() , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 32 , c.getY() , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 36 , c.getZ() , GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void multTran_2d_3d() {
		Vector2D_F32 a = new Vector2D_F32( -1, 2 );
		DenseMatrix64F M = new DenseMatrix64F(3,3,true,1,2,3,4,5,6,7,8,9);
		Vector3D_F32 c = new Vector3D_F32();

		GeometryMath_F32.multTran( M , a , c );

		assertEquals( 14 , c.getX() , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 16 , c.getY() , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 18 , c.getZ() , GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void multCrossA_2D() {
		Point2D_F32 a = new Point2D_F32(3,2);
		DenseMatrix64F b = RandomMatrices.createRandom(3,3,rand);

		DenseMatrix64F a_hat = GeometryMath_F32.crossMatrix(a.x,a.y,1,null);
		DenseMatrix64F expected = new DenseMatrix64F(3,3);
		CommonOps.mult(a_hat,b,expected);

		DenseMatrix64F found = GeometryMath_F32.multCrossA(a,b,null);

		assertTrue(MatrixFeatures.isIdentical(expected,found,GrlConstants.FLOAT_TEST_TOL));
	}

	@Test
	public void multCrossA_3D() {
		Point3D_F32 a = new Point3D_F32(1,2,3);
		DenseMatrix64F b = RandomMatrices.createRandom(3,3,rand);

		DenseMatrix64F a_hat = GeometryMath_F32.crossMatrix(a.x,a.y,a.z,null);
		DenseMatrix64F expected = new DenseMatrix64F(3,3);
		CommonOps.mult(a_hat,b,expected);

		DenseMatrix64F found = GeometryMath_F32.multCrossA(a,b,null);

		assertTrue(MatrixFeatures.isIdentical(expected,found,GrlConstants.FLOAT_TEST_TOL));
	}

	@Test
	public void innerProd_3D() {
		Vector3D_F32 a = new Vector3D_F32( 2, -2, 3 );
		Vector3D_F32 b = new Vector3D_F32( 4, 3, 2 );
		DenseMatrix64F M = new DenseMatrix64F( 3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9 );

		float found = GeometryMath_F32.innerProd( a, M, b );

		assertEquals( 156, found, GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void innerProdTranM() {
		Vector3D_F32 a = new Vector3D_F32( 2, -2, 3 );
		Vector3D_F32 b = new Vector3D_F32( 4, 3, 2 );
		DenseMatrix64F M = new DenseMatrix64F( 3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9 );

		float found = GeometryMath_F32.innerProdTranM( a, M, b );

		assertEquals( 126, found, GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void innerProd_2D() {
		Vector2D_F32 a = new Vector2D_F32( 2, -2 );
		Vector2D_F32 b = new Vector2D_F32( 4, 3 );
		DenseMatrix64F M = new DenseMatrix64F( 3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9 );

		float found = GeometryMath_F32.innerProd( a, M, b );

		assertEquals( 13, found, GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void outerProd_3D() {
		Vector3D_F32 a = new Vector3D_F32( 2, -2 , 5);
		Vector3D_F32 b = new Vector3D_F32( 4, 3 , 9);
		DenseMatrix64F M = new DenseMatrix64F( 3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9 );

		DenseMatrix64F expected = new DenseMatrix64F(3,3,true,8 , 6 ,18 , -8 ,-6,-18,20,15,45);

		GeometryMath_F32.outerProd( a, b , M );

		assertTrue( MatrixFeatures.isIdentical(expected,M,GrlConstants.FLOAT_TEST_TOL) );
	}

	@Test
	public void addOuterProd_3D() {
		Vector3D_F32 a = new Vector3D_F32( 2, -2 , 5);
		Vector3D_F32 b = new Vector3D_F32( 4, 3 , 9);
		DenseMatrix64F A = new DenseMatrix64F( 3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9 );

		DenseMatrix64F found = new DenseMatrix64F( 3, 3);

		DenseMatrix64F expected = new DenseMatrix64F(3,3,true,-7,-4,-15,12 , 11,24,-13,-7,-36);

		GeometryMath_F32.addOuterProd( A , -1 , a, b , found );

		assertTrue( MatrixFeatures.isIdentical(expected,found,GrlConstants.FLOAT_TEST_TOL) );
	}

	@Test
	public void dot() {
		Vector3D_F32 a = new Vector3D_F32( 2, -2, 3 );
		Vector3D_F32 b = new Vector3D_F32( 4, 3, 2 );
		float found = GeometryMath_F32.dot( a, b );

		assertEquals( 8, found, GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void scale() {
		Vector3D_F32 a = new Vector3D_F32( 1, -2, 3 );
		GeometryMath_F32.scale( a, 2 );

		assertEquals( 2, a.x, GrlConstants.FLOAT_TEST_TOL );
		assertEquals( -4, a.y, GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 6, a.z, GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void changeSign() {
		Vector3D_F32 a = new Vector3D_F32( 1, -2, 3 );
		GeometryMath_F32.changeSign( a );

		assertEquals( -1, a.x, GrlConstants.FLOAT_TEST_TOL );
		assertEquals( 2, a.y, GrlConstants.FLOAT_TEST_TOL );
		assertEquals( -3, a.z, GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void toMatrix() {
		Vector3D_F32 a = new Vector3D_F32( 1, -2, 3 );
		DenseMatrix64F found = GeometryMath_F32.toMatrix(a,null);

		assertEquals(1,found.get(0),GrlConstants.FLOAT_TEST_TOL);
		assertEquals(-2,found.get(1),GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3,found.get(2),GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void toTuple3D() {
		DenseMatrix64F a = new DenseMatrix64F(3,1,true,1,-2,3);
		Vector3D_F32 b = new Vector3D_F32();
		GeometryMath_F32.toTuple3D(a, b);

		assertEquals(b.x,a.get(0),GrlConstants.FLOAT_TEST_TOL);
		assertEquals(b.y,a.get(1),GrlConstants.FLOAT_TEST_TOL);
		assertEquals(b.z,a.get(2),GrlConstants.FLOAT_TEST_TOL);
	}
}
