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

package georegression.struct.plane;

import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;

import java.io.Serializable;

/**
 * Defines a plane using a point on the plane and the plane's normal.  N*(x-p0) = 0, where N is the plane's
 * normal, p0 is a point on the plane, and 'x' is another point on the plane.
 *
 * @author Peter Abeles
 */
public class PlaneNormal3D_F32 implements Serializable {
	/** An arbitrary point in the plane */
	public Point3D_F32 p = new Point3D_F32();
	/** The plane's normal */
	public Vector3D_F32 n = new Vector3D_F32();

	public PlaneNormal3D_F32(PlaneNormal3D_F32 o) {
		set(o);
	}

	public PlaneNormal3D_F32(Point3D_F32 point, Vector3D_F32 normal) {
		set(point,normal);
	}

	public PlaneNormal3D_F32( float px , float py , float pz , float nx , float ny, float nz ) {
		set(px,py,pz,nx,ny,nz);
	}

	public PlaneNormal3D_F32() {
	}

	public Point3D_F32 getP() {
		return p;
	}

	public void set( float px , float py , float pz , float nx , float ny, float nz ) {
		this.p.set(px,py,pz);
		this.n.set(nx,ny,nz);
	}

	public void set( Point3D_F32 point, Vector3D_F32 normal ) {
		this.p.set(point);
		this.n.set(normal);
	}

	public void set( PlaneNormal3D_F32 o ) {
		this.p.set(o.p);
		this.n.set(o.n);
	}

	public void setP(Point3D_F32 p) {
		this.p.set(p);
	}

	public Vector3D_F32 getN() {
		return n;
	}

	public void setN(Vector3D_F32 n) {
		this.n.set(n);
	}

	public String toString() {
		return getClass().getSimpleName()+"[ p( "+p.x+" "+p.y+" "+p.z+" ) , n( "+n.x+" "+n.y+" "+n.z+" ) ]";
	}
}
