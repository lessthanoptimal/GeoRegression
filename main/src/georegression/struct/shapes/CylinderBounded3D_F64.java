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

import java.io.Serializable;

/**
 * Specifies a Cylinder in 3D space which is bounded along the axis. The axis line is specified using two points,
 * where the points are its end points.
 *
 * @author Peter Abeles
 */
@SuppressWarnings("NullAway.Init")
@Getter @Setter
public class CylinderBounded3D_F64 implements Serializable {
	/** End points of the cylinder */
	public Point3D_F64 endA, endB;

	/** Radius of the cylinder */
	public double radius;

	public CylinderBounded3D_F64() {
		endA = new Point3D_F64();
		endB = new Point3D_F64();
	}

	public CylinderBounded3D_F64( double x_0, double y_0, double z_0,
								  double x_1, double y_1, double z_1,
								  double radius ) {
		this();
		this.endA.setTo(x_0, y_0, z_0);
		this.endB.setTo(x_1, y_1, z_1);
		this.radius = radius;
	}

	public CylinderBounded3D_F64( Point3D_F64 endA, Point3D_F64 endB, double radius ) {
		this();
		setTo(endA, endB, radius);
	}

	public CylinderBounded3D_F64( CylinderBounded3D_F64 o ) {
		this();
		setTo(o);
	}

	/**
	 * Constructor which makes declaration of the two end points optional
	 *
	 * @param declare if true the end points will be declared, otherwise they will be initalized to null.
	 */
	public CylinderBounded3D_F64( boolean declare ) {
		if (declare) {
			endA = new Point3D_F64();
			endB = new Point3D_F64();
		}
	}

	public void setTo( double x_0, double y_0, double z_0,
					   double x_1, double y_1, double z_1,
					   double radius ) {
		this.endA.setTo(x_0, y_0, z_0);
		this.endB.setTo(x_1, y_1, z_1);
		this.radius = radius;
	}

	public void setTo( Point3D_F64 endA, Point3D_F64 endB, double radius ) {
		this.endA.setTo(endA);
		this.endB.setTo(endB);
		this.radius = radius;
	}

	public void setTo( CylinderBounded3D_F64 o ) {
		this.endA.setTo(o.endA);
		this.endB.setTo(o.endB);
		this.radius = o.radius;
	}

	public void zero() {
		endA.setTo(0, 0, 0);
		endB.setTo(0, 0, 0);
		radius = 0;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " A( " + endA.x + " " + endA.y + " " + endA.z + " ) B( " + endB.x + " " + endB.y + " " + endB.z + " ) radius " + radius;
	}
}
