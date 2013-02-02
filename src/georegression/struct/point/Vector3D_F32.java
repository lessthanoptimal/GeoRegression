/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.struct.point;

import georegression.geometry.GeometryMath_F32;
import georegression.struct.GeoTuple3D_F32;

/**
 *
 *
 */
public class Vector3D_F32 extends GeoTuple3D_F32 {

	public Vector3D_F32( float x, float y, float z ) {
		super( x, y, z );
	}

	public Vector3D_F32() {
	}

	public Vector3D_F32( Point3D_F32 a, Point3D_F32 b ) {
		x = b.getX() - a.getX();
		y = b.getY() - a.getY();
		z = b.getZ() - a.getZ();
	}

	public void set( Vector3D_F32 v ) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	public Vector3D_F32 times( float scalar ) {
		return new Vector3D_F32( x * scalar, y * scalar, z * scalar );
	}

	public Vector3D_F32 copy() {
		return new Vector3D_F32( x, y, z );
	}

	public Vector3D_F32 cross( Vector3D_F32 b ) {
		Vector3D_F32 c = new Vector3D_F32();
		GeometryMath_F32.cross( this, b, c );

		return c;
	}

	@Override
	public Vector3D_F32 createNewInstance() {
		return new Vector3D_F32();
	}

	@Override
	public String toString() {
		return "V( " + x + " " + y + " " + z + " )";
	}

	public void normalize() {
		float r = norm();
		x /= r;
		y /= r;
		z /= r;
	}

	public float dot( Vector3D_F32 a ) {
		return x * a.x + y * a.y + z * a.z;
	}
}
