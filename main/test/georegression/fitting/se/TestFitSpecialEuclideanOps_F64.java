/*
 * Copyright (C) 2011-2020, Peter Abeles. All Rights Reserved.
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

package georegression.fitting.se;

import georegression.fitting.MotionTransformPoint;
import georegression.geometry.ConvertRotation3D_F64;
import georegression.geometry.UtilPoint2D_F64;
import georegression.geometry.UtilPoint3D_F64;
import georegression.misc.GrlConstants;
import georegression.struct.EulerType;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.se.Se2_F64;
import georegression.struct.se.Se3_F64;
import georegression.transform.se.SePointOps_F64;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.MatrixFeatures_DDRM;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestFitSpecialEuclideanOps_F64 {

	Random rand = new Random(234);

	/**
	 * Compares the computation produced from the {@link MotionTransformPoint} to the one found
	 * by providing two lists
	 */
	@Test
	void fitPoints2D() {
		Se2_F64 tran = new Se2_F64( 2, -4, 0.93 );

		List<Point2D_F64> from = UtilPoint2D_F64.random( -10, 10, 30, rand );
		List<Point2D_F64> to = new ArrayList<Point2D_F64>();
		for( Point2D_F64 p : from ) {
			to.add( SePointOps_F64.transform( tran, p, null ) );
		}

		MotionTransformPoint<Se2_F64, Point2D_F64> alg = FitSpecialEuclideanOps_F64.fitPoints2D();
		assertTrue( alg.process( from , to ) );

		Se2_F64 expected = alg.getTransformSrcToDst();
		Se2_F64 found = FitSpecialEuclideanOps_F64.fitPoints2D( from , to );

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
	void fitPoints3D_list() {
		DMatrixRMaj R = ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ, 0.1, 1.0, -1.5 , null );
		Se3_F64 tran = new Se3_F64( R , new Vector3D_F64( 1 , 2 , 3));

		List<Point3D_F64> from = UtilPoint3D_F64.random( -10, 10, 30, rand );
		List<Point3D_F64> to = new ArrayList<Point3D_F64>();
		for( Point3D_F64 p : from ) {
			to.add( SePointOps_F64.transform( tran, p, null ) );
		}

		MotionTransformPoint<Se3_F64, Point3D_F64> alg = FitSpecialEuclideanOps_F64.fitPoints3D();
		assertTrue( alg.process( from , to ) );

		Se3_F64 expected = alg.getTransformSrcToDst();
		Se3_F64 found = FitSpecialEuclideanOps_F64.fitPoints3D( from , to );

		// the exact same algorithm should be called and they should produce the same results
		assertTrue( MatrixFeatures_DDRM.isIdentical( expected.getR() , found.getR() , GrlConstants.TEST_F64));
		assertTrue( expected.getT().isIdentical(found.getT() , GrlConstants.TEST_F64));
	}
}
