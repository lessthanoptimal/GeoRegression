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

package georegression.struct.se;

import georegression.struct.GenericInvertibleTransformTests_F64;
import georegression.struct.InvertibleTransform;
import georegression.struct.point.Point2D_F64;
import georegression.transform.se.SePointOps_F64;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * @author Peter Abeles
 */
public class TestSe2_F64 extends GenericInvertibleTransformTests_F64<Point2D_F64> {

	Random rand = new Random();

	@Override
	public Point2D_F64 createRandomPoint() {
		return new Point2D_F64( rand.nextGaussian() * 3,
				rand.nextGaussian() * 3 );
	}

	@Override
	public SpecialEuclidean createRandomTransform() {
		return new Se2_F64( rand.nextGaussian() * 3, rand.nextGaussian() * 3,
				( rand.nextDouble() - 0.5 ) * 2.0 * Math.PI );
	}

	@Override
	public Point2D_F64 apply( InvertibleTransform se, Point2D_F64 point, @Nullable Point2D_F64 result ) {
		return SePointOps_F64.transform( (Se2_F64) se, point, result );
	}
}
