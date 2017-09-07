/*
 * Copyright (C) 2011-2017, Peter Abeles. All Rights Reserved.
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
import georegression.struct.trig.Circle2D_F32;

/**
 * Functions related to circles.
 *
 * @author Peter Abeles
 */
public class UtilCircle2D_F32 {

	/**
	 * Computes (x-x_c)**2 + (y-y_c)**2 - r. If (x,y) lies on the circle then it should be 0.
	 * @param x x-coordinate of a point
	 * @param y y-coordinate of a point
	 * @param circle circle
	 * @return Result of the equation
	 */
	public static float evaluate( float x , float y, Circle2D_F32 circle ) {
		x -= circle.center.x;
		y -= circle.center.y;

		return x*x + y*y - circle.radius*circle.radius;

	}

	/**
	 * Given three points find the circle that intersects all three. If false is returned that means the points all
	 * lie along a line and there is no circle.
	 * @param x0 Point
	 * @param x1 Point
	 * @param x2 Point
	 * @param circle (Output) found circle
	 * @return true if a circle was found or false if not
	 */
	public static boolean circle(Point2D_F32 x0 , Point2D_F32 x1 , Point2D_F32 x2 , Circle2D_F32 circle ) {

		// define the bottom 3 rows of a 4x4 matrix
		float a21 = x0.normSq(), a22 = x0.x,a23=x0.y;//,a24 = 1;
		float a31 = x1.normSq(), a32 = x1.x,a33=x1.y;//,a34 = 1;
		float a41 = x2.normSq(), a42 = x2.x,a43=x2.y;//,a44 = 1;

//		float M11 = det(a22,a23,a24, a32,a33,a34, a42,a43,a44);
//		float M12 = det(a21,a23,a24, a31,a33,a34, a41,a43,a44);
//		float M13 = det(a21,a22,a24, a31,a32,a34, a41,a42,a44);
//		float M14 = det(a21,a22,a23, a31,a32,a33, a41,a42,a43);

		// simplified by removing multiply by 1
		float M11 = a22*(a33-a43) - a23*(a32-a42) + (a32*a43 - a42*a33);
		float M12 = a21*(a33-a43) - a23*(a31-a41) + (a31*a43 - a41*a33);
		float M13 = a21*(a32-a42) - a22*(a31-a41) + (a31*a42 - a41*a32);
		float M14 = a21*(a32*a43 - a33*a42) - a22*(a31*a43 - a33*a41) + a23*(a31*a42 - a41*a32);

		if( M11 == 0 )
			return false;

		circle.center.x = 0.5f*M12/M11;
		circle.center.y = -0.5f*M13/M11;
		circle.radius = (float)Math.sqrt(circle.center.normSq() + M14/M11);

		return true;
	}
}
