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
public abstract class GeoTuple3D_F32<T extends GeoTuple3D_F32> extends GeoTuple_F32<T> {
	public float x;
	public float y;
	public float z;

	public GeoTuple3D_F32( float x, float y, float z ) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public GeoTuple3D_F32() {
	}

	@Override
	public int getDimension() {
		return 3;
	}

	protected void _set( GeoTuple3D_F32 a ) {
		x = a.x;
		y = a.y;
		z = a.z;
	}

	public void set( float x, float y, float z ) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public boolean isIdentical( float x, float y, float z ) {
		return ( this.x == x && this.y == y && this.z == z );
	}

	public boolean isIdentical( float x, float y, float z, float tol ) {
		return ( (float)Math.abs( this.x - x ) <= tol && (float)Math.abs( this.y - y ) <= tol && (float)Math.abs( this.z - z ) <= tol );
	}

	public boolean isIdentical( GeoTuple3D_F32 t, float tol ) {
		return ( (float)Math.abs( this.x - t.x ) <= tol && (float)Math.abs( this.y - t.y ) <= tol && (float)Math.abs( this.z - t.z ) <= tol );
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public float getIndex( int index ) {
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

	public void setIndex( int index, float value ) {
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

	/**
	 * <p>In-place addition</p>
	 *
	 * this.x = this.x + a.x;
	 *
	 * @param a value which is to be added
	 */
	public void plusIP( GeoTuple3D_F32 a ) {
		x += a.x;
		y += a.y;
		z += a.z;
	}

	/**
	 * <p>Addition</p>
	 *
	 * ret.x = this.x + a.x;
	 *
	 * @param a value which is to be added
	 */
	public T plus( GeoTuple3D_F32 a ) {
		T ret = createNewInstance();
		ret.x = x + a.x;
		ret.y = y + a.y;
		ret.z = z + a.z;
		return ret;
	}

	/**
	 * In-place scalar multiplication
	 * @param scalar value that it is multiplied by
	 */
	public void timesIP( float scalar ) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
	}

	/**
	 * In-place scalar multiplication
	 * @param scalar value that it is multiplied by
	 */
	public void scale( float scalar ) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
	}

	/**
	 * Scalar multiplication
	 * @param scalar value which is it multiplied by
	 * @return new matrix which is the original scaled
	 */
	public T times( float scalar ) {
		T ret = createNewInstance();
		ret.x = x*scalar;
		ret.y = y*scalar;
		ret.z = z*scalar;
		return ret;
	}

	public float norm() {
		return (float)Math.sqrt( x * x + y * y + z * z );
	}

	public float normSq() {
		return x * x + y * y + z * z;
	}

	@Override
	public float distance( GeoTuple3D_F32 t ) {
		float dx = t.x - x;
		float dy = t.y - y;
		float dz = t.z - z;

		return (float)Math.sqrt( dx * dx + dy * dy + dz * dz );
	}

	@Override
	public float distance2( GeoTuple3D_F32 t ) {
		float dx = t.x - x;
		float dy = t.y - y;
		float dz = t.z - z;

		return dx * dx + dy * dy + dz * dz;
	}

	public void print() {
		System.out.println( this );
	}

	public boolean isNaN() {
		return ( Float.isNaN( x ) || Float.isNaN( y ) || Float.isNaN( z ) );
	}

	public void setX( float x ) {
		this.x = x;
	}

	public void setY( float y ) {
		this.y = y;
	}

	public void setZ( float z ) {
		this.z = z;
	}
}
