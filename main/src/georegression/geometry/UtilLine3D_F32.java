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

package georegression.geometry;

import georegression.struct.line.LineParametric3D_F32;
import georegression.struct.line.LineSegment3D_F32;
import georegression.struct.point.Point3D_F32;

/**
 * Various utilty functions related to lines in 3D
 *
 * @author Peter Abeles
 */
public class UtilLine3D_F32 {
	/**
	 * Converts a {@link LineSegment3D_F32} into {@link LineParametric3D_F32}.
	 *
	 * @param line Line segment
	 * @param output Storage for converted line.  If null new line will be declared.
	 * @return Line in parametric format
	 */
	public static LineParametric3D_F32 convert( LineSegment3D_F32 line , LineParametric3D_F32 output ) {
		if( output == null )
			output = new LineParametric3D_F32();

		output.p.set(line.a);
		output.slope.x = line.b.x - line.a.x;
		output.slope.y = line.b.y - line.a.y;
		output.slope.z = line.b.z - line.a.z;

		return output;
	}

	/**
	 * Computes the value of T for a point on the parametric line
	 *
	 * @param line The line
	 * @param pointOnLine Point on a line
	 * @return Value of T for the point
	 */
	public static float computeT( LineParametric3D_F32 line , Point3D_F32 pointOnLine ) {

		float dx = pointOnLine.x - line.p.x;
		float dy = pointOnLine.y - line.p.y;
		float dz = pointOnLine.z - line.p.z;

		float adx = (float)Math.abs(dx);
		float ady = (float)Math.abs(dy);
		float adz = (float)Math.abs(dz);

		float t;

		if( adx > ady ) {
			if( adx > adz ) {
				t = dx/line.slope.x;
			} else {
				t = dz/line.slope.z;
			}
		} else if( ady > adz ) {
			t = dy/line.slope.y;
		}  else {
			t = dz/line.slope.z;
		}
		return t;
	}
}
