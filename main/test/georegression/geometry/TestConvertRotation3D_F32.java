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

import georegression.metric.UtilAngle;
import georegression.misc.GrlConstants;
import georegression.misc.test.GeometryUnitTest;
import georegression.struct.EulerType;
import georegression.struct.point.Point3D_F32;
import georegression.struct.so.Quaternion_F32;
import georegression.struct.so.Rodrigues_F32;
import org.ejml.UtilEjml;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps_D64;
import org.ejml.ops.MatrixFeatures_D64;
import org.ejml.ops.RandomMatrices_D64;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;




/**
 * @author Peter Abeles
 */
public class TestConvertRotation3D_F32 {

	Random rand = new Random( 234234 );

	@Test
	public void rodriguesToMatrix() {
		DenseMatrix64F rotZ = ConvertRotation3D_F32.rotZ( 0.5f, null );

		Rodrigues_F32 r = new Rodrigues_F32( 0.5f, 0, 0, 1 );

		DenseMatrix64F rod = ConvertRotation3D_F32.rodriguesToMatrix( r, null );

		assertTrue( MatrixFeatures_D64.isIdentical( rotZ, rod, GrlConstants.FLOAT_TEST_TOL ) );
	}

	@Test
	public void rodriguesToEuler() throws InvocationTargetException, IllegalAccessException {
		somethingToEulerTest("rodriguesToEuler",rand);
	}

	public static void rodriguesToEuler(EulerType type , float rotA , float rotB , float rotC )
	{
		DenseMatrix64F expected = ConvertRotation3D_F32.eulerToMatrix(type,rotA,rotB,rotC,null);

		Rodrigues_F32 rod = ConvertRotation3D_F32.matrixToRodrigues(expected,(Rodrigues_F32)null);

		float[] euler = ConvertRotation3D_F32.rodriguesToEuler(rod,type,null);
		DenseMatrix64F found = ConvertRotation3D_F32.eulerToMatrix(type,euler[0],euler[1],euler[2],null);

		DenseMatrix64F difference = new DenseMatrix64F(3,3);
		CommonOps_D64.multTransB(expected,found,difference);
		assertTrue(MatrixFeatures_D64.isIdentity(difference,Math.sqrt(GrlConstants.FLOAT_TEST_TOL)));
	}

	@Test
	public void rodriguesToQuaternion() {
		Rodrigues_F32 rod = new Rodrigues_F32(-1.5f,1,3,-4);

		Quaternion_F32 quat = ConvertRotation3D_F32.rodriguesToQuaternion(rod, null);

		DenseMatrix64F A = ConvertRotation3D_F32.quaternionToMatrix(quat, null);
		DenseMatrix64F B = ConvertRotation3D_F32.rodriguesToMatrix(rod, null);

		DenseMatrix64F C = new DenseMatrix64F(3,3);
		CommonOps_D64.multTransA(A,B,C);

		assertTrue(MatrixFeatures_D64.isIdentity(C, GrlConstants.FLOAT_TEST_TOL));
	}

	@Test
	public void quaternionToRodrigues() {
		Quaternion_F32 quat = new Quaternion_F32(0.6f,2,3,-1);
		quat.normalize();

		Rodrigues_F32 rod = ConvertRotation3D_F32.quaternionToRodrigues(quat,null);

		quat.normalize();
		DenseMatrix64F A = ConvertRotation3D_F32.quaternionToMatrix(quat, null);
		DenseMatrix64F B = ConvertRotation3D_F32.rodriguesToMatrix(rod, null);

		DenseMatrix64F C = new DenseMatrix64F(3,3);
		CommonOps_D64.multTransA(A,B,C);

		assertTrue(MatrixFeatures_D64.isIdentity(C, GrlConstants.FLOAT_TEST_TOL));
	}

	@Test
	public void quaternionToEuler() throws InvocationTargetException, IllegalAccessException {
		somethingToEulerTest("quaternionToEuler",rand);
	}

	public static void quaternionToEuler(EulerType type , float rotA , float rotB , float rotC )
	{
		DenseMatrix64F expected = ConvertRotation3D_F32.eulerToMatrix(type,rotA,rotB,rotC,null);

		Quaternion_F32 q = ConvertRotation3D_F32.matrixToQuaternion(expected,null);
		float euler[] = ConvertRotation3D_F32.quaternionToEuler(q,type,null);

		DenseMatrix64F found = ConvertRotation3D_F32.eulerToMatrix(type,euler[0],euler[1],euler[2],null);

		DenseMatrix64F difference = new DenseMatrix64F(3,3);
		CommonOps_D64.multTransB(expected,found,difference);
		assertTrue(MatrixFeatures_D64.isIdentity(difference,Math.sqrt(GrlConstants.FLOAT_TEST_TOL)));
	}

	@Test
	public void matrixToQuaternion() {

		float pid2 = (float)Math.PI/2.0f;

		matrixToQuaternion(ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ,0,0,0,null));

		// single axis rotations
		matrixToQuaternion(ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ,pid2,0,0,null));
		matrixToQuaternion(ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ,0,pid2,0,null));
		matrixToQuaternion(ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ,0,0,pid2,null));
		matrixToQuaternion(ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ,-pid2,0,0,null));
		matrixToQuaternion(ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ,0,-pid2,0,null));
		matrixToQuaternion(ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ,0,0,-pid2,null));

		// two axis rotations which could be pathological
		matrixToQuaternion(ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ,pid2,pid2,0,null));
		matrixToQuaternion(ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ,pid2,0,pid2,null));

		matrixToQuaternion(ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ,pid2,pid2,0,null));
		matrixToQuaternion(ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ,0,pid2,pid2,null));

		matrixToQuaternion(ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ,pid2,0,pid2,null));
		matrixToQuaternion(ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ,0,pid2,pid2,null));

		for (int i = 0; i < 100; i++) {
			float rotX = 2.0f*rand.nextFloat() * (float)Math.PI - (float)Math.PI;
			float rotY = 2.0f*rand.nextFloat() * (float)Math.PI - (float)Math.PI;
			float rotZ = 2.0f*rand.nextFloat() * (float)Math.PI - (float)Math.PI;

			DenseMatrix64F R = ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ,rotX,rotY,rotZ,null);
			matrixToQuaternion(R);
		}
	}

	public void matrixToQuaternion( DenseMatrix64F R ) {
		Quaternion_F32 q = ConvertRotation3D_F32.matrixToQuaternion(R,null);
		q.normalize();
		DenseMatrix64F found = ConvertRotation3D_F32.quaternionToMatrix(q,null);

		DenseMatrix64F result = new DenseMatrix64F(3,3);
		CommonOps_D64.multTransB(R,found,result);

		assertTrue(MatrixFeatures_D64.isIdentity(result,Math.sqrt(GrlConstants.FLOAT_TEST_TOL)));
	}

	@Test
	public void matrixToRodrigues() {
		// create the rotation axis
		for( int i = 1; i < 20; i++ ) {
			float angle = i * (float)Math.PI / 20;
			checkMatrixToRodrigues( new Rodrigues_F32( angle, 0.1f, 2, 6 ) );
			checkMatrixToRodrigues( new Rodrigues_F32( angle, 1, 0, 0 ) );
			checkMatrixToRodrigues( new Rodrigues_F32( angle, 1, 1, 1 ) );
			checkMatrixToRodrigues( new Rodrigues_F32( angle, -1, -1, -1 ) );
		}

		// see how well it handles underflow
		checkMatrixToRodrigues( new Rodrigues_F32( 50*GrlConstants.FLOAT_TEST_TOL, -1, -1, -1 ) );

		// test known pathological cases
		checkMatrixToRodrigues( new Rodrigues_F32( 0, 1, 1, 1 ), new Rodrigues_F32( 0, 1, 0, 0 ) );
		checkMatrixToRodrigues( new Rodrigues_F32( (float)Math.PI / 2, 1, 1, 1 ), new Rodrigues_F32( (float)Math.PI/2, 1, 1, 1 ) );
		checkMatrixToRodrigues( new Rodrigues_F32( (float)Math.PI, 1, 1, 1 ), new Rodrigues_F32( (float)Math.PI, 1, 1, 1 ) );
		checkMatrixToRodrigues( new Rodrigues_F32( - (float)Math.PI, 1, 1, 1 ), new Rodrigues_F32( (float)Math.PI, 1, 1, 1 ) );

		// edge case where diagonals sum up to 1
		checkMatrixToRodrigues( new Rodrigues_F32( (float)Math.PI, 1, 0, 0 ) );
		checkMatrixToRodrigues( new Rodrigues_F32( (float)Math.PI, 0, 1, 0 ) );
		checkMatrixToRodrigues( new Rodrigues_F32( (float)Math.PI, 0, 0, 1 ));

		checkMatrixToRodrigues( (float)Math.PI, (float)Math.PI/2,0);
		checkMatrixToRodrigues( (float)Math.PI, (float)-Math.PI/2,0);
		checkMatrixToRodrigues( (float)Math.PI,0, (float)Math.PI/2);
		checkMatrixToRodrigues( (float)Math.PI,0, (float)-Math.PI/2);
		checkMatrixToRodrigues(0, (float)Math.PI, (float)Math.PI/2);
		checkMatrixToRodrigues(0, (float)Math.PI, (float)-Math.PI/2);;
	}

	private void checkMatrixToRodrigues( Rodrigues_F32 rodInput ) {
		// create the matrix using rodrigues
		DenseMatrix64F rod = ConvertRotation3D_F32.rodriguesToMatrix( rodInput, null );

		// see if the vectors are the same
		Rodrigues_F32 found = ConvertRotation3D_F32.matrixToRodrigues( rod, (Rodrigues_F32)null );

		// if the lines are parallel the dot product will be 1 or -1
		float dot = found.unitAxisRotation.dot( rodInput.unitAxisRotation);
		assertEquals( 1, (float)Math.abs( dot ), GrlConstants.FLOAT_TEST_TOL );

		// if the rotation vector is in the opposite direction then the found angle will be
		// the negative of the input.  both are equivalent
		assertTrue(UtilAngle.dist(rodInput.theta * dot, found.theta) <= GrlConstants.FLOAT_TEST_TOL);
	}

	private void checkMatrixToRodrigues( float eulerX , float eulerY , float eulerZ ) {

		DenseMatrix64F M = ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ,eulerX,eulerY,eulerZ,null);
		Rodrigues_F32 rod = ConvertRotation3D_F32.matrixToRodrigues(M, (Rodrigues_F32)null);
		DenseMatrix64F found = ConvertRotation3D_F32.rodriguesToMatrix(rod,null);
		assertTrue(MatrixFeatures_D64.isIdentical(M,found,1e-6));
	}

	private void checkMatrixToRodrigues( Rodrigues_F32 input,
										 Rodrigues_F32 expected ) {

		// create the matrix using rodrigues
		DenseMatrix64F rod = ConvertRotation3D_F32.rodriguesToMatrix( input, null );

		// see if the vectors are the same
		Rodrigues_F32 found = ConvertRotation3D_F32.matrixToRodrigues( rod, (Rodrigues_F32)null );

		// if the lines are parallel the dot product will be 1 or -1
		assertEquals( 1, (float)Math.abs( found.unitAxisRotation.dot( expected.unitAxisRotation) ), GrlConstants.FLOAT_TEST_TOL );

		// if the rotation vector is in the opposite direction then the found angle will be
		// the negative of the input.  both are equivalent
		assertEquals( expected.theta, found.theta, 10.0f*GrlConstants.FLOAT_TEST_TOL );
	}

	/**
	 * A found test case where it failed
	 */
	@Test
	public void matrixToRodrigues_case0() {
		DenseMatrix64F R = UtilEjml.parseD64(
						"1.00000000000000000000e+00f -5.42066399999221260000e-14f -3.16267800000013500000e-13f \n" +
						"5.42066400000000000000e-14f 1.00000000000000040000e+00f 2.46136444559397200000e-13f \n" +
						"3.16267800000000000000e-13f -2.46191955710628460000e-13f 1.00000000000000040000e+00f", 3);

		Rodrigues_F32 found = ConvertRotation3D_F32.matrixToRodrigues( R, (Rodrigues_F32)null );

		assertEquals(0,found.getTheta(),GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void matrixToRodrigues_case1() {
		DenseMatrix64F R = UtilEjml.parseD64(
						"0.99999999999999000000e+00f -5.42066399999221260000e-14f -3.16267800000013500000e-13f \n" +
						"5.42066400000000000000e-14f 0.99999999999999000000e+00f 2.46136444559397200000e-13f \n" +
						"3.16267800000000000000e-13f -2.46191955710628460000e-13f 0.99999999999999000000e+00f", 3);

		Rodrigues_F32 found = ConvertRotation3D_F32.matrixToRodrigues( R, (Rodrigues_F32)null );

		assertEquals(0,found.getTheta(),50*GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void rotX() {
		Point3D_F32 pt_y = new Point3D_F32( 0, 1.5f, 0 );
		Point3D_F32 pt_z = new Point3D_F32( 0, 0, 1.5f );

		DenseMatrix64F R = ConvertRotation3D_F32.rotX( (float)Math.PI / 2.0f, null );

		GeometryMath_F32.mult( R, pt_y, pt_y );
		GeometryMath_F32.mult( R, pt_z, pt_z );

		assertTrue( pt_y.isIdentical( 0, 0, 1.5f, GrlConstants.FLOAT_TEST_TOL ) );
		assertTrue( pt_z.isIdentical( 0, -1.5f, 0, GrlConstants.FLOAT_TEST_TOL ) );
	}

	@Test
	public void rotY() {
		Point3D_F32 pt_x = new Point3D_F32( 1.5f, 0, 0 );
		Point3D_F32 pt_z = new Point3D_F32( 0, 0, 1.5f );

		DenseMatrix64F R = ConvertRotation3D_F32.rotY( (float)Math.PI / 2.0f, null );

		GeometryMath_F32.mult( R, pt_x, pt_x );
		GeometryMath_F32.mult( R, pt_z, pt_z );

		assertTrue( pt_x.isIdentical( 0, 0, -1.5f, GrlConstants.FLOAT_TEST_TOL ) );
		assertTrue( pt_z.isIdentical( 1.5f, 0, 0, GrlConstants.FLOAT_TEST_TOL ) );
	}

	@Test
	public void rotZ() {
		Point3D_F32 pt_x = new Point3D_F32( 1.5f, 0, 0 );
		Point3D_F32 pt_y = new Point3D_F32( 0, 1.5f, 0 );

		DenseMatrix64F R = ConvertRotation3D_F32.rotZ( (float)Math.PI / 2.0f, null );

		GeometryMath_F32.mult( R, pt_x, pt_x );
		GeometryMath_F32.mult( R, pt_y, pt_y );

		assertTrue( pt_x.isIdentical( 0, 1.5f, 0, GrlConstants.FLOAT_TEST_TOL ) );
		assertTrue( pt_y.isIdentical( -1.5f, 0, 0, GrlConstants.FLOAT_TEST_TOL ) );
	}

	@Test
	public void matrixToEuler() throws InvocationTargetException, IllegalAccessException {
		somethingToEulerTest("matrixToEuler",rand);
	}

	public static void matrixToEuler(EulerType type , float rotA , float rotB , float rotC )
	{
		DenseMatrix64F expected = ConvertRotation3D_F32.eulerToMatrix(type,rotA,rotB,rotC,null);

		float euler[] = ConvertRotation3D_F32.matrixToEuler(expected,type,(float[])null);

		DenseMatrix64F found = ConvertRotation3D_F32.eulerToMatrix(type,euler[0],euler[1],euler[2],null);

		DenseMatrix64F difference = new DenseMatrix64F(3,3);
		CommonOps_D64.multTransB(expected,found,difference);
		assertTrue(MatrixFeatures_D64.isIdentity(difference,Math.sqrt(GrlConstants.FLOAT_TEST_TOL)));
	}

	/**
	 * Creates a random matrix and sees if the approximation is a valid rotation matrix
	 */
	@Test
	public void approximateRotationMatrix_random() {
		DenseMatrix64F Q = RandomMatrices_D64.createRandom( 3, 3, rand );

		DenseMatrix64F R = ConvertRotation3D_F32.approximateRotationMatrix( Q, null );

		assertTrue( MatrixFeatures_D64.isOrthogonal( R, GrlConstants.FLOAT_TEST_TOL ) );
	}

	/**
	 * Create a rotation matrix and see if the exact same matrix is returned
	 */
	@Test
	public void approximateRotationMatrix_nochange() {
		DenseMatrix64F Q = RandomMatrices_D64.createOrthogonal( 3, 3, rand );

		DenseMatrix64F R = ConvertRotation3D_F32.approximateRotationMatrix( Q, null );

		assertTrue( MatrixFeatures_D64.isIdentical( Q, R, GrlConstants.FLOAT_TEST_TOL ) );
	}

	@Test
	public void eulerToMatrix() throws InvocationTargetException, IllegalAccessException {
		somethingToEulerTest("eulerToMatrix",rand);
	}

	public static void eulerToMatrix(EulerType type , float rotA , float rotB , float rotC ) {

		DenseMatrix64F matA = rotateAxis(type.getAxisA(),rotA);
		DenseMatrix64F matB = rotateAxis(type.getAxisB(),rotB);
		DenseMatrix64F matC = rotateAxis(type.getAxisC(),rotC);

		DenseMatrix64F matEuler = ConvertRotation3D_F32.eulerToMatrix(type,rotA,rotB,rotC,null);

		Point3D_F32 a = new Point3D_F32(1,2,3);

		Point3D_F32 tmp = new Point3D_F32();
		Point3D_F32 expected = new Point3D_F32();
		Point3D_F32 found = new Point3D_F32();

		GeometryMath_F32.mult(matA,a,expected);
		GeometryMath_F32.mult(matB,expected,tmp);
		GeometryMath_F32.mult(matC,tmp,expected);

		GeometryMath_F32.mult(matEuler,a,found);

		assertTrue(expected.distance(found) < GrlConstants.FLOAT_TEST_TOL);
	}

	private static DenseMatrix64F rotateAxis( int which , float angle ) {
		if( which == 0 )
			return ConvertRotation3D_F32.rotX(angle,null);
		else if( which == 1 )
			return ConvertRotation3D_F32.rotY(angle,null);
		else
			return ConvertRotation3D_F32.rotZ(angle,null);
	}

	@Test
	public void eulerToQuaternion() throws InvocationTargetException, IllegalAccessException {
		somethingToEulerTest("eulerToQuaternion",rand);
	}

	public static void eulerToQuaternion(EulerType type , float rotA , float rotB , float rotC ) {
		DenseMatrix64F expected = ConvertRotation3D_F32.eulerToMatrix(type,rotA,rotB,rotC,null);

		Quaternion_F32 q = new Quaternion_F32();
		ConvertRotation3D_F32.eulerToQuaternion(type,rotA,rotB,rotC,q);

		DenseMatrix64F found = ConvertRotation3D_F32.quaternionToMatrix(q,null);

		DenseMatrix64F result = new DenseMatrix64F(3,3);
		CommonOps_D64.multTransB(expected,found,result);
		assertTrue(MatrixFeatures_D64.isIdentity(result, (float)Math.sqrt(GrlConstants.FLOAT_TEST_TOL)));
	}

	/**
	 * Tests quaternions using the following property:
	 * <p/>
	 * q = cos(a/2) + u*sin(a/2)
	 * <p/>
	 * where 'a' is the angle of rotation, u is the unit axis of rotation.
	 */
	@Test
	public void quaternionToMatrix() {
		// rotate around z-axis 90 degrees
		Quaternion_F32 q = ConvertRotation3D_F32.rodriguesToQuaternion( new Rodrigues_F32( (float)Math.PI / 2.0f, 0, 0, 1 ), null );

		DenseMatrix64F R = ConvertRotation3D_F32.quaternionToMatrix( q, null );

		Point3D_F32 p = new Point3D_F32( 1, 0, 0 );
		GeometryMath_F32.mult( R, p, p );
		GeometryUnitTest.assertEquals( p, 0, 1, 0, GrlConstants.FLOAT_TEST_TOL );


		// rotate around y-axis 90 degrees
		q = ConvertRotation3D_F32.rodriguesToQuaternion( new Rodrigues_F32( (float)Math.PI / 2.0f, 0, 1, 0 ), null );
		q.normalize();

		R = ConvertRotation3D_F32.quaternionToMatrix( q, R );

		p.set( 1, 0, 0 );
		GeometryMath_F32.mult( R, p, p );
		GeometryUnitTest.assertEquals( p, 0, 0, -1, GrlConstants.FLOAT_TEST_TOL );

		for (int i = 0; i < 30; i++) {
			Rodrigues_F32 rod = new Rodrigues_F32();
			rod.theta = (float)Math.PI*(2*rand.nextFloat()-1);
			rod.setParamVector(rand.nextFloat()-0.5f,rand.nextFloat()-0.5f,rand.nextFloat()-0.5f);
			rod.unitAxisRotation.normalize();

			q = ConvertRotation3D_F32.rodriguesToQuaternion( rod, null );
			q.normalize();
			DenseMatrix64F expected = ConvertRotation3D_F32.rodriguesToMatrix( rod, null );
			DenseMatrix64F found = ConvertRotation3D_F32.quaternionToMatrix( q, null );

			DenseMatrix64F difference = new DenseMatrix64F(3,3);
			CommonOps_D64.multTransB(expected,found,difference);
			assertTrue(MatrixFeatures_D64.isIdentity(difference,GrlConstants.FLOAT_TEST_TOL));
		}
	}

	/**
	 * Standard checks for converting one parameterization into Euler.  Checks special cases with rotations of 90 and
	 * 180 degrees plus random ones
	 */
	public static void somethingToEulerTest(String functionName , Random rand ) throws InvocationTargetException, IllegalAccessException {

		Method m;
		try {
			m = TestConvertRotation3D_F32.class.getMethod(functionName,EulerType.class,float.class,float.class,float.class);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e.getMessage());
		}

		for(EulerType type : EulerType.values() ) {
//			System.out.println("type = "+type);

			float PId2 = (float)Math.PI/2.0f;
			float PI = (float)Math.PI;

			m.invoke(null,type,0,0,0);

			// single axis
			m.invoke(null,type,PId2,0,0);
			m.invoke(null,type,0,PId2,0);
			m.invoke(null,type,0,0,PId2);
			m.invoke(null,type,-PId2,0,0);
			m.invoke(null,type,0,-PId2,0);
			m.invoke(null,type,0,0,-PId2);
			m.invoke(null,type,PI,0,0);
			m.invoke(null,type,0,0,PI);
			m.invoke(null,type,-PI,0,0);
			m.invoke(null,type,0,0,-PI);

			// two axis maybe pathological
			for (int i = 0; i < 4; i++) {
				float sgn0 = i%2==0?1:-1;
				float sgn1 = i/2==0?1:-1;
//				System.out.println(sgn0+" "+sgn1);

				m.invoke(null,type,sgn0*PId2,sgn1*PId2,0);
				m.invoke(null,type,sgn0*PId2,0,sgn1*PId2);

				m.invoke(null,type,sgn0*PId2,sgn1*PId2,0);
				m.invoke(null,type,0,sgn0*PId2,sgn1*PId2);

				m.invoke(null,type,sgn0*PId2,0,sgn1*PId2);
				m.invoke(null,type,0,sgn0*PId2,sgn1*PId2);

				m.invoke(null,type,sgn0*PI,sgn1*PId2,0);
				m.invoke(null,type,sgn0*PI,0,sgn1*PI);

				m.invoke(null,type,sgn0*PI,sgn1*PId2,0);
				m.invoke(null,type,0,sgn0*PId2,sgn1*PI);

				m.invoke(null,type,sgn0*PI,0,sgn1*PI);
				m.invoke(null,type,0,sgn0*PId2,sgn1*PI);
			}

			for (int i = 0; i < 30; i++) {
				float rotA = 2.0f*rand.nextFloat() * (float)Math.PI - (float)Math.PI;
				float rotB = rand.nextFloat() * (float)Math.PI  - (float)Math.PI/2.0f;
				float rotC = 2.0f*rand.nextFloat() * (float)Math.PI - (float)Math.PI;

				m.invoke(null,type,rotA,rotB,rotC);
			}
		}
	}
}
