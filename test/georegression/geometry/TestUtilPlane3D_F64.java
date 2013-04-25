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

package georegression.geometry;

import georegression.misc.GrlConstants;
import georegression.struct.plane.PlaneGeneral3D_F64;
import georegression.struct.plane.PlaneNormal3D_F64;
import georegression.struct.plane.PlaneTangent3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestUtilPlane3D_F64 {

	Random rand = new Random(234);

	@Test
	public void convert_norm_general() {
		PlaneNormal3D_F64 original = new PlaneNormal3D_F64();
		original.n.set(1,2,3);
		original.n.normalize();
		original.p.set(-2,3,5);

		PlaneGeneral3D_F64 test = UtilPlane3D_F64.convert(original,null);
		List<Point3D_F64> points = randPointOnPlane(original,10);

		for( Point3D_F64 p : points ) {
			double found = UtilPlane3D_F64.evaluate(test,p);
			assertEquals(0,found, GrlConstants.DOUBLE_TEST_TOL);
		}
	}

	@Test
	public void convert_tangent_norm() {
		PlaneNormal3D_F64 original = new PlaneNormal3D_F64();
		original.n.set(1,0,0);
		original.n.normalize();
		original.p.set(-2,3,5);

		// create a bunch of points which are on the original plane
		List<Point3D_F64> points = randPointOnPlane(original,10);

		// now manually construct the plane in tangent form
		PlaneTangent3D_F64 tangent = new PlaneTangent3D_F64(-2,0,0);

		// convert this back into normal form
		PlaneNormal3D_F64 conv = UtilPlane3D_F64.convert(tangent,(PlaneNormal3D_F64)null);

		// the points should still be on the plane
		for( Point3D_F64 p : points ) {
			double found = UtilPlane3D_F64.evaluate(conv,p);
			assertEquals(0,found, GrlConstants.DOUBLE_TEST_TOL);
		}
	}


	@Test
	public void evaluate_general() {
		PlaneNormal3D_F64 original = new PlaneNormal3D_F64();
		original.n.set(1,2,3);
		original.n.normalize();
		original.p.set(-2,3,5);

		PlaneGeneral3D_F64 test = UtilPlane3D_F64.convert(original,null);

		List<Point3D_F64> points = randPointOnPlane(original,10);

		for( Point3D_F64 p : points ) {
			double found = UtilPlane3D_F64.evaluate(test,p);
			assertEquals(0,found, GrlConstants.DOUBLE_TEST_TOL);
		}
	}

	@Test
	public void evaluate_normal() {
		PlaneNormal3D_F64 input = new PlaneNormal3D_F64();
		input.n.set(1,2,3);
		input.n.normalize();
		input.p.set(-2,3,5);

		List<Point3D_F64> points = randPointOnPlane(input, 10);

		for( Point3D_F64 p : points ) {
			double found = UtilPlane3D_F64.evaluate(input,p);
			assertEquals(0,found, GrlConstants.DOUBLE_TEST_TOL);
		}
	}

	/**
	 * Randomly generate points on a plane by randomly selecting two vectors on the plane using cross products
	 */
	private List<Point3D_F64> randPointOnPlane( PlaneNormal3D_F64 plane , int N ) {
		Vector3D_F64 v = new Vector3D_F64(-2,0,1);
		Vector3D_F64 a = UtilTrig_F64.cross(plane.n,v);
		a.normalize();
		Vector3D_F64 b = UtilTrig_F64.cross(plane.n,a);
		b.normalize();

		List<Point3D_F64> ret = new ArrayList<Point3D_F64>();

		for( int i = 0; i < N; i++ ) {
			double v0 = rand.nextGaussian();
			double v1 = rand.nextGaussian();

			Point3D_F64 p = new Point3D_F64();
			p.x = plane.p.x + v0*a.x + v1*b.x;
			p.y = plane.p.y + v0*a.y + v1*b.y;
			p.z = plane.p.z + v0*a.z + v1*b.z;

			ret.add(p);
		}

		return ret;
	}

}
