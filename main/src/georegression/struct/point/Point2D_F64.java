/*
 * Copyright (C) 2021, Peter Abeles. All Rights Reserved.
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

package georegression.struct.point;

import georegression.struct.GeoTuple2D_F64;

/**
 * A point in 2D
 */
@SuppressWarnings({"unchecked"})
public class Point2D_F64 extends GeoTuple2D_F64<Point2D_F64> {

	public Point2D_F64( GeoTuple2D_F64 orig ) {
		this(orig.x, orig.y);
	}

	public Point2D_F64( double x, double y ) {
		setTo(x, y);
	}

	public Point2D_F64() {}

	public Point2D_F64( Point2D_F64 pt ) {
		setTo(pt.x, pt.y);
	}

	@Override
	public Point2D_F64 createNewInstance() {
		return new Point2D_F64();
	}

	@Override
	public void setTo( Point2D_F64 orig ) {
		_setTo(orig);
	}

	@Override
	public Point2D_F64 copy() {
		return new Point2D_F64(this);
	}

	@Override
	public String toString() {
		return toString("P");
	}
}