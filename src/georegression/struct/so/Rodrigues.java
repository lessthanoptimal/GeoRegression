/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Geometric Regression Library (GeoRegression).
 *
 * GeoRegression is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * JGRL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with GeoRegression.  If not, see <http://www.gnu.org/licenses/>.
 */

package georegression.struct.so;

import georegression.struct.point.Vector3D_F64;


/**
 * <p>
 * Defines a 3D rotation based upon the axis of rotation and the angle of rotation in SO(3).
 * </p>
 *
 * <p>
 * R(&theta;) = e<sup>w*&theta</sup><br>
 * R(&theta;) = 1 + w*sin(&theta;) + w<sup>2</sup>*(1-cos(&theta;))<br>
 * where &theta; is the angle of rotation, 'w' is a skew symmetric matrix around the axis of rotation.
 * </p>
 *
 * @author Peter Abeles
 */
public class Rodrigues {
	// unit vector defining the axis of rotation
	public Vector3D_F64 unitAxisRotation = new Vector3D_F64();
	// the angle it is rotated by
	public double theta;

	public Rodrigues() {
	}

	public Rodrigues( double theta, Vector3D_F64 unitAxisRotation) {
		this.theta = theta;
		this.unitAxisRotation.set(unitAxisRotation);
	}

	public Rodrigues( double theta, double x, double y, double z ) {
		this.theta = theta;
		this.unitAxisRotation.set( x, y, z );
		unitAxisRotation.normalize();
	}

	public Vector3D_F64 getUnitAxisRotation() {
		return unitAxisRotation;
	}

	public void setUnitAxisRotation(Vector3D_F64 unitAxisRotation) {
		this.unitAxisRotation.set(unitAxisRotation);
	}

	public double getTheta() {
		return theta;
	}

	public void setTheta( double theta ) {
		this.theta = theta;
	}
}
