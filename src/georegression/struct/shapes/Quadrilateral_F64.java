/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.struct.shapes;

import georegression.struct.point.Point2D_F64;

/**
 * A polygon with 4 vertices, a,b,c, and d.  The vertices are in order sequential order of a,b,c,d.
 *
 */
public class Quadrilateral_F64 {
	public Point2D_F64 a;
	public Point2D_F64 b;
	public Point2D_F64 c;
	public Point2D_F64 d;

	public Quadrilateral_F64() {
		a = new Point2D_F64();
		b = new Point2D_F64();
		c = new Point2D_F64();
		d = new Point2D_F64();
	}

	public Quadrilateral_F64( double x0, double y0 , double x1, double y1 ,
							  double x2, double y2 , double x3, double y3 ) {
		a = new Point2D_F64(x0,y0);
		b = new Point2D_F64(x1,y1);
		c = new Point2D_F64(x2,y2);
		d = new Point2D_F64(x3,y3);
	}

	public Quadrilateral_F64(Point2D_F64 a, Point2D_F64 b, Point2D_F64 c, Point2D_F64 d, boolean copy ) {
		if( copy ) {
			this.a.set(a);
			this.b.set(b);
			this.c.set(c);
			this.d.set(d);
		} else {
			this.a = a;
			this.b = b;
			this.c = c;
			this.d = d;
		}
	}

	public Point2D_F64 getA() {
		return a;
	}

	public void setA(Point2D_F64 a) {
		this.a = a;
	}

	public Point2D_F64 getB() {
		return b;
	}

	public void setB(Point2D_F64 b) {
		this.b = b;
	}

	public Point2D_F64 getC() {
		return c;
	}

	public void setC(Point2D_F64 c) {
		this.c = c;
	}

	public Point2D_F64 getD() {
		return d;
	}

	public void setD(Point2D_F64 d) {
		this.d = d;
	}

	public void set(Quadrilateral_F64 quad) {
		this.a.set(quad.a);
		this.b.set(quad.b);
		this.c.set(quad.c);
		this.d.set(quad.d);
	}
}
