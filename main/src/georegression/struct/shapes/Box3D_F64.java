/*
 * Copyright (C) 2021, Peter Abeles. All Rights Reserved.
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

import georegression.struct.point.Point3D_F64;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * An axis aligned box in 3D that is specified by two points, p0 and p1, the lower and upper extents of the box.
 * Point p0 is less or equal to point p1, 0.x &le; p1.x, p0.y &le; p1.y, p0.z &le; p1.z.
 */
@Getter
public class Box3D_F64 implements Serializable {
	/** The lower point/extent. */
	public Point3D_F64 p0 = new Point3D_F64();

	/** The upper point/extent */
	public Point3D_F64 p1 = new Point3D_F64();

	public Box3D_F64( double x0, double y0, double z0, double x1, double y1, double z1 ) {
		this.p0.setTo(x0, y0, z0);
		this.p1.setTo(x1, y1, z1);
	}

	public Box3D_F64( Box3D_F64 orig ) {
		setTo(orig);
	}

	public Box3D_F64() {}

	public void setTo( Box3D_F64 orig ) {
		setTo(orig.p0.x, orig.p0.y, orig.p0.z, orig.p1.x, orig.p1.y, orig.p1.z);
	}

	public void setTo( double x0, double y0, double z0, double x1, double y1, double z1 ) {
		this.p0.setTo(x0, y0, z0);
		this.p1.setTo(x1, y1, z1);
	}

	public void zero() {
		setTo(0, 0, 0, 0, 0, 0);
	}

	/**
	 * Returns true if all the extents are within tolerance.
	 *
	 * @param r Box
	 * @param tol tolerance for equality. To be equal the difference must be less than or equal to this
	 * @return true if equal or false if not
	 */
	public boolean isEquals( Box3D_F64 r, double tol ) {
		return isEquals(r.p0.x, r.p0.y, r.p0.z, r.p1.x, r.p1.y, r.p1.z, tol);
	}

	/**
	 * Returns true if all the extents are within tolerance.
	 *
	 * @param x0 lower extent. x-axis
	 * @param y0 lower extent. y-axis
	 * @param x1 upper extent. x-axis
	 * @param y1 upper extent. y-axis
	 * @param tol tolerance for equality. To be equal the difference must be less than or equal to this
	 * @return true if equal or false if not
	 */
	public boolean isEquals( double x0, double y0, double z0,
							 double x1, double y1, double z1, double tol ) {
		if (Math.abs(x0 - this.p0.x) > tol)
			return false;
		if (Math.abs(y0 - this.p0.y) > tol)
			return false;
		if (Math.abs(z0 - this.p0.z) > tol)
			return false;
		if (Math.abs(x1 - this.p1.x) > tol)
			return false;
		if (Math.abs(y1 - this.p1.y) > tol)
			return false;
		return !(Math.abs(z1 - this.p1.z) > tol);
	}

	/**
	 * The box's area. area = lengthX*lengthY*lengthZ
	 *
	 * @return area
	 */
	public double area() {
		return (p1.x - p0.x)*(p1.y - p0.y)*(p1.z - p0.z);
	}

	/**
	 * Length of the box along the x-axis
	 *
	 * @return length
	 */
	public double getLengthX() {
		return p1.x - p0.x;
	}

	/**
	 * Length of the box along the y-axis
	 *
	 * @return length
	 */
	public double getLengthY() {
		return p1.y - p0.y;
	}

	/**
	 * Length of the box along the z-axis
	 *
	 * @return length
	 */
	public double getLengthZ() {
		return p1.z - p0.z;
	}

	public void setP1( Point3D_F64 p1 ) {
		this.p1.setTo(p1);
	}

	public void setP0( Point3D_F64 p0 ) {
		this.p0.setTo(p0);
	}

	/**
	 * Computes and return the center of the cube.
	 *
	 * @param storage Optional storage for the center. If null a new instance will be created and returned.
	 * @return The cube's center point
	 */
	public Point3D_F64 center( @Nullable Point3D_F64 storage ) {
		if (storage == null)
			storage = new Point3D_F64();
		storage.x = (p0.x + p1.x)/2.0;
		storage.y = (p0.y + p1.y)/2.0;
		storage.z = (p0.z + p1.z)/2.0;
		return storage;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{ P0( " + p0.x + " " + p0.y + " " + p0.z + " ) P1( " + p1.x + " " + p1.y + " " + p1.z + " ) }";
	}
}
