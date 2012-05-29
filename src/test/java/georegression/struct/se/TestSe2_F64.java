/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
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

package georegression.struct.se;

import georegression.struct.GenericInvertibleTransformTests_F64;
import georegression.struct.InvertibleTransform;
import georegression.struct.point.Point2D_F64;
import georegression.transform.se.SePointOps_F64;

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
	public Point2D_F64 apply( InvertibleTransform se, Point2D_F64 point, Point2D_F64 result ) {
		return SePointOps_F64.transform( (Se2_F64) se, point, result );
	}
}
