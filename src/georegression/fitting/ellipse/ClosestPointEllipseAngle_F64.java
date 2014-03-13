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

package georegression.fitting.ellipse;

import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.EllipseRotated_F64;

/**
 * Finds the closest point on an ellipse to a point.  Point is first put into the ellipse's
 * coordinate system.  Then newton's method is used to find the solution.  The following parameterization is used:
 * (x,y) = a*cos(t) + b*sin(t).  For the center a point is arbitrarily selected.
 *
 * @author Peter Abeles
 */
public class ClosestPointEllipseAngle_F64 {

	// tolerance to test for solution.  Must be this close to zero
	double tol;
	// maximum number of newton steps
	int maxIterations;

	// location of the closest point
	Point2D_F64 closest = new Point2D_F64();

	EllipseRotated_F64 ellipse;
	double ce;
	double se;

	// optimal value of parameterization
	double theta;

	/**
	 * Specifies convergence criteria
	 *
	 * @param tol Convergence tolerance.  Try 1e-8
	 * @param maxIterations Maximum number of iterations.  Try 100
	 */
	public ClosestPointEllipseAngle_F64(double tol, int maxIterations) {
		this.tol = tol;
		this.maxIterations = maxIterations;
	}

	/**
	 * Specifies the ellipse which point distance is going to be found from
	 * @param ellipse Ellipse description
	 */
	public void setEllipse( EllipseRotated_F64 ellipse ) {
		this.ellipse = ellipse;
		ce = Math.cos(ellipse.phi);
		se = Math.sin(ellipse.phi);
	}

	/**
	 * Find the closest point on the ellipse to the specified point.  To get the solution call {@link #getClosest()}
	 *
	 * @param point Point which it is being fit to
	 */
	public void process( Point2D_F64 point ) {
		// put point into ellipse's coordinate system
		double xc = point.x - ellipse.center.x;
		double yc = point.y - ellipse.center.y;
//
		double x =  ce*xc + se*yc;
		double y = -se*xc + ce*yc;

		// initial guess for the angle
		theta = Math.atan2( ellipse.a*y , ellipse.b*x);

		double a2_m_b2 = ellipse.a*ellipse.a - ellipse.b*ellipse.b;

		// use Newton's Method to find the solution
		int i = 0;
		for(; i < maxIterations; i++ ) {
			double c = Math.cos(theta);
			double s = Math.sin(theta);

			double f = a2_m_b2*c*s - x*ellipse.a*s + y*ellipse.b*c;
			if( Math.abs(f) < tol )
				break;

			double d = a2_m_b2*(c*c - s*s) - x*ellipse.a*c - y*ellipse.b*s;

			theta = theta - f/d;
		}

		// compute solution in ellipse coordinate frame
		x = ellipse.a*(double)Math.cos(theta);
		y = ellipse.b*(double)Math.sin(theta);

		// put back into original coordinate system
		closest.x = ce*x - se*y + ellipse.center.x;
		closest.y = se*x + ce*y + ellipse.center.y;
	}

	public Point2D_F64 getClosest() {
		return closest;
	}

	public double getTheta() {
		return theta;
	}
}
