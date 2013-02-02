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

import georegression.struct.GeoTuple4D_F32;

/**
 *
 *
 */
@SuppressWarnings({"unchecked"})
public class Point4D_F32 extends GeoTuple4D_F32 {

	public Point4D_F32(Point4D_F32 pt) {
		super( pt.x, pt.y, pt.z , pt.w );
	}

	public Point4D_F32(float x, float y, float z, float w) {
		super( x, y, z , w);
	}

	public Point4D_F32() {
	}

	public Point4D_F32 plus( Vector4D_F32 a ) {
		return new Point4D_F32( x + a.getX(), y + a.getY(), z + a.getZ() , w + a.getW());
	}

	// todo remove this?  Makes no sense mathematically

	public Point4D_F32 plus( Point4D_F32 a ) {
		return new Point4D_F32( x + a.getX(), y + a.getY(), z + a.getZ() , w + a.getW() );
	}

	@Override
	public Point4D_F32 copy() {
		return new Point4D_F32( x, y, z , w);
	}

	public void set( Point4D_F32 worldLoc ) {
		_set( worldLoc );
	}

	@Override
	public String toString() {
		return "P( " + x + " " + y + " " + z + " " + w +" )";
	}

	@Override
	public Point4D_F32 createNewInstance() {
		return new Point4D_F32();
	}
}
