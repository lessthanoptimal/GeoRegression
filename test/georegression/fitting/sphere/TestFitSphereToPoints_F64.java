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

package georegression.fitting.sphere;

import georegression.geometry.GeometryMath_F64;
import georegression.geometry.RotationMatrixGenerator;
import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.shapes.Sphere3D_F64;
import georegression.struct.so.Rodrigues_F64;
import org.ejml.data.DenseMatrix64F;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestFitSphereToPoints_F64 {

	Random rand = new Random(234);

	@Test
	public void perfectModel() {

		Sphere3D_F64 sphere = new Sphere3D_F64(1,2,3,4);

		List<Point3D_F64> points = new ArrayList<Point3D_F64>();
		for( int i = 0; i < 50; i++ ) {

			double phi = rand.nextDouble()*GrlConstants.PI2;
			double theta = rand.nextDouble()*GrlConstants.PI2;

			points.add(createPt(sphere,phi,theta));
		}

		FitSphereToPoints_F64 alg = new FitSphereToPoints_F64(200);

		Sphere3D_F64 found = new Sphere3D_F64();
		alg.fitModel(points, sphere, found);

		assertEquals(0,sphere.center.distance(found.center),GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(sphere.radius,found.radius,GrlConstants.DOUBLE_TEST_TOL);
	}

	@Test
	public void perfectWithBadInitialModel() {
		Sphere3D_F64 sphere = new Sphere3D_F64(1,2,3,4);

		List<Point3D_F64> points = new ArrayList<Point3D_F64>();
		for( int i = 0; i < 50; i++ ) {

			double phi = rand.nextDouble()*GrlConstants.PI2;
			double theta = rand.nextDouble()*GrlConstants.PI2;

			points.add(createPt(sphere,phi,theta));
		}

		FitSphereToPoints_F64 alg = new FitSphereToPoints_F64(200);

		// make the initial model a bit off
		Sphere3D_F64 initial = new Sphere3D_F64(1.05,1.99,3,3.8);
		Sphere3D_F64 found = new Sphere3D_F64();
		alg.fitModel(points, initial, found);

		assertEquals(0,sphere.center.distance(found.center),GrlConstants.DOUBLE_TEST_TOL);
		assertEquals(sphere.radius, found.radius, GrlConstants.DOUBLE_TEST_TOL);
	}

	public static Point3D_F64 createPt( Sphere3D_F64 sphere , double phi , double theta ) {
		Point3D_F64 p = new Point3D_F64();
		p.set(0,0,sphere.radius);

		Rodrigues_F64 rodX = new Rodrigues_F64(phi,new Vector3D_F64(1,0,0));
		DenseMatrix64F rotX = RotationMatrixGenerator.rodriguesToMatrix(rodX, null);
		Rodrigues_F64 rodZ = new Rodrigues_F64(theta,new Vector3D_F64(0,0,1));
		DenseMatrix64F rotZ = RotationMatrixGenerator.rodriguesToMatrix(rodZ, null);

		GeometryMath_F64.mult(rotX, p, p);
		GeometryMath_F64.mult(rotZ, p, p);
		p.x += sphere.center.x;
		p.y += sphere.center.y;
		p.z += sphere.center.z;

		return p;
	}

}
