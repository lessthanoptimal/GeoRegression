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

import georegression.geometry.UtilPoint2D_F32;
import georegression.misc.GrlConstants;
import georegression.misc.test.GeometryUnitTest;
import georegression.struct.point.Point2D_F32;
import georegression.struct.se.Se2_F32;
import georegression.transform.se.SePointOps_F32;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestMotionSe2PointSVD_F32 {

	Random rand = new Random( 434324 );

	@Test
	public void noiseless() {
		Se2_F32 tran = new Se2_F32( 2, -4, 0.93f );

		List<Point2D_F32> from = UtilPoint2D_F32.random( -10, 10, 30, rand );
		List<Point2D_F32> to = new ArrayList<Point2D_F32>();
		for( Point2D_F32 p : from ) {
			to.add( SePointOps_F32.transform( tran, p, null ) );
		}

		MotionSe2PointSVD_F32 alg = new MotionSe2PointSVD_F32();

		assertTrue( alg.process( from, to ) );

		Se2_F32 tranFound = alg.getMotion();

//        tranFound.getTranslation().print();
//        tran.getTranslation().print();

		checkTransform( from, to, tranFound, GrlConstants.FLOAT_TEST_TOL );
	}

	public static void checkTransform( List<Point2D_F32> from, List<Point2D_F32> to, Se2_F32 tranFound, float tol ) {
		Point2D_F32 foundPt = new Point2D_F32();
		for( int i = 0; i < from.size(); i++ ) {

			Point2D_F32 p = from.get( i );

			SePointOps_F32.transform( tranFound, p, foundPt );

			GeometryUnitTest.assertEquals( to.get( i ), foundPt, tol );
		}
	}
}
