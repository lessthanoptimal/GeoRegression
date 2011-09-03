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

package jgrl.struct;


/**
 * Generic Tuple for geometric objects that store (x,y)
 *
 * @author Peter Abeles
 */
public abstract class GeoTuple2D_F64<T extends GeoTuple2D_F64> extends GeoTuple_F64<T> {
	public double x;
	public double y;

	public GeoTuple2D_F64( double x, double y ) {
		this.x = x;
		this.y = y;
	}

	public GeoTuple2D_F64() {
	}

	protected void _set( GeoTuple2D_F64 a ) {
		x = a.x;
		y = a.y;
	}

	public void set( double x, double y ) {
		this.x = x;
		this.y = y;
	}

	public boolean isIdentical( double x, double y ) {
		return this.x == x && this.y == y;
	}

	public boolean isIdentical( double x, double y, double tol ) {
		return ( Math.abs( this.x - x ) < tol && Math.abs( this.y - y ) < tol );
	}

	public boolean isIdentical( T t, double tol ) {
		return ( Math.abs( this.x - t.x ) < tol && Math.abs( this.y - t.y ) < tol );
	}

	public void setX( double x ) {
		this.x = x;
	}

	public void setY( double y ) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	@Override
	public double distance( T t ) {
		double dx = t.x - x;
		double dy = t.y - y;

		return Math.sqrt( dx * dx + dy * dy );
	}

	@Override
	public double distance2( T t ) {
		double dx = t.x - x;
		double dy = t.y - y;

		return dx * dx + dy * dy;
	}

	@Override
	public double getIndex( int index ) {
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
	public void setIndex( int index, double value ) {
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
	public double norm() {
		return Math.sqrt( x * x + y * y );
	}

	@Override
	public double normSq() {
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