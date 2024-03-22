/*
 * Copyright (C) 2022, Peter Abeles. All Rights Reserved.
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

import georegression.struct.point.Point3D_F64;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * Defines a line segment by its two end points.
 *
 * @author Peter Abeles
 * @see georegression.geometry.UtilLine3D_F64
 */
@Getter @Setter
public class LineSegment3D_F64 implements Serializable {
	public Point3D_F64 a = new Point3D_F64();
	public Point3D_F64 b = new Point3D_F64();

	public LineSegment3D_F64() {}

	public LineSegment3D_F64( Point3D_F64 a, Point3D_F64 b ) {
		setTo(a, b);
	}

	public LineSegment3D_F64( double x0, double y0, double z0, double x1, double y1, double z1 ) {
		setTo(x0, y0, z0, x1, y1, z1);
	}

	public static LineSegment3D_F64 wrap( Point3D_F64 a, Point3D_F64 b ) {
		LineSegment3D_F64 ret = new LineSegment3D_F64();
		ret.a = a;
		ret.b = b;
		return ret;
	}

	public LineSegment3D_F64 setTo( LineSegment3D_F64 l ) {
		this.a.setTo(l.a);
		this.b.setTo(l.b);
		return this;
	}

	public LineSegment3D_F64 setTo( Point3D_F64 a, Point3D_F64 b ) {
		this.a.setTo(a);
		this.b.setTo(b);
		return this;
	}

	public LineSegment3D_F64 setTo( double x0, double y0, double z0, double x1, double y1, double z1 ) {
		a.setTo(x0, y0, z0);
		b.setTo(x1, y1, z1);
		return this;
	}

	public void zero() {
		a.zero();
		b.zero();
	}

	/**
	 * Computes a point on the line based on the fraction distance between 'a' and 'b'
	 */
	public Point3D_F64 pointOnLine( double fraction, @Nullable Point3D_F64 p ) {
		if (p == null)
			p = new Point3D_F64();
		p.x = axisOnLineX(fraction);
		p.y = axisOnLineY(fraction);
		p.z = axisOnLineZ(fraction);
		return p;
	}

	/**
	 * Value of x-axis for a point on the line at this fractional location between 'a' and 'b'
	 */
	public double axisOnLineX( double fraction ) {
		return slopeX()*fraction + a.x;
	}

	/**
	 * Value of y-axis for a point on the line at this fractional location between 'a' and 'b'
	 */
	public double axisOnLineY( double fraction ) {
		return slopeY()*fraction + a.y;
	}

	/**
	 * Value of z-axis for a point on the line at this fractional location between 'a' and 'b'
	 */
	public double axisOnLineZ( double fraction ) {
		return slopeZ()*fraction + a.z;
	}

	public double slopeX() {
		return b.x - a.x;
	}

	public double slopeY() {
		return b.y - a.y;
	}

	public double slopeZ() {
		return b.z - a.z;
	}

	public double getLength() {
		return a.distance(b);
	}

	public double getLength2() {
		return a.distance2(b);
	}

	public LineSegment3D_F64 copy() {
		return new LineSegment3D_F64(a, b);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{" +
				"a=" + a +
				", b=" + b +
				'}';
	}

	@Override
	public boolean equals( Object obj ) {
		if (this == obj)
			return true;

		if (!(obj instanceof LineSegment3D_F64))
			return false;

		var o = (LineSegment3D_F64)obj;
		return a.equals(o.a) && b.equals(o.b);
	}

	@Override
	public int hashCode() {
		return a.hashCode() + b.hashCode();
	}
}
