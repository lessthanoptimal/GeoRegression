/*
 * Copyright (C) 2011-2020, Peter Abeles. All Rights Reserved.
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

package georegression.struct.homography;

import georegression.struct.GenericInvertibleTransformTests_F64;
import georegression.struct.InvertibleTransform;
import georegression.struct.point.Point2D_F64;
import georegression.transform.homography.HomographyPointOps_F64;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * @author Peter Abeles
 */
public class TestHomography2D_F64 extends GenericInvertibleTransformTests_F64<Point2D_F64> {

	Random rand = new Random(23423);

	@Override
	public Point2D_F64 createRandomPoint() {
		return new Point2D_F64( (double) rand.nextGaussian() * 3,
				(double) rand.nextGaussian() * 3 );
	}

	@Override
	public Homography2D_F64 createRandomTransform() {

		double a11 = (double) rand.nextGaussian() * 3.0;
		double a12 = (double) rand.nextGaussian() * 3.0;
		double a13 = (double) rand.nextGaussian() * 3.0;
		double a21 = (double) rand.nextGaussian() * 3.0;
		double a22 = (double) rand.nextGaussian() * 3.0;
		double a23 = (double) rand.nextGaussian() * 3.0;
		double a31 = (double) rand.nextGaussian() * 3.0;
		double a32 = (double) rand.nextGaussian() * 3.0;
		double a33 = (double) rand.nextGaussian() * 3.0;

		return new Homography2D_F64( a11, a12, a13 , a21, a22, a23, a31 , a32 , a33 );
	}

	@Override
	public Point2D_F64 apply( InvertibleTransform se, Point2D_F64 point, @Nullable Point2D_F64 result ) {
		return HomographyPointOps_F64.transform((Homography2D_F64) se, point, result);
	}
}
