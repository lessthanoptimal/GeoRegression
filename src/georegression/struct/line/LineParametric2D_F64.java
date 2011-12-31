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
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.struct.line;

import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Vector2D_F64;

/**
 * <p>
 * 2D line parameterized using parametric equation:<br>
 * [x, y] = [x_0, y_0] + t·[slopeX, slopeY]<br>
 * where t specifies the location along the line, (x_0,y_0) is an arbitrary point on the line,
 * and (slopeX,slopeY).
 * </p>
 */
public class LineParametric2D_F64 {
	/**
	 * A point on the line
	 */
	public Point2D_F64 p = new Point2D_F64();
	/**
	 * The line's slope
	 */
	public Vector2D_F64 slope = new Vector2D_F64();

	public LineParametric2D_F64( double x_0, double y_0, double slopeX, double slopeY ) {
		p.set( x_0, y_0 );
		slope.set( slopeX, slopeY );
	}

	public LineParametric2D_F64( Point2D_F64 p, Vector2D_F64 slope ) {
		setPoint( p );
		setSlope( slope );
	}

	public LineParametric2D_F64() {
	}

	public void setPoint( Point2D_F64 pt ) {
		this.p.set( pt );
	}

	public void setPoint( double x, double y ) {
		this.p.x = x;
		this.p.y = y;
	}

	public void setSlope( Vector2D_F64 slope ) {
		this.slope.set( slope );
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
		slope.set( Math.cos( angle ), Math.sin( angle ) );
	}

	public double getAngle() {
		return Math.atan2( slope.y, slope.x );
	}

	/**
	 * Returns a point along the line.  See parametric equation in class description.
	 *
	 * @param t Location along the line.
	 * @return Point on the line.
	 */
	public Point2D_F64 getPointOnLine( double t ) {
		return new Point2D_F64( slope.x * t + p.x, slope.y * t + p.y );
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
		return new LineParametric2D_F64( p, slope );
	}
}
