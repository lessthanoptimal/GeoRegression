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

import georegression.struct.point.Point3D_F64;

/**
 * An axis aligned cube in 3D that is specified by two points, p0 and p1.  Point p0 is less than point p1,
 * p0.x <= p1.x, p0.y <= p1.y, p0.z <= p1.z.
 */
public class Cube3D_F64 {

	/**
	 * The lesser point
	 */
	public Point3D_F64 p0 = new Point3D_F64();
	/**
	 * The greater point
	 */
	public Point3D_F64 p1 = new Point3D_F64();

	public Cube3D_F64(double x0, double y0, double z0, double x1, double y1, double z1 ) {
		this.p0.set(x0, y0, z0);
		this.p1.set(x1, y1, z1);
	}

	public Cube3D_F64(Cube3D_F64 orig) {
		set(orig);
	}

	public void set( Cube3D_F64 orig ) {
		set(orig.p0.x,orig.p0.y,orig.p0.z,orig.p1.x,orig.p1.y,orig.p1.z);
	}

	public Cube3D_F64() {
	}

	public void set(double x0, double y0, double z0, double x1, double y1, double z1 ) {
		this.p0.set(x0, y0, z0);
		this.p1.set(x1, y1, z1);
	}

	public double area() {
		return (p1.x-p0.x)*(p1.y-p0.y)*(p1.z-p0.z);
	}

	public Point3D_F64 getP0() {
		return p0;
	}

	public void setP1(Point3D_F64 p1) {
		this.p1.set(p1);
	}

	public Point3D_F64 getP1() {
		return p1;
	}

	public void setP0(Point3D_F64 p0) {
		this.p0.set(p0);
	}

	public String toString() {
		return getClass().getSimpleName()+"P1( "+ p0.x+" "+ p0.y+" "+ p0.z+" ) P1( "+ p0.x+" "+ p0.y+" "+ p0.z+" )";
	}
}
