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

import georegression.metric.UtilAngle;
import georegression.misc.GrlConstants;
import georegression.misc.test.GeometryUnitTest;
import georegression.struct.EulerType;
import georegression.struct.point.Point3D_F64;
import georegression.struct.so.Quaternion_F64;
import georegression.struct.so.Rodrigues_F64;
import org.ejml.UtilEjml;
import org.ejml.data.RowMatrix_F64;
import org.ejml.ops.CommonOps_R64;
import org.ejml.ops.MatrixFeatures_R64;
import org.ejml.ops.RandomMatrices_R64;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;




/**
 * @author Peter Abeles
 */
public class TestConvertRotation3D_F64 {

	Random rand = new Random( 234234 );

	@Test
	public void rodriguesToMatrix() {
		RowMatrix_F64 rotZ = ConvertRotation3D_F64.rotZ( 0.5, null );

		Rodrigues_F64 r = new Rodrigues_F64( 0.5, 0, 0, 1 );

		RowMatrix_F64 rod = ConvertRotation3D_F64.rodriguesToMatrix( r, null );

		assertTrue( MatrixFeatures_R64.isIdentical( rotZ, rod, GrlConstants.TEST_F64) );
	}

	@Test
	public void rodriguesToEuler() throws InvocationTargetException, IllegalAccessException {
		somethingToEulerTest("rodriguesToEuler",rand);
	}

	public static void rodriguesToEuler(EulerType type , double rotA , double rotB , double rotC )
	{
		RowMatrix_F64 expected = ConvertRotation3D_F64.eulerToMatrix(type,rotA,rotB,rotC,null);

		Rodrigues_F64 rod = ConvertRotation3D_F64.matrixToRodrigues(expected,(Rodrigues_F64)null);

		double[] euler = ConvertRotation3D_F64.rodriguesToEuler(rod,type,null);
		RowMatrix_F64 found = ConvertRotation3D_F64.eulerToMatrix(type,euler[0],euler[1],euler[2],null);

		RowMatrix_F64 difference = new RowMatrix_F64(3,3);
		CommonOps_R64.multTransB(expected,found,difference);
		assertTrue(MatrixFeatures_R64.isIdentity(difference, GrlConstants.TEST_SQ_F64));
	}

	@Test
	public void rodriguesToQuaternion() {
		Rodrigues_F64 rod = new Rodrigues_F64(-1.5,1,3,-4);

		Quaternion_F64 quat = ConvertRotation3D_F64.rodriguesToQuaternion(rod, null);

		RowMatrix_F64 A = ConvertRotation3D_F64.quaternionToMatrix(quat, null);
		RowMatrix_F64 B = ConvertRotation3D_F64.rodriguesToMatrix(rod, null);

		RowMatrix_F64 C = new RowMatrix_F64(3,3);
		CommonOps_R64.multTransA(A,B,C);

		assertTrue(MatrixFeatures_R64.isIdentity(C, GrlConstants.TEST_F64));
	}

	@Test
	public void quaternionToRodrigues() {
		Quaternion_F64 quat = new Quaternion_F64(0.6,2,3,-1);
		quat.normalize();

		Rodrigues_F64 rod = ConvertRotation3D_F64.quaternionToRodrigues(quat,null);

		quat.normalize();
		RowMatrix_F64 A = ConvertRotation3D_F64.quaternionToMatrix(quat, null);
		RowMatrix_F64 B = ConvertRotation3D_F64.rodriguesToMatrix(rod, null);

		RowMatrix_F64 C = new RowMatrix_F64(3,3);
		CommonOps_R64.multTransA(A,B,C);

		assertTrue(MatrixFeatures_R64.isIdentity(C, GrlConstants.TEST_F64));
	}

	@Test
	public void quaternionToEuler() throws InvocationTargetException, IllegalAccessException {
		somethingToEulerTest("quaternionToEuler",rand);
	}

	public static void quaternionToEuler(EulerType type , double rotA , double rotB , double rotC )
	{
		RowMatrix_F64 expected = ConvertRotation3D_F64.eulerToMatrix(type,rotA,rotB,rotC,null);

		Quaternion_F64 q = ConvertRotation3D_F64.matrixToQuaternion(expected,null);
		double euler[] = ConvertRotation3D_F64.quaternionToEuler(q,type,null);

		RowMatrix_F64 found = ConvertRotation3D_F64.eulerToMatrix(type,euler[0],euler[1],euler[2],null);

		RowMatrix_F64 difference = new RowMatrix_F64(3,3);
		CommonOps_R64.multTransB(expected,found,difference);
		assertTrue(MatrixFeatures_R64.isIdentity(difference, GrlConstants.TEST_SQ_F64));
	}

	@Test
	public void matrixToQuaternion() {

		double pid2 = Math.PI/2.0;

		matrixToQuaternion(ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,0,0,0,null));

		// single axis rotations
		matrixToQuaternion(ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,pid2,0,0,null));
		matrixToQuaternion(ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,0,pid2,0,null));
		matrixToQuaternion(ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,0,0,pid2,null));
		matrixToQuaternion(ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,-pid2,0,0,null));
		matrixToQuaternion(ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,0,-pid2,0,null));
		matrixToQuaternion(ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,0,0,-pid2,null));

		// two axis rotations which could be pathological
		matrixToQuaternion(ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,pid2,pid2,0,null));
		matrixToQuaternion(ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,pid2,0,pid2,null));

		matrixToQuaternion(ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,pid2,pid2,0,null));
		matrixToQuaternion(ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,0,pid2,pid2,null));

		matrixToQuaternion(ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,pid2,0,pid2,null));
		matrixToQuaternion(ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,0,pid2,pid2,null));

		for (int i = 0; i < 100; i++) {
			double rotX = 2.0*rand.nextDouble() * Math.PI - Math.PI;
			double rotY = 2.0*rand.nextDouble() * Math.PI - Math.PI;
			double rotZ = 2.0*rand.nextDouble() * Math.PI - Math.PI;

			RowMatrix_F64 R = ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,rotX,rotY,rotZ,null);
			matrixToQuaternion(R);
		}
	}

	public void matrixToQuaternion( RowMatrix_F64 R ) {
		Quaternion_F64 q = ConvertRotation3D_F64.matrixToQuaternion(R,null);
		q.normalize();
		RowMatrix_F64 found = ConvertRotation3D_F64.quaternionToMatrix(q,null);

		RowMatrix_F64 result = new RowMatrix_F64(3,3);
		CommonOps_R64.multTransB(R,found,result);

		assertTrue(MatrixFeatures_R64.isIdentity(result, GrlConstants.TEST_SQ_F64));
	}

	@Test
	public void matrixToRodrigues() {
		// create the rotation axis
		for( int i = 1; i < 20; i++ ) {
			double angle = i * Math.PI / 20;
			checkMatrixToRodrigues( new Rodrigues_F64( angle, 0.1, 2, 6 ) );
			checkMatrixToRodrigues( new Rodrigues_F64( angle, 1, 0, 0 ) );
			checkMatrixToRodrigues( new Rodrigues_F64( angle, 1, 1, 1 ) );
			checkMatrixToRodrigues( new Rodrigues_F64( angle, -1, -1, -1 ) );
		}

		// see how well it handles underflow
		checkMatrixToRodrigues( new Rodrigues_F64( 50*GrlConstants.TEST_F64, -1, -1, -1 ) );

		// test known pathological cases
		checkMatrixToRodrigues( new Rodrigues_F64( 0, 1, 1, 1 ), new Rodrigues_F64( 0, 1, 0, 0 ) );
		checkMatrixToRodrigues( new Rodrigues_F64( Math.PI / 2, 1, 1, 1 ), new Rodrigues_F64( Math.PI/2, 1, 1, 1 ) );
		checkMatrixToRodrigues( new Rodrigues_F64( Math.PI, 1, 1, 1 ), new Rodrigues_F64( Math.PI, 1, 1, 1 ) );
		checkMatrixToRodrigues( new Rodrigues_F64( - Math.PI, 1, 1, 1 ), new Rodrigues_F64( Math.PI, 1, 1, 1 ) );

		// edge case where diagonals sum up to 1
		checkMatrixToRodrigues( new Rodrigues_F64( Math.PI, 1, 0, 0 ) );
		checkMatrixToRodrigues( new Rodrigues_F64( Math.PI, 0, 1, 0 ) );
		checkMatrixToRodrigues( new Rodrigues_F64( Math.PI, 0, 0, 1 ));

		checkMatrixToRodrigues( Math.PI, Math.PI/2,0);
		checkMatrixToRodrigues( Math.PI, -Math.PI/2,0);
		checkMatrixToRodrigues( Math.PI,0, Math.PI/2);
		checkMatrixToRodrigues( Math.PI,0, -Math.PI/2);
		checkMatrixToRodrigues(0, Math.PI, Math.PI/2);
		checkMatrixToRodrigues(0, Math.PI, -Math.PI/2);;
	}

	private void checkMatrixToRodrigues( Rodrigues_F64 rodInput ) {
		// create the matrix using rodrigues
		RowMatrix_F64 rod = ConvertRotation3D_F64.rodriguesToMatrix( rodInput, null );

		// see if the vectors are the same
		Rodrigues_F64 found = ConvertRotation3D_F64.matrixToRodrigues( rod, (Rodrigues_F64)null );

		// if the lines are parallel the dot product will be 1 or -1
		double dot = found.unitAxisRotation.dot( rodInput.unitAxisRotation);
		assertEquals( 1, Math.abs( dot ), GrlConstants.TEST_F64);

		// if the rotation vector is in the opposite direction then the found angle will be
		// the negative of the input.  both are equivalent
		assertTrue(UtilAngle.dist(rodInput.theta * dot, found.theta) <= GrlConstants.TEST_F64);
	}

	private void checkMatrixToRodrigues( double eulerX , double eulerY , double eulerZ ) {

		RowMatrix_F64 M = ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ,eulerX,eulerY,eulerZ,null);
		Rodrigues_F64 rod = ConvertRotation3D_F64.matrixToRodrigues(M, (Rodrigues_F64)null);
		RowMatrix_F64 found = ConvertRotation3D_F64.rodriguesToMatrix(rod,null);
		assertTrue(MatrixFeatures_R64.isIdentical(M,found,GrlConstants.TEST_F64));
	}

	private void checkMatrixToRodrigues( Rodrigues_F64 input,
										 Rodrigues_F64 expected ) {

		// create the matrix using rodrigues
		RowMatrix_F64 rod = ConvertRotation3D_F64.rodriguesToMatrix( input, null );

		// see if the vectors are the same
		Rodrigues_F64 found = ConvertRotation3D_F64.matrixToRodrigues( rod, (Rodrigues_F64)null );

		// if the lines are parallel the dot product will be 1 or -1
		assertEquals( 1, Math.abs( found.unitAxisRotation.dot( expected.unitAxisRotation) ), GrlConstants.TEST_F64);

		// if the rotation vector is in the opposite direction then the found angle will be
		// the negative of the input.  both are equivalent
		assertEquals( expected.theta, found.theta, 10.0*GrlConstants.TEST_F64);
	}

	/**
	 * A found test case where it failed
	 */
	@Test
	public void matrixToRodrigues_case0() {
		RowMatrix_F64 R = UtilEjml.parse_R64(
						"1.00000000000000000000e+00 -5.42066399999221260000e-14 -3.16267800000013500000e-13 \n" +
						"5.42066400000000000000e-14 1.00000000000000040000e+00 2.46136444559397200000e-13 \n" +
						"3.16267800000000000000e-13 -2.46191955710628460000e-13 1.00000000000000040000e+00", 3);

		Rodrigues_F64 found = ConvertRotation3D_F64.matrixToRodrigues( R, (Rodrigues_F64)null );

		assertEquals(0,found.getTheta(),GrlConstants.TEST_F64);
	}

	@Test
	public void matrixToRodrigues_case1() {
		RowMatrix_F64 R = UtilEjml.parse_R64(
						"0.99999999999999000000e+00 -5.42066399999221260000e-14 -3.16267800000013500000e-13 \n" +
						"5.42066400000000000000e-14 0.99999999999999000000e+00 2.46136444559397200000e-13 \n" +
						"3.16267800000000000000e-13 -2.46191955710628460000e-13 0.99999999999999000000e+00", 3);

		Rodrigues_F64 found = ConvertRotation3D_F64.matrixToRodrigues( R, (Rodrigues_F64)null );

		assertEquals(0,found.getTheta(),50*GrlConstants.TEST_F64);
	}

	@Test
	public void rotX() {
		Point3D_F64 pt_y = new Point3D_F64( 0, 1.5, 0 );
		Point3D_F64 pt_z = new Point3D_F64( 0, 0, 1.5 );

		RowMatrix_F64 R = ConvertRotation3D_F64.rotX( Math.PI / 2.0, null );

		GeometryMath_F64.mult( R, pt_y, pt_y );
		GeometryMath_F64.mult( R, pt_z, pt_z );

		assertTrue( pt_y.isIdentical( 0, 0, 1.5, GrlConstants.TEST_F64) );
		assertTrue( pt_z.isIdentical( 0, -1.5, 0, GrlConstants.TEST_F64) );
	}

	@Test
	public void rotY() {
		Point3D_F64 pt_x = new Point3D_F64( 1.5, 0, 0 );
		Point3D_F64 pt_z = new Point3D_F64( 0, 0, 1.5 );

		RowMatrix_F64 R = ConvertRotation3D_F64.rotY( Math.PI / 2.0, null );

		GeometryMath_F64.mult( R, pt_x, pt_x );
		GeometryMath_F64.mult( R, pt_z, pt_z );

		assertTrue( pt_x.isIdentical( 0, 0, -1.5, GrlConstants.TEST_F64) );
		assertTrue( pt_z.isIdentical( 1.5, 0, 0, GrlConstants.TEST_F64) );
	}

	@Test
	public void rotZ() {
		Point3D_F64 pt_x = new Point3D_F64( 1.5, 0, 0 );
		Point3D_F64 pt_y = new Point3D_F64( 0, 1.5, 0 );

		RowMatrix_F64 R = ConvertRotation3D_F64.rotZ( Math.PI / 2.0, null );

		GeometryMath_F64.mult( R, pt_x, pt_x );
		GeometryMath_F64.mult( R, pt_y, pt_y );

		assertTrue( pt_x.isIdentical( 0, 1.5, 0, GrlConstants.TEST_F64) );
		assertTrue( pt_y.isIdentical( -1.5, 0, 0, GrlConstants.TEST_F64) );
	}

	@Test
	public void matrixToEuler() throws InvocationTargetException, IllegalAccessException {
		somethingToEulerTest("matrixToEuler",rand);
	}

	public static void matrixToEuler(EulerType type , double rotA , double rotB , double rotC )
	{
		RowMatrix_F64 expected = ConvertRotation3D_F64.eulerToMatrix(type,rotA,rotB,rotC,null);

		double euler[] = ConvertRotation3D_F64.matrixToEuler(expected,type,(double[])null);

		RowMatrix_F64 found = ConvertRotation3D_F64.eulerToMatrix(type,euler[0],euler[1],euler[2],null);

		RowMatrix_F64 difference = new RowMatrix_F64(3,3);
		CommonOps_R64.multTransB(expected,found,difference);
		assertTrue(MatrixFeatures_R64.isIdentity(difference, GrlConstants.TEST_SQ_F64));
	}

	/**
	 * Creates a random matrix and sees if the approximation is a valid rotation matrix
	 */
	@Test
	public void approximateRotationMatrix_random() {
		RowMatrix_F64 Q = RandomMatrices_R64.createRandom( 3, 3, rand );

		RowMatrix_F64 R = ConvertRotation3D_F64.approximateRotationMatrix( Q, null );

		assertTrue( MatrixFeatures_R64.isOrthogonal( R, GrlConstants.TEST_F64) );
	}

	/**
	 * Create a rotation matrix and see if the exact same matrix is returned
	 */
	@Test
	public void approximateRotationMatrix_nochange() {
		RowMatrix_F64 Q = RandomMatrices_R64.createOrthogonal( 3, 3, rand );

		if( CommonOps_R64.det(Q) < 0 )
			CommonOps_R64.changeSign(Q);

		RowMatrix_F64 R = ConvertRotation3D_F64.approximateRotationMatrix( Q, null );

		assertTrue( MatrixFeatures_R64.isIdentical( Q, R, GrlConstants.TEST_F64) );
	}

	@Test
	public void eulerToMatrix() throws InvocationTargetException, IllegalAccessException {
		somethingToEulerTest("eulerToMatrix",rand);
	}

	public static void eulerToMatrix(EulerType type , double rotA , double rotB , double rotC ) {

		RowMatrix_F64 matA = rotateAxis(type.getAxisA(),rotA);
		RowMatrix_F64 matB = rotateAxis(type.getAxisB(),rotB);
		RowMatrix_F64 matC = rotateAxis(type.getAxisC(),rotC);

		RowMatrix_F64 matEuler = ConvertRotation3D_F64.eulerToMatrix(type,rotA,rotB,rotC,null);

		Point3D_F64 a = new Point3D_F64(1,2,3);

		Point3D_F64 tmp = new Point3D_F64();
		Point3D_F64 expected = new Point3D_F64();
		Point3D_F64 found = new Point3D_F64();

		GeometryMath_F64.mult(matA,a,expected);
		GeometryMath_F64.mult(matB,expected,tmp);
		GeometryMath_F64.mult(matC,tmp,expected);

		GeometryMath_F64.mult(matEuler,a,found);

		assertTrue(expected.distance(found) < GrlConstants.TEST_F64);
	}

	private static RowMatrix_F64 rotateAxis( int which , double angle ) {
		if( which == 0 )
			return ConvertRotation3D_F64.rotX(angle,null);
		else if( which == 1 )
			return ConvertRotation3D_F64.rotY(angle,null);
		else
			return ConvertRotation3D_F64.rotZ(angle,null);
	}

	@Test
	public void eulerToQuaternion() throws InvocationTargetException, IllegalAccessException {
		somethingToEulerTest("eulerToQuaternion",rand);
	}

	public static void eulerToQuaternion(EulerType type , double rotA , double rotB , double rotC ) {
		RowMatrix_F64 expected = ConvertRotation3D_F64.eulerToMatrix(type,rotA,rotB,rotC,null);

		Quaternion_F64 q = new Quaternion_F64();
		ConvertRotation3D_F64.eulerToQuaternion(type,rotA,rotB,rotC,q);

		RowMatrix_F64 found = ConvertRotation3D_F64.quaternionToMatrix(q,null);

		RowMatrix_F64 result = new RowMatrix_F64(3,3);
		CommonOps_R64.multTransB(expected,found,result);
		assertTrue(MatrixFeatures_R64.isIdentity(result, Math.sqrt(GrlConstants.TEST_F64)));
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
		Quaternion_F64 q = ConvertRotation3D_F64.rodriguesToQuaternion( new Rodrigues_F64( Math.PI / 2.0, 0, 0, 1 ), null );

		RowMatrix_F64 R = ConvertRotation3D_F64.quaternionToMatrix( q, null );

		Point3D_F64 p = new Point3D_F64( 1, 0, 0 );
		GeometryMath_F64.mult( R, p, p );
		GeometryUnitTest.assertEquals( p, 0, 1, 0, GrlConstants.TEST_F64);


		// rotate around y-axis 90 degrees
		q = ConvertRotation3D_F64.rodriguesToQuaternion( new Rodrigues_F64( Math.PI / 2.0, 0, 1, 0 ), null );
		q.normalize();

		R = ConvertRotation3D_F64.quaternionToMatrix( q, R );

		p.set( 1, 0, 0 );
		GeometryMath_F64.mult( R, p, p );
		GeometryUnitTest.assertEquals( p, 0, 0, -1, GrlConstants.TEST_F64);

		for (int i = 0; i < 30; i++) {
			Rodrigues_F64 rod = new Rodrigues_F64();
			rod.theta = Math.PI*(2*rand.nextDouble()-1);
			rod.setParamVector(rand.nextDouble()-0.5,rand.nextDouble()-0.5,rand.nextDouble()-0.5);
			rod.unitAxisRotation.normalize();

			q = ConvertRotation3D_F64.rodriguesToQuaternion( rod, null );
			q.normalize();
			RowMatrix_F64 expected = ConvertRotation3D_F64.rodriguesToMatrix( rod, null );
			RowMatrix_F64 found = ConvertRotation3D_F64.quaternionToMatrix( q, null );

			RowMatrix_F64 difference = new RowMatrix_F64(3,3);
			CommonOps_R64.multTransB(expected,found,difference);
			assertTrue(MatrixFeatures_R64.isIdentity(difference,GrlConstants.TEST_F64));
		}
	}

	/**
	 * Standard checks for converting one parameterization into Euler.  Checks special cases with rotations of 90 and
	 * 180 degrees plus random ones
	 */
	public static void somethingToEulerTest(String functionName , Random rand ) throws InvocationTargetException, IllegalAccessException {

		Method m;
		try {
			m = TestConvertRotation3D_F64.class.getMethod(functionName,EulerType.class,double.class,double.class,double.class);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e.getMessage());
		}

		for(EulerType type : EulerType.values() ) {
//			System.out.println("type = "+type);

			double PId2 = Math.PI/2.0;
			double PI = Math.PI;

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
				double sgn0 = i%2==0?1:-1;
				double sgn1 = i/2==0?1:-1;
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
				double rotA = 2.0*rand.nextDouble() * Math.PI - Math.PI;
				double rotB = rand.nextDouble() * Math.PI  - Math.PI/2.0;
				double rotC = 2.0*rand.nextDouble() * Math.PI - Math.PI;

				m.invoke(null,type,rotA,rotB,rotC);
			}
		}
	}
}
