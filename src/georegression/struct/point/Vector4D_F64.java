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
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.struct.point;

import georegression.struct.GeoTuple4D_F64;

/**
 *
 *
 */
public class Vector4D_F64 extends GeoTuple4D_F64 {

	public Vector4D_F64(double x, double y, double z , double w ) {
		super( x, y, z , w );
	}

	public Vector4D_F64() {
	}

	public Vector4D_F64(Point4D_F64 a, Point4D_F64 b) {
		x = b.getX() - a.getX();
		y = b.getY() - a.getY();
		z = b.getZ() - a.getZ();
		w = b.getW() - a.getW();
	}

	public void set( Vector4D_F64 v ) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = v.w;
	}

	public Vector4D_F64 times( double scalar ) {
		return new Vector4D_F64( x * scalar, y * scalar, z * scalar , w * scalar );
	}

	public Vector4D_F64 copy() {
		return new Vector4D_F64( x, y, z , w );
	}

	@Override
	public Vector4D_F64 createNewInstance() {
		return new Vector4D_F64();
	}

	@Override
	public String toString() {
		return "V( " + x + " " + y + " " + z + " " + w + " )";
	}

	public void normalize() {
		double r = norm();
		x /= r;
		y /= r;
		z /= r;
		w /= r;
	}

	public double dot( Vector4D_F64 a ) {
		return x * a.x + y * a.y + z * a.z + w * a.w;
	}
}
