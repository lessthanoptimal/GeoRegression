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

import georegression.struct.point.Point3D_F32;

import java.io.Serializable;

/**
 * An axis aligned cube in 3D that is specified by two points, p0 and p1.  Point p0 is less than point p1,
 * p0.x <= p1.x, p0.y <= p1.y, p0.z <= p1.z.
 */
public class Cube3D_F32 implements Serializable {

	/**
	 * The lesser point
	 */
	public Point3D_F32 p0 = new Point3D_F32();
	/**
	 * The greater point
	 */
	public Point3D_F32 p1 = new Point3D_F32();

	public Cube3D_F32(float x0, float y0, float z0, float x1, float y1, float z1 ) {
		this.p0.set(x0, y0, z0);
		this.p1.set(x1, y1, z1);
	}

	public Cube3D_F32(Cube3D_F32 orig) {
		set(orig);
	}

	public void set( Cube3D_F32 orig ) {
		set(orig.p0.x,orig.p0.y,orig.p0.z,orig.p1.x,orig.p1.y,orig.p1.z);
	}

	public Cube3D_F32() {
	}

	public void set(float x0, float y0, float z0, float x1, float y1, float z1 ) {
		this.p0.set(x0, y0, z0);
		this.p1.set(x1, y1, z1);
	}

	public float area() {
		return (p1.x-p0.x)*(p1.y-p0.y)*(p1.z-p0.z);
	}

	public float getLengthX() {
		return p1.x-p0.x;
	}

	public float getLengthY() {
		return p1.y-p0.y;
	}

	public float getLengthZ() {
		return p1.z-p0.z;
	}

	public Point3D_F32 getP0() {
		return p0;
	}

	public void setP1(Point3D_F32 p1) {
		this.p1.set(p1);
	}

	public Point3D_F32 getP1() {
		return p1;
	}

	public void setP0(Point3D_F32 p0) {
		this.p0.set(p0);
	}

	public String toString() {
		return getClass().getSimpleName()+"P1( "+ p0.x+" "+ p0.y+" "+ p0.z+" ) P1( "+ p0.x+" "+ p0.y+" "+ p0.z+" )";
	}
}
