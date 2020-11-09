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

package georegression.transform.se;

import georegression.geometry.ConvertRotation3D_F64;
import georegression.misc.GrlConstants;
import georegression.struct.EulerType;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Point4D_F64;
import georegression.struct.point.Vector3D_F64;
import georegression.struct.se.Se2_F64;
import georegression.struct.se.Se3_F64;
import org.ejml.data.DMatrixRMaj;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestSePointOps_F64 {

	@Test
	void transform_2d_single() {
		Se2_F64 tran = new Se2_F64( -2, 3, Math.PI );

		Point2D_F64 pt = new Point2D_F64( 2, 4 );

		// see if it creates a new instance correctly
		Point2D_F64 found = SePointOps_F64.transform( tran, pt, null );

		assertEquals( -4, found.getX(), GrlConstants.TEST_F64);
		assertEquals( -1, found.getY(), GrlConstants.TEST_F64);
		assertEquals( 2, pt.getX(), GrlConstants.TEST_F64);
		assertEquals( 4, pt.getY(), GrlConstants.TEST_F64);

		// now provide it an input to work off of
		found.setTo( 10, 10 );
		SePointOps_F64.transform( tran, pt, found );
		assertEquals( -4, found.getX(), GrlConstants.TEST_F64);
		assertEquals( -1, found.getY(), GrlConstants.TEST_F64);

		// modify the original
		SePointOps_F64.transform( tran, pt, pt );
		assertEquals( -4, pt.getX(), GrlConstants.TEST_F64);
		assertEquals( -1, pt.getY(), GrlConstants.TEST_F64);
	}

	@Test
	void transformReverse_2d_single() {
		Se2_F64 tran = new Se2_F64( -2, 3, Math.PI / 2.0 );

		Point2D_F64 pt = new Point2D_F64( 2, 4 );

		// see if it creates a new instance correctly
		Point2D_F64 found = SePointOps_F64.transformReverse( tran, pt, null );

		assertEquals( 1, found.getX(), GrlConstants.TEST_F64);
		assertEquals( -4, found.getY(), GrlConstants.TEST_F64);
		assertEquals( 2, pt.getX(), GrlConstants.TEST_F64);
		assertEquals( 4, pt.getY(), GrlConstants.TEST_F64);

		// now provide it an input to work off of
		found.setTo( 10, 10 );
		SePointOps_F64.transformReverse( tran, pt, found );
		assertEquals( 1, found.getX(), GrlConstants.TEST_F64);
		assertEquals( -4, found.getY(), GrlConstants.TEST_F64);

		// modify the original
		SePointOps_F64.transformReverse( tran, pt, pt );
		assertEquals( 1, pt.getX(), GrlConstants.TEST_F64);
		assertEquals( -4, pt.getY(), GrlConstants.TEST_F64);
	}

	@Test
	void transform_2d_array() {
		Se2_F64 tran = new Se2_F64( -2, 3, Math.PI );

		Point2D_F64 pts[] = new Point2D_F64[20];
		for( int i = 0; i < pts.length; i++ ) {
			pts[i] = new Point2D_F64( 2, 4 );
		}

		int N = 12;
		SePointOps_F64.transform( tran, pts, N );
		for( int i = 0; i < N; i++ ) {
			assertEquals( -4, pts[i].getX(), GrlConstants.TEST_F64);
			assertEquals( -1, pts[i].getY(), GrlConstants.TEST_F64);
		}
		// see if the stuff after N has not been modified
		for( int i = N; i < pts.length; i++ ) {
			assertEquals( 2, pts[i].getX(), GrlConstants.TEST_F64);
			assertEquals( 4, pts[i].getY(), GrlConstants.TEST_F64);
		}
	}

	@Test
	void transform_2d_list() {
		Se2_F64 tran = new Se2_F64( -2, 3, Math.PI );

		List<Point2D_F64> pts = new ArrayList<Point2D_F64>();
		for( int i = 0; i < 20; i++ ) {
			pts.add( new Point2D_F64( 2, 4 ) );
		}

		SePointOps_F64.transform( tran, pts );
		for( Point2D_F64 pt : pts ) {
			assertEquals( -4, pt.getX(), GrlConstants.TEST_F64);
			assertEquals( -1, pt.getY(), GrlConstants.TEST_F64);
		}
	}

	@Test
	void transform_3d_single() {
		DMatrixRMaj R = ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ, 0, Math.PI / 2, 0, null );
		Vector3D_F64 T = new Vector3D_F64( 1, 2, 3 );

		Point3D_F64 P = new Point3D_F64( 1, 7, 9 );
		Point3D_F64 Pt = new Point3D_F64();

		Se3_F64 se = new Se3_F64( R, T );

		SePointOps_F64.transform( se, P, Pt );

		assertEquals( 10, Pt.getX(), GrlConstants.TEST_F64);
		assertEquals( 9 , Pt.getY(), GrlConstants.TEST_F64);
		assertEquals( 2 , Pt.getZ(), GrlConstants.TEST_F64);
	}

	@Test
	void transform_4d_3D() {
		DMatrixRMaj R = ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ, 0, Math.PI / 2, 0, null );
		Vector3D_F64 T = new Vector3D_F64( 1, 2, 3 );

		double w = 1.5;
		Point4D_F64 P = new Point4D_F64( 1, 7, 9 , w);
		Point3D_F64 P3 = new Point3D_F64(1/w,7/w,9/w);
		Point3D_F64 Pt = new Point3D_F64();

		Se3_F64 se = new Se3_F64( R, T );

		SePointOps_F64.transform( se, P, Pt );
		SePointOps_F64.transform( se, P3, P3 );

		assertEquals( P3.x, Pt.getX(), GrlConstants.TEST_F64);
		assertEquals( P3.y , Pt.getY(), GrlConstants.TEST_F64);
		assertEquals( P3.z , Pt.getZ(), GrlConstants.TEST_F64);
	}

	@Test
	void transform_4d_4D() {
		DMatrixRMaj R = ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ, 0, Math.PI / 2, 0, null );
		Vector3D_F64 T = new Vector3D_F64( 1, 2, 3 );

		Point4D_F64 P = new Point4D_F64( 1, 7, 9 , 1);
		Point4D_F64 Pt = new Point4D_F64();

		Se3_F64 se = new Se3_F64( R, T );

		SePointOps_F64.transform( se, P, Pt );

		assertEquals( 10, Pt.getX(), GrlConstants.TEST_F64);
		assertEquals( 9 , Pt.getY(), GrlConstants.TEST_F64);
		assertEquals( 2 , Pt.getZ(), GrlConstants.TEST_F64);
		assertEquals( 1 , Pt.w, GrlConstants.TEST_F64);

		P.w = 0;
		SePointOps_F64.transform( se, P, Pt );
		assertEquals( 9 , Pt.getX(), GrlConstants.TEST_F64);
		assertEquals( 7 , Pt.getY(), GrlConstants.TEST_F64);
		assertEquals(-1 , Pt.getZ(), GrlConstants.TEST_F64);
		assertEquals(0 , Pt.w, GrlConstants.TEST_F64);
	}

	@Test
	void transformV_4d_3d() {
		DMatrixRMaj R = ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ, 0, Math.PI / 2, 0, null );
		var T = new Vector3D_F64( 1, 2, 3 );

		var P = new Point4D_F64( 1, 7, 9 , 1);
		var Pt = new Point3D_F64();

		var se = new Se3_F64( R, T );

		SePointOps_F64.transformV( se, P, Pt );

		assertEquals( 10, Pt.getX(), GrlConstants.TEST_F64);
		assertEquals( 9 , Pt.getY(), GrlConstants.TEST_F64);
		assertEquals( 2 , Pt.getZ(), GrlConstants.TEST_F64);
	}

	@Test
	void transformReverse_3d_single() {
		DMatrixRMaj R = ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ, 0, Math.PI / 2, 0, null );
		Vector3D_F64 T = new Vector3D_F64( 1, 2, 3 );

		Point3D_F64 P = new Point3D_F64( 10, 9, 2 );
		Point3D_F64 Pt = new Point3D_F64();

		Se3_F64 se = new Se3_F64( R, T );

		SePointOps_F64.transformReverse( se, P, Pt );

		assertEquals( 1, Pt.getX(), GrlConstants.TEST_F64);
		assertEquals( 7, Pt.getY(), GrlConstants.TEST_F64);
		assertEquals( 9, Pt.getZ(), GrlConstants.TEST_F64);
	}
}
