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

package georegression.struct.shapes;

import georegression.struct.point.Point2D_F32;

import java.io.Serializable;

/**
 * A polygon with 4 vertices, a,b,c, and d.  The vertices are in order sequential order of a,b,c,d.
 *
 */
public class Quadrilateral_F32 implements Serializable {
	public Point2D_F32 a;
	public Point2D_F32 b;
	public Point2D_F32 c;
	public Point2D_F32 d;

	public Quadrilateral_F32() {
		a = new Point2D_F32();
		b = new Point2D_F32();
		c = new Point2D_F32();
		d = new Point2D_F32();
	}

	public Quadrilateral_F32( Quadrilateral_F32 quad ) {
		this();
		a.set(quad.a);
		b.set(quad.b);
		c.set(quad.c);
		d.set(quad.d);
	}

	public Quadrilateral_F32( float x0, float y0 , float x1, float y1 ,
							  float x2, float y2 , float x3, float y3 ) {
		a = new Point2D_F32(x0,y0);
		b = new Point2D_F32(x1,y1);
		c = new Point2D_F32(x2,y2);
		d = new Point2D_F32(x3,y3);
	}

	public Quadrilateral_F32(Point2D_F32 a, Point2D_F32 b, Point2D_F32 c, Point2D_F32 d ) {
		this(a,b,c,d,true);
	}

	public Quadrilateral_F32(Point2D_F32 a, Point2D_F32 b, Point2D_F32 c, Point2D_F32 d, boolean copy ) {
		if( copy ) {
			this.a = new Point2D_F32(a);
			this.b = new Point2D_F32(b);
			this.c = new Point2D_F32(c);
			this.d = new Point2D_F32(d);
		} else {
			this.a = a;
			this.b = b;
			this.c = c;
			this.d = d;
		}
	}

	public Point2D_F32 getA() {
		return a;
	}

	public void setA(Point2D_F32 a) {
		this.a = a;
	}

	public Point2D_F32 getB() {
		return b;
	}

	public void setB(Point2D_F32 b) {
		this.b = b;
	}

	public Point2D_F32 getC() {
		return c;
	}

	public void setC(Point2D_F32 c) {
		this.c = c;
	}

	public Point2D_F32 getD() {
		return d;
	}

	public void setD(Point2D_F32 d) {
		this.d = d;
	}

	public void set(Quadrilateral_F32 quad) {
		this.a.set(quad.a);
		this.b.set(quad.b);
		this.c.set(quad.c);
		this.d.set(quad.d);
	}

	public Quadrilateral_F32 copy() {
		return new Quadrilateral_F32(this);
	}
}
