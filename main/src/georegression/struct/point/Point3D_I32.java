/*
 * Copyright (C) 2011-2015, Peter Abeles. All Rights Reserved.
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

import georegression.struct.GeoTuple;

/**
 * Point in 3D with integer values.
 *
 */
@SuppressWarnings({"unchecked"})
public class Point3D_I32 extends GeoTuple<Point3D_I32> {

	public int x;
	public int y;
	public int z;

	public Point3D_I32(Point3D_I32 pt) {
		this(pt.x,pt.y,pt.z);
	}

	public Point3D_I32(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point3D_I32() {
	}


	@Override
	public Point3D_I32 copy() {
		return new Point3D_I32( x, y, z );
	}

	public void set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void set( Point3D_I32 p ) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}

	public boolean isIdentical( Point3D_I32 p ) {
		return x == p.x && y == p.y && z == p.z;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
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
}
