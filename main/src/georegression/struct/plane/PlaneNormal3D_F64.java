/*
 * Copyright (C) 2026, Peter Abeles. All Rights Reserved.
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

import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import lombok.Getter;
import org.ejml.MapPrintFormat;
import org.ejml.MatrixPrintFormat;

import java.io.Serializable;
import java.util.Objects;

/**
 * Defines a plane using a point on the plane and the plane's normal. N*(x-p0) = 0, where N is the plane's
 * normal, p0 is a point on the plane, and 'x' is another point on the plane.
 *
 * @author Peter Abeles
 */
@Getter
public class PlaneNormal3D_F64 implements Serializable {
	/** An arbitrary point in the plane */
	public Point3D_F64 p = new Point3D_F64();
	/** The plane's normal */
	public Vector3D_F64 n = new Vector3D_F64();

	public PlaneNormal3D_F64( PlaneNormal3D_F64 o ) {
		setTo(o);
	}

	public PlaneNormal3D_F64( Point3D_F64 point, Vector3D_F64 normal ) {
		setTo(point, normal);
	}

	public PlaneNormal3D_F64( double px, double py, double pz, double nx, double ny, double nz ) {
		setTo(px, py, pz, nx, ny, nz);
	}

	public PlaneNormal3D_F64() {}

	public PlaneNormal3D_F64 setTo( double px, double py, double pz, double nx, double ny, double nz ) {
		this.p.setTo(px, py, pz);
		this.n.setTo(nx, ny, nz);
		return this;
	}

	public PlaneNormal3D_F64 setTo( Point3D_F64 point, Vector3D_F64 normal ) {
		this.p.setTo(point);
		this.n.setTo(normal);
		return this;
	}

	public PlaneNormal3D_F64 setTo( PlaneNormal3D_F64 o ) {
		this.p.setTo(o.p);
		this.n.setTo(o.n);
		return this;
	}

	public void zero() {
		p.zero();
		n.zero();
	}

	public void setP( Point3D_F64 p ) {
		this.p.setTo(p);
	}

	public void setN( Vector3D_F64 n ) {
		this.n.setTo(n);
	}

	/// Converts into a [String] using a Matrix like format.
	public String format( MatrixPrintFormat format ) {
		return format.prefix + p.format(format) + format.rowSeparator + n.format(format) + format.suffix;
	}

	/// Converts into a [String] using a Map like format.
	public String format( MapPrintFormat format ) {
		return format.itemPrefix +
				format.pair("p", p.format(format), true) +
				format.pair("n", n.format(format), false) +
				format.itemSuffix;
	}

	@Override public String toString() {
		return getClass().getSimpleName() + format(MapPrintFormat.DEFAULT);
	}

	@Override public boolean equals( Object o ) {
		if (this == o) return true;
		if (!(o instanceof PlaneNormal3D_F64 that)) return false;
		return p.equals(that.p) && n.equals(that.n);
	}

	@Override public int hashCode() {
		return Objects.hash(p, n);
	}
}
