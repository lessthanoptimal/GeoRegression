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

package georegression.struct.plane;

import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;

/**
 * Defines a plane using a point on the plane and the plane's normal.  N*(x-p0) = 0, where N is the plane's
 * normal, p0 is a point on the plane, and 'x' is another point on the plane.
 *
 * @author Peter Abeles
 */
public class PlaneNormal3D_F32 {
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
}
