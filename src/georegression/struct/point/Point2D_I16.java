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

package georegression.struct.point;

/**
 * A point in 2D composed of shorts
 */
public class Point2D_I16 {
	public short x;
	public short y;

	public Point2D_I16( short x, short y ) {
		this.x = x;
		this.y = y;
	}

	public Point2D_I16() {
	}

	public void set( Point2D_I16 pt ) {
		this.x = pt.x;
		this.y = pt.y;
	}

	public void set( int x, int y ) {
		this.x = (short)x;
		this.y = (short)y;
	}

	public void setX( int x ) {
		this.x = (short)x;
	}

	public void setY( int y ) {
		this.y = (short)y;
	}

	public final int getX() {
		return x;
	}

	public final int getY() {
		return y;
	}

	public Point2D_I16 copy() {
		return new Point2D_I16( x, y );
	}

	@Override
	public String toString() {
		return "Point2D_I16{ x= " + x +", y= " + y +'}';
	}
}