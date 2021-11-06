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

import georegression.struct.GeoTuple3D_F64;

/**
 *
 */
@SuppressWarnings({"unchecked"})
public class Point3D_F64 extends GeoTuple3D_F64<Point3D_F64> {

	public Point3D_F64( GeoTuple3D_F64 pt ) {
		super(pt.x, pt.y, pt.z);
	}

	public Point3D_F64( double x, double y, double z ) {
		super(x, y, z);
	}

	public Point3D_F64() {}

	/**
	 * Converts the point into a vector.
	 *
	 * @return Vector with the same (x,y,z) values.
	 */
	public Vector3D_F64 toVector() {
		return new Vector3D_F64(x, y, z);
	}

	@Override
	public Point3D_F64 copy() {
		return new Point3D_F64(x, y, z);
	}

	@Override
	public void setTo( Point3D_F64 worldLoc ) {
		_setTo(worldLoc);
	}

	@Override
	public String toString() {
		return toString("P");
	}

	@Override
	public Point3D_F64 createNewInstance() {
		return new Point3D_F64();
	}
}
