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

package georegression.geometry;

import georegression.misc.GrlConstants;
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

		output.b = 1/(double)Math.sqrt(l1);
		output.a = 1/(double)Math.sqrt(l2);

		// direction of minor axis
		double dx,dy;
		if( m11 >= m22 ) {
			dx = l1-m22;
			dy = m12;
		} else {
			dx = m12;
			dy = l1-m11;
		}

		// direction of major axis
		output.phi = Math.atan2(-dx,dy);
		if( output.phi < -GrlConstants.PId2 ) {
			output.phi += (double)Math.PI;
		} else if( output.phi > GrlConstants.PId2 ) {
			output.phi -= (double)Math.PI;
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

		// TODO simplfy using more trig identities
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
	 * Computes the value of the quadratic ellipse function at point (x,y). Should equal 0 if the point
	 * is along the ellipse.
	 *
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param ellipse Ellipse equation being evaluated.
	 * @return value of ellipse equation at point (x,y)
	 */
	public static double evaluate( double x , double y , EllipseQuadratic_F64 ellipse ) {
		return ellipse.a*x*x + 2*ellipse.b*x*y + ellipse.c*y*y + 2*ellipse.d*x + 2*ellipse.e*y + ellipse.f;
	}

	/**
	 * Computes the value of the quadratic ellipse function at point (x,y).  Should equal 1 if the point is on the
	 * ellipse.
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
	 * Computes the point on the ellipse at location 't', where t is an angle in radians
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

	/**
	 * Computes the value of 't' used to specify a point's location
	 *
	 * @param p Point on the ellipse
	 * @param ellipse Ellipse
	 * @return Angle from -pi to pi
	 */
	public static double computeAngle( Point2D_F64 p , EllipseRotated_F64 ellipse ) {
		// put point into ellipse's reference frame
		double ce = Math.cos(ellipse.phi);
		double se = Math.sin(ellipse.phi);

		double xc = p.x - ellipse.center.x;
		double yc = p.y - ellipse.center.y;

		double x =  ce*xc + se*yc;
		double y = -se*xc + ce*yc;

		return Math.atan2( y/ellipse.b , x/ellipse.a );
	}
}
