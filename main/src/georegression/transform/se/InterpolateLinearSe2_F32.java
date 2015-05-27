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

package georegression.transform.se;

import georegression.metric.UtilAngle;
import georegression.struct.se.Se2_F32;

/**
 * Performs linear interpolation between two rigid body transforms
 *
 * @author Peter Abeles
 */
public class InterpolateLinearSe2_F32 {


	/**
	 * Perform linear interpolation
	 *
	 * @param a The first transform
	 * @param b The second transform
	 * @param where from 0 to 1, inclusive.  0 means it will be more similar to 'a'.
	 * @param output The interpolated transform
	 */
	public static void interpolate(Se2_F32 a , Se2_F32 b , float where, Se2_F32 output) {

		float w0 = 1.0f-where;

		output.T.x = a.T.x*w0 + b.T.x*where;
		output.T.y = a.T.y*w0 + b.T.y*where;

		// interpolating rotation is more difficult
		// This only works well if the difference between the two angles is small
		float yaw0 = a.getYaw();
		float yaw1 = b.getYaw();

		float cw = UtilAngle.distanceCW(yaw0, yaw1);
		float ccw = UtilAngle.distanceCCW(yaw0,yaw1);

		float yaw;
		if( cw > ccw ) {
			yaw = yaw0+ccw*where;
		} else {
			yaw = yaw0-cw*where;
		}
		output.setYaw(yaw);
	}
}
