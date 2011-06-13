/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
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
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.struct.point;

import jgrl.geometry.GeometryMath_F64;
import jgrl.struct.GeoTuple3D_F64;

/**
 *
 *
 */
public class Vector3D_F64 extends GeoTuple3D_F64 {

	public Vector3D_F64( double x, double y, double z ) {
		super( x, y, z );
	}

	public Vector3D_F64() {
	}

	public Vector3D_F64( Point3D_F64 a, Point3D_F64 b ) {
		x = b.getX() - a.getX();
		y = b.getY() - a.getY();
		z = b.getZ() - a.getZ();
	}

	public void set( Vector3D_F64 v ) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	public Vector3D_F64 times( double scalar ) {
		return new Vector3D_F64( x * scalar, y * scalar, z * scalar );
	}

	public Vector3D_F64 copy() {
		return new Vector3D_F64( x, y, z );
	}

	public Vector3D_F64 cross( Vector3D_F64 b ) {
		Vector3D_F64 c = new Vector3D_F64();
		GeometryMath_F64.cross( this, b, c );

		return c;
	}

	@Override
	public Vector3D_F64 createNewInstance() {
		return new Vector3D_F64();
	}

	@Override
	public String toString() {
		return "V( " + x + " " + y + " " + z + " )";
	}

	public void normalize() {
		double r = norm();
		x /= r;
		y /= r;
		z /= r;
	}

	public double dot( Vector3D_F64 a ) {
		return x * a.x + y * a.y + z * a.z;
	}
}
