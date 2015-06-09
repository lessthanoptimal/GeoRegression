/*
 * Copyright (C) 2011-2015, Peter Abeles. All Rights Reserved.
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

import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Vector2D_F64;

/**
 * @author Peter Abeles
 */
public class UtilVector2D_F64 {
	/**
	 * Returns the acute angle between the two vectors.  Computed using the dot product.
	 * @param a vector
	 * @param b vector
	 * @return acute angle
	 */
	public static double acute( Vector2D_F64 a , Vector2D_F64 b ) {
		double dot = a.dot(b);

		double value = dot/(a.norm()*b.norm());
		if( value > 1.0 )
			value = 1.0;
		else if( value < -1.0 )
			value = -1.0;

		return Math.acos( value );
	}

	/**
	 * Sets the vector equal to 'a' - 'b'.
	 *
	 * @param a point
	 * @param b point
	 * @param output (output) optional storage for vector.
	 * @return Solution
	 */
	public static Vector2D_F64 minus( Point2D_F64 a , Point2D_F64 b , Vector2D_F64 output ) {
		if( output == null )
			output = new Vector2D_F64();

		output.x = a.x - b.x;
		output.y = a.y - b.y;

		return output;
	}
}
