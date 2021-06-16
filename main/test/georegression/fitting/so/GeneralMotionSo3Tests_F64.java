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

package georegression.fitting.so;

import georegression.fitting.MotionTransformPoint;
import georegression.geometry.ConvertRotation3D_F64;
import georegression.geometry.GeometryMath_F64;
import georegression.geometry.UtilPoint3D_F64;
import georegression.misc.GrlConstants;
import georegression.misc.test.GeometryUnitTest;
import georegression.struct.EulerType;
import georegression.struct.point.Point3D_F64;
import georegression.struct.so.So3_F64;
import org.ejml.data.DMatrixRMaj;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Peter Abeles
 */
public abstract class GeneralMotionSo3Tests_F64 {
	Random rand = new Random( 434324 );

	abstract MotionTransformPoint<So3_F64, Point3D_F64> createAlg();

	@Test
	void noiseless() {
		for( int i = 0; i < 100; i++ ) {
			DMatrixRMaj R = ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ, rand.nextGaussian(),
					rand.nextGaussian(), rand.nextGaussian(), null);

			So3_F64 tran = new So3_F64( R );

			List<Point3D_F64> src = UtilPoint3D_F64.random(-10, 10, 30, rand);
			List<Point3D_F64> dst = new ArrayList<>();
			for( Point3D_F64 p : src ) {
				dst.add(GeometryMath_F64.mult(tran.R, p, (Point3D_F64) null));
			}

			MotionTransformPoint<So3_F64, Point3D_F64> alg = createAlg();

			assertTrue( alg.process( src, dst ) );

			So3_F64 foundSrcToDst = alg.getTransformSrcToDst();

			checkTransform( src, dst, foundSrcToDst, GrlConstants.TEST_F64);
		}
	}

	@Test
	void noiselessPlanar() {
		for( int i = 0; i < 100; i++ ) {
			DMatrixRMaj R = ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ, rand.nextGaussian(),
					rand.nextGaussian(), rand.nextGaussian(), null );

			So3_F64 tran = new So3_F64( R );

			List<Point3D_F64> from = createPlanar(30);
			List<Point3D_F64> to = new ArrayList<>();
			for( Point3D_F64 p : from ) {
				to.add(GeometryMath_F64.mult(tran.R, p, (Point3D_F64) null));
			}

			MotionTransformPoint<So3_F64, Point3D_F64> alg = createAlg();

			assertTrue( alg.process( from, to ) );

			So3_F64 tranFound = alg.getTransformSrcToDst();
			
//			R.print();
//			tranFound.getR().print();
			
			checkTransform( from, to, tranFound, GrlConstants.TEST_F64);
		}
	}

	private List<Point3D_F64> createPlanar( int N ) {
		List<Point3D_F64> ret = new ArrayList<>();

		for( int i = 0; i < N; i++ )
			ret.add( new Point3D_F64((double) (rand.nextGaussian()*2),(double)(rand.nextGaussian()*2),3));

		return ret;
	}

	public static void checkTransform( List<Point3D_F64> src, List<Point3D_F64> dst, So3_F64 foundSrcToDst, double tol ) {
		Point3D_F64 foundPt = new Point3D_F64();
		for( int i = 0; i < src.size(); i++ ) {
			Point3D_F64 p = src.get( i );
			GeometryMath_F64.mult( foundSrcToDst.R, p, foundPt );
			GeometryUnitTest.assertEquals(dst.get(i), foundPt, tol);
		}
	}
}
