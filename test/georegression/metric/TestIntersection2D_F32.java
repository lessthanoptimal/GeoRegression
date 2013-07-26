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
import georegression.struct.line.LineGeneral2D_F32;
import georegression.struct.line.LineParametric2D_F32;
import georegression.struct.line.LineSegment2D_F32;
import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point3D_F32;
import georegression.struct.se.Se2_F32;
import georegression.struct.shapes.Polygon2D_F32;
import georegression.struct.shapes.RectangleCorner2D_F32;
import georegression.transform.se.SePointOps_F32;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;


/**
 * @author Peter Abeles
 */
public class TestIntersection2D_F32 {
	Random rand = new Random( 234 );

	@Test
	public void containConvex() {
		Polygon2D_F32 poly = new Polygon2D_F32(4);
		poly.vertexes[0].set(-1,-1);
		poly.vertexes[1].set(1, -1);
		poly.vertexes[2].set(1, 1);
		poly.vertexes[3].set(-1, 1);

		Point2D_F32 online = new Point2D_F32(1,-1);
		Point2D_F32 inside = new Point2D_F32(0.5f,0.5f);
		Point2D_F32 outside = new Point2D_F32(1.5f,0.5f);

		assertFalse(Intersection2D_F32.containConvex(poly,online));
		assertTrue(Intersection2D_F32.containConvex(poly,inside));
		assertFalse(Intersection2D_F32.containConvex(poly,outside));

		// change the order of the vertexes
		poly.vertexes[0].set(-1, 1);
		poly.vertexes[1].set(1, 1);
		poly.vertexes[2].set(1, -1);
		poly.vertexes[3].set(-1,-1);

		assertFalse(Intersection2D_F32.containConvex(poly,online));
		assertTrue(Intersection2D_F32.containConvex(poly,inside));
		assertFalse(Intersection2D_F32.containConvex(poly,outside));
	}

	@Test
	public void containConcave_rectangle() {
		Polygon2D_F32 poly = new Polygon2D_F32(4);
		poly.vertexes[0].set(-1,-1);
		poly.vertexes[1].set(1, -1);
		poly.vertexes[2].set(1, 1);
		poly.vertexes[3].set(-1, 1);

		assertTrue(Intersection2D_F32.containConcave(poly, new Point2D_F32(0,0)));

		assertFalse(Intersection2D_F32.containConcave(poly, new Point2D_F32(2,0)));
		assertFalse(Intersection2D_F32.containConcave(poly, new Point2D_F32(-2,0)));
		assertFalse(Intersection2D_F32.containConcave(poly, new Point2D_F32(0,2)));
		assertFalse(Intersection2D_F32.containConcave(poly, new Point2D_F32(0,-2)));
	}

	@Test
	public void containConcave_concave() {
		Polygon2D_F32 poly = new Polygon2D_F32(5);
		poly.vertexes[0].set(-1,-1);
		poly.vertexes[1].set( 0, 0);
		poly.vertexes[2].set(1, -1);
		poly.vertexes[3].set(1, 1);
		poly.vertexes[4].set(-1, 1);

		assertTrue(Intersection2D_F32.containConcave(poly, new Point2D_F32(0,0.5f)));
		assertTrue(Intersection2D_F32.containConcave(poly, new Point2D_F32(-0.75f,-0.25f)));
		assertTrue(Intersection2D_F32.containConcave(poly, new Point2D_F32(0.75f,-0.25f)));

		assertFalse(Intersection2D_F32.containConcave(poly, new Point2D_F32(0,-0.5f)));

		assertFalse(Intersection2D_F32.containConcave(poly, new Point2D_F32(2,0)));
		assertFalse(Intersection2D_F32.containConcave(poly, new Point2D_F32(-2,0)));
		assertFalse(Intersection2D_F32.containConcave(poly, new Point2D_F32(0,2)));
		assertFalse(Intersection2D_F32.containConcave(poly, new Point2D_F32(0,-2)));
	}
	
	@Test
	public void intersection_ls_to_ls() {
		// check positive, none pathological cases
		checkIntersection( new LineSegment2D_F32( 0, 2, 2, 2 ), new LineSegment2D_F32( 2, 0, 2, 3 ), new Point2D_F32( 2, 2 ) );
		checkIntersection( new LineSegment2D_F32( 0, 2, 2, 0 ), new LineSegment2D_F32( 0, 0, 2, 2 ), new Point2D_F32( 1, 1 ) );

		// check boundary conditions
		checkIntersection( new LineSegment2D_F32( 0, 2, 2, 2 ), new LineSegment2D_F32( 0, 0, 0, 2 ), new Point2D_F32( 0, 2 ) );
		checkIntersection( new LineSegment2D_F32( 0, 2, 2, 2 ), new LineSegment2D_F32( 2, 0, 2, 2 ), new Point2D_F32( 2, 2 ) );
		checkIntersection( new LineSegment2D_F32( 1, 0, 1, 2 ), new LineSegment2D_F32( 0, 0, 2, 0 ), new Point2D_F32( 1, 0 ) );

		// check negative
		checkIntersection( new LineSegment2D_F32( 0, 2, 2, 2 ), new LineSegment2D_F32( 0, 0, 0, 1.9f ), null );
		checkIntersection( new LineSegment2D_F32( 0, 2, 2, 2 ), new LineSegment2D_F32( 2, 0, 2, 1.9f ), null );
		checkIntersection( new LineSegment2D_F32( 1, 0.1f, 1, 2 ), new LineSegment2D_F32( 0, 0, 2, 0 ), null );

		// check parallel intersection
		checkIntersection( new LineSegment2D_F32( 0, 2, 0, 5 ), new LineSegment2D_F32( 0, 1, 0, 3 ), null );
	}

	public void checkIntersection( LineSegment2D_F32 a, LineSegment2D_F32 b, Point2D_F32 expected ) {
		Point2D_F32 found = Intersection2D_F32.intersection( a, b, null );
		if( found == null )
			assertTrue( expected == null );
		else {
			assertEquals( found.getX(), expected.getX(), GrlConstants.FLOAT_TEST_TOL );
			assertEquals( found.getY(), expected.getY(), GrlConstants.FLOAT_TEST_TOL );
		}
	}


	/**
	 * Checks to see if the expected distance is returned and that the end points of the
	 * line segment are respected.  The test cases are rotated around in a circle to test
	 * more geometric configurations
	 */
	@Test
	public void intersection_p_to_ls() {
		LineParametric2D_F32 paraLine = new LineParametric2D_F32();
		LineSegment2D_F32 target = new LineSegment2D_F32( -1, 1, 1, 1 );

		Se2_F32 tran = new Se2_F32();

		// rotate it in a circle to check more geometric configurations
		for( int i = 0; i < 20; i++ ) {

			tran.setTranslation( (float)rand.nextGaussian(), (float)rand.nextGaussian() );
			tran.setYaw( (float) ( 2 * (float)Math.PI * i / 20 ) );

			checkIntersection_p_to_ls( paraLine, target, tran );
		}

		// check parallel overlapping lines
		paraLine.setPoint(-1,1);
		paraLine.setSlope(2,0);
		assertTrue( Float.isNaN( Intersection2D_F32.intersection(paraLine,target) ) );
	}

	private void checkIntersection_p_to_ls( LineParametric2D_F32 paraLine,
											LineSegment2D_F32 target,
											Se2_F32 tran ) {
		// create a copy so the original isn't modified
		paraLine = paraLine.copy();
		target = target.copy();

		// apply the transform to the two lines
		paraLine.setPoint( SePointOps_F32.transform( tran, paraLine.getPoint(), null ) );

		target.setA( SePointOps_F32.transform( tran, target.getA(), null ) );
		target.setB( SePointOps_F32.transform( tran, target.getB(), null ) );

		// should hit it dead center
		paraLine.setSlope( 0, 1 );
		paraLine.setAngle( paraLine.getAngle() + tran.getYaw() );
		float dist = Intersection2D_F32.intersection( paraLine, target );
		assertEquals( 1, dist, GrlConstants.FLOAT_TEST_TOL );

		// should hit dead center, but negative
		paraLine.setSlope( 0, -1 );
		paraLine.setAngle( paraLine.getAngle() + tran.getYaw() );
		dist = Intersection2D_F32.intersection( paraLine, target );
		assertEquals( -1, dist, GrlConstants.FLOAT_TEST_TOL );

		// should miss it to the left
		paraLine.setSlope( -1.1f, 1 );
		paraLine.setAngle( paraLine.getAngle() + tran.getYaw() );
		dist = Intersection2D_F32.intersection( paraLine, target );
		assertTrue( Float.isNaN( dist ) );

		// should miss it to the right
		paraLine.setSlope( 1.1f, 1 );
		paraLine.setAngle( paraLine.getAngle() + tran.getYaw() );
		dist = Intersection2D_F32.intersection( paraLine, target );
		assertTrue( Float.isNaN( dist ) );
	}

	@Test
	public void intersection_l_to_l_parametric_pt() {
		LineParametric2D_F32 a = new LineParametric2D_F32(2,3,1,0);
		LineParametric2D_F32 b = new LineParametric2D_F32(-2,-4,0,1);

		Point2D_F32 found = Intersection2D_F32.intersection(a,b,null);
		assertEquals( -2, found.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3, found.y, GrlConstants.FLOAT_TEST_TOL);

		LineParametric2D_F32 c = new LineParametric2D_F32(-8,2,0,1);

		assertTrue(null == Intersection2D_F32.intersection(b,c,null));
	}

	@Test
	public void intersection_l_to_l_parametric_t() {
		LineParametric2D_F32 a = new LineParametric2D_F32(2,3,1,0);
		LineParametric2D_F32 b = new LineParametric2D_F32(-2,-4,0,1);

		float t = Intersection2D_F32.intersection(a,b);

		Point2D_F32 found = new Point2D_F32(2+t,3);

		assertEquals( -2, found.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(3, found.y, GrlConstants.FLOAT_TEST_TOL);

		LineParametric2D_F32 c = new LineParametric2D_F32(-8,2,0,1);

		assertTrue(Float.isNaN(Intersection2D_F32.intersection(b, c)));
	}
	
	@Test
	public void intersection_l_to_l_general() {
		// check two arbitrary lines
		LineGeneral2D_F32 a = new LineGeneral2D_F32(1,2,3);
		LineGeneral2D_F32 b = new LineGeneral2D_F32(2,-1,0.5f);

		Point3D_F32 found = Intersection2D_F32.intersection(a,b,null);
		assertEquals(0,a.A*found.x/found.z+a.B*found.y/found.z+a.C, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(0,a.A*found.x+a.B*found.y+a.C*found.z, GrlConstants.FLOAT_TEST_TOL);

		// give it two parallel lines
		a = new LineGeneral2D_F32(1,2,3);
		b = new LineGeneral2D_F32(1,2,0.5f);

		Intersection2D_F32.intersection(a,b,found);
		assertEquals(0,found.z,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(0,a.A*found.x+a.B*found.y+a.C*found.z, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void intersects_rect_corners() {
		// check several positive cases
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(0,0,100,120),true);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(10,12,99,119),true);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(50,50,200,200),true);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(-10,-10,10,10),true);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(90,-10,105,1),true);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(90,5,105,105),true);

		// negative cases
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(200,200,300,305),false);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(-200,-200,-10,-10),false);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(0,-20,100,-5),false);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(0,125,100,130),false);

		// edge cases
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(0,0,0,0),false);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(100,120,100,120),false);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(-10,0,0,120),false);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(100,0,105,120),false);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(0,-10,100,0),false);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(0,120,100,125),false);
	}

	private void check( RectangleCorner2D_F32 a , RectangleCorner2D_F32 b , boolean expected ) {
		assertTrue(expected==Intersection2D_F32.intersects(a,b));
	}

	@Test
	public void intersection_rect_corners() {
		// check several positive cases
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(0,0,100,120),
				new RectangleCorner2D_F32(0,0,100,120));
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(10,12,99,119),
				new RectangleCorner2D_F32(10,12,99,119));
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(50,50,200,200),
				new RectangleCorner2D_F32(50,50,100,120));
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(-10,-10,10,10),
				new RectangleCorner2D_F32(0,0,10,10));
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(90,-10,105,1),
				new RectangleCorner2D_F32(90,0,100,1));
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(90,5,105,105),
				new RectangleCorner2D_F32(90,5,100,105));

		// negative cases
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(200,200,300,305),null);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(-200,-200,-10,-10),null);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(0,-20,100,-5),null);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(0,125,100,130),null);

		// edge cases
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(0,0,0,0),null);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(100,120,100,120),null);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(-10,0,0,120),null);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(100,0,105,120),null);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(0,-10,100,0),null);
		check( new RectangleCorner2D_F32(0,0,100,120),new RectangleCorner2D_F32(0,120,100,125),null);
	}

	private void check( RectangleCorner2D_F32 a , RectangleCorner2D_F32 b , RectangleCorner2D_F32 expected ) {
		if( expected == null ) {
			assertFalse(Intersection2D_F32.intersection(a, b, null));
			return;
		}

		RectangleCorner2D_F32 found = new RectangleCorner2D_F32();
		assertTrue(Intersection2D_F32.intersection(a, b, found));

		assertEquals(expected.x0,found.x0,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(expected.x1,found.x1,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(expected.y0,found.y0,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(expected.y1,found.y1,GrlConstants.FLOAT_TEST_TOL);
	}
}
