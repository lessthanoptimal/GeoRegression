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

package georegression.struct.se;

import georegression.struct.GeoTuple2D_F64;
import georegression.struct.point.Vector2D_F64;

/**
 * <p>
 * Special Euclidean transform that has been parameterized with three parameters:
 * translation (x,y) and rotation (yaw).  First the rotation is applied then the translation.
 * </p>
 * <p/>
 * <p>
 * Note that this data structure does not specify the units or coordinate frames.
 * </p>
 *
 * @author Peter Abeles
 */
public class Se2_F64 implements SpecialEuclidean<Se2_F64> {

	// the translational component
	public Vector2D_F64 T = new Vector2D_F64();

	// rotational component parameterized for speed
	public double c; // cos(yaw)
	public double s; // sin(yaw)

	public Se2_F64( GeoTuple2D_F64 T, double yaw ) {
		this( T.getX(), T.getY(), yaw );
	}

	public Se2_F64( double x, double y, double yaw ) {
		set( x, y, yaw );
	}

	public Se2_F64( double x, double y, double cosYaw, double sinYaw ) {
		set( x, y, cosYaw, sinYaw );
	}

	/**
	 * Constructor which initializes it to no transform
	 */
	public Se2_F64() {
		c=1.0;
	}

	public void set( double x, double y, double yaw ) {
		this.T.set(x, y);
		this.c = Math.cos( yaw );
		this.s = Math.sin( yaw );
	}

	public void set( double x, double y, double cosYaw, double sinYaw ) {
		this.T.set(x, y);
		this.c = cosYaw;
		this.s = sinYaw;
	}

	public void set( Se2_F64 target ) {
		this.T.set(target.T);
		this.c = target.c;
		this.s = target.s;
	}

	public double getX() {
		return T.getX();
	}

	public void setX( double x ) {
		T.setX(x);
	}

	public double getY() {
		return T.getY();
	}

	public void setY( double y ) {
		T.setY(y);
	}

	public Vector2D_F64 getTranslation() {
		return T;
	}

	public void setTranslation( Vector2D_F64 tran ) {
		this.T = tran;
	}

	public void setTranslation( double x, double y ) {
		this.T.set(x, y);
	}

	public double getYaw() {
		return Math.atan2( s, c );
	}

	public void setYaw( double yaw ) {
		this.c = Math.cos( yaw );
		this.s = Math.sin( yaw );
	}

	public double getCosineYaw() {
		return c;
	}

	public double getSineYaw() {
		return s;
	}

	@Override
	public int getDimension() {
		return 2;
	}

	@Override
	public Se2_F64 createInstance() {
		return new Se2_F64();
	}

	@Override
	public Se2_F64 concat( Se2_F64 second, Se2_F64 result ) {
		if( result == null )
			result = new Se2_F64();

		result.setYaw( getYaw() + second.getYaw() );

		result.T.x = second.T.x + second.c * T.x - second.s * T.y;
		result.T.y = second.T.y + second.s * T.x + second.c * T.y;

		return result;
	}

	@Override
	public Se2_F64 invert( Se2_F64 inverse ) {
		if( inverse == null )
			inverse = new Se2_F64();

		double x = -T.x;
		double y = -T.y;

		inverse.s = -s;
		inverse.c = c;
		inverse.T.x = c * x + s * y;
		inverse.T.y = -s * x + c * y;

		return inverse;
	}

	@Override
	public void reset() {
		c = 1;
		s = 0;
		T.set(0, 0);
	}

	public Se2_F64 copy() {
		return new Se2_F64( T.x, T.y, c, s );
	}

	public String toString() {
		return "Se2( x = " + T.x + " y = " + T.y + " yaw = " + getYaw() + " )";
	}

	public void print() {
		System.out.println(this);
	}
}
