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

import georegression.struct.point.Point2D_F32;

import java.io.Serializable;

/**
 * An axis aligned rectangle in 2D that is specified by its lower-extent p0, and upper-extent p1.
 */
public class Rectangle2D_F32 implements Serializable {
	/**
	 * Lower extent
	 */
	public Point2D_F32 p0 = new Point2D_F32();
	/**
	 * Upper extent
	 */
	public Point2D_F32 p1 = new Point2D_F32();

	public Rectangle2D_F32(float x0, float y0, float x1, float y1) {
		set(x0,y0,x1,y1);
	}

	public Rectangle2D_F32(Rectangle2D_F32 orig) {
		set(orig);
	}

	public void set( Rectangle2D_F32 orig ) {
		this.p0.set(orig.p0);
		this.p1.set(orig.p1);
	}

	public void set(float x0, float y0, float x1, float y1) {
		this.p0.set(x0,y0);
		this.p1.set(x1,y1);
	}

	public Rectangle2D_F32() {
	}

	/**
	 * Makes sure x0,y0 is the lower extent and x1,y1 is the upper extent
	 */
	public void enforceExtents() {
		if( p1.x < p0.x ) {
			float tmp = p1.x;
			p1.x = p0.x;
			p0.x = tmp;
		}
		if( p1.y < p0.y ) {
			float tmp = p1.y;
			p1.y = p0.y;
			p0.y = tmp;
		}
	}

	public float getWidth() {
		return p1.x-p0.x;
	}

	public float getHeight() {
		return p1.y-p0.y;
	}

	public float area() {
		return ( p1.y-p0.y)*(p1.x-p0.x);
	}

	public Point2D_F32 getP0() {
		return p0;
	}

	public void setP0(Point2D_F32 p0) {
		this.p0 = p0;
	}

	public Point2D_F32 getP1() {
		return p1;
	}

	public void setP1(Point2D_F32 p1) {
		this.p1 = p1;
	}

	public String toString() {
		return getClass().getSimpleName()+"{ p0("+p0.x+" "+p0.y+") p1("+p1.x+" "+p1.y+") }";
	}
}
