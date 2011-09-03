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

package georegression.fitting.affine;

import georegression.geometry.UtilPoint2D_F64;
import georegression.misc.autocode.JgrlConstants;
import georegression.misc.test.GeometryUnitTest;
import georegression.struct.affine.Affine2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.transform.affine.AffinePointOps;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestMotionAffinePoint2D_F64 {

	Random rand = new Random( 434324 );

	@Test
	public void noiseless() {
		Affine2D_F64 tran = new Affine2D_F64( 2, -4, 0.3, 1.1, 0.93, -3 );

		List<Point2D_F64> from = UtilPoint2D_F64.random( -10, 10, 30, rand );
		List<Point2D_F64> to = new ArrayList<Point2D_F64>();
		for( Point2D_F64 p : from ) {
			to.add( AffinePointOps.transform( tran, p, null ) );
		}

		MotionAffinePoint2D_F64 alg = new MotionAffinePoint2D_F64();

		assertTrue( alg.process( from, to ) );

		Affine2D_F64 tranFound = alg.getMotion();

		checkTransform( from, to, tranFound, JgrlConstants.DOUBLE_TEST_TOL );
	}

	public static void checkTransform( List<Point2D_F64> from, List<Point2D_F64> to, Affine2D_F64 tranFound, double tol ) {
		Point2D_F64 foundPt = new Point2D_F64();
		for( int i = 0; i < from.size(); i++ ) {

			Point2D_F64 p = from.get( i );

			AffinePointOps.transform( tranFound, p, foundPt );

			GeometryUnitTest.assertEquals( to.get( i ), foundPt, tol );
		}
	}
}
