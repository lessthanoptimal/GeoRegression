/*
 * Copyright (C) 2011-2014, Peter Abeles. All Rights Reserved.
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

package georegression.struct.se;

import georegression.struct.GenericInvertibleTransformTests_F32;
import georegression.struct.InvertibleTransform;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;
import georegression.transform.se.SePointOps_F32;
import org.ejml.data.DenseMatrix64F;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertTrue;

/**
 * @author Peter Abeles
 */
public class TestSe3_F32 extends GenericInvertibleTransformTests_F32<Point3D_F32> {

	Random rand = new Random( 234234 );

	/**
	 * Checks to see if the constructor correctly saves the reference or copies the values.
	 */
	@Test
	public void constructor_assign() {
		DenseMatrix64F R = new DenseMatrix64F( 3, 3 );
		Vector3D_F32 T = new Vector3D_F32( 1, 2, 3 );

		Se3_F32 a = new Se3_F32( R, T, false );
		assertTrue( R != a.getR() );
		assertTrue( T != a.getT() );

		a = new Se3_F32( R, T, true );
		assertTrue( R == a.getR() );
		assertTrue( T == a.getT() );
	}

	@Override
	public Point3D_F32 createRandomPoint() {
		return new Point3D_F32( (float)rand.nextGaussian() * 3,
				(float)rand.nextGaussian() * 3, (float)rand.nextGaussian() * 3 );
	}

	@Override
	public SpecialEuclidean createRandomTransform() {

		float rotX = (float) ( ( rand.nextFloat() - 0.5f ) * 2.0f * (float)Math.PI );
		float rotY = (float) ( ( rand.nextFloat() - 0.5f ) * 2.0f * (float)Math.PI );
		float rotZ = (float) ( ( rand.nextFloat() - 0.5f ) * 2.0f * (float)Math.PI );
		float x = (float) ( (float)rand.nextGaussian() * 2 );
		float y = (float) ( (float)rand.nextGaussian() * 2 );
		float z = (float) ( (float)rand.nextGaussian() * 2 );

		Se3_F32 ret = new Se3_F32();

		SpecialEuclideanOps_F32.setEulerXYZ( rotX, rotY, rotZ, x, y, z, ret );

		return ret;
	}

	@Override
	public Point3D_F32 apply( InvertibleTransform se, Point3D_F32 point, Point3D_F32 result ) {
		return SePointOps_F32.transform( (Se3_F32) se, (Point3D_F32) point, (Point3D_F32) result );
	}
}
