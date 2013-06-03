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

package georegression.struct;

import georegression.struct.point.*;
import georegression.struct.se.Se3_F32;
import georegression.struct.se.Se3_F64;

/**
 * Functions for converting between 32bit and 64bit structures
 *
 * @author Peter Abeles
 */
public class ConvertStructureDataType {

	public static Se3_F32 convert( Se3_F64 src , Se3_F32 dst ) {
		if( dst == null ) {
			dst = new Se3_F32();
		}

		dst.getR().set(src.getR());
		convert(src.T,dst.T);

		return dst;
	}

	public static Point3D_F32 convert( Point3D_F64 src , Point3D_F32 dst ) {
		if( dst == null ) {
			dst = new Point3D_F32();
		}

		dst.x = (float)src.x;
		dst.y = (float)src.y;
		dst.z = (float)src.z;

		return dst;
	}

	public static Point2D_F32 convert( Point2D_F64 src , Point2D_F32 dst ) {
		if( dst == null ) {
			dst = new Point2D_F32();
		}

		dst.x = (float)src.x;
		dst.y = (float)src.y;

		return dst;
	}

	public static Vector3D_F32 convert( Vector3D_F64 src , Vector3D_F32 dst ) {
		if( dst == null ) {
			dst = new Vector3D_F32();
		}

		dst.x = (float)src.x;
		dst.y = (float)src.y;
		dst.z = (float)src.z;

		return dst;
	}

	public static Vector2D_F32 convert( Vector2D_F64 src , Vector2D_F32 dst ) {
		if( dst == null ) {
			dst = new Vector2D_F32();
		}

		dst.x = (float)src.x;
		dst.y = (float)src.y;

		return dst;
	}
}
