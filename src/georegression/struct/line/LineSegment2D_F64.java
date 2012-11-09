/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
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

package georegression.struct.line;

import georegression.struct.point.Point2D_F64;


/**
 * Defines a line segment by its two end points.
 *
 * @author Peter Abeles
 */
public class LineSegment2D_F64 {
	public Point2D_F64 a = new Point2D_F64();
	public Point2D_F64 b = new Point2D_F64();

	public LineSegment2D_F64() {
	}

	public LineSegment2D_F64( Point2D_F64 a, Point2D_F64 b ) {
		set( a, b );
	}

	public LineSegment2D_F64( double x0, double y0, double x1, double y1 ) {
		set( x0, y0, x1, y1 );
	}
	
	public static LineSegment2D_F64 wrap( Point2D_F64 a , Point2D_F64 b ) {
		LineSegment2D_F64 ret = new LineSegment2D_F64();
		ret.a = a;
		ret.b = b;
		return ret;
	}

	public void set( Point2D_F64 a, Point2D_F64 b ) {
		this.a.set( a );
		this.b.set( b );
	}

	public void set( double x0, double y0, double x1, double y1 ) {
		a.set( x0, y0 );
		b.set( x1, y1 );
	}

	public Point2D_F64 getA() {
		return a;
	}

	public void setA( Point2D_F64 a ) {
		this.a = a;
	}

	public Point2D_F64 getB() {
		return b;
	}

	public void setB( Point2D_F64 b ) {
		this.b = b;
	}

	public double slopeX() {
		return b.x-a.x;
	}

	public double slopeY() {
		return b.y-a.y;
	}

	public double getLength() {
		return a.distance(b);
	}

	public double getLength2() {
		return a.distance2(b);
	}

	public LineSegment2D_F64 copy() {
		return new LineSegment2D_F64( a, b );
	}
}
