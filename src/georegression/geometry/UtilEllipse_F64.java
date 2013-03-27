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

package georegression.geometry;

import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.EllipseQuadratic_F64;
import georegression.struct.shapes.EllipseRotated_F64;

/**
 * Functions for extracting information from ellipses and converting between different ellipse formats.
 *
 * @author Peter Abeles
 */
public class UtilEllipse_F64 {

	/**
	 * <p>
	 * Convert from quadratic to rotated formats.  Equations taken from [1].
	 * </p>
	 *
	 * <p>
	 * [1] David Eberly "Information About Ellipses", Geometric Tools, LLC 2011
	 * </p>
	 *
	 * @param input Input in quadratic format.
	 * @param output (Optional) Storage for converted format.  Can be null.
	 * @return Ellipse in rotated format.
	 */
	public static EllipseRotated_F64 convert( EllipseQuadratic_F64 input , EllipseRotated_F64 output ) {
		if( output == null )
			output = new EllipseRotated_F64();

		double a11 = input.a;
		double a12 = input.b;
		double a22 = input.c;
		double b1  = 2*input.d;
		double b2  = 2*input.e;
		double c = input.f;

		output.center.x = (a22*b1-a12*b2)/(2*(a12*a12 - a11*a22));
		output.center.y = (a11*b2-a12*b1)/(2*(a12*a12 - a11*a22));

		double k1 = output.center.x;
		double k2 = output.center.y;

		double mu = 1.0/(a11*k1*k1 + 2*a12*k1*k2 + a22*k2*k2 - c);
		double m11 = mu*a11;
		double m12 = mu*a12;
		double m22 = mu*a22;

		double inner = Math.sqrt((m11-m22)*(m11-m22) + 4*m12*m12);
		double l1 = ((m11+m22) + inner)/2.0;
		double l2 = ((m11+m22) - inner)/2.0;

		output.b = 1/Math.sqrt(l1);
		output.a = 1/Math.sqrt(l2);

		output.phi = Math.atan2(-2*a12,a22-a11)/2.0;

		return output;
	}

	/**
	 * Convert from rotated to quadratic.
	 *
	 * @param input Input rotated format.
	 * @param output (Optional) Storage for quadratic format.  Can be null.
	 * @return Ellipse in quadratic format.
	 */
	public static EllipseQuadratic_F64 convert( EllipseRotated_F64 input , EllipseQuadratic_F64 output ) {
		if( output == null )
			output = new EllipseQuadratic_F64();

		double x0 = input.center.x;
		double y0 = input.center.y;
		double a = input.a;
		double b = input.b;
		double phi = input.phi;

		double cphi = Math.cos(phi);
		double sphi = Math.sin(phi);
		double cphi2 = cphi*cphi;
		double sphi2 = sphi*sphi;

		double a2 = a*a;
		double b2 = b*b;
		double x02 = x0*x0;
		double y02 = y0*y0;

		output.a = cphi2/a2 + sphi2/b2;
		output.b = sphi*cphi/a2 - sphi*cphi/b2;
		output.c = sphi2/a2 + cphi2/b2;
		output.d = -x0*cphi2/a2 - y0*sphi*cphi/a2 - x0*sphi2/b2 + y0*sphi*cphi/b2;
		output.e = -x0*sphi*cphi/a2 - y0*sphi2/a2 + x0*sphi*cphi/b2 - y0*cphi2/b2;
		output.f = x02*cphi2/a2 + 2*x0*y0*sphi*cphi/a2 + y02*sphi2/a2 +
				x02*sphi2/b2 - 2*x0*y0*sphi*cphi/b2 + y02*cphi2/b2 - 1;

		return output;
	}

	/**
	 * Computes the value of the quadratic ellipse function at point (x,y)
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param ellipse Ellipse equation being evaluated.
	 * @return value of ellipse equation at point (x,y)
	 */
	public static double evaluate( double x , double y , EllipseQuadratic_F64 ellipse ) {
		return ellipse.a*x*x + 2*ellipse.b*x*y + ellipse.c*y*y + 2*ellipse.d*x + 2*ellipse.e*y + ellipse.f;
	}

	/**
	 * Computes the value of the quadratic ellipse function at point (x,y)
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param ellipse Ellipse equation being evaluated.
	 * @return value of ellipse equation at point (x,y)
	 */
	public static double evaluate( double x , double y , EllipseRotated_F64 ellipse ) {

		double cphi = Math.cos(ellipse.phi);
		double sphi = Math.sin(ellipse.phi);

		x -= ellipse.center.x;
		y -= ellipse.center.y;


		double left = (x*cphi + y*sphi);
		double right = (-x*sphi + y*cphi);

		return (left*left)/(ellipse.a*ellipse.a) + (right*right)/(ellipse.b*ellipse.b);
	}

	/**
	 * Computes the point on the ellipse at location 't'.
	 *
	 * @param t An angle in radians from 0 to 2*PI
	 * @param ellipse Ellipse
	 * @param output (Optional) point on the ellipse .  Can be null.
	 * @return Point on the ellipse
	 */
	public static Point2D_F64 computePoint( double t , EllipseRotated_F64 ellipse , Point2D_F64 output ) {
		if( output == null )
			output = new Point2D_F64();

		double ct = Math.cos(t);
		double st = Math.sin(t);
		double cphi = Math.cos(ellipse.phi);
		double sphi = Math.sin(ellipse.phi);

		output.x = ellipse.center.x + ellipse.a*ct*cphi - ellipse.b*st*sphi;
		output.y = ellipse.center.y + ellipse.a*ct*sphi + ellipse.b*st*cphi;

		return output;
	}
}
