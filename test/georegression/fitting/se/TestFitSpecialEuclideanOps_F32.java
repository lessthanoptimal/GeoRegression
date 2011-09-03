/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.se;

import georegression.fitting.MotionTransformPoint;
import georegression.geometry.RotationMatrixGenerator;
import georegression.geometry.UtilPoint2D_F32;
import georegression.geometry.UtilPoint3D_F32;
import georegression.misc.autocode.JgrlConstants;
import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;
import georegression.struct.se.Se2_F32;
import georegression.struct.se.Se3_F32;
import georegression.transform.se.SePointOps_F32;
import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.MatrixFeatures;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestFitSpecialEuclideanOps_F32 {

	Random rand = new Random(234);

	/**
	 * Compares the computation produced from the {@link MotionTransformPoint} to the one found
	 * by providing two lists
	 */
	@Test
	public void fitPoints2D() {
		Se2_F32 tran = new Se2_F32( 2, -4, 0.93f );

		List<Point2D_F32> from = UtilPoint2D_F32.random( -10, 10, 30, rand );
		List<Point2D_F32> to = new ArrayList<Point2D_F32>();
		for( Point2D_F32 p : from ) {
			to.add( SePointOps_F32.transform( tran, p, null ) );
		}

		MotionTransformPoint<Se2_F32, Point2D_F32> alg = FitSpecialEuclideanOps_F32.fitPoints2D();
		assertTrue( alg.process( from , to ) );

		Se2_F32 expected = alg.getMotion();
		Se2_F32 found = FitSpecialEuclideanOps_F32.fitPoints2D( from , to );

		// the exact same algorithm should be called and they should produce the same results
		assertTrue( expected.getYaw() == found.getYaw() );
		assertTrue( expected.getX() == found.getX() );
		assertTrue( expected.getY() == found.getY() );
	}

	/**
	 * Compares the computation produced from the {@link MotionTransformPoint} to the one found
	 * by providing two lists
	 */
	@Test
	public void fitPoints3D_list() {
		DenseMatrix64F R = RotationMatrixGenerator.eulerXYZ( 0.1f, 1.0f, -1.5f , null );
		Se3_F32 tran = new Se3_F32( R , new Vector3D_F32( 1 , 2 , 3));

		List<Point3D_F32> from = UtilPoint3D_F32.random( -10, 10, 30, rand );
		List<Point3D_F32> to = new ArrayList<Point3D_F32>();
		for( Point3D_F32 p : from ) {
			to.add( SePointOps_F32.transform( tran, p, null ) );
		}

		MotionTransformPoint<Se3_F32, Point3D_F32> alg = FitSpecialEuclideanOps_F32.fitPoints3D();
		assertTrue( alg.process( from , to ) );

		Se3_F32 expected = alg.getMotion();
		Se3_F32 found = FitSpecialEuclideanOps_F32.fitPoints3D( from , to );

		// the exact same algorithm should be called and they should produce the same results
		assertTrue( MatrixFeatures.isIdentical( expected.getR() , found.getR() , JgrlConstants.FLOAT_TEST_TOL ));
		assertTrue( expected.getT().isIdentical(found.getT() , JgrlConstants.FLOAT_TEST_TOL));
	}
}
