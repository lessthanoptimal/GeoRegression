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
 * Defines a 3D rotation based upon the axis of rotation ang the angle of rotation.
 *
 * @author Peter Abeles
 */
public class Rodrigues {
	// unit vector defining the axis of rotation
	public Vector3D_F64 unitAxisRoation = new Vector3D_F64();
	// the angle it is rotated by
	public double theta;

	public Rodrigues() {
	}

	public Rodrigues( double theta, Vector3D_F64 unitAxisRoation ) {
		this.theta = theta;
		this.unitAxisRoation.set( unitAxisRoation );
	}

	public Rodrigues( double theta, double x, double y, double z ) {
		this.theta = theta;
		this.unitAxisRoation.set( x, y, z );
		unitAxisRoation.normalize();
	}

	public Vector3D_F64 getUnitAxisRoation() {
		return unitAxisRoation;
	}

	public void setUnitAxisRoation( Vector3D_F64 unitAxisRoation ) {
		this.unitAxisRoation.set( unitAxisRoation );
	}

	public double getTheta() {
		return theta;
	}

	public void setTheta( double theta ) {
		this.theta = theta;
	}
}
