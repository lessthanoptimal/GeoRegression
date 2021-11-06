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

import georegression.struct.point.Point3D_I32;
import lombok.Getter;

import java.io.Serializable;

/**
 * An axis aligned box in 3D that is specified by two points, p0 and p1, the lower and upper extents of the box.
 * Point p0 is less or equal to point p1, 0.x &le; p1.x, p0.y &le; p1.y, p0.z &le; p1.z.
 */
@Getter
public class Box3D_I32 implements Serializable {
	/** The lower point/extent. */
	public Point3D_I32 p0 = new Point3D_I32();

	/** The upper point/extent */
	public Point3D_I32 p1 = new Point3D_I32();

	public Box3D_I32( int x0, int y0, int z0, int x1, int y1, int z1 ) {
		this.p0.setTo(x0, y0, z0);
		this.p1.setTo(x1, y1, z1);
	}

	public Box3D_I32( Box3D_I32 orig ) {
		setTo(orig);
	}

	public void setTo( Box3D_I32 orig ) {
		setTo(orig.p0.x, orig.p0.y, orig.p0.z, orig.p1.x, orig.p1.y, orig.p1.z);
	}

	public Box3D_I32() {}

	public void setTo( int x0, int y0, int z0, int x1, int y1, int z1 ) {
		this.p0.setTo(x0, y0, z0);
		this.p1.setTo(x1, y1, z1);
	}

	public void zero() {
		setTo(0, 0, 0, 0, 0, 0);
	}

	/**
	 * The box's area. area = lengthX*lengthY*lengthZ
	 *
	 * @return area
	 */
	public int area() {
		return (p1.x - p0.x)*(p1.y - p0.y)*(p1.z - p0.z);
	}

	/**
	 * Length of the box along the x-axis
	 *
	 * @return length
	 */
	public int getLengthX() {
		return p1.x - p0.x;
	}

	/**
	 * Length of the box along the y-axis
	 *
	 * @return length
	 */
	public int getLengthY() {
		return p1.y - p0.y;
	}

	/**
	 * Length of the box along the z-axis
	 *
	 * @return length
	 */
	public int getLengthZ() {
		return p1.z - p0.z;
	}

	public void setP1( Point3D_I32 p1 ) {
		this.p1.setTo(p1);
	}

	public void setP0( Point3D_I32 p0 ) {
		this.p0.setTo(p0);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{ P0( " + p0.x + " " + p0.y + " " + p0.z + " ) P1( " + p1.x + " " + p1.y + " " + p1.z + " ) }";
	}
}
