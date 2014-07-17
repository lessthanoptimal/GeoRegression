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
 * Triangle in 3D space.  Described by 3 vertices/points.
 *
 * @author Peter Abeles
 */
public class Triangle3D_F32 implements Serializable {
	public Point3D_F32 v0 = new Point3D_F32();
	public Point3D_F32 v1 = new Point3D_F32();
	public Point3D_F32 v2 = new Point3D_F32();

	public Triangle3D_F32(Point3D_F32 v0, Point3D_F32 v1, Point3D_F32 v2) {
		this.v0.set(v0);
		this.v1.set(v1);
		this.v2.set(v2);
	}

	public Triangle3D_F32(float x0, float y0, float z0,
						  float x1, float y1, float z1,
						  float x2, float y2, float z2) {
		this.v0.set(x0,y0,z0);
		this.v1.set(x1,y1,z1);
		this.v2.set(x2,y2,z2);
	}

	public Triangle3D_F32( Triangle3D_F32 orig ) {
		set(orig);
	}

	public Triangle3D_F32() {
	}

	public void set( Triangle3D_F32 orig ) {
		v0.set(orig.v0);
		v1.set(orig.v1);
		v2.set(orig.v2);
	}

	public void set(float x0, float y0, float z0,
					float x1, float y1, float z1,
					float x2, float y2, float z2) {
		this.v0.set(x0,y0,z0);
		this.v1.set(x1,y1,z1);
		this.v2.set(x2,y2,z2);
	}

	public Point3D_F32 getV0() {
		return v0;
	}

	public void setV0(Point3D_F32 v0) {
		this.v0 = v0;
	}

	public Point3D_F32 getV1() {
		return v1;
	}

	public void setV1(Point3D_F32 v1) {
		this.v1 = v1;
	}

	public Point3D_F32 getV2() {
		return v2;
	}

	public void setV2(Point3D_F32 v2) {
		this.v2 = v2;
	}
}
