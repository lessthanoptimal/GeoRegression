/*
 * Copyright (C) 2011-2015, Peter Abeles. All Rights Reserved.
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

import georegression.struct.point.Point2D_F64;

import java.io.Serializable;

/**
 * An axis aligned rectangle in 2D that is specified by its lower-extent p0, and upper-extent p1.
 */
public class Rectangle2D_F64 implements Serializable {
	/**
	 * Lower extent
	 */
	public Point2D_F64 p0 = new Point2D_F64();
	/**
	 * Upper extent
	 */
	public Point2D_F64 p1 = new Point2D_F64();

	public Rectangle2D_F64(double x0, double y0, double x1, double y1) {
		set(x0,y0,x1,y1);
	}

	public Rectangle2D_F64(Rectangle2D_F64 orig) {
		set(orig);
	}

	public void set( Rectangle2D_F64 orig ) {
		this.p0.set(orig.p0);
		this.p1.set(orig.p1);
	}

	public void set(double x0, double y0, double x1, double y1) {
		this.p0.set(x0,y0);
		this.p1.set(x1,y1);
	}

	public Rectangle2D_F64() {
	}

	/**
	 * Makes sure x0,y0 is the lower extent and x1,y1 is the upper extent
	 */
	public void enforceExtents() {
		if( p1.x < p0.x ) {
			double tmp = p1.x;
			p1.x = p0.x;
			p0.x = tmp;
		}
		if( p1.y < p0.y ) {
			double tmp = p1.y;
			p1.y = p0.y;
			p0.y = tmp;
		}
	}

	public double getWidth() {
		return p1.x-p0.x;
	}

	public double getHeight() {
		return p1.y-p0.y;
	}

	public double area() {
		return ( p1.y-p0.y)*(p1.x-p0.x);
	}

	public Point2D_F64 getP0() {
		return p0;
	}

	public void setP0(Point2D_F64 p0) {
		this.p0 = p0;
	}

	public Point2D_F64 getP1() {
		return p1;
	}

	public void setP1(Point2D_F64 p1) {
		this.p1 = p1;
	}

	public String toString() {
		return getClass().getSimpleName()+"{ p0("+p0.x+" "+p0.y+") p1("+p1.x+" "+p1.y+") }";
	}
}
