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

import georegression.struct.GeoTuple4D_F64;

/**
 *
 *
 */
@SuppressWarnings({"unchecked"})
public class Point4D_F64 extends GeoTuple4D_F64 {

	public Point4D_F64(Point4D_F64 pt) {
		super( pt.x, pt.y, pt.z , pt.w );
	}

	public Point4D_F64(double x, double y, double z, double w) {
		super( x, y, z , w);
	}

	public Point4D_F64() {
	}

	public Point4D_F64 plus( Vector4D_F64 a ) {
		return new Point4D_F64( x + a.getX(), y + a.getY(), z + a.getZ() , w + a.getW());
	}

	// todo remove this?  Makes no sense mathematically

	public Point4D_F64 plus( Point4D_F64 a ) {
		return new Point4D_F64( x + a.getX(), y + a.getY(), z + a.getZ() , w + a.getW() );
	}

	@Override
	public Point4D_F64 copy() {
		return new Point4D_F64( x, y, z , w);
	}

	public void set( Point4D_F64 worldLoc ) {
		_set( worldLoc );
	}

	@Override
	public String toString() {
		return "P( " + x + " " + y + " " + z + " " + w +" )";
	}

	@Override
	public Point4D_F64 createNewInstance() {
		return new Point4D_F64();
	}
}
