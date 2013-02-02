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

package georegression.metric;

import georegression.misc.GrlConstants;
import georegression.struct.line.LineGeneral2D_F64;
import georegression.struct.line.LineParametric2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.se.Se2_F64;
import georegression.struct.shapes.Polygon2D_F64;
import georegression.transform.se.SePointOps_F64;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;


/**
 * @author Peter Abeles
 */
public class TestIntersection2D_F64 {
	Random rand = new Random( 234 );

	@Test
	public void containConvex() {
		Polygon2D_F64 poly = new Polygon2D_F64(4);
		poly.vertexes[0].set(-1,-1);
		poly.vertexes[1].set(1, -1);
		poly.vertexes[2].set(1, 1);
		poly.vertexes[3].set(-1, 1);

		Point2D_F64 online = new Point2D_F64(1,-1);
		Point2D_F64 inside = new Point2D_F64(0.5,0.5);
		Point2D_F64 outside = new Point2D_F64(1.5,0.5);

		assertFalse(Intersection2D_F64.containConvex(poly,online));
		assertTrue(Intersection2D_F64.containConvex(poly,inside));
		assertFalse(Intersection2D_F64.containConvex(poly,outside));

		// change the order of the vertexes
		poly.vertexes[0].set(-1, 1);
		poly.vertexes[1].set(1, 1);
		poly.vertexes[2].set(1, -1);
		poly.vertexes[3].set(-1,-1);

		assertFalse(Intersection2D_F64.containConvex(poly,online));
		assertTrue(Intersection2D_F64.containConvex(poly,inside));
		assertFalse(Intersection2D_F64.containConvex(poly,outside));
	}
	
	@Test
	public void intersection_ls_to_ls() {
		// check positive, none pathological cases
		checkIntersection( new LineSegment2D_F64( 0, 2, 2, 2 ), new LineSegment2D_F64( 2, 0, 2, 3 ), new Point2D_F64( 2, 2 ) );
		checkIntersection( new LineSegment2D_F64( 0, 2, 2, 0 ), new LineSegment2D_F64( 0, 0, 2, 2 ), new Point2D_F64( 1, 1 ) );

		// check boundary conditions
		checkIntersection( new LineSegment2D_F64( 0, 2, 2, 2 ), new LineSegment2D_F64( 0, 0, 0, 2 ), new Point2D_F64( 0, 2 ) );
		checkIntersection( new LineSegment2D_F64( 0, 2, 2, 2 ), new LineSegment2D_F64( 2, 0, 2, 2 ), new Point2D_F64( 2, 2 ) );
		checkIntersection( new LineSegment2D_F64( 1, 0, 1, 2 ), new LineSegment2D_F64( 0, 0, 2, 0 ), new Point2D_F64( 1, 0 ) );

		// check negative
		checkIntersection( new LineSegment2D_F64( 0, 2, 2, 2 ), new LineSegment2D_F64( 0, 0, 0, 1.9 ), null );
		checkIntersection( new LineSegment2D_F64( 0, 2, 2, 2 ), new LineSegment2D_F64( 2, 0, 2, 1.9 ), null );
		checkIntersection( new LineSegment2D_F64( 1, 0.1, 1, 2 ), new LineSegment2D_F64( 0, 0, 2, 0 ), null );

		// check parallel intersection
		checkIntersection( new LineSegment2D_F64( 0, 2, 0, 5 ), new LineSegment2D_F64( 0, 1, 0, 3 ), null );
	}

	public void checkIntersection( LineSegment2D_F64 a, LineSegment2D_F64 b, Point2D_F64 expected ) {
		Point2D_F64 found = Intersection2D_F64.intersection( a, b, null );
		if( found == null )
			assertTrue( expected == null );
		else {
			assertEquals( found.getX(), expected.getX(), GrlConstants.DOUBLE_TEST_TOL );
			assertEquals( found.getY(), expected.getY(), GrlConstants.DOUBLE_TEST_TOL );
		}
	}


	/**
	 * Checks to see if the expected distance is returned and that the end points of the
	 * line segment are respected.  The test cases are rotated around in a circle to test
	 * more geometric configurations
	 */
	@Test
	public void intersection_p_to_ls() {
		LineParametric2D_F64 paraLine = new LineParametric2D_F64();
		LineSegment2D_F64 target = new LineSegment2D_F64( -1, 1, 1, 1 );

		Se2_F64 tran = new Se2_F64();

		// rotate it in a circle to check more geometric configurations
		for( int i = 0; i < 20; i++ ) {

			tran.setTranslation( rand.nextGaussian(), rand.nextGaussian() );
			tran.setYaw( (double) ( 2 * Math.PI * i / 20 ) );

			checkIntersection_p_to_ls( paraLine, target, tran );
		}

		// check parallel overlapping lines
		paraLine.setPoint(-1,1);
		paraLine.setSlope(2,0);
		assertTrue( Double.isNaN( Intersection2D_F64.intersection(paraLine,target) ) );
	}

	private void checkIntersection_p_to_ls( LineParametric2D_F64 paraLine,
											LineSegment2D_F64 target,
											Se2_F64 tran ) {
		// create a copy so the original isn't modified
		paraLine = paraLine.copy();
		target = target.copy();

		// apply the transform to the two lines
		paraLine.setPoint( SePointOps_F64.transform( tran, paraLine.getPoint(), null ) );

		target.setA( SePointOps_F64.transform( tran, target.getA(), null ) );
		target.setB( SePointOps_F64.transform( tran, target.getB(), null ) );

		// should hit it dead center
		paraLine.setSlope( 0, 1 );
		paraLine.setAngle( paraLine.getAngle() + tran.getYaw() );
		double dist = Intersection2D_F64.intersection( paraLine, target );
		assertEquals( 1, dist, GrlConstants.DOUBLE_TEST_TOL );

		// should hit dead center, but negative
		paraLine.setSlope( 0, -1 );
		paraLine.setAngle( paraLine.getAngle() + tran.getYaw() );
		dist = Intersection2D_F64.intersection( paraLine, target );
		assertEquals( -1, dist, GrlConstants.DOUBLE_TEST_TOL );

		// should miss it to the left
		paraLine.setSlope( -1.1, 1 );
		paraLine.setAngle( paraLine.getAngle() + tran.getYaw() );
		dist = Intersection2D_F64.intersection( paraLine, target );
		assertTrue( Double.isNaN( dist ) );

		// should miss it to the right
		paraLine.setSlope( 1.1, 1 );
		paraLine.setAngle( paraLine.getAngle() + tran.getYaw() );
		dist = Intersection2D_F64.intersection( paraLine, target );
		assertTrue( Double.isNaN( dist ) );
	}

	@Test
	public void intersection_l_to_l_parametric() {
		fail("implement");
	}
	
	@Test
	public void intersection_l_to_l_general() {
		// check two arbitrary lines
		LineGeneral2D_F64 a = new LineGeneral2D_F64(1,2,3);
		LineGeneral2D_F64 b = new LineGeneral2D_F64(2,-1,0.5);

		Point3D_F64 found = Intersection2D_F64.intersection(a,b,null);
		assertEquals(0,a.A*found.x/found.z+a.B*found.y/found.z+a.C, GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(0,a.A*found.x+a.B*found.y+a.C*found.z, GrlConstants.DOUBLE_TEST_TOL);

		// give it two parallel lines
		a = new LineGeneral2D_F64(1,2,3);
		b = new LineGeneral2D_F64(1,2,0.5);

		Intersection2D_F64.intersection(a,b,found);
		assertEquals(0,found.z,GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(0,a.A*found.x+a.B*found.y+a.C*found.z, GrlConstants.DOUBLE_TEST_TOL);

	}
}
