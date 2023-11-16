/*
 * Copyright (C) 2022, Peter Abeles. All Rights Reserved.
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

import org.ejml.UtilEjml;
import org.ejml.ops.MatrixIO;

import java.text.DecimalFormat;

/**
 * Point and a normal vector. Typically used to define a region on an object's surface.
 */
public class PointNormal3D_F64 {
	/** Point in 3D space */
	public Point3D_F64 p = new Point3D_F64();

	/** Norm of the surface at this point */
	public Vector3D_F64 n = new Vector3D_F64();

	public PointNormal3D_F64 setTo( PointNormal3D_F64 src ) {
		this.p.setTo(src.p);
		this.n.setTo(src.n);
		return this;
	}

	public PointNormal3D_F64 setTo( Point3D_F64 point, Vector3D_F64 norm ) {
		this.p.setTo(point);
		this.n.setTo(norm);
		return this;
	}

	public PointNormal3D_F64 setTo( double px, double py, double pz, double nx, double ny, double nz ) {
		this.p.setTo(px, py, pz);
		this.n.setTo(nx, ny, nz);
		return this;
	}

	public void zero() {
		p.zero();
		n.zero();
	}

	@Override
	public String toString() {
		DecimalFormat format = new DecimalFormat("#");
		String sx = UtilEjml.fancyString(p.x, format, MatrixIO.DEFAULT_LENGTH, 4);
		String sy = UtilEjml.fancyString(p.y, format, MatrixIO.DEFAULT_LENGTH, 4);
		String sz = UtilEjml.fancyString(p.z, format, MatrixIO.DEFAULT_LENGTH, 4);

		String nx = UtilEjml.fancyString(n.x, format, MatrixIO.DEFAULT_LENGTH, 4);
		String ny = UtilEjml.fancyString(n.y, format, MatrixIO.DEFAULT_LENGTH, 4);
		String nz = UtilEjml.fancyString(n.z, format, MatrixIO.DEFAULT_LENGTH, 4);

		return getClass().getSimpleName() + "{ P(" + sx + " , " + sy + " , " + sz + " ), V( " + nx + " , " + ny + " , " + nz + ") }";
	}
}
