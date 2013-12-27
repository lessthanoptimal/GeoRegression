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
