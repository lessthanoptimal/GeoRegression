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

package georegression.struct;


/**
 * Generic Tuple for geometric objects that store (x,y,z)
 *
 * @author Peter Abeles
 */
public abstract class GeoTuple3D_F64<T extends GeoTuple3D_F64> extends GeoTuple_F64<T> {
	public double x;
	public double y;
	public double z;

	public GeoTuple3D_F64( double x, double y, double z ) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public GeoTuple3D_F64() {
	}

	@Override
	public int getDimension() {
		return 3;
	}

	protected void _set( GeoTuple3D_F64 a ) {
		x = a.x;
		y = a.y;
		z = a.z;
	}

	public void set( double x, double y, double z ) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public boolean isIdentical( double x, double y, double z ) {
		return ( this.x == x && this.y == y && this.z == z );
	}

	public boolean isIdentical( double x, double y, double z, double tol ) {
		return ( Math.abs( this.x - x ) <= tol && Math.abs( this.y - y ) <= tol && Math.abs( this.z - z ) <= tol );
	}

	public boolean isIdentical( GeoTuple3D_F64 t, double tol ) {
		return ( Math.abs( this.x - t.x ) <= tol && Math.abs( this.y - t.y ) <= tol && Math.abs( this.z - t.z ) <= tol );
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public double getIndex( int index ) {
		switch( index ) {
			case 0:
				return x;

			case 1:
				return y;

			case 2:
				return z;

			default:
				throw new IllegalArgumentException( "Invalid index" );
		}
	}

	public void setIndex( int index, double value ) {
		switch( index ) {
			case 0:
				x = value;
				break;

			case 1:
				y = value;
				break;

			case 2:
				z = value;
				break;

			default:
				throw new IllegalArgumentException( "Invalid index" );
		}
	}

	public double norm() {
		return Math.sqrt( x * x + y * y + z * z );
	}

	public double normSq() {
		return x * x + y * y + z * z;
	}

	/**
	 * Multiplies each coordinate by the specified number
	 * @param value scale factor
	 */
	public void scale( double value ) {
		x *= value;
		y *= value;
		z *= value;
	}

	@Override
	public double distance( GeoTuple3D_F64 t ) {
		double dx = t.x - x;
		double dy = t.y - y;
		double dz = t.z - z;

		return Math.sqrt( dx * dx + dy * dy + dz * dz );
	}

	@Override
	public double distance2( GeoTuple3D_F64 t ) {
		double dx = t.x - x;
		double dy = t.y - y;
		double dz = t.z - z;

		return dx * dx + dy * dy + dz * dz;
	}

	public void print() {
		System.out.println( this );
	}

	public boolean isNaN() {
		return ( Double.isNaN( x ) || Double.isNaN( y ) || Double.isNaN( z ) );
	}

	public void setX( double x ) {
		this.x = x;
	}

	public void setY( double y ) {
		this.y = y;
	}

	public void setZ( double z ) {
		this.z = z;
	}
}
