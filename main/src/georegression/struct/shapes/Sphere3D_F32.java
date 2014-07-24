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
 * Defines a sphere in 3D space using a center point and radius.
 *
 * @author Peter Abeles
 */
public class Sphere3D_F32 implements Serializable {
	/**
	 * Center point of the sphere
	 */
	public Point3D_F32 center;
	/**
	 * Radius of the sphere
	 */
	public float radius;

	public Sphere3D_F32() {
		center = new Point3D_F32();
	}

	public Sphere3D_F32( float x , float y , float z , float radius ) {
		this();
		set(x,y,z,radius);
	}

	public Sphere3D_F32( Sphere3D_F32 o ) {
		this();
		set(o);
	}

	public void set( float x , float y , float z , float radius ) {
		this.center.x = x;
		this.center.y = y;
		this.center.z = z;
		this.radius = radius;
	}

	public void set( Sphere3D_F32 o ) {
		this.center.set(o.center);
		this.radius = o.radius;
	}

	public void setCenter(Point3D_F32 center) {
		this.center = center;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public Point3D_F32 getCenter() {
		return center;
	}

	public float getRadius() {
		return radius;
	}

	public String toString() {
		return getClass().getSimpleName()+" Center( "+center.x+" "+center.y+" "+center.z+" ) radius "+radius+" )";
	}
}
