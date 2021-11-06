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

import georegression.geometry.UtilLine2D_F64;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Vector2D_F64;
import lombok.Getter;
import lombok.Setter;
import org.ejml.FancyPrint;

import java.io.Serializable;

/**
 * <p>
 * 2D line parameterized using parametric equation:<br>
 * [x, y] = [x_0, y_0] + t·[slopeX, slopeY]<br>
 * where t specifies the location along the line, (x_0,y_0) is an arbitrary point on the line,
 * and (slopeX,slopeY).
 * </p>
 *
 * @see UtilLine2D_F64
 */
public class LineParametric2D_F64 implements Serializable {
	/** A point on the line */
	@Getter @Setter public Point2D_F64 p = new Point2D_F64();

	/** The line's slope */
	@Getter public Vector2D_F64 slope = new Vector2D_F64();

	public LineParametric2D_F64( double x_0, double y_0, double slopeX, double slopeY ) {
		p.setTo(x_0, y_0);
		slope.setTo(slopeX, slopeY);
	}

	public LineParametric2D_F64( Point2D_F64 p, Vector2D_F64 slope ) {
		setPoint(p);
		setSlope(slope);
	}

	/**
	 * Creates a line defined from two points. The slope will be in the direction from a to b.
	 *
	 * @param a point on line
	 * @param b point on line which isn't a
	 * @see UtilLine2D_F64#convert(Point2D_F64, Point2D_F64, LineParametric2D_F64)
	 */
	public LineParametric2D_F64( Point2D_F64 a, Point2D_F64 b ) {
		UtilLine2D_F64.convert(a, b, this);
	}

	public LineParametric2D_F64() {}

	public void setTo( LineParametric2D_F64 line ) {
		this.p.setTo(line.p);
		this.slope.set(line.slope);
	}

	public void zero() {
		p.zero();
		slope.zero();
	}

	public void setPoint( Point2D_F64 pt ) {
		this.p.setTo(pt);
	}

	public void setPoint( double x, double y ) {
		this.p.x = x;
		this.p.y = y;
	}

	public void setSlope( Vector2D_F64 slope ) {
		this.slope.set(slope);
	}

	public void setSlope( double slopeX, double slopeY ) {
		this.slope.x = slopeX;
		this.slope.y = slopeY;
	}

	/**
	 * Sets the slope to the unit vector specified by the provided angle.
	 *
	 * @param angle Angle of the line specified in radians.
	 */
	public void setAngle( double angle ) {
		slope.setTo( Math.cos(angle), Math.sin(angle));
	}

	public double getAngle() {
		return Math.atan2(slope.y, slope.x);
	}

	/**
	 * Returns a point along the line. See parametric equation in class description.
	 *
	 * @param t Location along the line.
	 * @return Point on the line.
	 */
	public Point2D_F64 getPointOnLine( double t ) {
		return new Point2D_F64(slope.x*t + p.x, slope.y*t + p.y);
	}

	public void getPointOnLine( double t, Point2D_F64 x ) {
		x.x = slope.x*t + p.x;
		x.y = slope.y*t + p.y;
	}

	public Point2D_F64 getPoint() {
		return p;
	}

	public final double getSlopeX() {
		return slope.x;
	}

	public final double getSlopeY() {
		return slope.y;
	}

	public final double getX() {
		return p.x;
	}

	public final double getY() {
		return p.y;
	}

	public LineParametric2D_F64 copy() {
		return new LineParametric2D_F64(p, slope);
	}

	@Override
	public String toString() {
		FancyPrint f = new FancyPrint();
		return getClass().getSimpleName() + " P( " + f.s(p.x) + " " + f.s(p.y) + " ) Slope( " + f.s(slope.x) + " " + f.s(slope.y) + " )";
	}

	@Override
	public boolean equals( Object obj ) {
		if (this == obj)
			return true;

		if (!(obj instanceof LineParametric2D_F64))
			return false;

		var o = (LineParametric2D_F64)obj;
		return p.equals(o.p) && slope.equals(o.slope);
	}

	@Override
	public int hashCode() {
		return p.hashCode() + slope.hashCode();
	}
}
