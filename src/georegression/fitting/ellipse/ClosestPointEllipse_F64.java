/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
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
public class ClosestPointEllipse_F64 {

	// tolerance to test for solution.  Must be this close to zero
	double tol;
	// maximum number of newton steps
	int maxIterations;

	// location of the closest point
	Point2D_F64 closest = new Point2D_F64();

	EllipseRotated_F64 ellipse;
	double ce;
	double se;

	// optimal value of paramiterization
	double theta;

	public ClosestPointEllipse_F64(double tol, int maxIterations) {
		this.tol = tol;
		this.maxIterations = maxIterations;
	}

	public void setEllipse( EllipseRotated_F64 ellipse ) {
		this.ellipse = ellipse;
		ce = Math.cos(ellipse.phi);
		se = Math.sin(ellipse.phi);
	}

	public boolean process( Point2D_F64 point ) {
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
		for( int i = 0; i < maxIterations; i++ ) {
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

		return true;
	}

	public Point2D_F64 getClosest() {
		return closest;
	}

	public double getTheta() {
		return theta;
	}
}
