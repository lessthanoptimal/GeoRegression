/*
 * Copyright (C) 2021, Peter Abeles. All Rights Reserved.
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

import georegression.geometry.UtilEllipse_F64;
import georegression.misc.GrlConstants;
import georegression.struct.curve.EllipseRotated_F64;
import georegression.struct.line.LineGeneral2D_F64;
import georegression.struct.line.LineParametric2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector2D_F64;
import georegression.struct.se.Se2_F64;
import georegression.struct.shapes.Polygon2D_F64;
import georegression.struct.shapes.Quadrilateral_F64;
import georegression.struct.shapes.Rectangle2D_F64;
import georegression.struct.shapes.RectangleLength2D_F64;
import georegression.transform.se.SePointOps_F64;
import org.ejml.UtilEjml;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Peter Abeles
 */
public class TestIntersection2D_F64 {
	Random rand = new Random( 234 );

	@Test void containsConvex() {
		Polygon2D_F64 poly = new Polygon2D_F64(4);
		poly.vertexes.data[0].setTo(-1,-1);
		poly.vertexes.data[1].setTo(1, -1);
		poly.vertexes.data[2].setTo(1, 1);
		poly.vertexes.data[3].setTo(-1, 1);

		Point2D_F64 online = new Point2D_F64(1,-1);
		Point2D_F64 inside = new Point2D_F64(0.5,0.5);
		Point2D_F64 outside = new Point2D_F64(1.5,0.5);

		assertFalse(Intersection2D_F64.containsConvex(poly,online));
		assertTrue(Intersection2D_F64.containsConvex(poly,inside));
		assertFalse(Intersection2D_F64.containsConvex(poly,outside));

		// change the order of the vertexes
		poly.flip();

		assertFalse(Intersection2D_F64.containsConvex(poly,online));
		assertTrue(Intersection2D_F64.containsConvex(poly,inside));
		assertFalse(Intersection2D_F64.containsConvex(poly,outside));
	}

	@Test void containsConvex2() {
		Polygon2D_F64 poly = new Polygon2D_F64(4);
		poly.vertexes.data[0].setTo(-1,-1);
		poly.vertexes.data[1].setTo(1, -1);
		poly.vertexes.data[2].setTo(1, 1);
		poly.vertexes.data[3].setTo(-1, 1);

		Point2D_F64 online = new Point2D_F64(1,-1);
		Point2D_F64 inside = new Point2D_F64(0.5,0.5);
		Point2D_F64 outside = new Point2D_F64(1.5,0.5);

		assertTrue(Intersection2D_F64.containsConvex2(poly,online.x,online.y));
		assertTrue(Intersection2D_F64.containsConvex2(poly,inside.x,inside.y));
		assertFalse(Intersection2D_F64.containsConvex2(poly,outside.x,outside.y));

		// change the order of the vertexes
		poly.flip();

		assertTrue(Intersection2D_F64.containsConvex2(poly,online.x,online.y));
		assertTrue(Intersection2D_F64.containsConvex2(poly,inside.x,inside.y));
		assertFalse(Intersection2D_F64.containsConvex2(poly,outside.x,outside.y));
	}

	@Test void containsConcave_rectangle() {
		Polygon2D_F64 poly = new Polygon2D_F64(4);
		poly.vertexes.data[0].setTo(-1,-1);
		poly.vertexes.data[1].setTo(1, -1);
		poly.vertexes.data[2].setTo(1, 1);
		poly.vertexes.data[3].setTo(-1, 1);

		assertTrue(Intersection2D_F64.containsConcave(poly, new Point2D_F64(0, 0)));

		// perimeter cases intentionally not handled here

		assertFalse(Intersection2D_F64.containsConcave(poly, new Point2D_F64(2, 0)));
		assertFalse(Intersection2D_F64.containsConcave(poly, new Point2D_F64(-2, 0)));
		assertFalse(Intersection2D_F64.containsConcave(poly, new Point2D_F64(0, 2)));
		assertFalse(Intersection2D_F64.containsConcave(poly, new Point2D_F64(0, -2)));
	}

	@Test void contains_quadrilateral() {
		Quadrilateral_F64 quad = new Quadrilateral_F64();
		quad.a.setTo(-1, -1);
		quad.b.setTo(1, -1);
		quad.c.setTo(1, 1);
		quad.d.setTo(-1, 1);

		assertTrue(Intersection2D_F64.contains(quad, new Point2D_F64(0, 0)));

		// perimeter cases intentionally not handled here

		assertFalse(Intersection2D_F64.contains(quad, new Point2D_F64(2, 0)));
		assertFalse(Intersection2D_F64.contains(quad, new Point2D_F64(-2, 0)));
		assertFalse(Intersection2D_F64.contains(quad, new Point2D_F64(0, 2)));
		assertFalse(Intersection2D_F64.contains(quad, new Point2D_F64(0, -2)));
	}

	@Test void containTriangle() {
		Point2D_F64 a = new Point2D_F64(1,2);
		Point2D_F64 b = new Point2D_F64(4,2);
		Point2D_F64 c = new Point2D_F64(4,5);

		Point2D_F64 inside = new Point2D_F64(3,3);
		Point2D_F64 outside = new Point2D_F64(-10,2);

		assertTrue(Intersection2D_F64.containTriangle(a, b, c, inside));
		assertFalse(Intersection2D_F64.containTriangle(a, b, c, outside));
	}

	@Test void containEllipseRotated() {

		EllipseRotated_F64 ellipse = new EllipseRotated_F64(5,6,4,3, GrlConstants.PId2);

		assertFalse(Intersection2D_F64.contains(ellipse,0,0));
		assertTrue(Intersection2D_F64.contains(ellipse,5,6));
		assertTrue(Intersection2D_F64.contains(ellipse,5,6+4 - GrlConstants.TEST_F64));
		assertTrue(Intersection2D_F64.contains(ellipse,5+3.0 - GrlConstants.TEST_F64,6));

		assertFalse(Intersection2D_F64.contains(ellipse,5,6+4.0 + GrlConstants.TEST_F64));
		assertFalse(Intersection2D_F64.contains(ellipse,5+3 + GrlConstants.TEST_F64,6));
	}

	/** Polygon has no elements. This caused an exception at one point */
	@Test void containConcave_zero_one() {
		// there is no polygon so it can't be inside
		var poly = new Polygon2D_F64(0);
		assertFalse(Intersection2D_F64.containsConcave(poly, new Point2D_F64(0,0.5)));

		// a single point and the test point is identical. This is ambiguous. For now we define it ot be false
		poly.vertexes.grow().setTo(0, 0.5);
		assertFalse(Intersection2D_F64.containsConcave(poly, new Point2D_F64(0,0.5)));
	}

	/** See if it handles line points. A.k.a. line segment */
	@Test void containConcave_two() {
		var poly = new Polygon2D_F64(0);
		poly.vertexes.grow().setTo(0,0);
		poly.vertexes.grow().setTo(1,0);

		// Point not on the line
		assertFalse(Intersection2D_F64.containsConcave(poly, new Point2D_F64(0,0.5)));

		// The current implementation doesn't define behavior for points on the line. We will just call
		// this and make sure it doesn't throw an exception
		Intersection2D_F64.containsConcave(poly, new Point2D_F64(0.5,0));
		Intersection2D_F64.containsConcave(poly, new Point2D_F64(0.0,0));
	}

	@Test void containConcave_concave() {
		Polygon2D_F64 poly = new Polygon2D_F64(5);
		poly.vertexes.data[0].setTo(-1,-1);
		poly.vertexes.data[1].setTo( 0, 0);
		poly.vertexes.data[2].setTo(1, -1);
		poly.vertexes.data[3].setTo(1, 1);
		poly.vertexes.data[4].setTo(-1, 1);

		assertTrue(Intersection2D_F64.containsConcave(poly, new Point2D_F64(0,0.5)));
		assertTrue(Intersection2D_F64.containsConcave(poly, new Point2D_F64(-0.75,-0.25)));
		assertTrue(Intersection2D_F64.containsConcave(poly, new Point2D_F64(0.75,-0.25)));

		assertFalse(Intersection2D_F64.containsConcave(poly, new Point2D_F64(0,-0.5)));

		assertFalse(Intersection2D_F64.containsConcave(poly, new Point2D_F64(2,0)));
		assertFalse(Intersection2D_F64.containsConcave(poly, new Point2D_F64(-2,0)));
		assertFalse(Intersection2D_F64.containsConcave(poly, new Point2D_F64(0, 2)));
		assertFalse(Intersection2D_F64.containsConcave(poly, new Point2D_F64(0, -2)));
	}
	
	@Test void intersection_ls_to_ls() {
		// check positive, none pathological cases
		checkIntersection(new LineSegment2D_F64(0, 2, 2, 2), new LineSegment2D_F64(2, 0, 2, 3), new Point2D_F64(2, 2));
		checkIntersection(new LineSegment2D_F64(0, 2, 2, 0), new LineSegment2D_F64(0, 0, 2, 2), new Point2D_F64(1, 1));

		// check boundary conditions
		checkIntersection(new LineSegment2D_F64(0, 2, 2, 2), new LineSegment2D_F64(0, 0, 0, 2), new Point2D_F64(0, 2));
		checkIntersection(new LineSegment2D_F64(0, 2, 2, 2), new LineSegment2D_F64(2, 0, 2, 2), new Point2D_F64(2, 2));
		checkIntersection(new LineSegment2D_F64(1, 0, 1, 2), new LineSegment2D_F64(0, 0, 2, 0), new Point2D_F64(1, 0));

		// check negative
		checkIntersection(new LineSegment2D_F64(0, 2, 2, 2), new LineSegment2D_F64(0, 0, 0, 1.9), null);
		checkIntersection(new LineSegment2D_F64(0, 2, 2, 2), new LineSegment2D_F64(2, 0, 2, 1.9), null);
		checkIntersection(new LineSegment2D_F64(1, 0.1, 1, 2), new LineSegment2D_F64(0, 0, 2, 0), null);

		// check parallel closestPoint
		checkIntersection(new LineSegment2D_F64(0, 2, 0, 5), new LineSegment2D_F64(0, 1, 0, 3), null);
	}

	public void checkIntersection( LineSegment2D_F64 a, LineSegment2D_F64 b, @Nullable Point2D_F64 expected ) {
		Point2D_F64 found = Intersection2D_F64.intersection( a, b, null );
		if( found == null )
			assertNull(expected);
		else {
			assertNotNull(expected);
			assertEquals( found.getX(), expected.getX(), GrlConstants.TEST_F64);
			assertEquals( found.getY(), expected.getY(), GrlConstants.TEST_F64);
		}
	}

	@Test void intersects_ls_to_ls() {
		final double tol = UtilEjml.TEST_F64;
		// check positive, none pathological cases
		assertTrue(Intersection2D_F64.intersects(new LineSegment2D_F64(0, 2, 2, 2), new LineSegment2D_F64(2, 0, 2, 3), tol));
		assertTrue(Intersection2D_F64.intersects(new LineSegment2D_F64(0, 2, 2, 0), new LineSegment2D_F64(0, 0, 2, 2), tol));

		// The end points are inclusive and should be considered an intersection
		assertTrue(Intersection2D_F64.intersects(new LineSegment2D_F64(0, 2, 2, 2), new LineSegment2D_F64(0, 0, 0, 2), tol));
		assertTrue(Intersection2D_F64.intersects(new LineSegment2D_F64(0, 2, 2, 2), new LineSegment2D_F64(2, 0, 2, 2), tol));
		assertTrue(Intersection2D_F64.intersects(new LineSegment2D_F64(1, 0, 1, 2), new LineSegment2D_F64(0, 0, 2, 0), tol));

		// check negative
		assertFalse(Intersection2D_F64.intersects(new LineSegment2D_F64(0, 2, 2, 2), new LineSegment2D_F64(0, 0, 0, 1.9), tol));
		assertFalse(Intersection2D_F64.intersects(new LineSegment2D_F64(0, 2, 2, 2), new LineSegment2D_F64(2, 0, 2, 1.9), tol));
		assertFalse(Intersection2D_F64.intersects(new LineSegment2D_F64(1, 0.1, 1, 2), new LineSegment2D_F64(0, 0, 2, 0), tol));

		// check parallel
		assertTrue(Intersection2D_F64.intersects(new LineSegment2D_F64(0, 2, 0, 5), new LineSegment2D_F64(0, 1, 0, 3), tol));
		assertFalse(Intersection2D_F64.intersects(new LineSegment2D_F64(0, 2, 0, 5), new LineSegment2D_F64(0, -0.5, 0, 1.5), tol));
	}

	@Test void intersects2_ls_to_ls() {
		final double tol = UtilEjml.TEST_F64;
		// check positive, none pathological cases
		assertTrue(Intersection2D_F64.intersects2(new LineSegment2D_F64(0, 2, 2, 2), new LineSegment2D_F64(2, 0, 2, 3), tol));
		assertTrue(Intersection2D_F64.intersects2(new LineSegment2D_F64(0, 2, 2, 0), new LineSegment2D_F64(0, 0, 2, 2), tol));

		// Two end points touching will not be considered an intersection
		assertFalse(Intersection2D_F64.intersects2(new LineSegment2D_F64(0, 2, 2, 2), new LineSegment2D_F64(0, 0, 0, 2), tol));
		assertFalse(Intersection2D_F64.intersects2(new LineSegment2D_F64(0, 2, 2, 2), new LineSegment2D_F64(2, 0, 2, 2), tol));

		// However a line passing through an end point is an intersection
		assertTrue(Intersection2D_F64.intersects2(new LineSegment2D_F64(1, 0, 1, 2), new LineSegment2D_F64(0, 0, 2, 0), tol));

		// check clear negative
		assertFalse(Intersection2D_F64.intersects2(new LineSegment2D_F64(0, 2, 2, 2), new LineSegment2D_F64(0, 0, 0, 1.9), tol));
		assertFalse(Intersection2D_F64.intersects2(new LineSegment2D_F64(0, 2, 2, 2), new LineSegment2D_F64(2, 0, 2, 1.9), tol));
		assertFalse(Intersection2D_F64.intersects2(new LineSegment2D_F64(1, 0.1, 1, 2), new LineSegment2D_F64(0, 0, 2, 0), tol));

		// check parallel
		assertTrue(Intersection2D_F64.intersects2(new LineSegment2D_F64(0, 2, 0, 5), new LineSegment2D_F64(0, 1, 0, 3), tol));
		assertFalse(Intersection2D_F64.intersects2(new LineSegment2D_F64(0, 2, 0, 5), new LineSegment2D_F64(0, -0.5, 0, 1.5), tol));
	}

	/**
	 * Checks to see if the expected distance is returned and that the end points of the
	 * line segment are respected. The test cases are rotated around in a circle to test
	 * more geometric configurations
	 */
	@Test void intersection_p_to_ls() {
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
		assertEquals( 1, dist, GrlConstants.TEST_F64);

		// should hit dead center, but negative
		paraLine.setSlope( 0, -1 );
		paraLine.setAngle( paraLine.getAngle() + tran.getYaw() );
		dist = Intersection2D_F64.intersection( paraLine, target );
		assertEquals( -1, dist, GrlConstants.TEST_F64);

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

	@Test void intersection_l_to_l_parametric_pt() {
		LineParametric2D_F64 a = new LineParametric2D_F64(2,3,1,0);
		LineParametric2D_F64 b = new LineParametric2D_F64(-2,-4,0,1);

		Point2D_F64 found = Intersection2D_F64.intersection(a,b,null);
		assertNotNull(found);
		assertEquals( -2, found.x, GrlConstants.TEST_F64);
		assertEquals(3, found.y, GrlConstants.TEST_F64);

		LineParametric2D_F64 c = new LineParametric2D_F64(-8,2,0,1);

		assertNull(Intersection2D_F64.intersection(b, c, null));
	}

	@Test void intersection_r_to_r_parametric_pt() {
		LineParametric2D_F64 a = new LineParametric2D_F64(2,3,1,0);
		LineParametric2D_F64 b = new LineParametric2D_F64(-2,-4,0,-1);

		assertNull(Intersection2D_F64.intersection(a, b, true, null));
		b.slope.setTo(0,1);
		assertNull(Intersection2D_F64.intersection(a, b, true, null));
		assertNull(Intersection2D_F64.intersection(b, a, true, null));
		a.slope.setTo(-1,0);
		b.slope.setTo(0,-1);
		assertNull(Intersection2D_F64.intersection(a, b, true, null));
		assertNull(Intersection2D_F64.intersection(b, a, true, null));

		b.slope.setTo(0,1);
		Point2D_F64 found = Intersection2D_F64.intersection(a,b,true,null);
		assertNotNull(found);
		assertEquals(0,found.distance(-2,3), UtilEjml.TEST_F64);
	}

	@Test void intersection_l_to_l_parametric_t() {
		LineParametric2D_F64 a = new LineParametric2D_F64(2,3,1,0);
		LineParametric2D_F64 b = new LineParametric2D_F64(-2,-4,0,1);

		double t = Intersection2D_F64.intersection(a,b);

		Point2D_F64 found = new Point2D_F64(2+t,3);

		assertEquals( -2, found.x, GrlConstants.TEST_F64);
		assertEquals(3, found.y, GrlConstants.TEST_F64);

		LineParametric2D_F64 c = new LineParametric2D_F64(-8,2,0,1);

		assertTrue(Double.isNaN(Intersection2D_F64.intersection(b, c)));
	}

	@Test void intersection_l_to_l_points() {
		Point2D_F64 a0 = new Point2D_F64(3,20);
		Point2D_F64 a1 = new Point2D_F64(10,20);
		Point2D_F64 b0 = new Point2D_F64(7,5);
		Point2D_F64 b1 = new Point2D_F64(8,30);

		LineParametric2D_F64 a = new LineParametric2D_F64(a0,new Vector2D_F64(a1.x-a0.x,a1.y-a0.y));
		LineParametric2D_F64 b = new LineParametric2D_F64(b0,new Vector2D_F64(b1.x-b0.x,b1.y-b0.y));

		Point2D_F64 expected = Intersection2D_F64.intersection(a,b,null);
		Point2D_F64 found = Intersection2D_F64.intersection(a0,a1,b0,b1,null);

		assertNotNull(expected);
		assertNotNull(found);
		assertEquals(expected.x, found.x, GrlConstants.TEST_F64);
		assertEquals(expected.y, found.y, GrlConstants.TEST_F64);

	}
	
	@Test void intersection_l_to_l_general_3D() {
		// check two arbitrary lines
		LineGeneral2D_F64 a = new LineGeneral2D_F64(1,2,3);
		LineGeneral2D_F64 b = new LineGeneral2D_F64(2,-1,0.5);

		Point3D_F64 found = Intersection2D_F64.intersection(a,b,(Point3D_F64)null);
		assertEquals(0,a.A*found.x/found.z+a.B*found.y/found.z+a.C, GrlConstants.TEST_F64);
		assertEquals(0,a.A*found.x+a.B*found.y+a.C*found.z, GrlConstants.TEST_F64);

		// give it two parallel lines
		a = new LineGeneral2D_F64(1,2,3);
		b = new LineGeneral2D_F64(1,2,0.5);

		Intersection2D_F64.intersection(a, b, found);
		assertEquals(0,found.z,GrlConstants.TEST_F64);
		assertEquals(0, a.A * found.x + a.B * found.y + a.C * found.z, GrlConstants.TEST_F64);
	}

	@Test void intersection_l_to_l_general_2D() {
		// check two arbitrary lines
		LineGeneral2D_F64 a = new LineGeneral2D_F64(1,2,3);
		LineGeneral2D_F64 b = new LineGeneral2D_F64(2,-1,0.5);

		Point2D_F64 found = Intersection2D_F64.intersection(a,b,(Point2D_F64)null);
		assertNotNull(found);
		assertEquals(0,a.A*found.x+a.B*found.y+a.C, GrlConstants.TEST_F64);

		// give it two parallel lines
		a = new LineGeneral2D_F64(1,2,3);
		b = new LineGeneral2D_F64(1,2,0.5);

		assertNull(Intersection2D_F64.intersection(a, b, found));
	}

	@Test void intersects_rect_corners() {
		// check several positive cases
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(0,0,100,120),true);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(10,12,99,119),true);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(50,50,200,200),true);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(-10,-10,10,10),true);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(90,-10,105,1),true);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(90,5,105,105),true);

		// negative cases
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(200,200,300,305),false);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(-200,-200,-10,-10),false);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(0,-20,100,-5),false);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(0,125,100,130),false);

		// edge cases
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(0,0,0,0),false);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(100,120,100,120),false);
		check( new Rectangle2D_F64(0, 0, 100, 120), new Rectangle2D_F64(-10, 0, 0, 120), false);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(100,0,105,120),false);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(0,-10,100,0),false);
		check( new Rectangle2D_F64(0, 0, 100, 120), new Rectangle2D_F64(0, 120, 100, 125), false);
	}

	private void check( Rectangle2D_F64 a , Rectangle2D_F64 b , boolean expected ) {
		assertEquals(Intersection2D_F64.intersects(a, b), expected);
	}

	@Test void intersection_rect_corners() {
		// check several positive cases
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(0,0,100,120),
				new Rectangle2D_F64(0,0,100,120));
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(10,12,99,119),
				new Rectangle2D_F64(10,12,99,119));
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(50,50,200,200),
				new Rectangle2D_F64(50,50,100,120));
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(-10,-10,10,10),
				new Rectangle2D_F64(0,0,10,10));
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(90,-10,105,1),
				new Rectangle2D_F64(90,0,100,1));
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(90,5,105,105),
				new Rectangle2D_F64(90,5,100,105));

		// negative cases
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(200,200,300,305),null);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(-200,-200,-10,-10),null);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(0,-20,100,-5),null);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(0,125,100,130),null);

		// edge cases
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(0,0,0,0),null);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(100,120,100,120),null);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(-10,0,0,120),null);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(100,0,105,120),null);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(0,-10,100,0),null);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(0,120,100,125),null);
	}

	private void check( Rectangle2D_F64 a , Rectangle2D_F64 b , @Nullable Rectangle2D_F64 expected ) {
		if( expected == null ) {
			assertFalse(Intersection2D_F64.intersection(a, b, new Rectangle2D_F64()));
			return;
		}

		Rectangle2D_F64 found = new Rectangle2D_F64();
		assertTrue(Intersection2D_F64.intersection(a, b, found));

		assertEquals(expected.p0.x,found.p0.x,GrlConstants.TEST_F64);
		assertEquals(expected.p1.x,found.p1.x,GrlConstants.TEST_F64);
		assertEquals(expected.p0.y,found.p0.y,GrlConstants.TEST_F64);
		assertEquals(expected.p1.y,found.p1.y,GrlConstants.TEST_F64);
	}

	@Test void contains_rectLength_pt() {
		RectangleLength2D_F64 rect = new RectangleLength2D_F64(-10,-5,5,10);

		assertTrue(Intersection2D_F64.contains(rect,-10,-5));
		assertTrue(Intersection2D_F64.contains(rect,-6,4));
		assertTrue(Intersection2D_F64.contains(rect,-9.9,-4.99));
		assertTrue(Intersection2D_F64.contains(rect,-6.001,4.99));

		assertTrue(Intersection2D_F64.contains(rect,-5.99,4));
		assertTrue(Intersection2D_F64.contains(rect, -10, 4.001));

		assertFalse(Intersection2D_F64.contains(rect, -11, -5));
		assertFalse(Intersection2D_F64.contains(rect, -10, -6));
		assertFalse(Intersection2D_F64.contains(rect, -5, 4));
		assertFalse(Intersection2D_F64.contains(rect, -6, 5));
	}

	@Test void contains2_rectLength_pt() {
		RectangleLength2D_F64 rect = new RectangleLength2D_F64(-10,-5,5,10);

		assertTrue(Intersection2D_F64.contains2(rect, -10, -5));
		assertTrue(Intersection2D_F64.contains2(rect, -6, 4));
		assertTrue(Intersection2D_F64.contains2(rect, -9.9, -4.99));
		assertTrue(Intersection2D_F64.contains2(rect, -6.001, 4.99));

		assertTrue(Intersection2D_F64.contains2(rect, -5.99, 4));
		assertTrue(Intersection2D_F64.contains2(rect, -10, 4.001));

		assertFalse(Intersection2D_F64.contains2(rect, -11, -5));
		assertFalse(Intersection2D_F64.contains2(rect, -10, -6));

		assertTrue(Intersection2D_F64.contains2(rect, -5, 4));
		assertTrue(Intersection2D_F64.contains2(rect, -6, 5));
	}

	@Test void contains_rect_pt() {
		Rectangle2D_F64 rect = new Rectangle2D_F64(-10,-5,-5,5);

		assertTrue(Intersection2D_F64.contains(rect, -10, -5));
		assertTrue(Intersection2D_F64.contains(rect, -6, 4));
		assertTrue(Intersection2D_F64.contains(rect, -9.9, -4.99));
		assertTrue(Intersection2D_F64.contains(rect, -6.001, 4.99));

		assertTrue(Intersection2D_F64.contains(rect, -5.99, 4));
		assertTrue(Intersection2D_F64.contains(rect, -10, 4.001));

		assertFalse(Intersection2D_F64.contains(rect, -11, -5));
		assertFalse(Intersection2D_F64.contains(rect, -10, -6));
		assertFalse(Intersection2D_F64.contains(rect, -5, 4));
		assertFalse(Intersection2D_F64.contains(rect, -6, 5));
	}

	@Test void contains2_rect_pt() {
		Rectangle2D_F64 rect = new Rectangle2D_F64(-10,-5,-5,5);

		assertTrue(Intersection2D_F64.contains2(rect, -10, -5));
		assertTrue(Intersection2D_F64.contains2(rect, -6, 4));
		assertTrue(Intersection2D_F64.contains2(rect, -9.9, -4.99));
		assertTrue(Intersection2D_F64.contains2(rect, -6.001, 4.99));

		assertTrue(Intersection2D_F64.contains2(rect, -5.99, 4));
		assertTrue(Intersection2D_F64.contains2(rect, -10, 4.001));

		assertFalse(Intersection2D_F64.contains2(rect, -11, -5));
		assertFalse(Intersection2D_F64.contains2(rect, -10, -6));

		assertTrue(Intersection2D_F64.contains2(rect, -5, 4));
		assertTrue(Intersection2D_F64.contains2(rect, -6, 5));
	}

	@Test void intersectionArea_rect_rect() {
		// check several positive cases
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(0,0,100,120), 100*120);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(10,12,99,119), 89*107);
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(50,50,200,200), 50*70 );
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(-10,-10,10,10), 10*10 );
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(90,-10,105,1), 10*1 );
		check( new Rectangle2D_F64(0,0,100,120),new Rectangle2D_F64(90,5,105,105), 10*100 );

		// negative cases
		check(new Rectangle2D_F64(0, 0, 100, 120), new Rectangle2D_F64(200, 200, 300, 305), 0);
		check(new Rectangle2D_F64(0, 0, 100, 120), new Rectangle2D_F64(-200, -200, -10, -10), 0);
		check(new Rectangle2D_F64(0, 0, 100, 120), new Rectangle2D_F64(0, -20, 100, -5), 0);
		check(new Rectangle2D_F64(0, 0, 100, 120), new Rectangle2D_F64(0, 125, 100, 130), 0);

		// edge cases
		check(new Rectangle2D_F64(0, 0, 100, 120), new Rectangle2D_F64(0, 0, 0, 0), 0);
		check(new Rectangle2D_F64(0, 0, 100, 120), new Rectangle2D_F64(100, 120, 100, 120), 0);
		check(new Rectangle2D_F64(0, 0, 100, 120), new Rectangle2D_F64(-10, 0, 0, 120), 0);
		check(new Rectangle2D_F64(0, 0, 100, 120), new Rectangle2D_F64(100, 0, 105, 120), 0);
		check(new Rectangle2D_F64(0, 0, 100, 120), new Rectangle2D_F64(0, -10, 100, 0), 0);
		check(new Rectangle2D_F64(0, 0, 100, 120), new Rectangle2D_F64(0, 120, 100, 125), 0);
	}

	private void check( Rectangle2D_F64 a , Rectangle2D_F64 b , double expected ) {
		assertEquals(expected,Intersection2D_F64.intersectionArea(a,b),GrlConstants.TEST_F64);
	}

	@Test void intersectionArea_poly_to_poly() {
		Polygon2D_F64 A = new Polygon2D_F64(new double[][]{{0,0},{2,0},{2,4},{0,4}});
		Polygon2D_F64 B = A.copy();

		assertEquals(8,Intersection2D_F64.intersectionArea(A,B), GrlConstants.TEST_SQ_F64);

		// make sure the order doesn't matter
		B.flip();
		assertEquals(8,Intersection2D_F64.intersectionArea(A,B), GrlConstants.TEST_SQ_F64);
	}

	@Test void intersection_poly_to_poly_Simple() {
		// A should have an area of 2*4 + 2*2
		// It's a concave polygon with no self intersections
		Polygon2D_F64 A = new Polygon2D_F64(new double[][]{{0,0},{4,0},{4,4},{2,2},{0,4}});
		Polygon2D_F64 B = A.copy();

		assertEquals(12,Intersection2D_F64.intersectionArea(A,B), GrlConstants.TEST_SQ_F64);

		// make sure the order doesn't matter
		B.flip();
		assertEquals(12,Intersection2D_F64.intersectionArea(A,B), GrlConstants.TEST_SQ_F64);

		// Now B will be a convex polygon that the intersects left side
		B = new Polygon2D_F64(new double[][]{{0,0},{2,0},{2,6},{0,4}});
		assertEquals(1.5*2*2,Intersection2D_F64.intersectionArea(A,B), GrlConstants.TEST_SQ_F64);
	}

	@Test void line_ellipse() {
		// easy cases where the ellipse is at the original aligned to the coordinate axis
		EllipseRotated_F64 ellipse = new EllipseRotated_F64(0,0,2,1,0);

		checkIntersection(new LineGeneral2D_F64(1,0,0),ellipse); // vertical line
		checkIntersection(new LineGeneral2D_F64(0,1,0),ellipse); // horizontal line
		checkIntersection(new LineGeneral2D_F64(0.25,2.0,1),ellipse); // angled line
		checkIntersection(new LineGeneral2D_F64(2.0,0.25,1),ellipse); // angled line
		checkSingleIntersection(new LineGeneral2D_F64(1,0,-2),ellipse); // single point
		checkSingleIntersection(new LineGeneral2D_F64(0,1,-1),ellipse); // single point
		checkNoIntersection(new LineGeneral2D_F64(1,0,20),ellipse);// no intersection

		// Test to see if the rotation is handled correctly. Still centered at the original but rotated 90 degrees
		ellipse = new EllipseRotated_F64(0,0,2,1, GrlConstants.PId2);

		checkIntersection(new LineGeneral2D_F64(1,0,0),ellipse); // vertical line
		checkIntersection(new LineGeneral2D_F64(0,1,0),ellipse); // horizontal line
		checkIntersection(new LineGeneral2D_F64(0.25,2.0,1),ellipse); // angled line
		checkIntersection(new LineGeneral2D_F64(2.0,0.25,1),ellipse); // angled line
		checkSingleIntersection(new LineGeneral2D_F64(1,0,-1),ellipse); // single point
		checkSingleIntersection(new LineGeneral2D_F64(0,1,-2),ellipse); // single point
		checkNoIntersection(new LineGeneral2D_F64(1,0,20),ellipse);// no intersection

		//  Offset it from the original
		ellipse = new EllipseRotated_F64(0.1,0,2,1,0);

		checkIntersection(new LineGeneral2D_F64(1,0,0),ellipse); // vertical line
		checkIntersection(new LineGeneral2D_F64(0,1,0),ellipse); // horizontal line

		// Hardest case. not at origin and rotated an arbitrary amount
		ellipse = new EllipseRotated_F64(0.12,-0.13,2,1,0.4);
		checkIntersection(new LineGeneral2D_F64(1,0,0),ellipse); // vertical line
		checkIntersection(new LineGeneral2D_F64(0,1,0),ellipse); // horizontal line
		checkIntersection(new LineGeneral2D_F64(0.25,2.0,1),ellipse); // angled line
		checkIntersection(new LineGeneral2D_F64(2.0,0.25,1),ellipse); // angled line
		checkIntersection(new LineGeneral2D_F64(1,0,-2),ellipse); // single point
		checkIntersection(new LineGeneral2D_F64(0,1,-1),ellipse); // single point
		checkNoIntersection(new LineGeneral2D_F64(1,0,20),ellipse);// no intersection
	}

	private void checkNoIntersection( LineGeneral2D_F64 line , EllipseRotated_F64 ellipse ) {
		Point2D_F64 a = new Point2D_F64();
		Point2D_F64 b = new Point2D_F64();

		assertEquals(0,Intersection2D_F64.intersection(line,ellipse,a,b,-1));
	}

	private void checkIntersection( LineGeneral2D_F64 line , EllipseRotated_F64 ellipse ) {
		Point2D_F64 a = new Point2D_F64();
		Point2D_F64 b = new Point2D_F64();

		assertEquals(2,Intersection2D_F64.intersection(line,ellipse,a,b, -1));

		// use the line and ellipse definition to check solution
		assertEquals(0, line.evaluate(a.x, a.y), GrlConstants.TEST_F64);
		assertEquals(0, line.evaluate(b.x, b.y), GrlConstants.TEST_F64);

		assertEquals(1.0, UtilEllipse_F64.evaluate(a.x, a.y, ellipse), GrlConstants.TEST_F64);
		assertEquals(1.0, UtilEllipse_F64.evaluate(b.x, b.y, ellipse), GrlConstants.TEST_F64);
	}

	private void checkSingleIntersection( LineGeneral2D_F64 line , EllipseRotated_F64 ellipse ) {
		Point2D_F64 a = new Point2D_F64();
		Point2D_F64 b = new Point2D_F64();

		assertEquals(1,Intersection2D_F64.intersection(line,ellipse,a,b, -1));

		assertEquals(0, a.distance(b), GrlConstants.TEST_F64);

		// use the line and ellipse definition to check solution
		assertEquals(0, line.evaluate(a.x, a.y), GrlConstants.TEST_F64);

		assertEquals(1.0, UtilEllipse_F64.evaluate(a.x, a.y, ellipse), GrlConstants.TEST_F64);
	}

}
