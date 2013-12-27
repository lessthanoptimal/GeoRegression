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

import georegression.struct.GeoTuple3D_F32;

/**
 *
 *
 */
@SuppressWarnings({"unchecked"})
public class Point3D_F32 extends GeoTuple3D_F32 {

	public Point3D_F32( Point3D_F32 pt ) {
		super( pt.x, pt.y, pt.z );
	}

	public Point3D_F32( float x, float y, float z ) {
		super( x, y, z );
	}

	public Point3D_F32() {
	}

	public Point3D_F32 plus( Vector3D_F32 a ) {
		return new Point3D_F32( x + a.getX(), y + a.getY(), z + a.getZ() );
	}

	// todo remove this?  Makes no sense mathematically

	public Point3D_F32 plus( Point3D_F32 a ) {
		return new Point3D_F32( x + a.getX(), y + a.getY(), z + a.getZ() );
	}

	/**
	 * Converts the point into a vector.
	 *
	 * @return Vector with the same (x,y,z) values.
	 */
	public Vector3D_F32 toVector() {
		return new Vector3D_F32(x,y,z);
	}

	@Override
	public Point3D_F32 copy() {
		return new Point3D_F32( x, y, z );
	}

	public void set( Point3D_F32 worldLoc ) {
		_set( worldLoc );
	}

	@Override
	public String toString() {
		return "P( " + x + " " + y + " " + z + " )";
	}

	@Override
	public Point3D_F32 createNewInstance() {
		return new Point3D_F32();
	}
}
