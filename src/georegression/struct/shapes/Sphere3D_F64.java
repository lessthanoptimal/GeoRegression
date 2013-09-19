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
 * Defines a sphere in 3D space using a center point and radius.
 *
 * @author Peter Abeles
 */
public class Sphere3D_F64 {
	/**
	 * Center point of the sphere
	 */
	public Point3D_F64 center;
	/**
	 * Radius of the sphere
	 */
	public double radius;

	public Sphere3D_F64() {
		center = new Point3D_F64();
	}

	public Sphere3D_F64( double x , double y , double z , double radius ) {
		this();
		set(x,y,z,radius);
	}

	public Sphere3D_F64( Sphere3D_F64 o ) {
		this();
		set(o);
	}

	public void set( double x , double y , double z , double radius ) {
		this.center.x = x;
		this.center.y = y;
		this.center.z = z;
		this.radius = radius;
	}

	public void set( Sphere3D_F64 o ) {
		this.center.set(o.center);
		this.radius = o.radius;
	}

	public void setCenter(Point3D_F64 center) {
		this.center = center;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public Point3D_F64 getCenter() {
		return center;
	}

	public double getRadius() {
		return radius;
	}

	public String toString() {
		return getClass().getSimpleName()+" Center( "+center.x+" "+center.y+" "+center.z+" ) radius "+radius+" )";
	}
}
