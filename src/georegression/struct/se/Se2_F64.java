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
	public Vector2D_F64 tran = new Vector2D_F64();

	// rotational component parameterized for speed
	public double c; // cos(yaw)
	public double s; // sin(yaw)

	public Se2_F64( GeoTuple2D_F64 tran, double yaw ) {
		this( tran.getX(), tran.getY(), yaw );
	}

	public Se2_F64( double x, double y, double yaw ) {
		set( x, y, yaw );
	}

	public Se2_F64( double x, double y, double cosYaw, double sinYaw ) {
		set( x, y, cosYaw, sinYaw );
	}

	public Se2_F64() {
	}

	public void set( double x, double y, double yaw ) {
		this.tran.set( x, y );
		this.c = Math.cos( yaw );
		this.s = Math.sin( yaw );
	}

	public void set( double x, double y, double cosYaw, double sinYaw ) {
		this.tran.set( x, y );
		this.c = cosYaw;
		this.s = sinYaw;
	}

	public void set( Se2_F64 target ) {
		this.tran.set( target.tran );
		this.c = target.c;
		this.s = target.s;
	}

	public double getX() {
		return tran.getX();
	}

	public void setX( double x ) {
		tran.setX( x );
	}

	public double getY() {
		return tran.getY();
	}

	public void setY( double y ) {
		tran.setY( y );
	}

	public Vector2D_F64 getTranslation() {
		return tran;
	}

	public void setTranslation( Vector2D_F64 tran ) {
		this.tran = tran;
	}

	public void setTranslation( double x, double y ) {
		this.tran.set( x, y );
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

		result.tran.x = second.tran.x + second.c * tran.x - second.s * tran.y;
		result.tran.y = second.tran.y + second.s * tran.x + second.c * tran.y;

		return result;
	}

	@Override
	public Se2_F64 invert( Se2_F64 inverse ) {
		if( inverse == null )
			inverse = new Se2_F64();

		double x = -tran.x;
		double y = -tran.y;

		inverse.s = -s;
		inverse.c = c;
		inverse.tran.x = c * x + s * y;
		inverse.tran.y = -s * x + c * y;

		return inverse;
	}

	@Override
	public void reset() {
		c = 1;
		s = 0;
		tran.set( 0, 0 );
	}

	public Se2_F64 copy() {
		return new Se2_F64( tran.x, tran.y, c, s );
	}

	public String toString() {
		return "Se2( x = " + tran.x + " y = " + tran.y + " yaw = " + getYaw() + " )";
	}
}
