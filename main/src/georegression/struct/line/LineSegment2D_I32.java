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

package georegression.struct.line;

import georegression.struct.point.Point2D_I32;

import java.io.Serializable;


/**
 * Defines a line segment by its two end points.
 *
 * @author Peter Abeles
 */
public class LineSegment2D_I32 implements Serializable {
	public Point2D_I32 a = new Point2D_I32();
	public Point2D_I32 b = new Point2D_I32();

	public LineSegment2D_I32() {
	}

	public LineSegment2D_I32(Point2D_I32 a, Point2D_I32 b) {
		set( a, b );
	}

	public LineSegment2D_I32(int x0, int y0, int x1, int y1) {
		set( x0, y0, x1, y1 );
	}
	
	public static LineSegment2D_I32 wrap( Point2D_I32 a , Point2D_I32 b ) {
		LineSegment2D_I32 ret = new LineSegment2D_I32();
		ret.a = a;
		ret.b = b;
		return ret;
	}

	public void set( LineSegment2D_I32 l ) {
		this.a.set( l.a );
		this.b.set( l.b );
	}

	public void set( Point2D_I32 a, Point2D_I32 b ) {
		this.a.set( a );
		this.b.set( b );
	}

	public void set( int x0, int y0, int x1, int y1 ) {
		a.set( x0, y0 );
		b.set( x1, y1 );
	}

	public Point2D_I32 getA() {
		return a;
	}

	public void setA( Point2D_I32 a ) {
		this.a = a;
	}

	public Point2D_I32 getB() {
		return b;
	}

	public void setB( Point2D_I32 b ) {
		this.b = b;
	}

	public int slopeX() {
		return b.x-a.x;
	}

	public int slopeY() {
		return b.y-a.y;
	}

	public double getLength() {
		return a.distance(b);
	}

	public int getLength2() {
		return a.distance2(b);
	}

	public LineSegment2D_I32 copy() {
		return new LineSegment2D_I32( a, b );
	}
}
