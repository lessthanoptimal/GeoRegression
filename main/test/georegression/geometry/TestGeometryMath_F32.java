/*
 * Copyright (C) 2011-2017, Peter Abeles. All Rights Reserved.
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
import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector2D_F32;
import georegression.struct.point.Vector3D_F32;
import org.ejml.data.RowMatrix_F32;
import org.ejml.ops.CommonOps_R32;
import org.ejml.ops.MatrixFeatures_R32;
import org.ejml.ops.RandomMatrices_R32;
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
		RowMatrix_F32 V = GeometryMath_F32.crossMatrix( a, b, c, null );

		GeometryMath_F32.mult( V, x, found1 );

		assertEquals( found0.x, found1.x, GrlConstants.TEST_F32);
		assertEquals( found0.y, found1.y, GrlConstants.TEST_F32);
		assertEquals( found0.z, found1.z, GrlConstants.TEST_F32);
	}

	/**
	 * Sees if both crossMatrix functions produce the same output
	 */
	@Test
	public void crossMatrix_sameOut() {
		float a = 1.1f, b = -0.5f, c = 2.2f;

		Vector3D_F32 v = new Vector3D_F32( a, b, c );

		RowMatrix_F32 V1 = GeometryMath_F32.crossMatrix( v, null );
		RowMatrix_F32 V2 = GeometryMath_F32.crossMatrix( a, b, c, null );

		assertTrue( MatrixFeatures_R32.isIdentical( V1 ,V2 , GrlConstants.TEST_F32));
	}

	@Test
	public void cross_3d_3d() {
		Vector3D_F32 a = new Vector3D_F32( 1, 0, 0 );
		Vector3D_F32 b = new Vector3D_F32( 0, 1, 0 );
		Vector3D_F32 c = new Vector3D_F32();

		GeometryMath_F32.cross( a, b, c );

		assertEquals( 0, c.x, GrlConstants.TEST_F32);
		assertEquals( 0, c.y, GrlConstants.TEST_F32);
		assertEquals( 1, c.z, GrlConstants.TEST_F32);

		GeometryMath_F32.cross( b, a, c );

		assertEquals( 0, c.x, GrlConstants.TEST_F32);
		assertEquals( 0, c.y, GrlConstants.TEST_F32);
		assertEquals( -1, c.z, GrlConstants.TEST_F32);
	}

	@Test
	public void cross_3d_3d_float() {
		Vector3D_F32 a = new Vector3D_F32( 1, 2, 3 );
		Vector3D_F32 b = new Vector3D_F32( 0.5f, 1.5f, -3 );
		Vector3D_F32 expected = new Vector3D_F32();
		Vector3D_F32 found = new Vector3D_F32();

		GeometryMath_F32.cross( a, b, expected );
		GeometryMath_F32.cross( a.x, a.y, a.z, b.x, b.y, b.z, found );

		assertEquals( expected.x, found.x, GrlConstants.TEST_F32);
		assertEquals( expected.y, found.y, GrlConstants.TEST_F32);
		assertEquals( expected.z, found.z, GrlConstants.TEST_F32);
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

		assertEquals( expected.x, found.x , GrlConstants.TEST_F32);
		assertEquals( expected.y, found.y , GrlConstants.TEST_F32);
		assertEquals( expected.z, found.z , GrlConstants.TEST_F32);
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

		assertEquals( expected.x, found.x , GrlConstants.TEST_F32);
		assertEquals( expected.y, found.y , GrlConstants.TEST_F32);
		assertEquals( expected.z, found.z , GrlConstants.TEST_F32);
	}

	@Test
	public void add() {
		Vector3D_F32 a = new Vector3D_F32( 1, 2, 3 );
		Vector3D_F32 b = new Vector3D_F32( 3, 1, 4 );
		Vector3D_F32 c = new Vector3D_F32();

		GeometryMath_F32.add( a , b , c );

		assertEquals( 4 , c.getX() , GrlConstants.TEST_F32);
		assertEquals( 3 , c.getY() , GrlConstants.TEST_F32);
		assertEquals( 7 , c.getZ() , GrlConstants.TEST_F32);
	}

	@Test
	public void add_scale() {
		Vector3D_F32 a = new Vector3D_F32( 1, 2, 3 );
		Vector3D_F32 b = new Vector3D_F32( 3, 1, 4 );
		Vector3D_F32 c = new Vector3D_F32();

		GeometryMath_F32.add( 2, a , -1 , b , c );

		assertEquals( -1 , c.getX() , GrlConstants.TEST_F32);
		assertEquals(  3 , c.getY() , GrlConstants.TEST_F32);
		assertEquals(  2 , c.getZ() , GrlConstants.TEST_F32);
	}

	@Test
	public void addMult() {
		Vector3D_F32 a = new Vector3D_F32( 1, 2, 3 );
		Vector3D_F32 b = new Vector3D_F32( 2, 3, 4 );
		Vector3D_F32 c = new Vector3D_F32();
		RowMatrix_F32 M = new RowMatrix_F32( 3,3,true,1,1,1,1,1,1,1,1,1);

		GeometryMath_F32.addMult( a , M , b , c );

		assertEquals( 10 , c.getX() , GrlConstants.TEST_F32);
		assertEquals( 11 , c.getY() , GrlConstants.TEST_F32);
		assertEquals( 12 , c.getZ() , GrlConstants.TEST_F32);
	}

	@Test
	public void sub() {
		Vector3D_F32 a = new Vector3D_F32( 1, 2, 3 );
		Vector3D_F32 b = new Vector3D_F32( 3, 1, 4 );
		Vector3D_F32 c = new Vector3D_F32();

		GeometryMath_F32.sub( a , b , c );

		assertEquals( -2 , c.getX() , GrlConstants.TEST_F32);
		assertEquals( 1 , c.getY() , GrlConstants.TEST_F32);
		assertEquals( -1 , c.getZ() , GrlConstants.TEST_F32);
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

		assertEquals(x,b.x, GrlConstants.TEST_F32);
		assertEquals(y,b.y, GrlConstants.TEST_F32);
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

		assertEquals(x,b.x, GrlConstants.TEST_F32);
		assertEquals(y,b.y, GrlConstants.TEST_F32);
	}

	@Test
	public void mult_3d_3d() {
		Vector3D_F32 a = new Vector3D_F32( -1, 2, 3 );
		RowMatrix_F32 M = new RowMatrix_F32(3,3,true,1,2,3,4,5,6,7,8,9);
		Vector3D_F32 c = new Vector3D_F32();

		GeometryMath_F32.mult( M , a , c );

		assertEquals( 12 , c.getX() , GrlConstants.TEST_F32);
		assertEquals( 24 , c.getY() , GrlConstants.TEST_F32);
		assertEquals( 36 , c.getZ() , GrlConstants.TEST_F32);
	}

	@Test
	public void mult_3d_2d() {
		Vector3D_F32 a = new Vector3D_F32( -1, 2, 3 );
		RowMatrix_F32 M = new RowMatrix_F32(3,3,true,1,2,3,4,5,6,7,8,9);
		Vector2D_F32 c = new Vector2D_F32();

		GeometryMath_F32.mult( M , a , c );

		assertEquals( 12.0f/36.0f , c.getX() , GrlConstants.TEST_F32);
		assertEquals( 24.0f/36.0f , c.getY() , GrlConstants.TEST_F32);
	}

	@Test
	public void mult_2d_3d() {
		Vector3D_F32 a3 = new Vector3D_F32( -1, 2, 1 );
		RowMatrix_F32 M = new RowMatrix_F32(3,3,true,1,2,3,4,5,6,7,8,9);
		Vector3D_F32 expected = new Vector3D_F32();

		GeometryMath_F32.mult( M , a3 , expected );

		Vector2D_F32 a2 = new Vector2D_F32( -1, 2 );
		Vector3D_F32 found = new Vector3D_F32();
		GeometryMath_F32.mult( M , a2 , found );

		assertEquals( expected.x , found.x , GrlConstants.TEST_F32);
		assertEquals( expected.y , found.y , GrlConstants.TEST_F32);
		assertEquals( expected.z , found.z , GrlConstants.TEST_F32);
	}

	@Test
	public void mult_2d_2d() {
		Vector3D_F32 a3 = new Vector3D_F32( -1, 2, 1 );
		RowMatrix_F32 M = new RowMatrix_F32(3,3,true,1,2,3,4,5,6,7,8,9);
		Vector3D_F32 expected = new Vector3D_F32();

		GeometryMath_F32.mult( M , a3 , expected );

		Vector2D_F32 a2 = new Vector2D_F32( -1, 2 );
		Vector2D_F32 found = new Vector2D_F32();
		GeometryMath_F32.mult( M , a2 , found );

		float z = expected.z;

		assertEquals( expected.x/z , found.x , GrlConstants.TEST_F32);
		assertEquals( expected.y/z , found.y , GrlConstants.TEST_F32);
	}

	@Test
	public void multTran_3d_3d() {
		Vector3D_F32 a = new Vector3D_F32( -1, 2, 3 );
		RowMatrix_F32 M = new RowMatrix_F32(3,3,true,1,2,3,4,5,6,7,8,9);
		Vector3D_F32 c = new Vector3D_F32();

		GeometryMath_F32.multTran( M , a , c );

		assertEquals( 28 , c.getX() , GrlConstants.TEST_F32);
		assertEquals( 32 , c.getY() , GrlConstants.TEST_F32);
		assertEquals( 36 , c.getZ() , GrlConstants.TEST_F32);
	}

	@Test
	public void multTran_2d_3d() {
		Vector2D_F32 a = new Vector2D_F32( -1, 2 );
		RowMatrix_F32 M = new RowMatrix_F32(3,3,true,1,2,3,4,5,6,7,8,9);
		Vector3D_F32 c = new Vector3D_F32();

		GeometryMath_F32.multTran( M , a , c );

		assertEquals( 14 , c.getX() , GrlConstants.TEST_F32);
		assertEquals( 16 , c.getY() , GrlConstants.TEST_F32);
		assertEquals( 18 , c.getZ() , GrlConstants.TEST_F32);
	}

	@Test
	public void multCrossA_2D() {
		Point2D_F32 a = new Point2D_F32(3,2);
		RowMatrix_F32 b = RandomMatrices_R32.createRandom(3,3,rand);

		RowMatrix_F32 a_hat = GeometryMath_F32.crossMatrix(a.x,a.y,1,null);
		RowMatrix_F32 expected = new RowMatrix_F32(3,3);
		CommonOps_R32.mult(a_hat,b,expected);

		RowMatrix_F32 found = GeometryMath_F32.multCrossA(a,b,null);

		assertTrue(MatrixFeatures_R32.isIdentical(expected,found,GrlConstants.TEST_F32));
	}

	@Test
	public void multCrossA_3D() {
		Point3D_F32 a = new Point3D_F32(1,2,3);
		RowMatrix_F32 b = RandomMatrices_R32.createRandom(3,3,rand);

		RowMatrix_F32 a_hat = GeometryMath_F32.crossMatrix(a.x,a.y,a.z,null);
		RowMatrix_F32 expected = new RowMatrix_F32(3,3);
		CommonOps_R32.mult(a_hat,b,expected);

		RowMatrix_F32 found = GeometryMath_F32.multCrossA(a,b,null);

		assertTrue(MatrixFeatures_R32.isIdentical(expected,found,GrlConstants.TEST_F32));
	}

	@Test
	public void innerProd_3D() {
		Vector3D_F32 a = new Vector3D_F32( 2, -2, 3 );
		Vector3D_F32 b = new Vector3D_F32( 4, 3, 2 );
		RowMatrix_F32 M = new RowMatrix_F32( 3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9 );

		float found = GeometryMath_F32.innerProd( a, M, b );

		assertEquals( 156, found, GrlConstants.TEST_F32);
	}

	@Test
	public void innerProdTranM() {
		Vector3D_F32 a = new Vector3D_F32( 2, -2, 3 );
		Vector3D_F32 b = new Vector3D_F32( 4, 3, 2 );
		RowMatrix_F32 M = new RowMatrix_F32( 3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9 );

		float found = GeometryMath_F32.innerProdTranM( a, M, b );

		assertEquals( 126, found, GrlConstants.TEST_F32);
	}

	@Test
	public void innerProd_2D() {
		Vector2D_F32 a = new Vector2D_F32( 2, -2 );
		Vector2D_F32 b = new Vector2D_F32( 4, 3 );
		RowMatrix_F32 M = new RowMatrix_F32( 3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9 );

		float found = GeometryMath_F32.innerProd( a, M, b );

		assertEquals( 13, found, GrlConstants.TEST_F32);
	}

	@Test
	public void outerProd_3D() {
		Vector3D_F32 a = new Vector3D_F32( 2, -2 , 5);
		Vector3D_F32 b = new Vector3D_F32( 4, 3 , 9);
		RowMatrix_F32 M = new RowMatrix_F32( 3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9 );

		RowMatrix_F32 expected = new RowMatrix_F32(3,3,true,8 , 6 ,18 , -8 ,-6,-18,20,15,45);

		GeometryMath_F32.outerProd( a, b , M );

		assertTrue( MatrixFeatures_R32.isIdentical(expected,M,GrlConstants.TEST_F32) );
	}

	@Test
	public void addOuterProd_3D() {
		Vector3D_F32 a = new Vector3D_F32( 2, -2 , 5);
		Vector3D_F32 b = new Vector3D_F32( 4, 3 , 9);
		RowMatrix_F32 A = new RowMatrix_F32( 3, 3, true, 1, 2, 3, 4, 5, 6, 7, 8, 9 );

		RowMatrix_F32 found = new RowMatrix_F32( 3, 3);

		RowMatrix_F32 expected = new RowMatrix_F32(3,3,true,-7,-4,-15,12 , 11,24,-13,-7,-36);

		GeometryMath_F32.addOuterProd( A , -1 , a, b , found );

		assertTrue( MatrixFeatures_R32.isIdentical(expected,found,GrlConstants.TEST_F32) );
	}

	@Test
	public void dot() {
		Vector3D_F32 a = new Vector3D_F32( 2, -2, 3 );
		Vector3D_F32 b = new Vector3D_F32( 4, 3, 2 );
		float found = GeometryMath_F32.dot( a, b );

		assertEquals( 8, found, GrlConstants.TEST_F32);
	}

	@Test
	public void scale() {
		Vector3D_F32 a = new Vector3D_F32( 1, -2, 3 );
		GeometryMath_F32.scale( a, 2 );

		assertEquals( 2, a.x, GrlConstants.TEST_F32);
		assertEquals( -4, a.y, GrlConstants.TEST_F32);
		assertEquals( 6, a.z, GrlConstants.TEST_F32);
	}

	@Test
	public void divide() {
		Vector3D_F32 a = new Vector3D_F32( 1, -2, 3 );
		GeometryMath_F32.divide( a, 2 );

		assertEquals( 0.5f, a.x, GrlConstants.TEST_F32);
		assertEquals( -1.0f, a.y, GrlConstants.TEST_F32);
		assertEquals( 1.5f, a.z, GrlConstants.TEST_F32);
	}


	@Test
	public void changeSign() {
		Vector3D_F32 a = new Vector3D_F32( 1, -2, 3 );
		GeometryMath_F32.changeSign( a );

		assertEquals( -1, a.x, GrlConstants.TEST_F32);
		assertEquals( 2, a.y, GrlConstants.TEST_F32);
		assertEquals( -3, a.z, GrlConstants.TEST_F32);
	}

	@Test
	public void toMatrix() {
		Vector3D_F32 a = new Vector3D_F32( 1, -2, 3 );
		RowMatrix_F32 found = GeometryMath_F32.toMatrix(a,null);

		assertEquals(1,found.get(0),GrlConstants.TEST_F32);
		assertEquals(-2,found.get(1),GrlConstants.TEST_F32);
		assertEquals(3,found.get(2),GrlConstants.TEST_F32);
	}

	@Test
	public void toTuple3D() {
		RowMatrix_F32 a = new RowMatrix_F32(3,1,true,1,-2,3);
		Vector3D_F32 b = new Vector3D_F32();
		GeometryMath_F32.toTuple3D(a, b);

		assertEquals(b.x,a.get(0),GrlConstants.TEST_F32);
		assertEquals(b.y,a.get(1),GrlConstants.TEST_F32);
		assertEquals(b.z,a.get(2),GrlConstants.TEST_F32);
	}
}
