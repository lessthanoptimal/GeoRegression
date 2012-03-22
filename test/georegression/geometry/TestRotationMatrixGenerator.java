/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.geometry;

import georegression.misc.test.GeometryUnitTest;
import georegression.struct.point.Point3D_F64;
import georegression.struct.so.Quaternion;
import georegression.struct.so.Rodrigues;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.MatrixFeatures;
import org.ejml.ops.RandomMatrices;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;


/**
 * @author Peter Abeles
 */
public class TestRotationMatrixGenerator {

	Random rand = new Random( 234234 );

	@Test
	public void rodriguesToMatrix() {
		DenseMatrix64F rotZ = RotationMatrixGenerator.rotZ( 0.5, null );

		Rodrigues r = new Rodrigues( 0.5, 0, 0, 1 );

		DenseMatrix64F rod = RotationMatrixGenerator.rodriguesToMatrix( r, null );

		assertTrue( MatrixFeatures.isIdentical( rotZ, rod, 1e-8 ) );
	}

	@Test
	public void rodriguesToQuaternion() {
		fail( "Implement" );
	}

	@Test
	public void quaternionToRodrigues() {
		fail( "implement" );
	}

	@Test
	public void matrixToQuaternion() {
		fail( "implement" );
	}

	@Test
	public void rotationAxis() {
		fail( "Implement" );
	}

	@Test
	public void rotationAngle() {
		fail( "Implement" );
	}

	@Test
	public void matrixToRodrigues() {
		// create the rotation axis
		for( int i = 1; i < 20; i++ ) {
			double angle = i * Math.PI / 20;
			checkMatrixToRodrigues( new Rodrigues( angle, 0.1, 2, 6 ) );
			checkMatrixToRodrigues( new Rodrigues( angle, 1, 0, 0 ) );
			checkMatrixToRodrigues( new Rodrigues( angle, 1, 1, 1 ) );
			checkMatrixToRodrigues( new Rodrigues( angle, -1, -1, -1 ) );
		}

		// see how well it handles underflow
		checkMatrixToRodrigues( new Rodrigues( 1e-7, -1, -1, -1 ) );

		// test known pathological cases
		checkMatrixToRodrigues( new Rodrigues( 0, 1, 1, 1 ), new Rodrigues( 0, 1, 0, 0 ) );
		checkMatrixToRodrigues( new Rodrigues( Math.PI, 1, 1, 1 ), new Rodrigues( Math.PI, 1, 1, 1 ) );
		checkMatrixToRodrigues( new Rodrigues( -Math.PI, 1, 1, 1 ), new Rodrigues( Math.PI, 1, 1, 1 ) );
	}

	private void checkMatrixToRodrigues( Rodrigues rodInput ) {
		// create the matrix using rodrigues
		DenseMatrix64F rod = RotationMatrixGenerator.rodriguesToMatrix( rodInput, null );

		// see if the vectors are the same
		Rodrigues found = RotationMatrixGenerator.matrixToRodrigues( rod, null );

		// if the lines are parallel the dot product will be 1 or -1
		double dot = found.unitAxisRotation.dot( rodInput.unitAxisRotation);
		assertEquals( 1, Math.abs( dot ), 1e-8 );

		// if the rotation vector is in the opposite direction then the found angle will be
		// the negative of the input.  both are equivalent
		assertEquals( rodInput.theta * dot, found.theta, 1e-8 );
	}

	private void checkMatrixToRodrigues( Rodrigues input,
										 Rodrigues expected ) {

		// create the matrix using rodrigues
		DenseMatrix64F rod = RotationMatrixGenerator.rodriguesToMatrix( input, null );

		// see if the vectors are the same
		Rodrigues found = RotationMatrixGenerator.matrixToRodrigues( rod, null );

		// if the lines are parallel the dot product will be 1 or -1
		assertEquals( 1, Math.abs( found.unitAxisRotation.dot( expected.unitAxisRotation) ), 1e-8 );

		// if the rotation vector is in the opposite direction then the found angle will be
		// the negative of the input.  both are equivalent
		assertEquals( expected.theta, found.theta, 1e-7 );
	}

	@Test
	public void rotX() {
		Point3D_F64 pt_y = new Point3D_F64( 0, 1.5, 0 );
		Point3D_F64 pt_z = new Point3D_F64( 0, 0, 1.5 );

		DenseMatrix64F R = RotationMatrixGenerator.rotX( Math.PI / 2.0, null );

		GeometryMath_F64.mult( R, pt_y, pt_y );
		GeometryMath_F64.mult( R, pt_z, pt_z );

		assertTrue( pt_y.isIdentical( 0, 0, 1.5, 1e-8 ) );
		assertTrue( pt_z.isIdentical( 0, -1.5, 0, 1e-8 ) );
	}

	@Test
	public void rotY() {
		Point3D_F64 pt_x = new Point3D_F64( 1.5, 0, 0 );
		Point3D_F64 pt_z = new Point3D_F64( 0, 0, 1.5 );

		DenseMatrix64F R = RotationMatrixGenerator.rotY( Math.PI / 2.0, null );

		GeometryMath_F64.mult( R, pt_x, pt_x );
		GeometryMath_F64.mult( R, pt_z, pt_z );

		assertTrue( pt_x.isIdentical( 0, 0, -1.5, 1e-8 ) );
		assertTrue( pt_z.isIdentical( 1.5, 0, 0, 1e-8 ) );
	}

	@Test
	public void rotZ() {
		Point3D_F64 pt_x = new Point3D_F64( 1.5, 0, 0 );
		Point3D_F64 pt_y = new Point3D_F64( 0, 1.5, 0 );

		DenseMatrix64F R = RotationMatrixGenerator.rotZ( Math.PI / 2.0, null );

		GeometryMath_F64.mult( R, pt_x, pt_x );
		GeometryMath_F64.mult( R, pt_y, pt_y );

		assertTrue( pt_x.isIdentical( 0, 1.5, 0, 1e-8 ) );
		assertTrue( pt_y.isIdentical( -1.5, 0, 0, 1e-8 ) );
	}

	@Test
	public void eulerArbitrary() {
		DenseMatrix64F R_e = RotationMatrixGenerator.eulerXYZ( 1.2, -.5, 2.4, null );
		DenseMatrix64F R_a = RotationMatrixGenerator.eulerArbitrary( 0, 1, 2, 1.2, -.5, 2.4 );

		assertTrue( MatrixFeatures.isIdentical( R_e, R_a, 1e-8 ) );
	}

	@Test
	public void eulerXYZ() {
		DenseMatrix64F R_x = RotationMatrixGenerator.rotX( 1.2, null );
		DenseMatrix64F R_y = RotationMatrixGenerator.rotY( -.5, null );
		DenseMatrix64F R_z = RotationMatrixGenerator.rotZ( 2.4, null );

		DenseMatrix64F A = new DenseMatrix64F( 3, 3 );
		DenseMatrix64F R = new DenseMatrix64F( 3, 3 );

		CommonOps.mult( R_y, R_x, A );
		CommonOps.mult( R_z, A, R );

		DenseMatrix64F xyz = RotationMatrixGenerator.eulerXYZ( 1.2, -.5, 2.4, null );

		Point3D_F64 pt_found = new Point3D_F64( -1.56, 2.03, 0.5 );
		Point3D_F64 pt_expected = pt_found.copy();


		GeometryMath_F64.mult( R, pt_expected, pt_expected );
		GeometryMath_F64.mult( xyz, pt_found, pt_found );

		assertTrue( pt_expected.isIdentical( pt_found, 1e-8 ) );

	}

	@Test
	public void matrixToEulerXYZ() {
		// test case one
		DenseMatrix64F A = RotationMatrixGenerator.eulerXYZ( 0.1, -0.5, -0.96, null );
		double[] euler = RotationMatrixGenerator.matrixToEulerXYZ( A );
		DenseMatrix64F B = RotationMatrixGenerator.eulerXYZ( euler[0], euler[1], euler[2], null );

		assertTrue( MatrixFeatures.isIdentical( A, B, 1e-8 ) );

		// now try a pathological case
		A = RotationMatrixGenerator.eulerXYZ( 0.1, -0.5, 0, null );
		euler = RotationMatrixGenerator.matrixToEulerXYZ( A );
		B = RotationMatrixGenerator.eulerXYZ( euler[0], euler[1], euler[2], null );

		assertTrue( MatrixFeatures.isIdentical( A, B, 1e-8 ) );

		// try all zeros
		A = RotationMatrixGenerator.eulerXYZ( 0, 0, 0, null );
		euler = RotationMatrixGenerator.matrixToEulerXYZ( A );
		B = RotationMatrixGenerator.eulerXYZ( euler[0], euler[1], euler[2], null );

		assertTrue( MatrixFeatures.isIdentical( A, B, 1e-8 ) );
	}

	/**
	 * Creates a random matrix and sees if the approximation is a valid rotation matrix
	 */
	@Test
	public void approximateRotationMatrix_random() {
		DenseMatrix64F Q = RandomMatrices.createRandom( 3, 3, rand );

		DenseMatrix64F R = RotationMatrixGenerator.approximateRotationMatrix( Q, null );

		assertTrue( MatrixFeatures.isOrthogonal( R, 1e-8 ) );
	}

	/**
	 * Create a rotation matrix and see if the exact same matrix is returned
	 */
	@Test
	public void approximateRotationMatrix_nochange() {
		DenseMatrix64F Q = RandomMatrices.createOrthogonal( 3, 3, rand );

		DenseMatrix64F R = RotationMatrixGenerator.approximateRotationMatrix( Q, null );

		assertTrue( MatrixFeatures.isIdentical( Q, R, 1e-8 ) );
	}

	@Test
	public void eulerToQuaternions() {
		fail( "Implement" );
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
		Quaternion q = RotationMatrixGenerator.rodriguesToQuaternion( new Rodrigues( Math.PI / 2.0, 0, 0, 1 ), null );

		DenseMatrix64F R = RotationMatrixGenerator.quaternionToMatrix( q, null );

		Point3D_F64 p = new Point3D_F64( 1, 0, 0 );
		GeometryMath_F64.mult( R, p, p );
		GeometryUnitTest.assertEquals( p, 0, 1, 0, 1e-8 );


		// rotate around y-axis 90 degrees
		q = RotationMatrixGenerator.rodriguesToQuaternion( new Rodrigues( Math.PI / 2.0, 0, 1, 0 ), null );
		q.normalize();

		R = RotationMatrixGenerator.quaternionToMatrix( q, R );

		p.set( 1, 0, 0 );
		GeometryMath_F64.mult( R, p, p );
		GeometryUnitTest.assertEquals( p, 0, 0, -1, 1e-8 );
	}
}
