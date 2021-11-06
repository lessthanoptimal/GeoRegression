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

package georegression.geometry;

import georegression.geometry.curves.TangentLinesTwoEllipses_F64;
import georegression.misc.GrlConstants;
import georegression.struct.curve.EllipseQuadratic_F64;
import georegression.struct.curve.EllipseRotated_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Vector2D_F64;
import org.jetbrains.annotations.Nullable;

/**
 * Functions for extracting information from ellipses and converting between different ellipse formats.
 *
 * @author Peter Abeles
 */
public class UtilEllipse_F64 {

	/**
	 * <p>
	 * Convert from quadratic to rotated formats. Equations taken from [1].
	 * </p>
	 *
	 * <p>
	 * [1] David Eberly "Information About Ellipses", Geometric Tools, LLC 2011
	 * </p>
	 *
	 * @param input Input in quadratic format.
	 * @param output (Optional) Storage for converted format. Can be null.
	 * @return Ellipse in rotated format.
	 */
	public static EllipseRotated_F64 convert( EllipseQuadratic_F64 input , @Nullable EllipseRotated_F64 output ) {
		if( output == null )
			output = new EllipseRotated_F64();

		double a11 = input.A;
		double a12 = input.B;
		double a22 = input.C;
		double b1  = 2*input.D;
		double b2  = 2*input.E;
		double c = input.F;

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
	 * @param output (Optional) Storage for quadratic format. Can be null.
	 * @return Ellipse in quadratic format.
	 */
	public static EllipseQuadratic_F64 convert( EllipseRotated_F64 input , @Nullable EllipseQuadratic_F64 output ) {
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
		output.A = cphi2/a2 + sphi2/b2;
		output.B = sphi*cphi/a2 - sphi*cphi/b2;
		output.C = sphi2/a2 + cphi2/b2;
		output.D = -x0*cphi2/a2 - y0*sphi*cphi/a2 - x0*sphi2/b2 + y0*sphi*cphi/b2;
		output.E = -x0*sphi*cphi/a2 - y0*sphi2/a2 + x0*sphi*cphi/b2 - y0*cphi2/b2;
		output.F = x02*cphi2/a2 + 2*x0*y0*sphi*cphi/a2 + y02*sphi2/a2 +
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
		return ellipse.A *x*x + 2*ellipse.B *x*y + ellipse.C *y*y + 2*ellipse.D *x + 2*ellipse.E *y + ellipse.F;
	}

	/**
	 * Computes the value of the quadratic ellipse function at point (x,y). Should equal 1 if the point is on the
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

		double ll = left/ellipse.a;
		double rr = right/ellipse.b;

		return ll*ll + rr*rr;
	}

	/**
	 * Computes the point on the ellipse at location 't', where t is an angle in radians
	 *
	 * @param t An angle in radians from 0 to 2*PI
	 * @param ellipse Ellipse
	 * @param output (Optional) point on the ellipse . Can be null.
	 * @return Point on the ellipse
	 */
	public static Point2D_F64 computePoint( double t , EllipseRotated_F64 ellipse ,
											@Nullable Point2D_F64 output ) {
		if( output == null )
			output = new Point2D_F64();

		double ct = Math.cos(t);
		double st = Math.sin(t);
		double cphi = Math.cos(ellipse.phi);
		double sphi = Math.sin(ellipse.phi);

		// coordinate in ellipse frame
		double x = ellipse.a*ct;
		double y = ellipse.b*st;

		// put into global frame
		output.x = ellipse.center.x + x*cphi - y*sphi;
		output.y = ellipse.center.y + x*sphi + y*cphi;

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

		// world into ellipse frame
		double xc = p.x - ellipse.center.x;
		double yc = p.y - ellipse.center.y;

		double x =  ce*xc + se*yc;
		double y = -se*xc + ce*yc;

		return Math.atan2( y/ellipse.b , x/ellipse.a );
	}

	/**
	 * Computes the tangent to the ellipse at the specified location
	 *
	 * @param t Location on the ellipse. Radians
	 * @param ellipse Ellipse equation
	 * @param output Optional storage for tangent
	 * @return The tangent
	 */
	public static Vector2D_F64 computeTangent( double t ,
											   EllipseRotated_F64 ellipse ,
											   @Nullable Vector2D_F64 output  ) {
		if( output == null )
			output = new Vector2D_F64();

		double ct = Math.cos(t);
		double st = Math.sin(t);
		double cphi = Math.cos(ellipse.phi);
		double sphi = Math.sin(ellipse.phi);

		// point in ellipse frame multiplied by b^2 and a^2
		double x = ellipse.a*ct*ellipse.b*ellipse.b;
		double y = ellipse.b*st*ellipse.a*ellipse.a;

		// rotate vector normal into world frame
		double rx = x*cphi - y*sphi;
		double ry = x*sphi + y*cphi;

		// normalize and change into tangent
		double r = Math.sqrt(rx*rx + ry*ry);

		output.x = -ry/r;
		output.y = rx/r;

		return output;
	}

	/**
	 * <p>Finds two points on the ellipse that in combination with point 'pt' each define
	 * a line that is tangent to the ellipse.</p>
	 *
	 * Notes:<br>
	 * Point 'pt' is assumed to be outside of the ellipse.
	 *
	 * @param pt Point which the lines will pass though
	 * @param ellipse The ellipse which the lines will be tangent to
	 * @param tangentA (output) Point on the ellipse where tangent line A hits it
	 * @param tangentB (output) Point on the ellipse where tangent line B hits it
	 */
	public static boolean tangentLines(Point2D_F64 pt , EllipseRotated_F64 ellipse ,
									   Point2D_F64 tangentA , Point2D_F64 tangentB )
	{
		// Derivation:
		// Compute the tangent at only point along the ellipse by computing dy/dx
		//    x*b^2/(y*a^2) or - x*b^2/(y*a^2)  are the possible solutions for the tangent
		// The slope of the line and the gradient are the same, so this is true:
		//   y - y'     -x*b^2
		//  -------  =  -------
		//   x - x'      y*a^2
		//
		//  (x,y) is point on ellipse, (x',y') is pt that lines pass through
		//
		//  that becomes
		//  y^2*a^2 + x^2*b^2 = x'*x*b^2 + y'*y*a^2
		//  use the equation for the ellipse (centered and aligned at origin)
		//  a^2*b^2 =  x'*x*b^2 + y'*y*a^2
		//
		// solve for y
		// plug into ellipse equation
		// solve for x, which is a quadratic equation

		// translate and rotate into ellipse reference frame
		double cphi = Math.cos(ellipse.phi);
		double sphi = Math.sin(ellipse.phi);

		double tmpx = pt.x - ellipse.center.x;
		double tmpy = pt.y - ellipse.center.y;

		double xt =  tmpx*cphi + tmpy*sphi;
		double yt = -tmpx*sphi + tmpy*cphi;

		// solve
		double a2 = ellipse.a*ellipse.a;
		double b2 = ellipse.b*ellipse.b;

		// quadratic equation for the two variants.
		// solving for x
		double aa0 = yt*yt/b2 + xt*xt/a2;
		double bb0 = -2.0*xt;
		double cc0 = a2*(1.0-yt*yt/b2);

		double descriminant0 = bb0*bb0 - 4.0*aa0*cc0;

		// solving for y
		double aa1 = xt*xt/a2 + yt*yt/b2;
		double bb1 = -2.0*yt;
		double cc1 = b2*(1.0-xt*xt/a2);

		double descriminant1 = bb1*bb1 - 4.0*aa1*cc1;

		double x0,y0, x1,y1;
		if( descriminant0 < 0 && descriminant1 < 0 ) {
			return false;
		} else if( descriminant0 > descriminant1 ) {
			if( yt == 0 )
				return false;

			double right = Math.sqrt(descriminant0);

			x0 = (-bb0 + right)/(2.0*aa0);
			x1 = (-bb0 - right)/(2.0*aa0);

			y0 = b2/yt - xt*x0*b2/(yt*a2);
			y1 = b2/yt - xt*x1*b2/(yt*a2);

		} else {
			if( xt == 0 )
				return false;

			double right = Math.sqrt(descriminant1);

			y0 = (-bb1 + right)/(2.0*aa1);
			y1 = (-bb1 - right)/(2.0*aa1);

			x0 = a2/xt - yt*y0*a2/(xt*b2);
			x1 = a2/xt - yt*y1*a2/(xt*b2);
		}

		// convert the lines back into world space
		tangentA.x = x0*cphi - y0*sphi + ellipse.center.x;
		tangentA.y = x0*sphi + y0*cphi + ellipse.center.y;

		tangentB.x = x1*cphi - y1*sphi + ellipse.center.x;
		tangentB.y = x1*sphi + y1*cphi + ellipse.center.y;

		return true;
	}

	/**
	 * <p>Finds four lines which are tangent to both ellipses. Both ellipses must not intersect. Line 0
	 * and line 3 will not intersect the line joining the center of the two ellipses while line 1 and 2 will.</p>

	 * @see TangentLinesTwoEllipses_F64
	 *
	 * @param ellipseA (Input) First ellipse
	 * @param ellipseB (Input) Second ellipse
	 * @param tangentA0 (Output) Point on ellipseA in which tangent line0 passes through
	 * @param tangentA1 (Output) Point on ellipseA in which tangent line1 passes through
	 * @param tangentA2 (Output) Point on ellipseA in which tangent line2 passes through
	 * @param tangentA3 (Output) Point on ellipseA in which tangent line3 passes through
	 * @param tangentB0 (Output) Point on ellipseB in which tangent line0 passes through
	 * @param tangentB1 (Output) Point on ellipseB in which tangent line1 passes through
	 * @param tangentB2 (Output) Point on ellipseB in which tangent line2 passes through
	 * @param tangentB3 (Output) Point on ellipseB in which tangent line3 passes through
	 *
	 * @return true if a solution was found or false if it failed
	 */
	public static boolean tangentLines( EllipseRotated_F64 ellipseA , EllipseRotated_F64 ellipseB ,
										Point2D_F64 tangentA0 , Point2D_F64 tangentA1 ,
										Point2D_F64 tangentA2 , Point2D_F64 tangentA3 ,
										Point2D_F64 tangentB0 , Point2D_F64 tangentB1 ,
										Point2D_F64 tangentB2 , Point2D_F64 tangentB3 )
	{
		TangentLinesTwoEllipses_F64 alg = new TangentLinesTwoEllipses_F64(GrlConstants.TEST_F64,10);

		return alg.process(ellipseA, ellipseB,
				tangentA0, tangentA1, tangentA2, tangentA3,
				tangentB0, tangentB1, tangentB2, tangentB3) ;
	}
}
