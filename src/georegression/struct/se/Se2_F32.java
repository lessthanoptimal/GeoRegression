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

package georegression.struct.se;

import georegression.struct.GeoTuple2D_F32;
import georegression.struct.point.Vector2D_F32;

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
public class Se2_F32 implements SpecialEuclidean<Se2_F32> {

	// the translational component
	public Vector2D_F32 T = new Vector2D_F32();

	// rotational component parameterized for speed
	public float c; // cos(yaw)
	public float s; // sin(yaw)

	public Se2_F32( GeoTuple2D_F32 T, float yaw ) {
		this( T.getX(), T.getY(), yaw );
	}

	public Se2_F32( float x, float y, float yaw ) {
		set( x, y, yaw );
	}

	public Se2_F32( float x, float y, float cosYaw, float sinYaw ) {
		set( x, y, cosYaw, sinYaw );
	}

	public Se2_F32() {
	}

	public void set( float x, float y, float yaw ) {
		this.T.set(x, y);
		this.c = (float)Math.cos( yaw );
		this.s = (float)Math.sin( yaw );
	}

	public void set( float x, float y, float cosYaw, float sinYaw ) {
		this.T.set(x, y);
		this.c = cosYaw;
		this.s = sinYaw;
	}

	public void set( Se2_F32 target ) {
		this.T.set(target.T);
		this.c = target.c;
		this.s = target.s;
	}

	public float getX() {
		return T.getX();
	}

	public void setX( float x ) {
		T.setX(x);
	}

	public float getY() {
		return T.getY();
	}

	public void setY( float y ) {
		T.setY(y);
	}

	public Vector2D_F32 getTranslation() {
		return T;
	}

	public void setTranslation( Vector2D_F32 tran ) {
		this.T = tran;
	}

	public void setTranslation( float x, float y ) {
		this.T.set(x, y);
	}

	public float getYaw() {
		return (float)Math.atan2( s, c );
	}

	public void setYaw( float yaw ) {
		this.c = (float)Math.cos( yaw );
		this.s = (float)Math.sin( yaw );
	}

	public float getCosineYaw() {
		return c;
	}

	public float getSineYaw() {
		return s;
	}

	@Override
	public int getDimension() {
		return 2;
	}

	@Override
	public Se2_F32 createInstance() {
		return new Se2_F32();
	}

	@Override
	public Se2_F32 concat( Se2_F32 second, Se2_F32 result ) {
		if( result == null )
			result = new Se2_F32();

		result.setYaw( getYaw() + second.getYaw() );

		result.T.x = second.T.x + second.c * T.x - second.s * T.y;
		result.T.y = second.T.y + second.s * T.x + second.c * T.y;

		return result;
	}

	@Override
	public Se2_F32 invert( Se2_F32 inverse ) {
		if( inverse == null )
			inverse = new Se2_F32();

		float x = -T.x;
		float y = -T.y;

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

	public Se2_F32 copy() {
		return new Se2_F32( T.x, T.y, c, s );
	}

	public String toString() {
		return "Se2( x = " + T.x + " y = " + T.y + " yaw = " + getYaw() + " )";
	}
}
