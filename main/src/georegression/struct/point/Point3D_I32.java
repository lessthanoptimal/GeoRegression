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

package georegression.struct.point;

import georegression.struct.GeoTuple_I32;
import lombok.Getter;
import lombok.Setter;

/**
 * Point in 3D with integer values.
 */
@SuppressWarnings({"unchecked"})
@Getter @Setter
public class Point3D_I32 extends GeoTuple_I32<Point3D_I32> {

	public int x;
	public int y;
	public int z;

	public Point3D_I32( Point3D_I32 pt ) {
		this(pt.x, pt.y, pt.z);
	}

	public Point3D_I32( int x, int y, int z ) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point3D_I32() {}

	@Override
	public Point3D_I32 copy() {
		return new Point3D_I32(x, y, z);
	}

	@Override
	public int getIdx( int index ) {
		if (index == 0)
			return x;
		else if (index == 1)
			return y;
		else if (index == 2)
			return z;
		throw new RuntimeException("Invalid index " + index);
	}

	@Override
	public void setIdx( int index, int value ) {
		if (index == 0)
			this.x = value;
		else if (index == 1)
			this.y = value;
		else if (index == 2)
			this.z = value;
		else
			throw new RuntimeException("Invalid index " + index);
	}

	public void setTo( int x, int y, int z ) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void setTo( Point3D_I32 p ) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}

	public void zero() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public boolean isIdentical( Point3D_I32 p ) {
		return x == p.x && y == p.y && z == p.z;
	}

	@Override
	public String toString() {
		return "P( " + x + " " + y + " " + z + " )";
	}

	@Override
	public int getDimension() {
		return 3;
	}

	@Override
	public Point3D_I32 createNewInstance() {
		return new Point3D_I32();
	}

	@Override
	public boolean equals( Object obj ) {
		if (this == obj) return true;
		if (!(obj instanceof Point3D_I32)) return false;
		Point3D_I32 p = (Point3D_I32)obj;
		return x == p.x && y == p.y && z == p.z;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(x + y + z);
	}
}
