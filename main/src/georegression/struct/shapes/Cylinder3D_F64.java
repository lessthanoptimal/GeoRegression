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

import georegression.struct.line.LineParametric3D_F64;
import georegression.struct.point.Point3D_F64;
import georegression.struct.point.Vector3D_F64;
import org.ejml.FancyPrint;

import java.io.Serializable;

/**
 * Defines a cylinder in 3D space using a point and vector, which defines a line, and a radius around the line.
 *
 * @author Peter Abeles
 */
@SuppressWarnings("NullAway.Init")
public class Cylinder3D_F64 implements Serializable {
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

	public void setTo( double x_0, double y_0, double z_0,
					   double slopeX, double slopeY, double slopeZ,
					   double radius ) {
		this.line.setTo(x_0, y_0, z_0, slopeX, slopeY, slopeZ);
		this.radius = radius;
	}

	public void setTo( LineParametric3D_F64 line, double radius ) {
		this.line.setTo(line);
		this.radius = radius;
	}

	public void setTo( Cylinder3D_F64 o ) {
		this.line.setTo(o.line);
		this.radius = o.radius;
	}

	public void zero() {
		this.line.setTo(0, 0, 0, 0, 0, 0);
		this.radius = 0;
	}

	@Override
	public String toString() {
		FancyPrint fancy = new FancyPrint();

		Point3D_F64 p = line.p;
		Vector3D_F64 slope = line.slope;

		return getClass().getSimpleName() +
				" P( " + fancy.s(p.x) + " " + fancy.s(p.y) + " " + fancy.sf(p.z) +
				" ) Slope( " + fancy.s(slope.x) + " " + fancy.s(slope.y) + " " + fancy.s(slope.z) + " ) radius " + fancy.s(radius);
	}
}
