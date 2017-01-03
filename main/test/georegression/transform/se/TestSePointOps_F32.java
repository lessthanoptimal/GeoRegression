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

package georegression.transform.se;

import georegression.geometry.ConvertRotation3D_F32;
import georegression.misc.GrlConstants;
import georegression.struct.EulerType;
import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;
import georegression.struct.se.Se2_F32;
import georegression.struct.se.Se3_F32;
import org.ejml.data.DenseMatrix32F;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestSePointOps_F32 {

	@Test
	public void transform_2d_single() {
		Se2_F32 tran = new Se2_F32( -2, 3, (float)Math.PI );

		Point2D_F32 pt = new Point2D_F32( 2, 4 );

		// see if it creates a new instance correctly
		Point2D_F32 found = SePointOps_F32.transform( tran, pt, null );

		assertEquals( -4, found.getX(), GrlConstants.TEST_F32);
		assertEquals( -1, found.getY(), GrlConstants.TEST_F32);
		assertEquals( 2, pt.getX(), GrlConstants.TEST_F32);
		assertEquals( 4, pt.getY(), GrlConstants.TEST_F32);

		// now provide it an input to work off of
		found.set( 10, 10 );
		SePointOps_F32.transform( tran, pt, found );
		assertEquals( -4, found.getX(), GrlConstants.TEST_F32);
		assertEquals( -1, found.getY(), GrlConstants.TEST_F32);

		// modify the original
		SePointOps_F32.transform( tran, pt, pt );
		assertEquals( -4, pt.getX(), GrlConstants.TEST_F32);
		assertEquals( -1, pt.getY(), GrlConstants.TEST_F32);
	}

	@Test
	public void transformReverse_2d_single() {
		Se2_F32 tran = new Se2_F32( -2, 3, (float)Math.PI / 2.0f );

		Point2D_F32 pt = new Point2D_F32( 2, 4 );

		// see if it creates a new instance correctly
		Point2D_F32 found = SePointOps_F32.transformReverse( tran, pt, null );

		assertEquals( 1, found.getX(), GrlConstants.TEST_F32);
		assertEquals( -4, found.getY(), GrlConstants.TEST_F32);
		assertEquals( 2, pt.getX(), GrlConstants.TEST_F32);
		assertEquals( 4, pt.getY(), GrlConstants.TEST_F32);

		// now provide it an input to work off of
		found.set( 10, 10 );
		SePointOps_F32.transformReverse( tran, pt, found );
		assertEquals( 1, found.getX(), GrlConstants.TEST_F32);
		assertEquals( -4, found.getY(), GrlConstants.TEST_F32);

		// modify the original
		SePointOps_F32.transformReverse( tran, pt, pt );
		assertEquals( 1, pt.getX(), GrlConstants.TEST_F32);
		assertEquals( -4, pt.getY(), GrlConstants.TEST_F32);
	}

	@Test
	public void transform_2d_array() {
		Se2_F32 tran = new Se2_F32( -2, 3, (float)Math.PI );

		Point2D_F32 pts[] = new Point2D_F32[20];
		for( int i = 0; i < pts.length; i++ ) {
			pts[i] = new Point2D_F32( 2, 4 );
		}

		int N = 12;
		SePointOps_F32.transform( tran, pts, N );
		for( int i = 0; i < N; i++ ) {
			assertEquals( -4, pts[i].getX(), GrlConstants.TEST_F32);
			assertEquals( -1, pts[i].getY(), GrlConstants.TEST_F32);
		}
		// see if the stuff after N has not been modified
		for( int i = N; i < pts.length; i++ ) {
			assertEquals( 2, pts[i].getX(), GrlConstants.TEST_F32);
			assertEquals( 4, pts[i].getY(), GrlConstants.TEST_F32);
		}
	}

	@Test
	public void transform_2d_list() {
		Se2_F32 tran = new Se2_F32( -2, 3, (float)Math.PI );

		List<Point2D_F32> pts = new ArrayList<Point2D_F32>();
		for( int i = 0; i < 20; i++ ) {
			pts.add( new Point2D_F32( 2, 4 ) );
		}

		SePointOps_F32.transform( tran, pts );
		for( Point2D_F32 pt : pts ) {
			assertEquals( -4, pt.getX(), GrlConstants.TEST_F32);
			assertEquals( -1, pt.getY(), GrlConstants.TEST_F32);
		}
	}

	@Test
	public void transform_3d_single() {
		DenseMatrix32F R = ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ, 0, (float)Math.PI / 2, 0, null );
		Vector3D_F32 T = new Vector3D_F32( 1, 2, 3 );

		Point3D_F32 P = new Point3D_F32( 1, 7, 9 );
		Point3D_F32 Pt = new Point3D_F32();

		Se3_F32 se = new Se3_F32( R, T );

		SePointOps_F32.transform( se, P, Pt );

		assertEquals( 10, Pt.getX(), GrlConstants.TEST_F32);
		assertEquals( 9, Pt.getY(), GrlConstants.TEST_F32);
		assertEquals( 2, Pt.getZ(), GrlConstants.TEST_F32);
	}

	@Test
	public void transformReverse_3d_single() {
		DenseMatrix32F R = ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ, 0, (float)Math.PI / 2, 0, null );
		Vector3D_F32 T = new Vector3D_F32( 1, 2, 3 );

		Point3D_F32 P = new Point3D_F32( 10, 9, 2 );
		Point3D_F32 Pt = new Point3D_F32();

		Se3_F32 se = new Se3_F32( R, T );

		SePointOps_F32.transformReverse( se, P, Pt );

		assertEquals( 1, Pt.getX(), GrlConstants.TEST_F32);
		assertEquals( 7, Pt.getY(), GrlConstants.TEST_F32);
		assertEquals( 9, Pt.getZ(), GrlConstants.TEST_F32);
	}
}
