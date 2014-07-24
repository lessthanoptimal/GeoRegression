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
