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
 * @author Peter Abeles
 */
public class UtilEllipse_F64 {

	/**
	 * Convert from quadratic to rotated formats.  Equations taken from [1].
	 *
	 * [1] http://mathworld.wolfram.com/Ellipse.html March 2013.
	 *
	 * @param input Input in quadratic format.
	 * @param output (Optional) Storage for converted format.  Can be null.
	 * @return Ellipse in rotated format.
	 */
	public static EllipseRotated_F64 convert( EllipseQuadratic_F64 input , EllipseRotated_F64 output ) {
		if( output == null )
			output = new EllipseRotated_F64();

		double a=input.a;
		double b=input.b;
		double c=input.c;
		double d=input.d;
		double f=input.e;
		double g=input.f;

		if( a == c ) {
			// handle special case when it's a circle

			return output;
		}

		double bb_ac = b*b-a*c;
		double bottom_inner = Math.sqrt((a-c)*(a-c) + 4*b*b);

		double top = 2.0*(a*f*f + c*d*d + g*b*b - 2*b*d*f - a*c*g);
		double bottom = bb_ac*(bottom_inner - (a+c));

		output.a = Math.sqrt(top/bottom);

		bottom = bb_ac*( -bottom_inner - (a+c));

		output.b = Math.sqrt(top/bottom);

		output.center.x = (c*d - b*f)/bb_ac;
		output.center.y = (c*f - b*d)/bb_ac;

		double a11 = a;
		double a12 = b;
		double a22 = c;
		double b1 = d;
		double b2 = input.e;
		output.center.x = (a22*b1-a12*b2)/(2*(a12*a12 - a11*a22));
		output.center.y = (a11*b2-a12*b1)/(2*(a12*a12 - a11*a22));


		if( b == 0 ) {
			if( a < c ) {
				output.phi = 0;
			} else {
				output.phi = Math.PI/2.0;
			}
		} else {
			if( a < c ) {
				output.phi = 0.5/Math.atan((a-c)/(2*b));
			} else {
				output.phi = Math.PI/2.0 + 0.5/Math.atan((a-c)/(2*b));
			}
		}

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
		output.b = 2*sphi*cphi/a2 - 2*sphi*cphi/b2;
		output.c = sphi2/a2 + cphi2/b2;
		output.d = -2*x0*sphi2/b2 + 2*y0*sphi*cphi/b2 - 2*x0*cphi2/a2 - 2*y0*sphi*cphi/a2;
		output.e = -2*x0*sphi*cphi/a2 - 2*y0*sphi2/a2 + 2*x0*sphi*cphi/b2 - 2*y0*cphi2/b2;
		output.f = x02*cphi2/a2 + x02*sphi2/b2 + y02*sphi2/a2 + y02*cphi2/b2
				+ 2*x0*y0*sphi*cphi/a2 - 2*x0*y0*sphi*cphi/b2 - 1;


		return output;
	}

	public static double evaluate( double x , double y , EllipseQuadratic_F64 ellipse ) {
		return ellipse.a*x*x + ellipse.b*x*y +ellipse.c*y*y + ellipse.d*x + ellipse.e*y + ellipse.f;
	}

	public static double evaluate( double x , double y , EllipseRotated_F64 ellipse ) {

		double cphi = Math.cos(ellipse.phi);
		double sphi = Math.sin(ellipse.phi);

		x -= ellipse.center.x;
		y -= ellipse.center.y;


		double left = (x*cphi + y*sphi);
		double right = (x*sphi - y*cphi);

		return (left*left)/(ellipse.a*ellipse.a) +  (right*right)/(ellipse.b*ellipse.b);
	}

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
