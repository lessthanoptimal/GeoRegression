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

import georegression.struct.point.Point2D_F32;
import georegression.struct.point.Vector2D_F32;

import java.io.Serializable;

/**
 * <p>
 * 2D line parameterized using parametric equation:<br>
 * [x, y] = [x_0, y_0] + tÂ·[slopeX, slopeY]<br>
 * where t specifies the location along the line, (x_0,y_0) is an arbitrary point on the line,
 * and (slopeX,slopeY).
 * </p>
 */
public class LineParametric2D_F32 implements Serializable {
	/**
	 * A point on the line
	 */
	public Point2D_F32 p = new Point2D_F32();
	/**
	 * The line's slope
	 */
	public Vector2D_F32 slope = new Vector2D_F32();

	public LineParametric2D_F32( float x_0, float y_0, float slopeX, float slopeY ) {
		p.set( x_0, y_0 );
		slope.set( slopeX, slopeY );
	}

	public LineParametric2D_F32( Point2D_F32 p, Vector2D_F32 slope ) {
		setPoint( p );
		setSlope( slope );
	}

	public LineParametric2D_F32() {
	}

	public void set( LineParametric2D_F32 line ) {
		this.p.set(line.p);
		this.slope.set(line.slope);
	}
	
	public void setPoint( Point2D_F32 pt ) {
		this.p.set( pt );
	}

	public void setPoint( float x, float y ) {
		this.p.x = x;
		this.p.y = y;
	}

	public void setSlope( Vector2D_F32 slope ) {
		this.slope.set( slope );
	}

	public void setSlope( float slopeX, float slopeY ) {
		this.slope.x = slopeX;
		this.slope.y = slopeY;
	}

	/**
	 * Sets the slope to the unit vector specified by the provided angle.
	 *
	 * @param angle Angle of the line specified in radians.
	 */
	public void setAngle( float angle ) {
		slope.set( (float)Math.cos( angle ), (float)Math.sin( angle ) );
	}

	public float getAngle() {
		return (float)Math.atan2( slope.y, slope.x );
	}

	/**
	 * Returns a point along the line.  See parametric equation in class description.
	 *
	 * @param t Location along the line.
	 * @return Point on the line.
	 */
	public Point2D_F32 getPointOnLine( float t ) {
		return new Point2D_F32( slope.x * t + p.x, slope.y * t + p.y );
	}

	public Point2D_F32 getPoint() {
		return p;
	}

	public final float getSlopeX() {
		return slope.x;
	}

	public final float getSlopeY() {
		return slope.y;
	}

	public final float getX() {
		return p.x;
	}

	public final float getY() {
		return p.y;
	}

	public Point2D_F32 getP() {
		return p;
	}

	public void setP(Point2D_F32 p) {
		this.p = p;
	}

	public Vector2D_F32 getSlope() {
		return slope;
	}

	public LineParametric2D_F32 copy() {
		return new LineParametric2D_F32( p, slope );
	}
}
