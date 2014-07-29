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


import georegression.metric.ClosestPoint2D_F64;
import georegression.struct.line.LineGeneral2D_F64;
import georegression.struct.line.LineParametric2D_F64;
import georegression.struct.line.LinePolar2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;

/**
 * Various functions related to lines
 *
 * @author Peter Abeles
 */
public class UtilLine2D_F64 {

	/**
	 * Returns the acute angle between the slope of two lines.  Lines do not need to ever
	 * intersect.  Found using the dot product.
	 *
	 * @param a (input) line
	 * @param b (input) line
	 * @return acute angle in radians
	 */
	public static double acuteAngle( LineGeneral2D_F64 a , LineGeneral2D_F64 b ) {
		double la = Math.sqrt(a.A*a.A + a.B*a.B);
		double lb = Math.sqrt(b.A*b.A + b.B*b.B);

		// numerical round off error can cause it to be barely greater than 1, which is outside the allowed
		// domain of acos()
		double value = (a.A*b.A + a.B*b.B)/(la*lb);
		if( value < -1.0 ) value = -1.0;
		else if( value > 1.0 ) value = 1.0;
		return Math.acos(value);
	}

	/**
	 * Returns the acute angle between the slope of two lines and assumes that the lines have
	 * been normalized such that A*A + B*B = 1.  This avoids the need to compute the square root
	 * twice.  Lines do not need to ever intersect.  Found using the dot product.
	 *
	 * @param a (input) normalized line
	 * @param b (input) normalized line
	 * @return acute angle in radians
	 */
	public static double acuteAngleN( LineGeneral2D_F64 a , LineGeneral2D_F64 b ) {
		double value = a.A*b.A + a.B*b.B;
		if( value < -1.0 ) value = -1.0;
		else if( value > 1.0 ) value = 1.0;
		return Math.acos(value);
	}

	/**
	 * Converts a line from polar form to parametric.
	 *
	 * @param src (input) line is polar notation
	 * @param ret (output) line in parametric notation.  If null a new instance will be created.
	 * @return Converted line in parametric notation
	 */
	public static LineParametric2D_F64 convert( LinePolar2D_F64 src , LineParametric2D_F64 ret )
	{
		if( ret == null )
			ret = new LineParametric2D_F64();

		double c = (double) Math.cos(src.angle);
		double s = (double) Math.sin(src.angle);

		ret.p.set(c*src.distance,s*src.distance);
		ret.slope.set(-s,c);

		return ret;
	}

	/**
	 * Converts a line from polar form to general.  After conversion the line will be normalized, e.g. A*A + B*B == 1.
	 *
	 * @param src (input) line is polar notation
	 * @param ret (output) line in general notation.  If null a new instance will be created.
	 * @return Converted line in general notation
	 */
	public static LineGeneral2D_F64 convert( LinePolar2D_F64 src , LineGeneral2D_F64 ret )
	{
		if( ret == null )
			ret = new LineGeneral2D_F64();

		double c = (double) Math.cos(src.angle);
		double s = (double) Math.sin(src.angle);

		ret.A = c;
		ret.B = s;
		ret.C = -src.distance;

		return ret;
	}

	/**
	 * Converts a line from general to polar.
	 *
	 * @param src (input) line is general notation
	 * @param ret (output) line in polar notation.  If null a new instance will be created.
	 * @return Converted line in polar notation
	 */
	public static LinePolar2D_F64 convert( LineGeneral2D_F64 src , LinePolar2D_F64 ret )
	{
		if( ret == null )
			ret = new LinePolar2D_F64();

		double r = Math.sqrt(src.A*src.A + src.B*src.B);

		double sign = src.C < 0 ? -1 : 1;

		ret.angle = Math.atan2(-sign*src.B/r,-sign*src.A/r);
		ret.distance = sign*src.C/r;

		return ret;
	}

	/**
	 * Converts a line segment into a parametric line.  The start point will be 'src.a' and the
	 * direction will be in the direction of 'src.b-src.a'
	 *
	 * @param src (input) line segment
	 * @param ret (output) line in parametric notation.  If null a new instance will be created.
	 * @return Converted line in parametric notation
	 */
	public static LineParametric2D_F64 convert( LineSegment2D_F64 src , LineParametric2D_F64 ret )
	{
		if( ret == null )
			ret = new LineParametric2D_F64();

		ret.p.set(src.a);
		ret.slope.set(src.slopeX(),src.slopeY());

		return ret;
	}

	/**
	 * Converts a line segment into a general line.
	 *
	 * @param src (Input) line segment
	 * @param ret (output) line in general notation. If null a new instance will be created.
	 * @return Line in general notation
	 */
	public static LineGeneral2D_F64 convert( LineSegment2D_F64 src , LineGeneral2D_F64 ret )
	{
		if( ret == null )
			ret = new LineGeneral2D_F64();

		ret.A = src.a.y - src.b.y;
		ret.B = src.b.x - src.a.x;
		ret.C = -(ret.A*src.a.x + ret.B*src.a.y);

		return ret;
	}

	/**
	 * Converts a line from parametric to polar.
	 *
	 * @param src (Input) line in parametric notation
	 * @param ret (output) line in polar notation. If null a new instance will be created.
	 * @return Line in polar notation
	 */
	public static LinePolar2D_F64 convert( LineParametric2D_F64 src , LinePolar2D_F64 ret )
	{
		if( ret == null )
			ret = new LinePolar2D_F64();

		double t = ClosestPoint2D_F64.closestPointT(src, new Point2D_F64());

		double cpX = src.slope.x * t + src.p.x;
		double cpY = src.slope.y * t + src.p.y;

		ret.angle = Math.atan2(cpY,cpX);
		ret.distance = Math.sqrt(cpX*cpX + cpY*cpY);

		return ret;
	}

	/**
	 * Converts a line from parametric to general
	 *
	 * @param src (input) line in parametric notation.
	 * @param ret (output) line in general notation.  If null a new instance will be created.
	 * @return Converted line in general notation
	 */
	public static LineGeneral2D_F64 convert( LineParametric2D_F64 src , LineGeneral2D_F64 ret ) {
		if( ret == null ) {
			ret = new LineGeneral2D_F64();
		}

		double x1 = src.p.x + src.slope.x;
		double y1 = src.p.y + src.slope.y;

		ret.A = (src.p.y - y1);
		ret.B = (x1 - src.p.x);
		ret.C = src.p.x*y1 - x1*src.p.y;

		return ret;
	}

	/**
	 * Converts a line from general to parametric
	 *
	 * @param src (input) line in general notation.
	 * @param ret (output) line in parametric notation.  If null a new instance will be created.
	 * @return Converted line in parametric notation
	 */
	public static LineParametric2D_F64 convert( LineGeneral2D_F64 src ,  LineParametric2D_F64 ret ) {
		if( ret == null ) {
			ret = new LineParametric2D_F64();
		}

		ret.slope.x = src.B;
		ret.slope.y = -src.A;

		// find a point on the line
		if( Math.abs(src.B) > Math.abs(src.A) ) {
			ret.p.y = -src.C/src.B;
			ret.p.x = 0;
		} else {
			ret.p.x = -src.C/src.A;
			ret.p.y = 0;
		}

		return ret;
	}
}
