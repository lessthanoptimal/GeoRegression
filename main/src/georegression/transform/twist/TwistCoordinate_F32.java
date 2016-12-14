/*
 * Copyright (C) 2011-2016, Peter Abeles. All Rights Reserved.
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

import georegression.geometry.GeometryMath_F32;
import georegression.misc.GrlConstants;
import georegression.struct.point.Vector3D_F32;

/**
 * <p>Representation of a twist coordinate, which is se(3).  The exponential is SE(3) or rigid body.  A twist
 * can be interpreted as a mapping of points from their initial coordinate to another coordinate after the
 * rigid motion has been applied [1].</p>
 *
 * <p>
 * p(t) = exp(hat{&xi;}*t)*p(0)<br>
 * p'(t) = w &times; (p(t) -q)<br>
 * v = -w &times; q<br>
 * where ||w|| = 1, q in Re<sup>3</sup> is a point on the axis.  The point in a body undergoing a constant
 * screw motion which traces helices.
 * </p>
 *
 * <p>
 * [1] R. Murray, et. al. "A Mathematical Introduction to ROBOTIC MANIPULATION" 1994
 * </p>
 *
 * @author Peter Abeles
 */
public class TwistCoordinate_F32 {
	/**
	 * Angular component in so(3). Normalized such that the F-norm is 1 or 0 if no rotation.
	 */
	public Vector3D_F32 w = new Vector3D_F32();
	/**
	 * Translational component. Re<sup>3</sup>
	 */
	public Vector3D_F32 v = new Vector3D_F32();

	public TwistCoordinate_F32() {
	}

	public TwistCoordinate_F32( float wx, float wy , float wz, float vx , float vy, float vz ) {
		set(wx,wy,wz, vx,vy,vz);
	}

	public TwistCoordinate_F32(Vector3D_F32 w, Vector3D_F32 v) {
		this.w.set(w);
		this.v.set(v);
	}

	public void set( float wx, float wy , float wz, float vx , float vy, float vz ) {
		this.w.set(wx,wy,wz);
		this.v.set(vx,vy,vz);
	}

	public void set( TwistCoordinate_F32 original ) {
		this.w.set( original.w );
		this.v.set( original.v );
	}

	/**
	 * Normalizes w.  If it is close to zero then it will be set to zero.  Otherwise it will be modified such
	 * that the norm is 1.
	 */
	public void normalize() {
		float n = w.normSq();
		if( n <= GrlConstants.FLOAT_TEST_TOL )
			w.set(0,0,0);
		else
			GeometryMath_F32.divide(w, (float)Math.sqrt(n));
	}

	/**
	 * Returns true if the twist is pure translation or false is more rotation or a mix of both.  The
	 * test is performed by looking at the norm squared of w and seeing if it is significantly different from 1.0f.
	 * Since 'w' is required to be normalized to one this should test to see if
	 * @return true if a purely translational twist
	 */
	public boolean isPureTranslation() {
		return (float)Math.abs(w.normSq()-1.0f) > GrlConstants.FLOAT_TEST_TOL;
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
