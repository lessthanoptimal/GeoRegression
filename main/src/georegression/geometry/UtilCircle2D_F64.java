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

import georegression.struct.point.Point2D_F64;
import georegression.struct.trig.Circle2D_F64;

/**
 * Functions related to circles.
 *
 * @author Peter Abeles
 */
public class UtilCircle2D_F64 {

	/**
	 * Computes (x-x_c)**2 + (y-y_c)**2 - r. If (x,y) lies on the circle then it should be 0.
	 * @param x x-coordinate of a point
	 * @param y y-coordinate of a point
	 * @param circle circle
	 * @return Result of the equation
	 */
	public static double evaluate( double x , double y, Circle2D_F64 circle ) {
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
	public static boolean circle(Point2D_F64 x0 , Point2D_F64 x1 , Point2D_F64 x2 , Circle2D_F64 circle ) {

		// points that lie on line a and b
		double xa = (x0.x+x1.x)/2.0;
		double ya = (x0.y+x1.y)/2.0;
		double xb = (x1.x+x2.x)/2.0;
		double yb = (x1.y+x2.y)/2.0;

		// slopes of lines a and b
		double m2 = x0.x-x1.x;
		double m1 = x1.y-x0.y;

		double n2 = x2.x-x1.x;
		double n1 = x1.y-x2.y;

		// find the intersection of the lines
		double bottom = m2*n1-n2*m1;
		if( bottom == 0 )
			return false;

		double alpha = (-m2*(xb-xa) + m1*(yb-ya))/bottom;

		circle.center.x = xb + n1*alpha;
		circle.center.y = yb + n2*alpha;
		circle.radius = circle.center.distance(x0);

		return true;
	}

	/**
	 * Radius squares of the circle that passes through these three points.
	 * @param x0 Point
	 * @param x1 Point
	 * @param x2 Point
	 * @return Radius squares of circle or NaN if colinear
	 */
	public static double circleRadiusSq(Point2D_F64 x0 , Point2D_F64 x1 , Point2D_F64 x2) {
		// points that lie on line a and b
		double xa = (x0.x+x1.x)/2.0;
		double ya = (x0.y+x1.y)/2.0;
		double xb = (x1.x+x2.x)/2.0;
		double yb = (x1.y+x2.y)/2.0;

		// slopes of lines a and b
		double m2 = x0.x-x1.x;
		double m1 = x1.y-x0.y;

		double n2 = x2.x-x1.x;
		double n1 = x1.y-x2.y;

		// find the intersection of the lines
		double bottom = m2*n1-n2*m1;
		if( bottom == 0 )
			return Double.NaN;

		double alpha = (-m2*(xb-xa) + m1*(yb-ya))/bottom;

		double dx = xb + n1*alpha - x0.x;
		double dy = yb + n2*alpha - x0.y;
		return dx*dx + dy*dy;
	}
}
