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

package georegression.struct.so;

import georegression.struct.point.Vector3D_F32;

import java.io.Serializable;


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
public class Rodrigues_F32 implements Serializable {
	// unit vector defining the axis of rotation
	public Vector3D_F32 unitAxisRotation = new Vector3D_F32();
	// the angle it is rotated by
	public float theta;

	public Rodrigues_F32() {
	}

	/**
	 * Constructor which specifies the transform.
	 *
	 * @param theta Angle of rotation
	 * @param unitAxisRotation  Axis of rotation   Must be normalized to 1.
	 */
	public Rodrigues_F32(float theta, Vector3D_F32 unitAxisRotation) {
		this.theta = theta;
		this.unitAxisRotation.set(unitAxisRotation);
	}

	/**
	 * Constructor which specifies the transform using an unnormalized rotation axis.
	 *
	 * @param theta Angle of rotation
	 * @param x Axis of rotation. x-component.
	 * @param y Axis of rotation. y-component.
	 * @param z Axis of rotation. z-component.
	 */
	public Rodrigues_F32(float theta, float x, float y, float z) {
		this.theta = theta;
		this.unitAxisRotation.set( x, y, z );
		unitAxisRotation.normalize();
	}

	public Vector3D_F32 getUnitAxisRotation() {
		return unitAxisRotation;
	}

	public void setUnitAxisRotation(Vector3D_F32 unitAxisRotation) {
		this.unitAxisRotation.set(unitAxisRotation);
	}

	public float getTheta() {
		return theta;
	}

	public void setTheta( float theta ) {
		this.theta = theta;
	}

	/**
	 * Assign the Rodrigues coordinates using a 3 element vector.  Theta is the vector's
	 * magnitude and the axis of rotation is the unit vector.
	 *
	 * @param x x-component of 3 vector
	 * @param y y-component of 3 vector
	 * @param z z-component of 3 vector
	 */
	public void setParamVector( float  x , float y , float z ) {
		float ax = (float)Math.abs(x);
		float ay = (float)Math.abs(y);
		float az = (float)Math.abs(z);

		float max = (float)Math.max(ax,ay);
		max = (float)Math.max(max,az);
		
		if( max == 0 ) {
			theta = 0;
			unitAxisRotation.set(1,0,0);
		} else {
			x /= max;
			y /= max;
			z /= max;
			theta = (float)Math.sqrt(x*x + y*y + z*z);
			unitAxisRotation.x = x/theta;
			unitAxisRotation.y = y/theta;
			unitAxisRotation.z = z/theta;
			theta *= max;
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+
				" v{ "+unitAxisRotation.x+" , "+unitAxisRotation.x+" , "+unitAxisRotation.x+" } theta = "+
				theta;
	}
}
