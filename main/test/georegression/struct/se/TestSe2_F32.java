/*
 * Copyright (C) 2011-2015, Peter Abeles. All Rights Reserved.
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
import georegression.struct.point.Point2D_F32;
import georegression.transform.se.SePointOps_F32;

import java.util.Random;

/**
 * @author Peter Abeles
 */
public class TestSe2_F32 extends GenericInvertibleTransformTests_F32<Point2D_F32> {

	Random rand = new Random();

	@Override
	public Point2D_F32 createRandomPoint() {
		return new Point2D_F32( (float)rand.nextGaussian() * 3,
				(float)rand.nextGaussian() * 3 );
	}

	@Override
	public SpecialEuclidean createRandomTransform() {
		return new Se2_F32( (float)rand.nextGaussian() * 3, (float)rand.nextGaussian() * 3,
				( rand.nextFloat() - 0.5f ) * 2.0f * (float)Math.PI );
	}

	@Override
	public Point2D_F32 apply( InvertibleTransform se, Point2D_F32 point, Point2D_F32 result ) {
		return SePointOps_F32.transform( (Se2_F32) se, point, result );
	}
}
