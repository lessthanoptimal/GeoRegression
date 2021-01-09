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

package georegression.fitting.cylinder;

import georegression.geometry.ConvertRotation3D_F64;
import georegression.geometry.GeometryMath_F64;
import georegression.metric.Distance3D_F64;
import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.shapes.Cylinder3D_F64;
import georegression.struct.so.Rodrigues_F64;
import org.ejml.data.DMatrixRMaj;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestFitCylinderToPoints_F64 {

	Random rand = new Random(234);

	@Test
	void perfectModel() {

		Cylinder3D_F64 cylinder = new Cylinder3D_F64(1,2,3,0,0,1,2.5);

		List<Point3D_F64> points = new ArrayList<Point3D_F64>();
		for( int i = 0; i < 50; i++ ) {

			double H = (rand.nextDouble()-0.5)*4.0;
			double theta = rand.nextDouble()*GrlConstants.PI2;

			points.add(createPt(cylinder,H,theta));
		}

		FitCylinderToPoints_F64 alg = new FitCylinderToPoints_F64(200);

		Cylinder3D_F64 found = new Cylinder3D_F64();
		alg.fitModel(points, cylinder, found);

		checkEquivalent(cylinder,found);
	}

	@Test
	void perfectWithBadInitialModel() {
		Cylinder3D_F64 cylinder = new Cylinder3D_F64(1,2,3,0,0,1,2.5);

		List<Point3D_F64> points = new ArrayList<Point3D_F64>();
		for( int i = 0; i < 50; i++ ) {

			double H = (rand.nextDouble()-0.5)*4.0;
			double theta = rand.nextDouble()*GrlConstants.PI2;

			points.add(createPt(cylinder,H,theta));
		}

		FitCylinderToPoints_F64 alg = new FitCylinderToPoints_F64(200);

		// make the initial model a bit off
		Cylinder3D_F64 initial = new Cylinder3D_F64(0.95,2.1,3.05,0.05,-0.001,1.05,2.6);
		Cylinder3D_F64 found = new Cylinder3D_F64();
		alg.fitModel(points, initial, found);

		checkEquivalent(cylinder, found);
	}

	public static void checkEquivalent( Cylinder3D_F64 a , Cylinder3D_F64 b ) {
		assertEquals(a.radius,b.radius,GrlConstants.TEST_F64);

		// the points should be on the other line
		assertEquals(0, Distance3D_F64.distance(a.line, b.line.p),GrlConstants.TEST_F64);
		assertEquals(0, Distance3D_F64.distance(b.line, a.line.p), 10*GrlConstants.TEST_F64);

		// slopes should be the same
		double dot = a.line.slope.dot(b.line.slope);
		double tmp = dot / (a.line.slope.norm()*b.line.slope.norm());
		double angle;
		if( tmp > 1.0 )
			angle = 0;
		else if( tmp < -1.0 )
			angle = Math.PI;
		else
			angle = Math.acos( tmp );
		assertTrue( Math.abs(angle) < GrlConstants.TEST_F64 ||
				Math.abs(angle-Math.PI) < GrlConstants.TEST_F64);
	}

	public static Point3D_F64 createPt( Cylinder3D_F64 cylinder , double h , double theta ) {
		Point3D_F64 p = new Point3D_F64();
		p.x = cylinder.radius*(double)Math.cos(theta);
		p.y = cylinder.radius*(double)Math.sin(theta);
		p.z = h;

		Vector3D_F64 axisZ = new Vector3D_F64(0,0,1);
		Vector3D_F64 cross = axisZ.crossWith(cylinder.line.slope);
		if( Math.abs(cross.norm()) < GrlConstants.TEST_F64) {
			cross.setTo(0,0,1);
		} else {
			cross.normalize();
		}

		double angle = axisZ.dot(cylinder.line.slope);
		angle = Math.acos( angle / cylinder.line.slope.norm());

		Rodrigues_F64 rod = new Rodrigues_F64(angle,cross);
		DMatrixRMaj R = ConvertRotation3D_F64.rodriguesToMatrix(rod, null);

		GeometryMath_F64.mult(R, p, p);
		p.x += cylinder.line.p.x;
		p.y += cylinder.line.p.y;
		p.z += cylinder.line.p.z;

		return p;
	}

}
