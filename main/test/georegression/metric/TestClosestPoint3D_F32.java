/*
 * Copyright (C) 2011-2017, Peter Abeles. All Rights Reserved.
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

import georegression.geometry.UtilLine3D_F32;
import georegression.geometry.UtilPlane3D_F32;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineParametric3D_F32;
import georegression.struct.line.LineSegment3D_F32;
import georegression.struct.plane.PlaneGeneral3D_F32;
import georegression.struct.plane.PlaneNormal3D_F32;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Peter Abeles
 */
public class TestClosestPoint3D_F32 {
	/**
	 * Compute truth from 3 random points then see if the 3rd point is found again.
	 */
	@Test
	public void closestPoint_line() {
		Point3D_F32 a = new Point3D_F32( 1, 1, 1 );
		Point3D_F32 b = new Point3D_F32( 1.5f, -2.5f, 9 );
		Point3D_F32 c = new Point3D_F32( 10.1f, 6, -3 );

		Vector3D_F32 va = new Vector3D_F32( a, b );
		Vector3D_F32 vc = new Vector3D_F32( c, b );

		LineParametric3D_F32 lineA = new LineParametric3D_F32( a, va );
		LineParametric3D_F32 lineB = new LineParametric3D_F32( c, vc );

		Point3D_F32 foundB = ClosestPoint3D_F32.closestPoint(lineA, lineB, null);

		assertTrue( b.isIdentical( foundB, GrlConstants.TEST_F32) );
		checkIsClosest(foundB,lineA,lineB);

		// check two arbitrary lines
		lineA = new LineParametric3D_F32( 2,3,-4,-9,3,6.7f );
		lineB = new LineParametric3D_F32( -0.4f,0,1.2f,-3.4f,4,-5 );
		foundB = ClosestPoint3D_F32.closestPoint(lineA, lineB, null);
		checkIsClosest(foundB,lineA,lineB);
	}

	@Test
	public void closestPoints_lines() {
		Point3D_F32 a = new Point3D_F32( 1, 1, 1 );
		Point3D_F32 b = new Point3D_F32( 1.5f, -2.5f, 9 );
		Point3D_F32 c = new Point3D_F32( 10.1f, 6, -3 );

		Vector3D_F32 va = new Vector3D_F32( a, b );
		Vector3D_F32 vc = new Vector3D_F32( c, b );
		// normalize the vector so that the value 't' is euclidean distance
		va.normalize();
		vc.normalize();

		LineParametric3D_F32 lineA = new LineParametric3D_F32( a, va );
		LineParametric3D_F32 lineB = new LineParametric3D_F32( c, vc );

		float param[] = new float[2];

		assertTrue(ClosestPoint3D_F32.closestPoints(lineA, lineB, param));

		assertEquals( a.distance(b) , param[0] , GrlConstants.TEST_F32);
		assertEquals(c.distance(b), param[1], GrlConstants.TEST_F32);
	}

	@Test
	public void closestPoint_point() {
		Point3D_F32 a = new Point3D_F32( 1, 1, 1 );
		Point3D_F32 b = new Point3D_F32( 1.5f, -2.5f, 9 );
		Point3D_F32 c = new Point3D_F32( 10.1f, 6, -3 );

		Vector3D_F32 va = new Vector3D_F32( a, b );

		LineParametric3D_F32 lineA = new LineParametric3D_F32( a, va );

		Point3D_F32 foundB = ClosestPoint3D_F32.closestPoint(lineA, c, null);

		Vector3D_F32 p = new Vector3D_F32( foundB, c );

		// see if they are perpendicular and therefor c foundB is the closest point
		float d = p.dot( va );

		assertEquals( 0, d, GrlConstants.TEST_F32);
	}

	@Test
	public void closestPoint_point_d() {
		Point3D_F32 a = new Point3D_F32( 1, 1, 1 );
		Point3D_F32 b = new Point3D_F32( 1.5f, -2.5f, 9 );
		Point3D_F32 c = new Point3D_F32( 10.1f, 6, -3 );

		Vector3D_F32 va = new Vector3D_F32( a, b );

		LineParametric3D_F32 lineA = new LineParametric3D_F32( a, va );

		float d = ClosestPoint3D_F32.closestPoint(lineA, c);

		Point3D_F32 pt = new Point3D_F32();
		pt.x = a.x + va.x*d;
		pt.y = a.y + va.y*d;
		pt.z = a.z + va.z*d;

		Vector3D_F32 p = new Vector3D_F32( pt, c );

		// see if they are perpendicular and therefor c foundB is the closest point
		assertEquals( 0, p.dot(va), GrlConstants.TEST_F32);
	}

	@Test
	public void closestPoint_planeNorm_point() {
		Point3D_F32 found;

		PlaneNormal3D_F32 n = new PlaneNormal3D_F32(3,4,-5,3,4,-5);

		found = ClosestPoint3D_F32.closestPoint(n,new Point3D_F32(0,0,0),null);
		assertEquals(3,found.x, GrlConstants.TEST_F32);
		assertEquals(4,found.y, GrlConstants.TEST_F32);
		assertEquals(-5,found.z, GrlConstants.TEST_F32);

		// move it closer, but the point shouldn't change
		Vector3D_F32 v = n.n;
		v.normalize();
		found = ClosestPoint3D_F32.closestPoint(n,new Point3D_F32(v.x,v.y,v.z),null);
		assertEquals(3,found.x, GrlConstants.TEST_F32);
		assertEquals(4,found.y, GrlConstants.TEST_F32);
		assertEquals(-5,found.z, GrlConstants.TEST_F32);

		// other side of normal
		found = ClosestPoint3D_F32.closestPoint(n,new Point3D_F32(3+v.x,4+v.y,v.z-5),null);
		assertEquals(3,found.x, GrlConstants.TEST_F32);
		assertEquals(4,found.y, GrlConstants.TEST_F32);
		assertEquals(-5,found.z, GrlConstants.TEST_F32);
	}

	@Test
	public void closestPoint_planeGen_point() {
		Point3D_F32 found;

		PlaneNormal3D_F32 n = new PlaneNormal3D_F32(3,4,-5,3,4,-5);
		PlaneGeneral3D_F32 g = UtilPlane3D_F32.convert(n, null);

		found = ClosestPoint3D_F32.closestPoint(g,new Point3D_F32(0,0,0),null);
		assertEquals(3,found.x, GrlConstants.TEST_F32);
		assertEquals(4,found.y, GrlConstants.TEST_F32);
		assertEquals(-5,found.z, GrlConstants.TEST_F32);

		// move it closer, but the point shouldn't change
		Vector3D_F32 v = n.n;
		v.normalize();
		found = ClosestPoint3D_F32.closestPoint(g,new Point3D_F32(v.x,v.y,v.z),null);
		assertEquals(3,found.x, GrlConstants.TEST_F32);
		assertEquals(4,found.y, GrlConstants.TEST_F32);
		assertEquals(-5,found.z, GrlConstants.TEST_F32);

		// other side of normal
		found = ClosestPoint3D_F32.closestPoint(g,new Point3D_F32(3+v.x,4+v.y,v.z-5),null);
		assertEquals(3,found.x, GrlConstants.TEST_F32);
		assertEquals(4,found.y, GrlConstants.TEST_F32);
		assertEquals(-5,found.z, GrlConstants.TEST_F32);
	}

	@Test
	public void closestPointOrigin() {
		PlaneGeneral3D_F32 g = new PlaneGeneral3D_F32(1,2,3,4);

		Point3D_F32 expected = ClosestPoint3D_F32.closestPoint(g,new Point3D_F32(0,0,0),null);
		Point3D_F32 found = ClosestPoint3D_F32.closestPointOrigin(g,null);

		assertEquals(expected.x,found.x, GrlConstants.TEST_F32);
		assertEquals(expected.y,found.y, GrlConstants.TEST_F32);
		assertEquals(expected.z,found.z, GrlConstants.TEST_F32);
	}

	@Test
	public void closestPoint_lineSeg_pt() {
		// closest point is on the line
		LineSegment3D_F32 lineA = new LineSegment3D_F32(2,3,4,7,8,9);
		checkIsClosest(lineA,new Point3D_F32(2,3.5f,3.5f));

		// closest point is past a
		checkIsClosest(lineA, new Point3D_F32(1, 1.95f, 3));

		// closest point is past b
		checkIsClosest(lineA, new Point3D_F32(8, 9, 10.1f));

		// this was a bug
		lineA = new LineSegment3D_F32(0,0,0,0,2,0);
		checkIsClosest(lineA, new Point3D_F32(0, 1.5f, 0));
	}

	@Test
	public void closestPoint_lineSeg_lineSeg() {
		Point3D_F32 found;

		LineSegment3D_F32 lineA = new LineSegment3D_F32(2,3,4,7,8,9);
		LineSegment3D_F32 lineB = new LineSegment3D_F32(2,3,6,-3,8,1);

		// intersects in the middle
		found = ClosestPoint3D_F32.closestPoint(lineA, lineB, null);
		checkIsClosest(found,lineA,lineB);

		lineA.set(-10,0,0,10,0,0);
		// misses start of lineA
		lineB.set(-100,0,20,-100,0,-20);
		found = ClosestPoint3D_F32.closestPoint(lineA, lineB, null);
		checkIsClosest(found, lineA, lineB);
		// misses end of lineA
		lineB.set(100,0,20,100,0,-20);
		found = ClosestPoint3D_F32.closestPoint(lineA, lineB, null);
		checkIsClosest(found, lineA, lineB);
		// same but for line B
		checkIsClosest(found, lineB, lineA);
		lineB.set(-100,0,20,-100,0,-20);
		found = ClosestPoint3D_F32.closestPoint(lineA, lineB, null);
		checkIsClosest(found, lineB, lineA);
	}

	@Test
	public void closestPoint_triangle_point() {
		Point3D_F32 P0 = new Point3D_F32(0,0,0);
		Point3D_F32 P1 = new Point3D_F32(0,2,0);
		Point3D_F32 P2 = new Point3D_F32(1,1,0);

		Point3D_F32 P = new Point3D_F32(0.2f,0.5f,2);

		Point3D_F32 found = new Point3D_F32();

		ClosestPoint3D_F32.closestPoint(P0,P1,P2,P,found);

		assertEquals(0.2f,found.x,GrlConstants.TEST_F32);
		assertEquals(0.5f,found.y,GrlConstants.TEST_F32);
		assertEquals(0,found.z,GrlConstants.TEST_F32);
	}

	@Test
	public void closestPointT_line_plane() {
		LineParametric3D_F32 l = new LineParametric3D_F32(2,3,4,0,2,0);
		PlaneNormal3D_F32 above = new PlaneNormal3D_F32(5,6,7,0,1,0);
		PlaneNormal3D_F32 below = new PlaneNormal3D_F32(5,-1,7,0,1,0);

		float t_above = ClosestPoint3D_F32.closestPointT(l,above);
		float t_below = ClosestPoint3D_F32.closestPointT(l,below);

		assertEquals(1.5f,t_above,GrlConstants.TEST_F32);
		assertEquals(-2.0f,t_below,GrlConstants.TEST_F32);
	}

	private void checkIsClosest( LineSegment3D_F32 line ,  Point3D_F32 target  ) {

		Point3D_F32 pointOnLine = ClosestPoint3D_F32.closestPoint(line,target,null);

		LineParametric3D_F32 para = UtilLine3D_F32.convert(line,null);

		float t = UtilLine3D_F32.computeT(para,pointOnLine);

		float t0 = t - (float)Math.sqrt(GrlConstants.TEST_F32);
		float t1 = t + (float)Math.sqrt(GrlConstants.TEST_F32);

		if( t0 < 0 ) t0 = 0;
		if( t1 > 1 ) t1 = 1;

		Point3D_F32 work0 = para.getPointOnLine(t0);
		Point3D_F32 work1 = para.getPointOnLine(t1);

		float dist = pointOnLine.distance(target);
		float dist0 = work0.distance(target);
		float dist1 = work1.distance(target);

		assertTrue( dist <= dist0 );
		assertTrue( dist <= dist1 );
	}

	private void checkIsClosest( Point3D_F32 pt , LineSegment3D_F32 lineA , LineSegment3D_F32 lineB ) {
		float found = Distance3D_F32.distance(lineA,pt)+Distance3D_F32.distance(lineB,pt);

		Point3D_F32 work = pt.copy();

		for( int i = 0; i < 3; i++ ) {
			float orig = work.getIndex(i);
			work.setIndex(i, orig + (float)Math.sqrt(GrlConstants.TEST_F32));
			float d = Distance3D_F32.distance(lineA,work)+Distance3D_F32.distance(lineB,work);
			assertTrue(found+" "+d,found < d+10*GrlConstants.TEST_F32);
			work.setIndex(i, orig - (float)Math.sqrt(GrlConstants.TEST_F32));
			d = Distance3D_F32.distance(lineA,work)+Distance3D_F32.distance(lineB,work);
			assertTrue(found + " " + d, found <=d+10*GrlConstants.TEST_F32);
			work.setIndex(i,orig);
		}
	}

	private void checkIsClosest( Point3D_F32 pt , LineParametric3D_F32 lineA , LineParametric3D_F32 lineB ) {
		float found = Distance3D_F32.distance(lineA,pt)+Distance3D_F32.distance(lineB,pt);

		Point3D_F32 work = pt.copy();

		for( int i = 0; i < 3; i++ ) {
			float orig = work.getIndex(i);
			work.setIndex(i,orig + (float)Math.sqrt(GrlConstants.TEST_F32));
			float d = Distance3D_F32.distance(lineA,work)+Distance3D_F32.distance(lineB,work);
			assertTrue(found+" "+d,found <= d+10*GrlConstants.TEST_F32);
			work.setIndex(i,orig - (float)Math.sqrt(GrlConstants.TEST_F32));
			d = Distance3D_F32.distance(lineA,work)+Distance3D_F32.distance(lineB,work);
			assertTrue(found+" "+d,found <= d+10*GrlConstants.TEST_F32);
			work.setIndex(i,orig);
		}
	}
}
