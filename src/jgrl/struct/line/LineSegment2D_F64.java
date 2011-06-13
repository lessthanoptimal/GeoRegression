/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Java Geometric Regression Library (JGRL).
 *
 * JGRL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with JGRL.  If not, see <http://www.gnu.org/licenses/>.
 */

package jgrl.struct.line;

import jgrl.struct.point.Point2D_F64;


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

	public LineSegment2D_F64 copy() {
		return new LineSegment2D_F64( a, b );
	}
}
