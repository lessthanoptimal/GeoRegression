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

package georegression.transform.twist;

import georegression.struct.point.Vector3D_F64;

/**
 * <p>Representation of a twist coordinate, which is se(3). The exponential is SE(3) or rigid body. A twist
 * can be interpreted as a mapping of points from their initial coordinate to another coordinate after the
 * rigid motion has been applied [1].</p>
 *
 * <p>
 * p(t) = exp(hat{&xi;}*t)*p(0)<br>
 * p'(t) = w &times; (p(t) -q)<br>
 * v = -w &times; q<br>
 * where ||w|| = 1, q in Re<sup>3</sup> is a point on the axis. The point in a body undergoing a constant
 * screw motion which traces helices.
 * </p>
 *
 * <p>
 * [1] R. Murray, et. al. "A Mathematical Introduction to ROBOTIC MANIPULATION" 1994
 * </p>
 *
 * @author Peter Abeles
 */
public class TwistCoordinate_F64 {
	/**
	 * Angular component in so(3).   Does NOT need to be normalized to 1.
	 */
	public Vector3D_F64 w = new Vector3D_F64();
	/**
	 * Translational component. Re<sup>3</sup>
	 */
	public Vector3D_F64 v = new Vector3D_F64();

	public TwistCoordinate_F64() {
	}

	public TwistCoordinate_F64( double wx, double wy , double wz, double vx , double vy, double vz ) {
		set(wx,wy,wz, vx,vy,vz);
	}

	public TwistCoordinate_F64(Vector3D_F64 w, Vector3D_F64 v) {
		this.w.setTo(w);
		this.v.setTo(v);
	}

	public void set( double wx, double wy , double wz, double vx , double vy, double vz ) {
		this.w.setTo(wx,wy,wz);
		this.v.setTo(vx,vy,vz);
	}

	public void set( TwistCoordinate_F64 original ) {
		this.w.setTo( original.w );
		this.v.setTo( original.v );
	}

	public void print() {
		System.out.println(toString());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName()+"{" +
				"w=" + w +
				", v=" + v +
				'}';
	}
}
