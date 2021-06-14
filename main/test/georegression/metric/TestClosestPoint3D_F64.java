/*
 * Copyright (C) 2020, Peter Abeles. All Rights Reserved.
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

import georegression.geometry.UtilLine3D_F64;
import georegression.geometry.UtilPlane3D_F64;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineParametric3D_F64;
import georegression.struct.line.LineSegment3D_F64;
import georegression.struct.plane.PlaneGeneral3D_F64;
import georegression.struct.plane.PlaneNormal3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Point4D_F64;
import georegression.struct.point.Vector3D_F64;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Peter Abeles
 */
public class TestClosestPoint3D_F64 {
	/**
	 * Compute truth from 3 random points then see if the 3rd point is found again.
	 */
	@Test
	void closestPoint_line_3D() {
		Point3D_F64 a = new Point3D_F64( 1, 1, 1 );
		Point3D_F64 b = new Point3D_F64( 1.5, -2.5, 9 );
		Point3D_F64 c = new Point3D_F64( 10.1, 6, -3 );

		Vector3D_F64 va = new Vector3D_F64( a, b );
		Vector3D_F64 vc = new Vector3D_F64( c, b );

		LineParametric3D_F64 lineA = new LineParametric3D_F64( a, va );
		LineParametric3D_F64 lineB = new LineParametric3D_F64( c, vc );

		Point3D_F64 foundB = ClosestPoint3D_F64.closestPoint(lineA, lineB, (Point3D_F64)null);
		assertNotNull(foundB);
		assertTrue( b.isIdentical( foundB, GrlConstants.TEST_F64) );
		checkIsClosest(foundB,lineA,lineB);

		// check two arbitrary lines
		lineA = new LineParametric3D_F64( 2,3,-4,-9,3,6.7 );
		lineB = new LineParametric3D_F64( -0.4,0,1.2,-3.4,4,-5 );
		foundB = ClosestPoint3D_F64.closestPoint(lineA, lineB, (Point3D_F64)null);
		checkIsClosest(foundB,lineA,lineB);
	}

	@Test
	void closestPoint_line_Homogenous() {
		Point3D_F64 a = new Point3D_F64( 1, 1, 1 );
		Point3D_F64 b = new Point3D_F64( 1.5, -2.5, 9 );
		Point3D_F64 c = new Point3D_F64( 10.1, 6, -3 );
		Point3D_F64 foundB = new Point3D_F64();

		Vector3D_F64 va = new Vector3D_F64( a, b );
		Vector3D_F64 vc = new Vector3D_F64( c, b );

		LineParametric3D_F64 lineA = new LineParametric3D_F64( a, va );
		LineParametric3D_F64 lineB = new LineParametric3D_F64( c, vc );

		Point4D_F64 foundB_H = ClosestPoint3D_F64.closestPoint(lineA, lineB, (Point4D_F64) null);
		assertNotNull(foundB_H);
		foundB.setTo(foundB_H.x/foundB_H.w, foundB_H.y/foundB_H.w, foundB_H.z/foundB_H.w);
		assertTrue( b.isIdentical( foundB, GrlConstants.TEST_F64) );
		checkIsClosest(foundB,lineA,lineB);

		// check two arbitrary lines
		lineA = new LineParametric3D_F64( 2,3,-4,-9,3,6.7 );
		lineB = new LineParametric3D_F64( -0.4,0,1.2,-3.4,4,-5 );
		foundB_H = ClosestPoint3D_F64.closestPoint(lineA, lineB, foundB_H);
		foundB.setTo(foundB_H.x/foundB_H.w, foundB_H.y/foundB_H.w, foundB_H.z/foundB_H.w);
		checkIsClosest(foundB,lineA,lineB);

		// Check edge cases
		lineA = new LineParametric3D_F64( 0,0,0,1,0,0 );
		lineB = new LineParametric3D_F64( 0,0,0,0,1,0 );
		foundB_H = ClosestPoint3D_F64.closestPoint(lineA, lineB, foundB_H);
		foundB.setTo(foundB_H.x/foundB_H.w, foundB_H.y/foundB_H.w, foundB_H.z/foundB_H.w);
		checkIsClosest(foundB,lineA,lineB);

		lineA = new LineParametric3D_F64( 0,0,0,1,0,0 );
		lineB = new LineParametric3D_F64( 1,0,0,0,1,0 );
		foundB_H = ClosestPoint3D_F64.closestPoint(lineA, lineB, foundB_H);
		foundB.setTo(foundB_H.x/foundB_H.w, foundB_H.y/foundB_H.w, foundB_H.z/foundB_H.w);
		checkIsClosest(foundB,lineA,lineB);
	}

	@Test
	void closestPoints_lines() {
		Point3D_F64 a = new Point3D_F64( 1, 1, 1 );
		Point3D_F64 b = new Point3D_F64( 1.5, -2.5, 9 );
		Point3D_F64 c = new Point3D_F64( 10.1, 6, -3 );

		Vector3D_F64 va = new Vector3D_F64( a, b );
		Vector3D_F64 vc = new Vector3D_F64( c, b );
		// normalize the vector so that the value 't' is euclidean distance
		va.normalize();
		vc.normalize();

		LineParametric3D_F64 lineA = new LineParametric3D_F64( a, va );
		LineParametric3D_F64 lineB = new LineParametric3D_F64( c, vc );

		double[] param = new double[2];

		assertTrue(ClosestPoint3D_F64.closestPoints(lineA, lineB, param));

		assertEquals( a.distance(b) , param[0] , GrlConstants.TEST_F64);
		assertEquals(c.distance(b), param[1], GrlConstants.TEST_F64);
	}

	@Test
	void closestPoint_point() {
		Point3D_F64 a = new Point3D_F64( 1, 1, 1 );
		Point3D_F64 b = new Point3D_F64( 1.5, -2.5, 9 );
		Point3D_F64 c = new Point3D_F64( 10.1, 6, -3 );

		Vector3D_F64 va = new Vector3D_F64( a, b );

		LineParametric3D_F64 lineA = new LineParametric3D_F64( a, va );

		Point3D_F64 foundB = ClosestPoint3D_F64.closestPoint(lineA, c, null);

		Vector3D_F64 p = new Vector3D_F64( foundB, c );

		// see if they are perpendicular and therefor c foundB is the closest point
		double d = p.dot( va );

		assertEquals( 0, d, GrlConstants.TEST_F64);
	}

	@Test
	void closestPoint_point_d() {
		Point3D_F64 a = new Point3D_F64( 1, 1, 1 );
		Point3D_F64 b = new Point3D_F64( 1.5, -2.5, 9 );
		Point3D_F64 c = new Point3D_F64( 10.1, 6, -3 );

		Vector3D_F64 va = new Vector3D_F64( a, b );

		LineParametric3D_F64 lineA = new LineParametric3D_F64( a, va );

		double d = ClosestPoint3D_F64.closestPoint(lineA, c);

		Point3D_F64 pt = new Point3D_F64();
		pt.x = a.x + va.x*d;
		pt.y = a.y + va.y*d;
		pt.z = a.z + va.z*d;

		Vector3D_F64 p = new Vector3D_F64( pt, c );

		// see if they are perpendicular and therefor c foundB is the closest point
		assertEquals( 0, p.dot(va), GrlConstants.TEST_F64);
	}

	@Test
	void closestPoint_planeNorm_point() {
		Point3D_F64 found;

		PlaneNormal3D_F64 n = new PlaneNormal3D_F64(3,4,-5,3,4,-5);

		found = ClosestPoint3D_F64.closestPoint(n,new Point3D_F64(0,0,0),null);
		assertEquals(3,found.x, GrlConstants.TEST_F64);
		assertEquals(4,found.y, GrlConstants.TEST_F64);
		assertEquals(-5,found.z, GrlConstants.TEST_F64);

		// move it closer, but the point shouldn't change
		Vector3D_F64 v = n.n;
		v.normalize();
		found = ClosestPoint3D_F64.closestPoint(n,new Point3D_F64(v.x,v.y,v.z),null);
		assertEquals(3,found.x, GrlConstants.TEST_F64);
		assertEquals(4,found.y, GrlConstants.TEST_F64);
		assertEquals(-5,found.z, GrlConstants.TEST_F64);

		// other side of normal
		found = ClosestPoint3D_F64.closestPoint(n,new Point3D_F64(3+v.x,4+v.y,v.z-5),null);
		assertEquals(3,found.x, GrlConstants.TEST_F64);
		assertEquals(4,found.y, GrlConstants.TEST_F64);
		assertEquals(-5,found.z, GrlConstants.TEST_F64);
	}

	@Test
	void closestPoint_planeGen_point() {
		Point3D_F64 found;

		PlaneNormal3D_F64 n = new PlaneNormal3D_F64(3,4,-5,3,4,-5);
		PlaneGeneral3D_F64 g = UtilPlane3D_F64.convert(n, null);

		found = ClosestPoint3D_F64.closestPoint(g,new Point3D_F64(0,0,0),null);
		assertEquals(3,found.x, GrlConstants.TEST_F64);
		assertEquals(4,found.y, GrlConstants.TEST_F64);
		assertEquals(-5,found.z, GrlConstants.TEST_F64);

		// move it closer, but the point shouldn't change
		Vector3D_F64 v = n.n;
		v.normalize();
		found = ClosestPoint3D_F64.closestPoint(g,new Point3D_F64(v.x,v.y,v.z),null);
		assertEquals(3,found.x, GrlConstants.TEST_F64);
		assertEquals(4,found.y, GrlConstants.TEST_F64);
		assertEquals(-5,found.z, GrlConstants.TEST_F64);

		// other side of normal
		found = ClosestPoint3D_F64.closestPoint(g,new Point3D_F64(3+v.x,4+v.y,v.z-5),null);
		assertEquals(3,found.x, GrlConstants.TEST_F64);
		assertEquals(4,found.y, GrlConstants.TEST_F64);
		assertEquals(-5,found.z, GrlConstants.TEST_F64);
	}

	@Test
	void closestPointOrigin() {
		PlaneGeneral3D_F64 g = new PlaneGeneral3D_F64(1,2,3,4);

		Point3D_F64 expected = ClosestPoint3D_F64.closestPoint(g,new Point3D_F64(0,0,0),null);
		Point3D_F64 found = ClosestPoint3D_F64.closestPointOrigin(g,null);

		assertEquals(expected.x,found.x, GrlConstants.TEST_F64);
		assertEquals(expected.y,found.y, GrlConstants.TEST_F64);
		assertEquals(expected.z,found.z, GrlConstants.TEST_F64);
	}

	@Test
	void closestPoint_lineSeg_pt() {
		// closest point is on the line
		LineSegment3D_F64 lineA = new LineSegment3D_F64(2,3,4,7,8,9);
		checkIsClosest(lineA,new Point3D_F64(2,3.5,3.5));

		// closest point is past a
		checkIsClosest(lineA, new Point3D_F64(1, 1.95, 3));

		// closest point is past b
		checkIsClosest(lineA, new Point3D_F64(8, 9, 10.1));

		// this was a bug
		lineA = new LineSegment3D_F64(0,0,0,0,2,0);
		checkIsClosest(lineA, new Point3D_F64(0, 1.5, 0));
	}

	@Test
	void closestPoint_lineSeg_lineSeg() {
		Point3D_F64 found;

		LineSegment3D_F64 lineA = new LineSegment3D_F64(2,3,4,7,8,9);
		LineSegment3D_F64 lineB = new LineSegment3D_F64(2,3,6,-3,8,1);

		// intersects in the middle
		found = ClosestPoint3D_F64.closestPoint(lineA, lineB, null);
		checkIsClosest(found,lineA,lineB);

		lineA.setTo(-10,0,0,10,0,0);
		// misses start of lineA
		lineB.setTo(-100,0,20,-100,0,-20);
		found = ClosestPoint3D_F64.closestPoint(lineA, lineB, null);
		checkIsClosest(found, lineA, lineB);
		// misses end of lineA
		lineB.setTo(100,0,20,100,0,-20);
		found = ClosestPoint3D_F64.closestPoint(lineA, lineB, null);
		checkIsClosest(found, lineA, lineB);
		// same but for line B
		checkIsClosest(found, lineB, lineA);
		lineB.setTo(-100,0,20,-100,0,-20);
		found = ClosestPoint3D_F64.closestPoint(lineA, lineB, null);
		checkIsClosest(found, lineB, lineA);
	}

	@Test
	void closestPoint_triangle_point() {
		Point3D_F64 P0 = new Point3D_F64(0,0,0);
		Point3D_F64 P1 = new Point3D_F64(0,2,0);
		Point3D_F64 P2 = new Point3D_F64(1,1,0);

		Point3D_F64 P = new Point3D_F64(0.2,0.5,2);

		Point3D_F64 found = new Point3D_F64();

		ClosestPoint3D_F64.closestPoint(P0,P1,P2,P,found);

		assertEquals(0.2,found.x,GrlConstants.TEST_F64);
		assertEquals(0.5,found.y,GrlConstants.TEST_F64);
		assertEquals(0,found.z,GrlConstants.TEST_F64);
	}

	@Test
	void closestPointT_line_plane() {
		LineParametric3D_F64 l = new LineParametric3D_F64(2,3,4,0,2,0);
		PlaneNormal3D_F64 above = new PlaneNormal3D_F64(5,6,7,0,1,0);
		PlaneNormal3D_F64 below = new PlaneNormal3D_F64(5,-1,7,0,1,0);

		double t_above = ClosestPoint3D_F64.closestPointT(l,above);
		double t_below = ClosestPoint3D_F64.closestPointT(l,below);

		assertEquals(1.5,t_above,GrlConstants.TEST_F64);
		assertEquals(-2.0,t_below,GrlConstants.TEST_F64);
	}

	private void checkIsClosest( LineSegment3D_F64 line ,  Point3D_F64 target  ) {

		Point3D_F64 pointOnLine = ClosestPoint3D_F64.closestPoint(line,target,null);

		LineParametric3D_F64 para = UtilLine3D_F64.convert(line,null);

		double t = UtilLine3D_F64.computeT(para,pointOnLine);

		double t0 = t - Math.sqrt(GrlConstants.TEST_F64);
		double t1 = t + Math.sqrt(GrlConstants.TEST_F64);

		if( t0 < 0 ) t0 = 0;
		if( t1 > 1 ) t1 = 1;

		Point3D_F64 work0 = para.getPointOnLine(t0);
		Point3D_F64 work1 = para.getPointOnLine(t1);

		double dist = pointOnLine.distance(target);
		double dist0 = work0.distance(target);
		double dist1 = work1.distance(target);

		assertTrue( dist <= dist0 );
		assertTrue( dist <= dist1 );
	}

	private void checkIsClosest(@Nullable Point3D_F64 pt , LineSegment3D_F64 lineA , LineSegment3D_F64 lineB ) {
		assertNotNull(pt);
		double found = Distance3D_F64.distance(lineA,pt)+Distance3D_F64.distance(lineB,pt);

		Point3D_F64 work = pt.copy();

		for( int i = 0; i < 3; i++ ) {
			double orig = work.getIdx(i);
			work.setIdx(i, orig + Math.sqrt(GrlConstants.TEST_F64));
			double d = Distance3D_F64.distance(lineA,work)+Distance3D_F64.distance(lineB,work);
			assertTrue(found < d+10*GrlConstants.TEST_F64,found+" "+d);
			work.setIdx(i, orig - Math.sqrt(GrlConstants.TEST_F64));
			d = Distance3D_F64.distance(lineA,work)+Distance3D_F64.distance(lineB,work);
			assertTrue(found <=d+10*GrlConstants.TEST_F64, found + " " + d);
			work.setIdx(i,orig);
		}
	}

	private void checkIsClosest( @Nullable Point3D_F64 pt , LineParametric3D_F64 lineA , LineParametric3D_F64 lineB ) {
		assertNotNull(pt);
		double found = Distance3D_F64.distance(lineA,pt)+Distance3D_F64.distance(lineB,pt);

		Point3D_F64 work = pt.copy();

		for( int i = 0; i < 3; i++ ) {
			double orig = work.getIdx(i);
			work.setIdx(i,orig + Math.sqrt(GrlConstants.TEST_F64));
			double d = Distance3D_F64.distance(lineA,work)+Distance3D_F64.distance(lineB,work);
			assertTrue(found <= d+10*GrlConstants.TEST_F64,found+" "+d);
			work.setIdx(i,orig - Math.sqrt(GrlConstants.TEST_F64));
			d = Distance3D_F64.distance(lineA,work)+Distance3D_F64.distance(lineB,work);
			assertTrue(found <= d+10*GrlConstants.TEST_F64,found+" "+d);
			work.setIdx(i,orig);
		}
	}
}
