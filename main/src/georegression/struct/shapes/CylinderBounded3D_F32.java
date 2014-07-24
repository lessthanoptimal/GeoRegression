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
 * Specifies a Cylinder in 3D space which is bounded along the axis.  The axis line is specified using two points,
 * where the points are its end points.
 *
 * @author Peter Abeles
 */
public class CylinderBounded3D_F32 implements Serializable {
	/**
	 * End points of the cylinder
	 */
	public Point3D_F32 endA, endB;

	/**
	 * Radius of the cylinder
	 */
	public float radius;

	public CylinderBounded3D_F32() {
		endA = new Point3D_F32();
		endB = new Point3D_F32();
	}

	public CylinderBounded3D_F32(float x_0, float y_0, float z_0,
								 float x_1, float y_1, float z_1,
								 float radius) {
		this();
		this.endA.set(x_0, y_0, z_0);
		this.endB.set(x_1, y_1, z_1);
		this.radius = radius;
	}

	public CylinderBounded3D_F32(Point3D_F32 endA, Point3D_F32 endB, float radius) {
		this();
		set(endA, endB,radius);
	}

	public CylinderBounded3D_F32(CylinderBounded3D_F32 o) {
		this();
		set(o);
	}

	/**
	 * Constructor which makes declaration of the two end points optional
	 *
	 * @param declare if true the end points will be declared, otherwise they will be initalized to null.
	 */
	public CylinderBounded3D_F32(boolean declare) {
		if( declare ) {
			endA = new Point3D_F32();
			endB = new Point3D_F32();
		}
	}

	public void set( float x_0, float y_0, float z_0,
					 float x_1, float y_1, float z_1,
					 float radius ) {
		this.endA.set(x_0, y_0, z_0);
		this.endB.set(x_1, y_1, z_1);
		this.radius = radius;
	}

	public void set( Point3D_F32 endA , Point3D_F32 endB , float radius ) {
		this.endA.set(endA);
		this.endB.set(endB);
		this.radius = radius;
	}

	public void set( CylinderBounded3D_F32 o ) {
		this.endA.set(o.endA);
		this.endB.set(o.endB);
		this.radius = o.radius;
	}

	public Point3D_F32 getEndA() {
		return endA;
	}

	public Point3D_F32 getEndB() {
		return endB;
	}

	public float getRadius() {
		return radius;
	}

	public String toString() {
		return getClass().getSimpleName()+" A( "+ endA.x+" "+ endA.y+" "+ endA.z+" ) B( "+ endB.x+" "+ endB.y+" "+ endB.z+" ) radius "+radius;
	}
}
