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

package georegression.fitting.cylinder;

import georegression.geometry.ConvertRotation3D_F32;
import georegression.geometry.GeometryMath_F32;
import georegression.metric.Distance3D_F32;
import georegression.misc.GrlConstants;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;
import georegression.struct.shapes.Cylinder3D_F32;
import georegression.struct.so.Rodrigues_F32;
import org.ejml.data.RowMatrix_F32;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestFitCylinderToPoints_F32 {

	Random rand = new Random(234);

	@Test
	public void perfectModel() {

		Cylinder3D_F32 cylinder = new Cylinder3D_F32(1,2,3,0,0,1,2.5f);

		List<Point3D_F32> points = new ArrayList<Point3D_F32>();
		for( int i = 0; i < 50; i++ ) {

			float H = (rand.nextFloat()-0.5f)*4.0f;
			float theta = rand.nextFloat()*GrlConstants.F_PI2;

			points.add(createPt(cylinder,H,theta));
		}

		FitCylinderToPoints_F32 alg = new FitCylinderToPoints_F32(200);

		Cylinder3D_F32 found = new Cylinder3D_F32();
		alg.fitModel(points, cylinder, found);

		checkEquivalent(cylinder,found);
	}

	@Test
	public void perfectWithBadInitialModel() {
		Cylinder3D_F32 cylinder = new Cylinder3D_F32(1,2,3,0,0,1,2.5f);

		List<Point3D_F32> points = new ArrayList<Point3D_F32>();
		for( int i = 0; i < 50; i++ ) {

			float H = (rand.nextFloat()-0.5f)*4.0f;
			float theta = rand.nextFloat()*GrlConstants.F_PI2;

			points.add(createPt(cylinder,H,theta));
		}

		FitCylinderToPoints_F32 alg = new FitCylinderToPoints_F32(200);

		// make the initial model a bit off
		Cylinder3D_F32 initial = new Cylinder3D_F32(0.95f,2.1f,3.05f,0.05f,-0.001f,1.05f,2.6f);
		Cylinder3D_F32 found = new Cylinder3D_F32();
		alg.fitModel(points, initial, found);

		checkEquivalent(cylinder, found);
	}

	public static void checkEquivalent( Cylinder3D_F32 a , Cylinder3D_F32 b ) {
		assertEquals(a.radius,b.radius,GrlConstants.TEST_F32);

		// the points should be on the other line
		assertEquals(0, Distance3D_F32.distance(a.line, b.line.p),GrlConstants.TEST_F32);
		assertEquals(0, Distance3D_F32.distance(b.line,a.line.p),GrlConstants.TEST_F32);

		// slopes should be the same
		float dot = a.line.slope.dot(b.line.slope);
		float tmp = dot / (a.line.slope.norm()*b.line.slope.norm());
		float angle;
		if( tmp > 1.0f )
			angle = 0;
		else if( tmp < -1.0f )
			angle = (float)Math.PI;
		else
			angle = (float)Math.acos( tmp );
		assertTrue( (float)Math.abs(angle) < GrlConstants.TEST_F32 ||
				(float)Math.abs(angle-Math.PI) < GrlConstants.TEST_F32);
	}

	public static Point3D_F32 createPt( Cylinder3D_F32 cylinder , float h , float theta ) {
		Point3D_F32 p = new Point3D_F32();
		p.x = cylinder.radius*(float)Math.cos(theta);
		p.y = cylinder.radius*(float)Math.sin(theta);
		p.z = h;

		Vector3D_F32 axisZ = new Vector3D_F32(0,0,1);
		Vector3D_F32 cross = axisZ.cross(cylinder.line.slope);
		if( (float)Math.abs(cross.norm()) < GrlConstants.TEST_F32) {
			cross.set(0,0,1);
		} else {
			cross.normalize();
		}

		float angle = axisZ.dot(cylinder.line.slope);
		angle = (float)Math.acos( angle / (cylinder.line.slope.norm()));

		Rodrigues_F32 rod = new Rodrigues_F32(angle,cross);
		RowMatrix_F32 R = ConvertRotation3D_F32.rodriguesToMatrix(rod, null);

		GeometryMath_F32.mult(R, p, p);
		p.x += cylinder.line.p.x;
		p.y += cylinder.line.p.y;
		p.z += cylinder.line.p.z;

		return p;
	}

}
