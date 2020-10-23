/*
 * Copyright (C)  2020, Peter Abeles. All Rights Reserved.
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

package georegression.helper;

import georegression.struct.point.Point2D_F64;
import org.ddogleg.nn.alg.KdTreeDistance;

/**
 * @author Peter Abeles
 */
public class KdTreePoint2D_F64 implements KdTreeDistance<Point2D_F64> {
	@Override
	public /**/double distance(Point2D_F64 a, Point2D_F64 b) {
		return a.distance2(b);
	}

	@Override
	public /**/double valueAt(Point2D_F64 point, int index) {
		switch( index ) {
			case 0: return point.x;
			case 1: return point.y;
		}
		throw new IllegalArgumentException("Out of bounds. "+index);
	}

	@Override
	public int length() {
		return 2;
	}
}
