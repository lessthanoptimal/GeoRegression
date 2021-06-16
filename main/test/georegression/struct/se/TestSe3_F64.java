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

package georegression.struct.se;

import georegression.struct.GenericInvertibleTransformTests_F64;
import georegression.struct.InvertibleTransform;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import georegression.transform.se.SePointOps_F64;
import org.ejml.data.DMatrixRMaj;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * @author Peter Abeles
 */
public class TestSe3_F64 extends GenericInvertibleTransformTests_F64<Point3D_F64> {

	Random rand = new Random( 234234 );

	/**
	 * Checks to see if the constructor correctly saves the reference or copies the values.
	 */
	@Test
	void constructor_assign() {
		DMatrixRMaj R = new DMatrixRMaj( 3, 3 );
		Vector3D_F64 T = new Vector3D_F64( 1, 2, 3 );

		Se3_F64 a = new Se3_F64( R, T, false );
		assertNotSame(R, a.getR());
		assertNotSame(T, a.getT());

		a = new Se3_F64( R, T, true );
		assertSame(R, a.getR());
		assertSame(T, a.getT());
	}

	@Override
	public Point3D_F64 createRandomPoint() {
		return new Point3D_F64( rand.nextGaussian() * 3,
				rand.nextGaussian() * 3, rand.nextGaussian() * 3 );
	}

	@Override
	public SpecialEuclidean createRandomTransform() {

		double rotX = (double) ( ( rand.nextDouble() - 0.5 ) * 2.0 * Math.PI );
		double rotY = (double) ( ( rand.nextDouble() - 0.5 ) * 2.0 * Math.PI );
		double rotZ = (double) ( ( rand.nextDouble() - 0.5 ) * 2.0 * Math.PI );
		double x = (double) ( rand.nextGaussian() * 2 );
		double y = (double) ( rand.nextGaussian() * 2 );
		double z = (double) ( rand.nextGaussian() * 2 );

		Se3_F64 ret = new Se3_F64();

		SpecialEuclideanOps_F64.eulerXyz(x, y, z, rotX, rotY, rotZ, ret );

		return ret;
	}

	@Override
	public Point3D_F64 apply( InvertibleTransform se, Point3D_F64 point, @Nullable Point3D_F64 result ) {
		return SePointOps_F64.transform( (Se3_F64) se, (Point3D_F64) point, (Point3D_F64) result );
	}
}
