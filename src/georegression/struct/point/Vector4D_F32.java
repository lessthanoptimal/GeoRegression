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

package georegression.struct.point;

import georegression.struct.GeoTuple4D_F32;

/**
 *
 *
 */
public class Vector4D_F32 extends GeoTuple4D_F32 {

	public Vector4D_F32(float x, float y, float z , float w ) {
		super( x, y, z , w );
	}

	public Vector4D_F32() {
	}

	public Vector4D_F32(Point4D_F32 a, Point4D_F32 b) {
		x = b.getX() - a.getX();
		y = b.getY() - a.getY();
		z = b.getZ() - a.getZ();
		w = b.getW() - a.getW();
	}

	public void set( Vector4D_F32 v ) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = v.w;
	}

	public Vector4D_F32 times( float scalar ) {
		return new Vector4D_F32( x * scalar, y * scalar, z * scalar , w * scalar );
	}

	public Vector4D_F32 copy() {
		return new Vector4D_F32( x, y, z , w );
	}

	@Override
	public Vector4D_F32 createNewInstance() {
		return new Vector4D_F32();
	}

	@Override
	public String toString() {
		return "V( " + x + " " + y + " " + z + " " + w + " )";
	}

	public void normalize() {
		float r = norm();
		x /= r;
		y /= r;
		z /= r;
		w /= r;
	}

	public float dot( Vector4D_F32 a ) {
		return x * a.x + y * a.y + z * a.z + w * a.w;
	}
}
