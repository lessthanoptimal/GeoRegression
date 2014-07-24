/*
 * Copyright (C) 2011-2014, Peter Abeles. All Rights Reserved.
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

package georegression.metric.alg;

import georegression.metric.Distance3D_F32;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineSegment3D_F32;
import georegression.struct.point.Point3D_F32;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestDistancePointTriangle3D_F32 {

	DistancePointTriangle3D_F32 alg = new DistancePointTriangle3D_F32();

	Point3D_F32 P0 = new Point3D_F32(0,0,0);
	Point3D_F32 P1 = new Point3D_F32(0,2,0);
	Point3D_F32 P2 = new Point3D_F32(1,1,0);

	LineSegment3D_F32 L01 = new LineSegment3D_F32(P0,P1);
	LineSegment3D_F32 L12 = new LineSegment3D_F32(P1,P2);
	LineSegment3D_F32 L20 = new LineSegment3D_F32(P2,P0);


	@Before
	public void init() {
		alg.setTriangle(P0,P1,P2);
	}

	@Test
	public void sign() {
		assertTrue(alg.sign(new Point3D_F32(0,0,2))> 0);
		assertTrue(alg.sign(new Point3D_F32(3,-3,2))> 0);

		assertTrue(alg.sign(new Point3D_F32(0,0,-2))< 0);
		assertTrue(alg.sign(new Point3D_F32(3,-3,-2))< 0);
	}

	@Test
	public void region0() {
		assertEquals(2, distance(0.1f, 0.2f, 2), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(2, distance(0.1f, 0.2f, -2), GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void region1() {
		Point3D_F32 P = new Point3D_F32(2,1,2);
		float d = Distance3D_F32.distance(L12,P);

		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void region2() {
		Point3D_F32 P = new Point3D_F32(-0.2f,7.2f,2);
		float d = P1.distance(P);

		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void region3() {
		Point3D_F32 P = new Point3D_F32(-0.1f,1.5f,0);
		float d = Distance3D_F32.distance(L01,P);

		assertEquals(d, distance(P.x, P.y, P.z), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void region4() {
		Point3D_F32 P = new Point3D_F32(-0.2f,-0.5f,2);
		float d = P0.distance(P);

		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void region5() {
		Point3D_F32 P = new Point3D_F32(0.6f,0.5f,2);
		float d = Distance3D_F32.distance(L20,P);

		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void region6() {
		Point3D_F32 P = new Point3D_F32(1.5f,1,2);
		float d = P2.distance(P);

		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.FLOAT_TEST_TOL);
		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.FLOAT_TEST_TOL);
	}

	public float distance( float x , float y , float z ) {
		Point3D_F32 cp = new Point3D_F32();

		alg.closestPoint(new Point3D_F32(x,y,z),cp);

		return cp.distance(new Point3D_F32(x,y,z));
	}

}
