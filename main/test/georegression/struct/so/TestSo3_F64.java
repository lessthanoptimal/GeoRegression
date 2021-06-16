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

package georegression.struct.so;

import georegression.geometry.ConvertRotation3D_F64;
import georegression.geometry.GeometryMath_F64;
import georegression.struct.EulerType;
import georegression.struct.GenericInvertibleTransformTests_F64;
import georegression.struct.InvertibleTransform;
import georegression.struct.point.Point3D_F64;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * @author Peter Abeles
 */
class TestSo3_F64 extends GenericInvertibleTransformTests_F64<Point3D_F64> {

	Random rand = new Random( 234234 );

	@Override
	public Point3D_F64 createRandomPoint() {
		return new Point3D_F64( rand.nextGaussian() * 3, rand.nextGaussian() * 3, rand.nextGaussian() * 3 );
	}

	@Override
	public So3_F64 createRandomTransform() {

		double rotX = (double) ( ( rand.nextDouble() - 0.5 ) * 2.0 * Math.PI );
		double rotY = (double) ( ( rand.nextDouble() - 0.5 ) * 2.0 * Math.PI );
		double rotZ = (double) ( ( rand.nextDouble() - 0.5 ) * 2.0 * Math.PI );

		So3_F64 ret = new So3_F64();

		ConvertRotation3D_F64.eulerToMatrix(EulerType.XYZ, rotX, rotY, rotZ, ret.R);
		return ret;
	}

	@Override
	public Point3D_F64 apply(InvertibleTransform transform, Point3D_F64 point, @Nullable Point3D_F64 result ) {
		return GeometryMath_F64.mult(((So3_F64)transform).R, point, result);
	}
}