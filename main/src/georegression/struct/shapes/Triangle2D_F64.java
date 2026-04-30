/*
 * Copyright (C) 2026, Peter Abeles. All Rights Reserved.
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

package georegression.struct.shapes;

import georegression.struct.GeoTuple2D_F64;
import georegression.struct.line.LineSegment2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Vector2D_F64;
import lombok.Getter;
import lombok.Setter;
import org.ejml.MapFormattable;
import org.ejml.MapPrintFormat;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * Triangle in 2D space. Described by 3 vertices/points.
 */
@Getter @Setter
public class Triangle2D_F64 implements Serializable, MapFormattable {
	public Point2D_F64 v0 = new Point2D_F64();
	public Point2D_F64 v1 = new Point2D_F64();
	public Point2D_F64 v2 = new Point2D_F64();

	public Triangle2D_F64() {}

	public Triangle2D_F64( double x0, double y0,
						   double x1, double y1,
						   double x2, double y2 ) {
		setTo(x0, y0, x1, y1, x2, y2);
	}

	public Triangle2D_F64 setTo( Triangle2D_F64 orig ) {
		v0.setTo(orig.v0);
		v1.setTo(orig.v1);
		v2.setTo(orig.v2);
		return this;
	}

	public Triangle2D_F64 setTo( double x0, double y0,
								 double x1, double y1,
								 double x2, double y2 ) {
		this.v0.setTo(x0, y0);
		this.v1.setTo(x1, y1);
		this.v2.setTo(x2, y2);
		return this;
	}

	/** Sets the value of all fields to zero */
	public void zero() {
		this.v0.zero();
		this.v1.zero();
		this.v2.zero();
	}

	public Point2D_F64 get( int i ) {
		return switch (i) {
			case 0 -> v0;
			case 1 -> v1;
			case 2 -> v2;
			default -> throw new IllegalArgumentException("Invalid index");
		};
	}

	/// Sets the value of vertex\[i\] to (x, y)
	public void set( int i, double x, double y ) {
		get(i).setTo(x, y);
	}

	/// Sets the value of vertex\[i\] to (p.x, p.y)
	public void set( int i, GeoTuple2D_F64<?> p ) {
		get(i).setTo(p.x, p.y);
	}

	/// Returns the line segment for each side. 0 = (0,1), 1 = (1,2), 2 = (2,0).
	public LineSegment2D_F64 getSide( int i, @Nullable LineSegment2D_F64 side ) {
		if (side == null)
			side = new LineSegment2D_F64();
		side.a.setTo(get(i));
		side.b.setTo(get((i + 1)%3));

		return side;
	}

	/// Computes the normalized tangent vector for a side. If in CCW orientation it will point outwards.
	public Vector2D_F64 sideTangent( int i, @Nullable Vector2D_F64 normal ) {
		if (normal == null)
			normal = new Vector2D_F64();

		Point2D_F64 a = get(i);
		Point2D_F64 b = get((i + 1)%3);

		double dx = b.x - a.x;
		double dy = b.y - a.y;
		double length = Math.sqrt(dx*dx + dy*dy);

		normal.setTo(dy/length, -dx/length);
		return normal;
	}

	/// Maximum absolute value of any coordinate in each vertex
	public double maxAbsValue() {
		double found = 0.0;
		for (int i = 0; i < 3; i++) {
			Point2D_F64 p = get(i);
			found = Math.max(Math.abs(p.x), Math.abs(p.y));
		}
		return found;
	}

	public Triangle2D_F64 copy() {
		return new Triangle2D_F64().setTo(this);
	}

	@Override public String formatMap( MapPrintFormat format ) {
		return format.itemPrefix +
				format.pair("v0", v0.formatMap(format), true) +
				format.pair("v1", v1.formatMap(format), true) +
				format.pair("v2", v2.formatMap(format), false) +
				format.itemSuffix;
	}

	@Override public String toString() { return MapPrintFormat.DEFAULT.toString(this); }
}
