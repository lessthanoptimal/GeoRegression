/*
 * Copyright (C) 2026, Peter Abeles. All Rights Reserved.
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

import georegression.struct.so.Quaternion_F64;
import org.jetbrains.annotations.Nullable;

public class QuaternionMath_F64 {
	/// Multiplication for Hamilton quaternions. For JPL you need to reverse the order of q1 and q2.
	public static Quaternion_F64 multiply( Quaternion_F64 q1, Quaternion_F64 q2,
	                                       @Nullable Quaternion_F64 out ) {
		if (out == null)
			out = new Quaternion_F64();
		out.setTo(
				q1.w*q2.w - q1.x*q2.x - q1.y*q2.y - q1.z*q2.z,  // w
				q1.w*q2.x + q1.x*q2.w + q1.y*q2.z - q1.z*q2.y,  // x
				q1.w*q2.y - q1.x*q2.z + q1.y*q2.w + q1.z*q2.x,  // y
				q1.w*q2.z + q1.x*q2.y - q1.y*q2.x + q1.z*q2.w   // z
		);
		return out;
	}
}
