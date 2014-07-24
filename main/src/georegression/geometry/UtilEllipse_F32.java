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
import georegression.struct.point.Point2D_F32;
import georegression.struct.shapes.EllipseQuadratic_F32;
import georegression.struct.shapes.EllipseRotated_F32;

/**
 * Functions for extracting information from ellipses and converting between different ellipse formats.
 *
 * @author Peter Abeles
 */
public class UtilEllipse_F32 {

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
	public static EllipseRotated_F32 convert( EllipseQuadratic_F32 input , EllipseRotated_F32 output ) {
		if( output == null )
			output = new EllipseRotated_F32();

		float a11 = input.a;
		float a12 = input.b;
		float a22 = input.c;
		float b1  = 2*input.d;
		float b2  = 2*input.e;
		float c = input.f;

		output.center.x = (a22*b1-a12*b2)/(2*(a12*a12 - a11*a22));
		output.center.y = (a11*b2-a12*b1)/(2*(a12*a12 - a11*a22));

		float k1 = output.center.x;
		float k2 = output.center.y;

		float mu = 1.0f/(a11*k1*k1 + 2*a12*k1*k2 + a22*k2*k2 - c);
		float m11 = mu*a11;
		float m12 = mu*a12;
		float m22 = mu*a22;

		float inner = (float)Math.sqrt((m11-m22)*(m11-m22) + 4*m12*m12);
		float l1 = ((m11+m22) + inner)/2.0f;
		float l2 = ((m11+m22) - inner)/2.0f;

		output.b = 1/(float)Math.sqrt(l1);
		output.a = 1/(float)Math.sqrt(l2);

		// direction of minor axis
		float dx,dy;
		if( m11 >= m22 ) {
			dx = l1-m22;
			dy = m12;
		} else {
			dx = m12;
			dy = l1-m11;
		}

		// direction of major axis
		output.phi = (float)Math.atan2(-dx,dy);
		if( output.phi < -GrlConstants.F_PId2 ) {
			output.phi += (float)Math.PI;
		} else if( output.phi > GrlConstants.F_PId2 ) {
			output.phi -= (float)Math.PI;
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
	public static EllipseQuadratic_F32 convert( EllipseRotated_F32 input , EllipseQuadratic_F32 output ) {
		if( output == null )
			output = new EllipseQuadratic_F32();

		float x0 = input.center.x;
		float y0 = input.center.y;
		float a = input.a;
		float b = input.b;
		float phi = input.phi;

		float cphi = (float)Math.cos(phi);
		float sphi = (float)Math.sin(phi);
		float cphi2 = cphi*cphi;
		float sphi2 = sphi*sphi;

		float a2 = a*a;
		float b2 = b*b;
		float x02 = x0*x0;
		float y02 = y0*y0;

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
	public static float evaluate( float x , float y , EllipseQuadratic_F32 ellipse ) {
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
	public static float evaluate( float x , float y , EllipseRotated_F32 ellipse ) {

		float cphi = (float)Math.cos(ellipse.phi);
		float sphi = (float)Math.sin(ellipse.phi);

		x -= ellipse.center.x;
		y -= ellipse.center.y;


		float left = (x*cphi + y*sphi);
		float right = (-x*sphi + y*cphi);

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
	public static Point2D_F32 computePoint( float t , EllipseRotated_F32 ellipse , Point2D_F32 output ) {
		if( output == null )
			output = new Point2D_F32();

		float ct = (float)Math.cos(t);
		float st = (float)Math.sin(t);
		float cphi = (float)Math.cos(ellipse.phi);
		float sphi = (float)Math.sin(ellipse.phi);

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
	public static float computeAngle( Point2D_F32 p , EllipseRotated_F32 ellipse ) {
		// put point into ellipse's reference frame
		float ce = (float)Math.cos(ellipse.phi);
		float se = (float)Math.sin(ellipse.phi);

		float xc = p.x - ellipse.center.x;
		float yc = p.y - ellipse.center.y;

		float x =  ce*xc + se*yc;
		float y = -se*xc + ce*yc;

		return (float)Math.atan2( y/ellipse.b , x/ellipse.a );
	}
}
