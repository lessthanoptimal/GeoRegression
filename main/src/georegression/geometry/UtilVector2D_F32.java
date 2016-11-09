/*
 * Copyright (C) 2011-2016, Peter Abeles. All Rights Reserved.
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

import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Vector2D_F32;

/**
 * @author Peter Abeles
 */
public class UtilVector2D_F32 {
	/**
	 * Returns the acute angle between the two vectors.  Computed using the dot product.
	 * @param a vector
	 * @param b vector
	 * @return acute angle
	 */
	public static float acute( Vector2D_F32 a , Vector2D_F32 b ) {
		float dot = a.dot(b);

		float value = dot/(a.norm()*b.norm());
		if( value > 1.0f )
			value = 1.0f;
		else if( value < -1.0f )
			value = -1.0f;

		return (float)Math.acos( value );
	}

	public static float acute( float ax , float ay, float bx , float by ) {
		float dot = ax*bx + ay*by;
		float na = (float)Math.sqrt(ax*ax + ay*ay);
		float nb = (float)Math.sqrt(bx*bx + by*by);

		float value = dot/(na*nb);
		if( value > 1.0f )
			value = 1.0f;
		else if( value < -1.0f )
			value = -1.0f;

		return (float)Math.acos( value );
	}

	/**
	 * Sets the vector equal to 'a' - 'b'.
	 *
	 * @param a point
	 * @param b point
	 * @param output (output) optional storage for vector.
	 * @return Solution
	 */
	public static Vector2D_F32 minus( Point2D_F32 a , Point2D_F32 b , Vector2D_F32 output ) {
		if( output == null )
			output = new Vector2D_F32();

		output.x = a.x - b.x;
		output.y = a.y - b.y;

		return output;
	}

	/**
	 * Tests to see if the two vectors are identical up to a sign difference
	 * @param xa x-component of vector 'a'
	 * @param ya y-component of vector 'a'
	 * @param xb x-component of vector 'b'
	 * @param yb y-component of vector 'b'
	 * @param tol Tolerance in Euclidan distance
	 * @return true if identical to within tolerance and a sign ambiguity
	 */
	public static boolean identicalSign( float xa , float ya , float xb , float yb , float tol ) {
		float dx0 = xb-xa;
		float dy0 = yb-ya;
		float dx1 = xb+xa;
		float dy1 = yb+ya;


		float error0 = dx0*dx0 + dy0*dy0;
		float error1 = dx1*dx1 + dy1*dy1;

		if( error0 < error1 ) {
			return error0 <= tol*tol;
		} else {
			return error1 <= tol*tol;
		}

	}
}
