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

package georegression.struct.homo;

import georegression.struct.GenericInvertibleTransformTests_F32;
import georegression.struct.InvertibleTransform;
import georegression.struct.point.Point2D_F32;
import georegression.transform.homo.HomographyPointOps_F32;

import java.util.Random;

/**
 * @author Peter Abeles
 */
public class TestHomography2D_F32 extends GenericInvertibleTransformTests_F32<Point2D_F32> {

	Random rand = new Random(23423);

	@Override
	public Point2D_F32 createRandomPoint() {
		return new Point2D_F32( (float) (float)rand.nextGaussian() * 3,
				(float) (float)rand.nextGaussian() * 3 );
	}

	@Override
	public Homography2D_F32 createRandomTransform() {

		float a11 = (float) (float)rand.nextGaussian() * 3.0f;
		float a12 = (float) (float)rand.nextGaussian() * 3.0f;
		float a13 = (float) (float)rand.nextGaussian() * 3.0f;
		float a21 = (float) (float)rand.nextGaussian() * 3.0f;
		float a22 = (float) (float)rand.nextGaussian() * 3.0f;
		float a23 = (float) (float)rand.nextGaussian() * 3.0f;
		float a31 = (float) (float)rand.nextGaussian() * 3.0f;
		float a32 = (float) (float)rand.nextGaussian() * 3.0f;
		float a33 = (float) (float)rand.nextGaussian() * 3.0f;

		return new Homography2D_F32( a11, a12, a13 , a21, a22, a23, a31 , a32 , a33 );
	}

	@Override
	public Point2D_F32 apply( InvertibleTransform se, Point2D_F32 point, Point2D_F32 result ) {
		return HomographyPointOps_F32.transform((Homography2D_F32) se, point, result);
	}
}
