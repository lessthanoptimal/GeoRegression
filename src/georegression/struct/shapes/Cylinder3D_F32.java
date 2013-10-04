/*
 * Copyright (c) 2011-2013, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * GeoRegression is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.struct.shapes;

import georegression.struct.line.LineParametric3D_F32;
import georegression.struct.point.Point3D_F32;
import georegression.struct.point.Vector3D_F32;

/**
 * Defines a cylinder in 3D space using a point and vector, which defines a line, and a radius around the line.
 *
 * @author Peter Abeles
 */
public class Cylinder3D_F32 {
	/**
	 * Line which defines the cylinder's axis
	 */
	public LineParametric3D_F32 line;
	/**
	 * Radius of the cylinder
	 */
	public float radius;

	public Cylinder3D_F32() {
		line = new LineParametric3D_F32();
	}

	public Cylinder3D_F32( float x_0, float y_0, float z_0,
						   float slopeX, float slopeY, float slopeZ,
						   float radius ) {
		this();
		this.line.set(x_0,y_0,z_0,slopeX,slopeY,slopeZ);
		this.radius = radius;
	}

	public Cylinder3D_F32(LineParametric3D_F32 line , float radius) {
		this();
		set(line,radius);
	}

	public Cylinder3D_F32(Cylinder3D_F32 o) {
		this();
		set(o);
	}

	public Cylinder3D_F32( boolean declare ) {
		if( declare )
			line = new LineParametric3D_F32();
	}

	public void set( float x_0, float y_0, float z_0,
					 float slopeX, float slopeY, float slopeZ,
					 float radius ) {
		this.line.set(x_0,y_0,z_0,slopeX,slopeY,slopeZ);
		this.radius = radius;
	}

	public void set( LineParametric3D_F32 line , float radius ) {
		this.line.set(line);
		this.radius = radius;
	}

	public void set( Cylinder3D_F32 o ) {
		this.line.set(o.line);
		this.radius = o.radius;
	}


	public String toString() {
		Point3D_F32 p = line.p;
		Vector3D_F32 slope = line.slope;

		return getClass().getSimpleName()+" P( "+p.x+" "+p.y+" "+p.z+" ) Slope( "+slope.x+" "+slope.y+" "+slope.z+" ) radius "+radius;
	}
}
