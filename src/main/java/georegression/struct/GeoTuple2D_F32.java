/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
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

package georegression.struct;


/**
 * Generic Tuple for geometric objects that store (x,y)
 *
 * @author Peter Abeles
 */
public abstract class GeoTuple2D_F32<T extends GeoTuple2D_F32> extends GeoTuple_F32<T> {
	public float x;
	public float y;

	public GeoTuple2D_F32( float x, float y ) {
		this.x = x;
		this.y = y;
	}

	public GeoTuple2D_F32() {
	}

	protected void _set( GeoTuple2D_F32 a ) {
		x = a.x;
		y = a.y;
	}

	public void set( float x, float y ) {
		this.x = x;
		this.y = y;
	}

	public boolean isIdentical( float x, float y ) {
		return this.x == x && this.y == y;
	}

	public boolean isIdentical( float x, float y, float tol ) {
		return ( (float)Math.abs( this.x - x ) < tol && (float)Math.abs( this.y - y ) < tol );
	}

	public boolean isIdentical( T t, float tol ) {
		return ( (float)Math.abs( this.x - t.x ) < tol && (float)Math.abs( this.y - t.y ) < tol );
	}

	public void setX( float x ) {
		this.x = x;
	}

	public void setY( float y ) {
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	@Override
	public float distance( T t ) {
		float dx = t.x - x;
		float dy = t.y - y;

		return (float)Math.sqrt( dx * dx + dy * dy );
	}

	@Override
	public float distance2( T t ) {
		float dx = t.x - x;
		float dy = t.y - y;

		return dx * dx + dy * dy;
	}

	@Override
	public float getIndex( int index ) {
		switch( index ) {
			case 0:
				return x;

			case 1:
				return y;

			default:
				throw new IllegalArgumentException( "Invalid index" );
		}
	}

	@Override
	public void setIndex( int index, float value ) {
		switch( index ) {
			case 0:
				x = value;
				break;

			case 1:
				y = value;
				break;

			default:
				throw new IllegalArgumentException( "Invalid index" );
		}
	}

	@Override
	public float norm() {
		return (float)Math.sqrt( x * x + y * y );
	}

	@Override
	public float normSq() {
		return x * x + y * y;
	}

	@Override
	public int getDimension() {
		return 2;
	}

	public void print() {
		System.out.println( this );
	}


}