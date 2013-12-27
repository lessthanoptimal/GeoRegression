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

package georegression.struct.point;

import georegression.struct.GeoTuple2D_F32;

/**
 * A point in 2D composed of float
 */
@SuppressWarnings({"unchecked"})
public class Point2D_F32 extends GeoTuple2D_F32<Point2D_F32> {

	public Point2D_F32( float x, float y ) {
		set( x, y );
	}

	public Point2D_F32() {
	}

	public Point2D_F32( Point2D_F32 pt ) {
		set( pt.x, pt.y );
	}

	@Override
	public Point2D_F32 createNewInstance() {
		return new Point2D_F32();
	}

	public void set( Point2D_F32 orig ) {
		_set( orig );
	}

	@Override
	public Point2D_F32 copy() {
		return new Point2D_F32( this );
	}

	public String toString() {
		return "P( " + x + " " + y + " )";
	}
}