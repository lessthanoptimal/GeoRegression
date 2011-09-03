/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
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
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.struct.se;

import jgrl.struct.GenericInvertibleTransformTests_F32;
import jgrl.struct.InvertibleTransform;
import jgrl.struct.point.Point2D_F32;
import jgrl.transform.se.SePointOps_F32;

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
