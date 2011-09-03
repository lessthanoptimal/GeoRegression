/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
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

package jgrl.struct.point;

import jgrl.struct.GeoTuple3D_F32;

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
