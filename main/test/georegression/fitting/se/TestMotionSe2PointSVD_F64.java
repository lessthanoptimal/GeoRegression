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

import georegression.geometry.UtilPoint2D_F64;
import georegression.misc.GrlConstants;
import georegression.misc.test.GeometryUnitTest;
import georegression.struct.point.Point2D_F64;
import georegression.struct.se.Se2_F64;
import georegression.transform.se.SePointOps_F64;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestMotionSe2PointSVD_F64 {

	Random rand = new Random( 434324 );

	@Test
	void noiseless() {
		Se2_F64 tran = new Se2_F64( 2, -4, 0.93 );

		List<Point2D_F64> src = UtilPoint2D_F64.random( -10, 10, 30, rand );
		List<Point2D_F64> dst = new ArrayList<Point2D_F64>();
		for( Point2D_F64 p : src ) {
			dst.add(SePointOps_F64.transform(tran, p, null));
		}

		MotionSe2PointSVD_F64 alg = new MotionSe2PointSVD_F64();

		assertTrue( alg.process( src, dst ) );

		Se2_F64 foundSrcToDst = alg.getTransformSrcToDst();

//        tranFound.getTranslation().print();
//        tran.getTranslation().print();

		checkTransform( src, dst, foundSrcToDst, GrlConstants.TEST_F64);
	}

	public static void checkTransform( List<Point2D_F64> src, List<Point2D_F64> dst, Se2_F64 foundSrcToDst, double tol ) {
		Point2D_F64 foundPt = new Point2D_F64();
		for( int i = 0; i < src.size(); i++ ) {

			Point2D_F64 p = src.get( i );

			SePointOps_F64.transform( foundSrcToDst, p, foundPt );

			GeometryUnitTest.assertEquals( dst.get( i ), foundPt, tol );
		}
	}
}
