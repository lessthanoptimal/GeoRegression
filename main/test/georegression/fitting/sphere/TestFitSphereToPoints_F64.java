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

package georegression.fitting.sphere;

import georegression.geometry.ConvertRotation3D_F64;
import georegression.geometry.GeometryMath_F64;
import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.shapes.Sphere3D_F64;
import georegression.struct.so.Rodrigues_F64;
import org.ejml.data.DMatrixRMaj;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestFitSphereToPoints_F64 {

	Random rand = new Random(234);

	@Test
	void perfectModel() {

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

		assertEquals(0,sphere.center.distance(found.center),GrlConstants.TEST_F64);
		assertEquals(sphere.radius,found.radius,GrlConstants.TEST_F64);
	}

	@Test
	void perfectWithBadInitialModel() {
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

		assertEquals(0,sphere.center.distance(found.center),GrlConstants.TEST_F64);
		assertEquals(sphere.radius, found.radius, GrlConstants.TEST_F64);
	}

	public static Point3D_F64 createPt( Sphere3D_F64 sphere , double phi , double theta ) {
		Point3D_F64 p = new Point3D_F64();
		p.setTo(0,0,sphere.radius);

		Rodrigues_F64 rodX = new Rodrigues_F64(phi,new Vector3D_F64(1,0,0));
		DMatrixRMaj rotX = ConvertRotation3D_F64.rodriguesToMatrix(rodX, null);
		Rodrigues_F64 rodZ = new Rodrigues_F64(theta,new Vector3D_F64(0,0,1));
		DMatrixRMaj rotZ = ConvertRotation3D_F64.rodriguesToMatrix(rodZ, null);

		GeometryMath_F64.mult(rotX, p, p);
		GeometryMath_F64.mult(rotZ, p, p);
		p.x += sphere.center.x;
		p.y += sphere.center.y;
		p.z += sphere.center.z;

		return p;
	}

}
