/*
 * Copyright (C) 2011-2015, Peter Abeles. All Rights Reserved.
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

package georegression.metric;

import georegression.misc.GrlConstants;
import georegression.struct.line.LineGeneral2D_F32;
import georegression.struct.line.LineParametric2D_F32;
import georegression.struct.line.LineSegment2D_F32;
import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point3D_F32;
import georegression.struct.se.Se2_F32;
import georegression.struct.shapes.Polygon2D_F32;
import georegression.struct.shapes.Rectangle2D_F32;
import georegression.struct.shapes.RectangleLength2D_F32;
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
		poly.vertexes.data[0].set(-1,-1);
		poly.vertexes.data[1].set(1, -1);
		poly.vertexes.data[2].set(1, 1);
		poly.vertexes.data[3].set(-1, 1);

		Point2D_F32 online = new Point2D_F32(1,-1);
		Point2D_F32 inside = new Point2D_F32(0.5f,0.5f);
		Point2D_F32 outside = new Point2D_F32(1.5f,0.5f);

		assertFalse(Intersection2D_F32.containConvex(poly,online));
		assertTrue(Intersection2D_F32.containConvex(poly,inside));
		assertFalse(Intersection2D_F32.containConvex(poly,outside));

		// change the order of the vertexes
		poly.vertexes.data[0].set(-1, 1);
		poly.vertexes.data[1].set(1, 1);
		poly.vertexes.data[2].set(1, -1);
		poly.vertexes.data[3].set(-1,-1);

		assertFalse(Intersection2D_F32.containConvex(poly,online));
		assertTrue(Intersection2D_F32.containConvex(poly,inside));
		assertFalse(Intersection2D_F32.containConvex(poly,outside));
	}

	@Test
	public void containConcave_rectangle() {
		Polygon2D_F32 poly = new Polygon2D_F32(4);
		poly.vertexes.data[0].set(-1,-1);
		poly.vertexes.data[1].set(1, -1);
		poly.vertexes.data[2].set(1, 1);
		poly.vertexes.data[3].set(-1, 1);

		assertTrue(Intersection2D_F32.containConcave(poly, new Point2D_F32(0,0)));

		// perimeter cases intentionally not handled here

		assertFalse(Intersection2D_F32.containConcave(poly, new Point2D_F32(2,0)));
		assertFalse(Intersection2D_F32.containConcave(poly, new Point2D_F32(-2,0)));
		assertFalse(Intersection2D_F32.containConcave(poly, new Point2D_F32(0,2)));
		assertFalse(Intersection2D_F32.containConcave(poly, new Point2D_F32(0,-2)));
	}

	@Test
	public void containConcave_concave() {
		Polygon2D_F32 poly = new Polygon2D_F32(5);
		poly.vertexes.data[0].set(-1,-1);
		poly.vertexes.data[1].set( 0, 0);
		poly.vertexes.data[2].set(1, -1);
		poly.vertexes.data[3].set(1, 1);
		poly.vertexes.data[4].set(-1, 1);

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

		// check parallel closestPoint
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
	public void intersection_l_to_l_general_3D() {
		// check two arbitrary lines
		LineGeneral2D_F32 a = new LineGeneral2D_F32(1,2,3);
		LineGeneral2D_F32 b = new LineGeneral2D_F32(2,-1,0.5f);

		Point3D_F32 found = Intersection2D_F32.intersection(a,b,(Point3D_F32)null);
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
	public void intersection_l_to_l_general_2D() {
		// check two arbitrary lines
		LineGeneral2D_F32 a = new LineGeneral2D_F32(1,2,3);
		LineGeneral2D_F32 b = new LineGeneral2D_F32(2,-1,0.5f);

		Point2D_F32 found = Intersection2D_F32.intersection(a,b,(Point2D_F32)null);
		assertEquals(0,a.A*found.x+a.B*found.y+a.C, GrlConstants.FLOAT_TEST_TOL);

		// give it two parallel lines
		a = new LineGeneral2D_F32(1,2,3);
		b = new LineGeneral2D_F32(1,2,0.5f);

		assertTrue(null == Intersection2D_F32.intersection(a, b, found));
	}

	@Test
	public void intersects_rect_corners() {
		// check several positive cases
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(0,0,100,120),true);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(10,12,99,119),true);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(50,50,200,200),true);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(-10,-10,10,10),true);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(90,-10,105,1),true);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(90,5,105,105),true);

		// negative cases
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(200,200,300,305),false);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(-200,-200,-10,-10),false);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(0,-20,100,-5),false);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(0,125,100,130),false);

		// edge cases
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(0,0,0,0),false);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(100,120,100,120),false);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(-10,0,0,120),false);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(100,0,105,120),false);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(0,-10,100,0),false);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(0,120,100,125),false);
	}

	private void check( Rectangle2D_F32 a , Rectangle2D_F32 b , boolean expected ) {
		assertTrue(expected==Intersection2D_F32.intersects(a,b));
	}

	@Test
	public void intersection_rect_corners() {
		// check several positive cases
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(0,0,100,120),
				new Rectangle2D_F32(0,0,100,120));
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(10,12,99,119),
				new Rectangle2D_F32(10,12,99,119));
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(50,50,200,200),
				new Rectangle2D_F32(50,50,100,120));
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(-10,-10,10,10),
				new Rectangle2D_F32(0,0,10,10));
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(90,-10,105,1),
				new Rectangle2D_F32(90,0,100,1));
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(90,5,105,105),
				new Rectangle2D_F32(90,5,100,105));

		// negative cases
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(200,200,300,305),null);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(-200,-200,-10,-10),null);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(0,-20,100,-5),null);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(0,125,100,130),null);

		// edge cases
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(0,0,0,0),null);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(100,120,100,120),null);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(-10,0,0,120),null);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(100,0,105,120),null);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(0,-10,100,0),null);
		check( new Rectangle2D_F32(0,0,100,120),new Rectangle2D_F32(0,120,100,125),null);
	}

	private void check( Rectangle2D_F32 a , Rectangle2D_F32 b , Rectangle2D_F32 expected ) {
		if( expected == null ) {
			assertFalse(Intersection2D_F32.intersection(a, b, null));
			return;
		}

		Rectangle2D_F32 found = new Rectangle2D_F32();
		assertTrue(Intersection2D_F32.intersection(a, b, found));

		assertEquals(expected.p0.x,found.p0.x,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(expected.p1.x,found.p1.x,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(expected.p0.y,found.p0.y,GrlConstants.FLOAT_TEST_TOL);
		assertEquals(expected.p1.y,found.p1.y,GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void contains_rectLength_pt() {
		RectangleLength2D_F32 rect = new RectangleLength2D_F32(-10,-5,5,10);

		assertTrue(Intersection2D_F32.contains(rect,-10,-5));
		assertTrue(Intersection2D_F32.contains(rect,-6,4));
		assertTrue(Intersection2D_F32.contains(rect,-9.9f,-4.99f));
		assertTrue(Intersection2D_F32.contains(rect,-6.001f,4.99f));

		assertTrue(Intersection2D_F32.contains(rect,-5.99f,4));
		assertTrue(Intersection2D_F32.contains(rect,-10,4.001f));

		assertFalse(Intersection2D_F32.contains(rect,-11,-5));
		assertFalse(Intersection2D_F32.contains(rect,-10,-6));
		assertFalse(Intersection2D_F32.contains(rect,-5,4));
		assertFalse(Intersection2D_F32.contains(rect,-6,5));
	}

	@Test
	public void contains2_rectLength_pt() {
		RectangleLength2D_F32 rect = new RectangleLength2D_F32(-10,-5,5,10);

		assertTrue(Intersection2D_F32.contains2(rect, -10, -5));
		assertTrue(Intersection2D_F32.contains2(rect, -6, 4));
		assertTrue(Intersection2D_F32.contains2(rect, -9.9f, -4.99f));
		assertTrue(Intersection2D_F32.contains2(rect, -6.001f, 4.99f));

		assertTrue(Intersection2D_F32.contains2(rect, -5.99f, 4));
		assertTrue(Intersection2D_F32.contains2(rect, -10, 4.001f));

		assertFalse(Intersection2D_F32.contains2(rect, -11, -5));
		assertFalse(Intersection2D_F32.contains2(rect, -10, -6));

		assertTrue(Intersection2D_F32.contains2(rect, -5, 4));
		assertTrue(Intersection2D_F32.contains2(rect, -6, 5));
	}

	@Test
	public void contains_rect_pt() {
		Rectangle2D_F32 rect = new Rectangle2D_F32(-10,-5,-5,5);

		assertTrue(Intersection2D_F32.contains(rect,-10,-5));
		assertTrue(Intersection2D_F32.contains(rect,-6,4));
		assertTrue(Intersection2D_F32.contains(rect,-9.9f,-4.99f));
		assertTrue(Intersection2D_F32.contains(rect,-6.001f,4.99f));

		assertTrue(Intersection2D_F32.contains(rect,-5.99f,4));
		assertTrue(Intersection2D_F32.contains(rect,-10,4.001f));

		assertFalse(Intersection2D_F32.contains(rect,-11,-5));
		assertFalse(Intersection2D_F32.contains(rect,-10,-6));
		assertFalse(Intersection2D_F32.contains(rect,-5,4));
		assertFalse(Intersection2D_F32.contains(rect,-6,5));
	}

	@Test
	public void contains2_rect_pt() {
		Rectangle2D_F32 rect = new Rectangle2D_F32(-10,-5,-5,5);

		assertTrue(Intersection2D_F32.contains2(rect, -10, -5));
		assertTrue(Intersection2D_F32.contains2(rect, -6, 4));
		assertTrue(Intersection2D_F32.contains2(rect, -9.9f, -4.99f));
		assertTrue(Intersection2D_F32.contains2(rect, -6.001f, 4.99f));

		assertTrue(Intersection2D_F32.contains2(rect, -5.99f, 4));
		assertTrue(Intersection2D_F32.contains2(rect, -10, 4.001f));

		assertFalse(Intersection2D_F32.contains2(rect, -11, -5));
		assertFalse(Intersection2D_F32.contains2(rect, -10, -6));

		assertTrue(Intersection2D_F32.contains2(rect, -5, 4));
		assertTrue(Intersection2D_F32.contains2(rect, -6, 5));
	}
}
