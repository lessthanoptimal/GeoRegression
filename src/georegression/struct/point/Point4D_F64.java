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
 * JGRL is distributed in the hope that it will be useful,
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
