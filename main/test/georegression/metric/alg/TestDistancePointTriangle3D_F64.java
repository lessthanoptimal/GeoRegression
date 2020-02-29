/*
 * Copyright (C) 2011-2020, Peter Abeles. All Rights Reserved.
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

import georegression.metric.Distance3D_F64;
import georegression.misc.GrlConstants;
import georegression.struct.line.LineSegment3D_F64;
import georegression.struct.point.Point3D_F64;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestDistancePointTriangle3D_F64 {

	DistancePointTriangle3D_F64 alg = new DistancePointTriangle3D_F64();

	Point3D_F64 P0 = new Point3D_F64(0,0,0);
	Point3D_F64 P1 = new Point3D_F64(0,2,0);
	Point3D_F64 P2 = new Point3D_F64(1,1,0);

	LineSegment3D_F64 L01 = new LineSegment3D_F64(P0,P1);
	LineSegment3D_F64 L12 = new LineSegment3D_F64(P1,P2);
	LineSegment3D_F64 L20 = new LineSegment3D_F64(P2,P0);


	@BeforeEach
	public void init() {
		alg.setTriangle(P0,P1,P2);
	}

	@Test
	void sign() {
		assertTrue(alg.sign(new Point3D_F64(0,0,2))> 0);
		assertTrue(alg.sign(new Point3D_F64(3,-3,2))> 0);

		assertTrue(alg.sign(new Point3D_F64(0,0,-2))< 0);
		assertTrue(alg.sign(new Point3D_F64(3,-3,-2))< 0);
	}

	@Test
	void region0() {
		assertEquals(2, distance(0.1, 0.2, 2), GrlConstants.TEST_F64);
		assertEquals(2, distance(0.1, 0.2, -2), GrlConstants.TEST_F64);
	}

	@Test
	void region1() {
		Point3D_F64 P = new Point3D_F64(2,1,2);
		double d = Distance3D_F64.distance(L12,P);

		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.TEST_F64);
		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.TEST_F64);
	}

	@Test
	void region2() {
		Point3D_F64 P = new Point3D_F64(-0.2,7.2,2);
		double d = P1.distance(P);

		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.TEST_F64);
		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.TEST_F64);
	}

	@Test
	void region3() {
		Point3D_F64 P = new Point3D_F64(-0.1,1.5,0);
		double d = Distance3D_F64.distance(L01,P);

		assertEquals(d, distance(P.x, P.y, P.z), GrlConstants.TEST_F64);
		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.TEST_F64);
	}

	@Test
	void region4() {
		Point3D_F64 P = new Point3D_F64(-0.2,-0.5,2);
		double d = P0.distance(P);

		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.TEST_F64);
		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.TEST_F64);
	}

	@Test
	void region5() {
		Point3D_F64 P = new Point3D_F64(0.6,0.5,2);
		double d = Distance3D_F64.distance(L20,P);

		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.TEST_F64);
		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.TEST_F64);
	}

	@Test
	void region6() {
		Point3D_F64 P = new Point3D_F64(1.5,1,2);
		double d = P2.distance(P);

		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.TEST_F64);
		assertEquals(d, distance(P.x,P.y,P.z), GrlConstants.TEST_F64);
	}

	public double distance( double x , double y , double z ) {
		Point3D_F64 cp = new Point3D_F64();

		alg.closestPoint(new Point3D_F64(x,y,z),cp);

		return cp.distance(new Point3D_F64(x,y,z));
	}

}
