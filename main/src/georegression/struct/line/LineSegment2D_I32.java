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

import georegression.struct.point.Point2D_I32;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Defines a line segment by its two end points.
 *
 * @author Peter Abeles
 * @see georegression.geometry.UtilLine2D_I32
 */
@Getter @Setter
public class LineSegment2D_I32 implements Serializable {
	public Point2D_I32 a = new Point2D_I32();
	public Point2D_I32 b = new Point2D_I32();

	public LineSegment2D_I32() {}

	public LineSegment2D_I32( Point2D_I32 a, Point2D_I32 b ) {
		setTo(a, b);
	}

	public LineSegment2D_I32( int x0, int y0, int x1, int y1 ) {
		setTo(x0, y0, x1, y1);
	}

	public static LineSegment2D_I32 wrap( Point2D_I32 a, Point2D_I32 b ) {
		LineSegment2D_I32 ret = new LineSegment2D_I32();
		ret.a = a;
		ret.b = b;
		return ret;
	}

	public void setTo( LineSegment2D_I32 l ) {
		this.a.setTo(l.a);
		this.b.setTo(l.b);
	}

	public void setTo( Point2D_I32 a, Point2D_I32 b ) {
		this.a.setTo(a);
		this.b.setTo(b);
	}

	public void setTo( int x0, int y0, int x1, int y1 ) {
		a.setTo(x0, y0);
		b.setTo(x1, y1);
	}

	public void zero() {
		a.zero();
		b.zero();
	}

	public int slopeX() {
		return b.x - a.x;
	}

	public int slopeY() {
		return b.y - a.y;
	}

	public double getLength() {
		return a.distance(b);
	}

	public int getLength2() {
		return a.distance2(b);
	}

	public LineSegment2D_I32 copy() {
		return new LineSegment2D_I32(a, b);
	}

	@Override
	public String toString() {
		return "LineSegment2D_I32{" +
				"a=" + a +
				", b=" + b +
				'}';
	}

	@Override
	public boolean equals( Object obj ) {
		if (this == obj)
			return true;

		if (!(obj instanceof LineSegment2D_I32))
			return false;

		LineSegment2D_I32 o = (LineSegment2D_I32)obj;
		return a.equals(o.a) && b.equals(o.b);
	}

	@Override
	public int hashCode() {
		return a.hashCode() + b.hashCode();
	}
}
