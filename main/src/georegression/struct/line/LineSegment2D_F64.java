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

package georegression.struct.line;

import georegression.struct.point.Point2D_F64;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * Defines a line segment by its two end points.
 *
 * @see georegression.geometry.UtilLine2D_F64
 *
 * @author Peter Abeles
 */
@Getter @Setter
public class LineSegment2D_F64 implements Serializable {
	public Point2D_F64 a = new Point2D_F64();
	public Point2D_F64 b = new Point2D_F64();

	public LineSegment2D_F64() {}

	public LineSegment2D_F64( Point2D_F64 a, Point2D_F64 b ) {
		setTo( a, b );
	}

	public LineSegment2D_F64( double x0, double y0, double x1, double y1 ) {
		setTo( x0, y0, x1, y1 );
	}
	
	public static LineSegment2D_F64 wrap( Point2D_F64 a , Point2D_F64 b ) {
		LineSegment2D_F64 ret = new LineSegment2D_F64();
		ret.a = a;
		ret.b = b;
		return ret;
	}

	public void setTo(LineSegment2D_F64 l ) {
		this.a.setTo( l.a );
		this.b.setTo( l.b );
	}

	public void setTo(Point2D_F64 a, Point2D_F64 b ) {
		this.a.setTo( a );
		this.b.setTo( b );
	}

	public void setTo(double x0, double y0, double x1, double y1 ) {
		a.setTo( x0, y0 );
		b.setTo( x1, y1 );
	}

	public void zero() {
		a.zero();
		b.zero();
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

	@Override
	public String toString() {
		return getClass().getSimpleName()+"{" +
				"a=" + a +
				", b=" + b +
				'}';
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;

		if(!(obj instanceof LineSegment2D_F64))
			return false;

		var o = (LineSegment2D_F64) obj;
		return a.equals(o.a) && b.equals(o.b);
	}

	@Override
	public int hashCode() {
		return a.hashCode() + b.hashCode();
	}
}
