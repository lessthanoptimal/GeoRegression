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

package georegression.metric;

import georegression.struct.GeoTuple2D_F32;
import georegression.struct.GeoTuple2D_F64;
import georegression.struct.GeoTuple3D_F32;
import georegression.struct.GeoTuple3D_F64;


/**
 * @author Peter Abeles
 */
public class MiscOps {

	public static double dot( double x , double y , double z, GeoTuple3D_F64 b ) {
		return x * b.x + y * b.y + z * b.z;
	}

	public static float dot( float x , float y , float z, GeoTuple3D_F32 b ) {
		return x * b.x + y * b.y + z * b.z;
	}

	public static double dot( GeoTuple3D_F64 a, GeoTuple3D_F64 b ) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}

	public static float dot( GeoTuple3D_F32 a, GeoTuple3D_F32 b ) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}

	public static double dot( GeoTuple2D_F64 a, GeoTuple2D_F64 b ) {
		return a.x * b.x + a.y * b.y;
	}

	public static float dot( GeoTuple2D_F32 a, GeoTuple2D_F32 b ) {
		return a.x * b.x + a.y * b.y;
	}
}
