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
 * Generic Tuple for geometric objects that store (x,y,z,w)
 *
 * @author Peter Abeles
 */
public abstract class GeoTuple4D_F32<T extends GeoTuple4D_F32> extends GeoTuple_F32<T> {
	public float x;
	public float y;
	public float z;
	public float w;

	public GeoTuple4D_F32(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public GeoTuple4D_F32() {
	}

	@Override
	public int getDimension() {
		return 4;
	}

	protected void _set( GeoTuple4D_F32 a ) {
		x = a.x;
		y = a.y;
		z = a.z;
		w = a.w;
	}

	public void set( float x, float y, float z , float w ) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public boolean isIdentical( float x, float y, float z , float w ) {
		return ( this.x == x && this.y == y && this.z == z && this.w == w);
	}

	public boolean isIdentical( float x, float y, float z, float w , float tol ) {
		return ( (float)Math.abs( this.x - x ) <= tol && 
				(float)Math.abs( this.y - y ) <= tol && 
				(float)Math.abs( this.z - z ) <= tol && 
				(float)Math.abs( this.w - w ) <= tol );
	}

	public boolean isIdentical( GeoTuple4D_F32 t, float tol ) {
		return ( (float)Math.abs( this.x - t.x ) <= tol && 
				(float)Math.abs( this.y - t.y ) <= tol && 
				(float)Math.abs( this.z - t.z ) <= tol &&
				(float)Math.abs( this.w - t.w ) <= tol );
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

	public float getW() {
		return w;
	}

	public float getIndex( int index ) {
		switch( index ) {
			case 0:
				return x;

			case 1:
				return y;

			case 2:
				return z;

			case 3:
				return w;

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
			
			case 3:
				w = value;

			default:
				throw new IllegalArgumentException( "Invalid index" );
		}
	}

	public float norm() {
		return (float)Math.sqrt( x * x + y * y + z * z + w * w);
	}

	public float normSq() {
		return x * x + y * y + z * z + w * w;
	}

	@Override
	public float distance( GeoTuple4D_F32 t ) {
		float dx = t.x - x;
		float dy = t.y - y;
		float dz = t.z - z;
		float dw = t.w - w;

		return (float)Math.sqrt( dx * dx + dy * dy + dz * dz + dw * dw);
	}

	@Override
	public float distance2( GeoTuple4D_F32 t ) {
		float dx = t.x - x;
		float dy = t.y - y;
		float dz = t.z - z;
		float dw = t.w - w;

		return dx * dx + dy * dy + dz * dz + dw * dw;
	}

	public void print() {
		System.out.println( this );
	}

	public boolean isNaN() {
		return ( Float.isNaN( x ) || Float.isNaN( y ) || Float.isNaN( z ) || Float.isNaN( w ));
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

	public void setW( float w ) {
		this.w = w;
	}
}
