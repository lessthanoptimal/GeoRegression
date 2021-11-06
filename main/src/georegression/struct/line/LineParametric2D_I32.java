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
import georegression.struct.point.Point2D_I32;

import java.io.Serializable;

/**
 * <p>
 * 2D line parameterized using parametric equation:<br>
 * [x, y] = [x_0, y_0] + tÂ·[slopeX, slopeY]<br>
 * where t specifies the location along the line, (x_0,y_0) is an arbitrary point on the line,
 * and (slopeX,slopeY).
 * </p>
 *
 * @see georegression.geometry.UtilLine2D_I32
 */
public class LineParametric2D_I32 implements Serializable {
	/**
	 * A point on the line
	 */
	public Point2D_I32 p = new Point2D_I32();
	/**
	 * The line's slope
	 */
	public int slopeX,slopeY;

	public LineParametric2D_I32(int x_0, int y_0, int slopeX, int slopeY ) {
		p.setTo( x_0, y_0 );
		this.slopeX = slopeX;
		this.slopeY = slopeY;
	}

	public LineParametric2D_I32(Point2D_I32 p, int slopeX, int slopeY ) {
		setPoint(p);
		setSlope(slopeX,slopeY);
	}

	public LineParametric2D_I32() {}

	public void setTo(LineParametric2D_I32 line ) {
		this.p.setTo(line.p);
		this.slopeX = line.slopeX;
		this.slopeY = line.slopeY;
	}
	
	public void setPoint( Point2D_I32 pt ) {
		this.p.setTo( pt );
	}

	public void setPoint( int x, int y ) {
		this.p.x = x;
		this.p.y = y;
	}

	public void setSlope( int slopeX , int slopeY ) {
		this.slopeX = slopeX;
		this.slopeY = slopeY;
	}

	/**
	 * Returns a point along the line. See parametric equation in class description.
	 *
	 * @param t Location along the line.
	 * @return Point on the line.
	 */
	public Point2D_F64 getPointOnLine( double t ) {
		return new Point2D_F64( slopeX * t + p.x, slopeY * t + p.y );
	}

	public Point2D_I32 getPoint() {
		return p;
	}

	public final int getSlopeX() {
		return slopeX;
	}

	public final int getSlopeY() {
		return slopeY;
	}

	public final int getX() {
		return p.x;
	}

	public final int getY() {
		return p.y;
	}

	public Point2D_I32 getP() {
		return p;
	}

	public void setP(Point2D_I32 p) {
		this.p = p;
	}

	public LineParametric2D_I32 copy() {
		return new LineParametric2D_I32( p, slopeX, slopeY );
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+" P( "+p.x+" "+p.y+" ) Slope( "+slopeX+" "+slopeY+" )";
	}
}
