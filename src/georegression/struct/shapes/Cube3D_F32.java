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

import georegression.struct.point.Point3D_F32;

/**
 * An axis aligned cube in 3D that is specified by a point (x0,y0,z0), and its lengthX, lengthY, and lengthZ.
 * The point 'p' is the point which is closest to the corner.  The corner which is farthest away from 'p'
 * is (x0+lengthX , y0+lengthY, z0+lengthZ)
 */
public class Cube3D_F32 {
	/**
	 * Point which defines the corner closest to the origin
	 */
	public Point3D_F32 p = new Point3D_F32();
	/**
	 * The length of each size along their respective axises
	 */
	public float lengthX,lengthY,lengthZ;

	public Cube3D_F32(float x0, float y0, float z0, float lengthX, float lengthY, float lengthZ) {
		this.p.set(x0,y0,z0);
		this.lengthX = lengthX;
		this.lengthY = lengthY;
		this.lengthZ = lengthZ;
	}

	public Cube3D_F32(Cube3D_F32 orig) {
		set(orig);
	}

	public void set( Cube3D_F32 orig ) {
		set(orig.p.x,orig.p.y,orig.p.z,orig.lengthX,orig.lengthY,orig.lengthZ);
	}

	public Cube3D_F32() {
	}

	public void set(float x0, float y0, float z0, float lengthX, float lengthY, float lengthZ) {
		this.p.set(x0,y0,z0);
		this.lengthX = lengthX;
		this.lengthY = lengthY;
		this.lengthZ = lengthZ;
	}

	public float area() {
		return lengthX*lengthY*lengthZ;
	}

	public Point3D_F32 getP() {
		return p;
	}

	public float getLengthX() {
		return lengthX;
	}

	public float getLengthY() {
		return lengthY;
	}

	public float getLengthZ() {
		return lengthZ;
	}

	public void setP(Point3D_F32 p) {
		this.p.set(p);
	}

	public void setLengthX(float lengthX) {
		this.lengthX = lengthX;
	}

	public void setLengthY(float lengthY) {
		this.lengthY = lengthY;
	}

	public void setLengthZ(float lengthZ) {
		this.lengthZ = lengthZ;
	}

	public String toString() {
		return getClass().getSimpleName()+"P( "+p.x+" "+p.y+" "+p.z+" ) sides ( "+lengthX+" , "+lengthY+" , "+lengthZ+" )";
	}
}
