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

package jgrl.metric;

import jgrl.struct.GeoTuple3D_F32;
import jgrl.struct.GeoTuple3D_F64;


/**
 * @author Peter Abeles
 */
public class MiscOps {

	public static double dot( GeoTuple3D_F64 a, GeoTuple3D_F64 b ) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}

	public static float dot( GeoTuple3D_F32 a, GeoTuple3D_F32 b ) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}
}
