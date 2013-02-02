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

package georegression.struct.se;

import georegression.struct.GenericInvertibleTransformTests_F64;
import georegression.struct.InvertibleTransform;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import georegression.transform.se.SePointOps_F64;
import org.ejml.data.DenseMatrix64F;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestSe3_F64 extends GenericInvertibleTransformTests_F64<Point3D_F64> {

	Random rand = new Random( 234234 );

	/**
	 * Checks to see if the constructor correctly saves the reference or copies the values.
	 */
	@Test
	public void constructor_assign() {
		DenseMatrix64F R = new DenseMatrix64F( 3, 3 );
		Vector3D_F64 T = new Vector3D_F64( 1, 2, 3 );

		Se3_F64 a = new Se3_F64( R, T, false );
		assertTrue( R != a.getR() );
		assertTrue( T != a.getT() );

		a = new Se3_F64( R, T, true );
		assertTrue( R == a.getR() );
		assertTrue( T == a.getT() );
	}

	@Override
	public Point3D_F64 createRandomPoint() {
		return new Point3D_F64( rand.nextGaussian() * 3,
				rand.nextGaussian() * 3, rand.nextGaussian() * 3 );
	}

	@Override
	public SpecialEuclidean createRandomTransform() {

		double rotX = (double) ( ( rand.nextDouble() - 0.5 ) * 2.0 * Math.PI );
		double rotY = (double) ( ( rand.nextDouble() - 0.5 ) * 2.0 * Math.PI );
		double rotZ = (double) ( ( rand.nextDouble() - 0.5 ) * 2.0 * Math.PI );
		double x = (double) ( rand.nextGaussian() * 2 );
		double y = (double) ( rand.nextGaussian() * 2 );
		double z = (double) ( rand.nextGaussian() * 2 );

		Se3_F64 ret = new Se3_F64();

		SpecialEuclideanOps_F64.setEulerXYZ( rotX, rotY, rotZ, x, y, z, ret );

		return ret;
	}

	@Override
	public Point3D_F64 apply( InvertibleTransform se, Point3D_F64 point, Point3D_F64 result ) {
		return SePointOps_F64.transform( (Se3_F64) se, (Point3D_F64) point, (Point3D_F64) result );
	}
}
