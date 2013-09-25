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

import georegression.geometry.GeometryMath_F32;
import georegression.geometry.RotationMatrixGenerator;
import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;
import georegression.struct.shapes.Sphere3D_F32;
import georegression.struct.so.Rodrigues_F32;
import org.ejml.data.DenseMatrix64F;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestFitSphereToPoints_F32 {

	Random rand = new Random(234);

	@Test
	public void perfectModel() {

		Sphere3D_F32 sphere = new Sphere3D_F32(1,2,3,4);

		List<Point3D_F32> points = new ArrayList<Point3D_F32>();
		for( int i = 0; i < 50; i++ ) {

			float phi = rand.nextFloat()*GrlConstants.F_PI2;
			float theta = rand.nextFloat()*GrlConstants.F_PI2;

			points.add(createPt(sphere,phi,theta));
		}

		FitSphereToPoints_F32 alg = new FitSphereToPoints_F32(200);

		Sphere3D_F32 found = new Sphere3D_F32();
		alg.fitModel(points, sphere, found);

		assertEquals(0,sphere.center.distance(found.center),GrlConstants.FLOAT_TEST_TOL);
		assertEquals(sphere.radius,found.radius,GrlConstants.FLOAT_TEST_TOL);
	}

	@Test
	public void perfectWithBadInitialModel() {
		Sphere3D_F32 sphere = new Sphere3D_F32(1,2,3,4);

		List<Point3D_F32> points = new ArrayList<Point3D_F32>();
		for( int i = 0; i < 50; i++ ) {

			float phi = rand.nextFloat()*GrlConstants.F_PI2;
			float theta = rand.nextFloat()*GrlConstants.F_PI2;

			points.add(createPt(sphere,phi,theta));
		}

		FitSphereToPoints_F32 alg = new FitSphereToPoints_F32(200);

		// make the initial model a bit off
		Sphere3D_F32 initial = new Sphere3D_F32(1.05f,1.99f,3,3.8f);
		Sphere3D_F32 found = new Sphere3D_F32();
		alg.fitModel(points, initial, found);

		assertEquals(0,sphere.center.distance(found.center),GrlConstants.FLOAT_TEST_TOL);
		assertEquals(sphere.radius, found.radius, GrlConstants.FLOAT_TEST_TOL);
	}

	public static Point3D_F32 createPt( Sphere3D_F32 sphere , float phi , float theta ) {
		Point3D_F32 p = new Point3D_F32();
		p.set(0,0,sphere.radius);

		Rodrigues_F32 rodX = new Rodrigues_F32(phi,new Vector3D_F32(1,0,0));
		DenseMatrix64F rotX = RotationMatrixGenerator.rodriguesToMatrix(rodX, null);
		Rodrigues_F32 rodZ = new Rodrigues_F32(theta,new Vector3D_F32(0,0,1));
		DenseMatrix64F rotZ = RotationMatrixGenerator.rodriguesToMatrix(rodZ, null);

		GeometryMath_F32.mult(rotX, p, p);
		GeometryMath_F32.mult(rotZ, p, p);
		p.x += sphere.center.x;
		p.y += sphere.center.y;
		p.z += sphere.center.z;

		return p;
	}

}
