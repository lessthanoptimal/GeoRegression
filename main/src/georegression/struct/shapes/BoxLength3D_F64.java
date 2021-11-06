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
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * An axis aligned box in 3D that is specified by a point p=(x0,y0,z0), and its lengthX, lengthY, and lengthZ.
 * The point 'p' has the lowest values and is also known as the lower extent..  The corner which is farthest away from 'p'
 * is (x0+lengthX , y0+lengthY, z0+lengthZ), the upper extent.
 */
@Getter @Setter
public class BoxLength3D_F64 implements Serializable {
	/** Point on the box with the lowest values. The lower extent. */
	public Point3D_F64 p = new Point3D_F64();

	/** The length of each size along their respective axes */
	public double lengthX, lengthY, lengthZ;

	public BoxLength3D_F64( double x0, double y0, double z0, double lengthX, double lengthY, double lengthZ ) {
		this.p.setTo(x0, y0, z0);
		this.lengthX = lengthX;
		this.lengthY = lengthY;
		this.lengthZ = lengthZ;
	}

	public BoxLength3D_F64( BoxLength3D_F64 orig ) {
		setTo(orig);
	}

	public BoxLength3D_F64() {}

	public void setTo( BoxLength3D_F64 orig ) {
		setTo(orig.p.x, orig.p.y, orig.p.z, orig.lengthX, orig.lengthY, orig.lengthZ);
	}

	public void setTo( double x0, double y0, double z0, double lengthX, double lengthY, double lengthZ ) {
		this.p.setTo(x0, y0, z0);
		this.lengthX = lengthX;
		this.lengthY = lengthY;
		this.lengthZ = lengthZ;
	}

	public void zero() {
		setTo(0, 0, 0, 0, 0, 0);
	}

	public double area() {
		return lengthX*lengthY*lengthZ;
	}

	public void setP( Point3D_F64 p ) {
		this.p.setTo(p);
	}

	/**
	 * Used to retrieve the corners of the box.
	 *
	 * @param index A value from 0 to 7
	 * @param corner (Optional) storage for the corner. If null a new point will be declared
	 * @return The corner.
	 */
	public Point3D_F64 getCorner( int index, @Nullable Point3D_F64 corner ) {
		if (corner == null)
			corner = new Point3D_F64();

		corner.setTo(p);

		if ((index & 0x01) != 0) {
			corner.x += lengthX;
		}
		if ((index & 0x02) != 0) {
			corner.y += lengthY;
		}
		if ((index & 0x04) != 0) {
			corner.z += lengthZ;
		}
		return corner;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "P( " + p.x + " " + p.y + " " + p.z + " ) sides ( " + lengthX + " , " + lengthY + " , " + lengthZ + " )";
	}
}
