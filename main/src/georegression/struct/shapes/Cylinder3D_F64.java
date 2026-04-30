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

package georegression.struct.shapes;

import georegression.struct.line.LineParametric3D_F64;
import lombok.Getter;
import lombok.Setter;
import org.ejml.MapFormattable;
import org.ejml.MapPrintFormat;

import java.io.Serializable;

/**
 * Defines a cylinder in 3D space using a point and vector, which defines a line, and a radius around the line.
 *
 * @author Peter Abeles
 */
@SuppressWarnings("NullAway.Init")
@Getter @Setter
public class Cylinder3D_F64 implements Serializable, MapFormattable {
	/**
	 * Line which defines the cylinder's axis
	 */
	public LineParametric3D_F64 line;
	/**
	 * Radius of the cylinder
	 */
	public double radius;

	public Cylinder3D_F64() {
		line = new LineParametric3D_F64();
	}

	public Cylinder3D_F64( double x_0, double y_0, double z_0,
	                       double slopeX, double slopeY, double slopeZ,
	                       double radius ) {
		this();
		this.line.setTo(x_0, y_0, z_0, slopeX, slopeY, slopeZ);
		this.radius = radius;
	}

	public Cylinder3D_F64( LineParametric3D_F64 line, double radius ) {
		this();
		setTo(line, radius);
	}

	public Cylinder3D_F64( Cylinder3D_F64 o ) {
		this();
		setTo(o);
	}

	public Cylinder3D_F64( boolean declare ) {
		if (declare)
			line = new LineParametric3D_F64();
	}

	public Cylinder3D_F64 setTo( double x_0, double y_0, double z_0,
	                             double slopeX, double slopeY, double slopeZ,
	                             double radius ) {
		this.line.setTo(x_0, y_0, z_0, slopeX, slopeY, slopeZ);
		this.radius = radius;
		return this;
	}

	public Cylinder3D_F64 setTo( LineParametric3D_F64 line, double radius ) {
		this.line.setTo(line);
		this.radius = radius;
		return this;
	}

	public Cylinder3D_F64 setTo( Cylinder3D_F64 o ) {
		this.line.setTo(o.line);
		this.radius = o.radius;
		return this;
	}

	public void zero() {
		this.line.zero();
		this.radius = 0;
	}

	/**
	 * Returns true if every parameter is identical to the passed in Cylinder to within the specified tolerance
	 */
	public boolean isIdentical( Cylinder3D_F64 c, double tol ) {
		return Math.abs(radius - c.radius) <= tol && line.isIdentical(c.line, tol);
	}

	@Override
	public boolean equals( Object obj ) {
		if (this == obj)
			return true;

		if (!(obj instanceof Cylinder3D_F64))
			return false;

		return ((Cylinder3D_F64)obj).isIdentical(this, 0.0);
	}

	@Override public String formatMap( MapPrintFormat format ) {
		return format.itemPrefix +
				format.pair("line", line.formatMap(format), true) +
				format.pair("radius", radius, false) +
				format.itemSuffix;
	}

	@Override public String toString() { return MapPrintFormat.DEFAULT.toString(this); }

	@Override
	public int hashCode() {
		return Double.hashCode(radius) + line.hashCode();
	}
}
