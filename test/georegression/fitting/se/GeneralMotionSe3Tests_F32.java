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

package georegression.fitting.se;

import georegression.fitting.MotionTransformPoint;
import georegression.geometry.RotationMatrixGenerator;
import georegression.geometry.UtilPoint3D_F32;
import georegression.misc.GrlConstants;
import georegression.misc.test.GeometryUnitTest;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;
import georegression.struct.se.Se3_F32;
import georegression.transform.se.SePointOps_F32;
import org.ejml.data.DenseMatrix64F;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public abstract class GeneralMotionSe3Tests_F32 {
	Random rand = new Random( 434324 );

	abstract MotionTransformPoint<Se3_F32, Point3D_F32> createAlg();

	@Test
	public void noiseless() {
		for( int i = 0; i < 100; i++ ) {
			DenseMatrix64F R = RotationMatrixGenerator.eulerXYZ(rand.nextGaussian(),
					(float)rand.nextGaussian(), (float)rand.nextGaussian(), null);
			Vector3D_F32 T = new Vector3D_F32( (float)rand.nextGaussian(),
					(float)rand.nextGaussian(), (float)rand.nextGaussian() );

			Se3_F32 tran = new Se3_F32( R, T );

			List<Point3D_F32> from = UtilPoint3D_F32.random(-10, 10, 30, rand);
			List<Point3D_F32> to = new ArrayList<Point3D_F32>();
			for( Point3D_F32 p : from ) {
				to.add( SePointOps_F32.transform(tran, p, null) );
			}

			MotionTransformPoint<Se3_F32, Point3D_F32> alg = createAlg();

			assertTrue( alg.process( from, to ) );


			Se3_F32 tranFound = alg.getMotion();

			checkTransform( from, to, tranFound, GrlConstants.FLOAT_TEST_TOL );
		}
	}

	@Test
	public void noiselessPlanar() {
		for( int i = 0; i < 100; i++ ) {
			DenseMatrix64F R = RotationMatrixGenerator.eulerXYZ( (float)rand.nextGaussian(),
					(float)rand.nextGaussian(),rand.nextGaussian(), null );
			Vector3D_F32 T = new Vector3D_F32( (float)rand.nextGaussian(),
					(float)rand.nextGaussian(), (float)rand.nextGaussian() );

			Se3_F32 tran = new Se3_F32( R, T );

			List<Point3D_F32> from = createPlanar(30);
			List<Point3D_F32> to = new ArrayList<Point3D_F32>();
			for( Point3D_F32 p : from ) {
				to.add( SePointOps_F32.transform( tran, p, null ) );
			}

			MotionTransformPoint<Se3_F32, Point3D_F32> alg = createAlg();

			assertTrue( alg.process( from, to ) );

			Se3_F32 tranFound = alg.getMotion();
			
			R.print();
			tranFound.getR().print();
			
			checkTransform( from, to, tranFound, GrlConstants.FLOAT_TEST_TOL );
		}
	}

	private List<Point3D_F32> createPlanar( int N ) {
		List<Point3D_F32> ret = new ArrayList<Point3D_F32>();

		for( int i = 0; i < N; i++ )
			ret.add( new Point3D_F32((float) (rand.nextGaussian()*2),(float)(rand.nextGaussian()*2),3));

		return ret;
	}

	public static void checkTransform( List<Point3D_F32> from, List<Point3D_F32> to, Se3_F32 tranFound, float tol ) {
		Point3D_F32 foundPt = new Point3D_F32();
		for( int i = 0; i < from.size(); i++ ) {

			Point3D_F32 p = from.get( i );

			SePointOps_F32.transform( tranFound, p, foundPt );

			GeometryUnitTest.assertEquals(to.get(i), foundPt, tol);
		}
	}
}
