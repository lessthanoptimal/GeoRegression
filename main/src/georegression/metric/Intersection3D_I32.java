/*
 * Copyright (C) 2021, Peter Abeles. All Rights Reserved.
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

import georegression.struct.point.Point3D_I32;
import georegression.struct.shapes.Box3D_I32;

/**
 * @author Peter Abeles
 */
public class Intersection3D_I32 {
	/**
	 * Returns true if the point is contained inside the box. The point is considered to be inside the box
	 * if the following test passes for each dimension. box.p0.x &le; point.x {@code <} box.p1.x + box.lengthX
	 *
	 * @param box Box
	 * @param point Point which is tested to see if it is inside the box
	 * @return true for inside and false for not
	 */
	public static boolean contains(Box3D_I32 box , Point3D_I32 point ) {

		return( box.p0.x <= point.x && point.x < box.p1.x &&
				box.p0.y <= point.y && point.y < box.p1.y &&
				box.p0.z <= point.z && point.z < box.p1.z );
	}

	/**
	 * Returns true if boxB is contained inside of or is identical to boxA.
	 *
	 * @param boxA Box
	 * @param boxB Box which is being tested to see if it is inside of boxA
	 * @return true if inside/identical or false if outside
	 */
	public static boolean contains(Box3D_I32 boxA , Box3D_I32 boxB ) {
		return( boxA.p0.x <= boxB.p0.x && boxA.p1.x >= boxB.p1.x &&
				boxA.p0.y <= boxB.p0.y && boxA.p1.y >= boxB.p1.y &&
				boxA.p0.z <= boxB.p0.z && boxA.p1.z >= boxB.p1.z );
	}

	/**
	 * Returns true if the two boxes intersect each other. p0 is inclusive and p1 is exclusive.
	 * So if the p0 edge and p1 edge overlap perfectly there is no intersection.
	 *
	 * @param boxA Box
	 * @param boxB Box
	 * @return true for intersection and false if no intersection
	 */
	public static boolean intersects(Box3D_I32 boxA , Box3D_I32 boxB ) {
		return( intersects(boxA.p0.x , boxB.p0.x , boxA.p1.x , boxB.p1.x ) &&
				intersects(boxA.p0.y , boxB.p0.y , boxA.p1.y , boxB.p1.y ) &&
				intersects(boxA.p0.z , boxB.p0.z , boxA.p1.z , boxB.p1.z ) );
	}

	protected static boolean intersects(int a0 , int b0 , int a1, int b1 ) {
		if( a0 <= b0 ) {
			return b0 < a1;
		} else {
			return a0 < b1;
		}
	}
}
