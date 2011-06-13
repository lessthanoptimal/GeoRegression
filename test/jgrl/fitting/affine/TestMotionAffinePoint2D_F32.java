/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
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
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.fitting.affine;

import jgrl.geometry.UtilPoint2D_F32;
import jgrl.misc.autocode.JgrlConstants;
import jgrl.misc.test.GeometryUnitTest;
import jgrl.struct.affine.Affine2D_F32;
import jgrl.struct.point.Point2D_F32;
import jgrl.transform.affine.AffinePointOps;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestMotionAffinePoint2D_F32 {

	Random rand = new Random( 434324 );

	@Test
	public void noiseless() {
		Affine2D_F32 tran = new Affine2D_F32( 2, -4, 0.3f, 1.1f, 0.93f, -3 );

		List<Point2D_F32> from = UtilPoint2D_F32.random( -10, 10, 30, rand );
		List<Point2D_F32> to = new ArrayList<Point2D_F32>();
		for( Point2D_F32 p : from ) {
			to.add( AffinePointOps.transform( tran, p, null ) );
		}

		MotionAffinePoint2D_F32 alg = new MotionAffinePoint2D_F32();

		assertTrue( alg.process( from, to ) );

		Affine2D_F32 tranFound = alg.getMotion();

		checkTransform( from, to, tranFound, JgrlConstants.FLOAT_TEST_TOL );
	}

	public static void checkTransform( List<Point2D_F32> from, List<Point2D_F32> to, Affine2D_F32 tranFound, float tol ) {
		Point2D_F32 foundPt = new Point2D_F32();
		for( int i = 0; i < from.size(); i++ ) {

			Point2D_F32 p = from.get( i );

			AffinePointOps.transform( tranFound, p, foundPt );

			GeometryUnitTest.assertEquals( to.get( i ), foundPt, tol );
		}
	}
}
