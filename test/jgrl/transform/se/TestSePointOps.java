/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.transform.se;

import jgrl.geometry.RotationMatrixGenerator;
import jgrl.struct.point.Point2D_F64;
import jgrl.struct.point.Point3D_F64;
import jgrl.struct.point.Vector3D_F64;
import jgrl.struct.se.Se2_F64;
import jgrl.struct.se.Se3_F64;
import org.ejml.data.DenseMatrix64F;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Peter Abeles
 */
public class TestSePointOps {

	@Test
	public void transform_2d_single() {
		Se2_F64 tran = new Se2_F64( -2, 3, Math.PI );

		Point2D_F64 pt = new Point2D_F64( 2, 4 );

		// see if it creates a new instance correctly
		Point2D_F64 found = SePointOps_F64.transform( tran, pt, null );

		assertEquals( -4, found.getX(), 1e-8 );
		assertEquals( -1, found.getY(), 1e-8 );
		assertEquals( 2, pt.getX(), 1e-8 );
		assertEquals( 4, pt.getY(), 1e-8 );

		// now provide it an input to work off of
		found.set( 10, 10 );
		SePointOps_F64.transform( tran, pt, found );
		assertEquals( -4, found.getX(), 1e-8 );
		assertEquals( -1, found.getY(), 1e-8 );

		// modify the original
		SePointOps_F64.transform( tran, pt, pt );
		assertEquals( -4, pt.getX(), 1e-8 );
		assertEquals( -1, pt.getY(), 1e-8 );
	}

	@Test
	public void transformReverse_2d_single() {
		Se2_F64 tran = new Se2_F64( -2, 3, Math.PI / 2.0 );

		Point2D_F64 pt = new Point2D_F64( 2, 4 );

		// see if it creates a new instance correctly
		Point2D_F64 found = SePointOps_F64.transformReverse( tran, pt, null );

		assertEquals( 1, found.getX(), 1e-8 );
		assertEquals( -4, found.getY(), 1e-8 );
		assertEquals( 2, pt.getX(), 1e-8 );
		assertEquals( 4, pt.getY(), 1e-8 );

		// now provide it an input to work off of
		found.set( 10, 10 );
		SePointOps_F64.transformReverse( tran, pt, found );
		assertEquals( 1, found.getX(), 1e-8 );
		assertEquals( -4, found.getY(), 1e-8 );

		// modify the original
		SePointOps_F64.transformReverse( tran, pt, pt );
		assertEquals( 1, pt.getX(), 1e-8 );
		assertEquals( -4, pt.getY(), 1e-8 );
	}

	@Test
	public void transform_2d_array() {
		Se2_F64 tran = new Se2_F64( -2, 3, Math.PI );

		Point2D_F64 pts[] = new Point2D_F64[20];
		for( int i = 0; i < pts.length; i++ ) {
			pts[i] = new Point2D_F64( 2, 4 );
		}

		int N = 12;
		SePointOps_F64.transform( tran, pts, N );
		for( int i = 0; i < N; i++ ) {
			assertEquals( -4, pts[i].getX(), 1e-8 );
			assertEquals( -1, pts[i].getY(), 1e-8 );
		}
		// see if the stuff after N has not been modified
		for( int i = N; i < pts.length; i++ ) {
			assertEquals( 2, pts[i].getX(), 1e-8 );
			assertEquals( 4, pts[i].getY(), 1e-8 );
		}
	}

	@Test
	public void transform_2d_list() {
		Se2_F64 tran = new Se2_F64( -2, 3, Math.PI );

		List<Point2D_F64> pts = new ArrayList<Point2D_F64>();
		for( int i = 0; i < 20; i++ ) {
			pts.add( new Point2D_F64( 2, 4 ) );
		}

		SePointOps_F64.transform( tran, pts );
		for( Point2D_F64 pt : pts ) {
			assertEquals( -4, pt.getX(), 1e-8 );
			assertEquals( -1, pt.getY(), 1e-8 );
		}
	}

	@Test
	public void transform_3d_single() {
		DenseMatrix64F R = RotationMatrixGenerator.eulerXYZ( 0, Math.PI / 2, 0, null );
		Vector3D_F64 T = new Vector3D_F64( 1, 2, 3 );

		Point3D_F64 P = new Point3D_F64( 1, 7, 9 );
		Point3D_F64 Pt = new Point3D_F64();

		Se3_F64 se = new Se3_F64( R, T );

		SePointOps_F64.transform( se, P, Pt );

		assertEquals( 10, Pt.getX(), 1e-8 );
		assertEquals( 9, Pt.getY(), 1e-8 );
		assertEquals( 2, Pt.getZ(), 1e-8 );
	}

	@Test
	public void transformReverse_3d_single() {
		DenseMatrix64F R = RotationMatrixGenerator.eulerXYZ( 0, Math.PI / 2, 0, null );
		Vector3D_F64 T = new Vector3D_F64( 1, 2, 3 );

		Point3D_F64 P = new Point3D_F64( 10, 9, 2 );
		Point3D_F64 Pt = new Point3D_F64();

		Se3_F64 se = new Se3_F64( R, T );

		SePointOps_F64.transformReverse( se, P, Pt );

		assertEquals( 1, Pt.getX(), 1e-8 );
		assertEquals( 7, Pt.getY(), 1e-8 );
		assertEquals( 9, Pt.getZ(), 1e-8 );
	}
}
