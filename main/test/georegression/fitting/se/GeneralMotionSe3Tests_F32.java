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

package georegression.fitting.se;

import georegression.fitting.MotionTransformPoint;
import georegression.geometry.ConvertRotation3D_F32;
import georegression.geometry.UtilPoint3D_F32;
import georegression.misc.GrlConstants;
import georegression.misc.test.GeometryUnitTest;
import georegression.struct.EulerType;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;
import georegression.struct.se.Se3_F32;
import georegression.transform.se.SePointOps_F32;
import org.ejml.data.RowMatrix_F32;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public abstract class GeneralMotionSe3Tests_F32 {
	Random rand = new Random( 434324 );

	abstract MotionTransformPoint<Se3_F32, Point3D_F32> createAlg();

	@Test
	public void noiseless() {
		for( int i = 0; i < 100; i++ ) {
			RowMatrix_F32 R = ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ, (float)rand.nextGaussian(),
					(float)rand.nextGaussian(), (float)rand.nextGaussian(), null);
			Vector3D_F32 T = new Vector3D_F32( (float)rand.nextGaussian(),
					(float)rand.nextGaussian(), (float)rand.nextGaussian() );

			Se3_F32 tran = new Se3_F32( R, T );

			List<Point3D_F32> src = UtilPoint3D_F32.random(-10, 10, 30, rand);
			List<Point3D_F32> dst = new ArrayList<Point3D_F32>();
			for( Point3D_F32 p : src ) {
				dst.add(SePointOps_F32.transform(tran, p, null));
			}

			MotionTransformPoint<Se3_F32, Point3D_F32> alg = createAlg();

			assertTrue( alg.process( src, dst ) );

			Se3_F32 foundSrcToDst = alg.getTransformSrcToDst();

			checkTransform( src, dst, foundSrcToDst, GrlConstants.TEST_F32);
		}
	}

	@Test
	public void noiselessPlanar() {
		for( int i = 0; i < 100; i++ ) {
			RowMatrix_F32 R = ConvertRotation3D_F32.eulerToMatrix(EulerType.XYZ, (float)rand.nextGaussian(),
					(float)rand.nextGaussian(), (float)rand.nextGaussian(), null );
			Vector3D_F32 T = new Vector3D_F32( (float)rand.nextGaussian(),
					(float)rand.nextGaussian(), (float)rand.nextGaussian() );

			Se3_F32 tran = new Se3_F32( R, T );

			List<Point3D_F32> from = createPlanar(30);
			List<Point3D_F32> to = new ArrayList<Point3D_F32>();
			for( Point3D_F32 p : from ) {
				to.add( SePointOps_F32.transform( tran, p, null ) );
			}

			MotionTransformPoint<Se3_F32, Point3D_F32> alg = createAlg();

			assertTrue( alg.process( from, to ) );

			Se3_F32 tranFound = alg.getTransformSrcToDst();
			
//			R.print();
//			tranFound.getR().print();
			
			checkTransform( from, to, tranFound, GrlConstants.TEST_F32);
		}
	}

	private List<Point3D_F32> createPlanar( int N ) {
		List<Point3D_F32> ret = new ArrayList<Point3D_F32>();

		for( int i = 0; i < N; i++ )
			ret.add( new Point3D_F32((float) ((float)rand.nextGaussian()*2),(float)((float)rand.nextGaussian()*2),3));

		return ret;
	}

	public static void checkTransform( List<Point3D_F32> src, List<Point3D_F32> dst, Se3_F32 foundSrcToDst, float tol ) {
		Point3D_F32 foundPt = new Point3D_F32();
		for( int i = 0; i < src.size(); i++ ) {
			Point3D_F32 p = src.get( i );
			SePointOps_F32.transform( foundSrcToDst, p, foundPt );
			GeometryUnitTest.assertEquals(dst.get(i), foundPt, tol);
		}
	}
}
