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

		assertTrue( b.isIdentical( foundB, GrlConstants.FLOAT_TEST_TOL ) );
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

		assertEquals( a.distance(b) , param[0] , GrlConstants.FLOAT_TEST_TOL );
		assertEquals( c.distance(b) , param[1] , GrlConstants.FLOAT_TEST_TOL );
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

		assertEquals( 0, d, GrlConstants.FLOAT_TEST_TOL );
	}

	@Test
	public void closestPoint_planeNorm_point() {
		Point3D_F32 found;

		PlaneNormal3D_F32 n = new PlaneNormal3D_F32(3,4,-5,3,4,-5);

		found = ClosestPoint3D_F32.closestPoint(n,new Point3D_F32(0,0,0),null);
		assertEquals(3,found.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,found.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(-5,found.z, GrlConstants.FLOAT_TEST_TOL);

		// move it closer, but the point shouldn't change
		Vector3D_F32 v = n.n;
		v.normalize();
		found = ClosestPoint3D_F32.closestPoint(n,new Point3D_F32(v.x,v.y,v.z),null);
		assertEquals(3,found.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,found.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(-5,found.z, GrlConstants.FLOAT_TEST_TOL);

		// other side of normal
		found = ClosestPoint3D_F32.closestPoint(n,new Point3D_F32(3+v.x,4+v.y,v.z-5),null);
		assertEquals(3,found.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,found.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(-5,found.z, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void closestPoint_planeGen_point() {
		Point3D_F32 found;

		PlaneNormal3D_F32 n = new PlaneNormal3D_F32(3,4,-5,3,4,-5);
		PlaneGeneral3D_F32 g = UtilPlane3D_F32.convert(n, null);

		found = ClosestPoint3D_F32.closestPoint(g,new Point3D_F32(0,0,0),null);
		assertEquals(3,found.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,found.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(-5,found.z, GrlConstants.FLOAT_TEST_TOL);

		// move it closer, but the point shouldn't change
		Vector3D_F32 v = n.n;
		v.normalize();
		found = ClosestPoint3D_F32.closestPoint(g,new Point3D_F32(v.x,v.y,v.z),null);
		assertEquals(3,found.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,found.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(-5,found.z, GrlConstants.FLOAT_TEST_TOL);

		// other side of normal
		found = ClosestPoint3D_F32.closestPoint(g,new Point3D_F32(3+v.x,4+v.y,v.z-5),null);
		assertEquals(3,found.x, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(4,found.y, GrlConstants.FLOAT_TEST_TOL);
		assertEquals(-5,found.z, GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void closestPoint_lineSeg_pt() {
		// closest point is on the line
		LineSegment3D_F32 lineA = new LineSegment3D_F32(2,3,4,7,8,9);
		checkIsClosest(lineA,new Point3D_F32(2,3.5f,3.5f));

		// closest point is past a
		checkIsClosest(lineA,new Point3D_F32(1,1.95f,3));

		// closest point is past b
		checkIsClosest(lineA, new Point3D_F32(8, 9, 10.1f));
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

	private void checkIsClosest( LineSegment3D_F32 line ,  Point3D_F32 target  ) {

		Point3D_F32 pointOnLine = ClosestPoint3D_F32.closestPoint(line,target,null);

		LineParametric3D_F32 para = UtilLine3D_F32.convert(line,null);

		float t = UtilLine3D_F32.computeT(para,pointOnLine);

		float t0 = t - (float)Math.sqrt(GrlConstants.FLOAT_TEST_TOL);
		float t1 = t + (float)Math.sqrt(GrlConstants.FLOAT_TEST_TOL);

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
			work.setIndex(i, orig + (float)Math.sqrt(GrlConstants.FLOAT_TEST_TOL));
			float d = Distance3D_F32.distance(lineA,work)+Distance3D_F32.distance(lineB,work);
			assertTrue(found+" "+d,found < d);
			work.setIndex(i, orig - (float)Math.sqrt(GrlConstants.FLOAT_TEST_TOL));
			d = Distance3D_F32.distance(lineA,work)+Distance3D_F32.distance(lineB,work);
			assertTrue(found + " " + d, found < d);
			work.setIndex(i,orig);
		}
	}

	private void checkIsClosest( Point3D_F32 pt , LineParametric3D_F32 lineA , LineParametric3D_F32 lineB ) {
		float found = Distance3D_F32.distance(lineA,pt)+Distance3D_F32.distance(lineB,pt);

		Point3D_F32 work = pt.copy();

		for( int i = 0; i < 3; i++ ) {
			float orig = work.getIndex(i);
			work.setIndex(i,orig + (float)Math.sqrt(GrlConstants.FLOAT_TEST_TOL));
			float d = Distance3D_F32.distance(lineA,work)+Distance3D_F32.distance(lineB,work);
			assertTrue(found+" "+d,found < d);
			work.setIndex(i,orig - (float)Math.sqrt(GrlConstants.FLOAT_TEST_TOL));
			d = Distance3D_F32.distance(lineA,work)+Distance3D_F32.distance(lineB,work);
			assertTrue(found+" "+d,found < d);
			work.setIndex(i,orig);
		}
	}
}
