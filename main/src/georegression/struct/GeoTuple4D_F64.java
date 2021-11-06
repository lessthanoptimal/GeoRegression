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

package georegression.struct;


import lombok.Getter;
import lombok.Setter;
import org.ejml.UtilEjml;
import org.ejml.ops.MatrixIO;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Generic Tuple for geometric objects that store (x,y,z,w)
 *
 * @author Peter Abeles
 */
@Getter @Setter
public abstract class GeoTuple4D_F64 <T extends GeoTuple4D_F64> extends GeoTuple_F64<T> {
	public double x;
	public double y;
	public double z;
	public double w;

	protected GeoTuple4D_F64(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	protected GeoTuple4D_F64() {}

	@Override
	public int getDimension() {
		return 4;
	}

	protected void _setTo(GeoTuple4D_F64 a ) {
		x = a.x;
		y = a.y;
		z = a.z;
		w = a.w;
	}

	public void setTo(double x, double y, double z , double w ) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public void zero() {
		setTo(0, 0, 0, 0);
	}

	public boolean isIdentical( double x, double y, double z , double w ) {
		return ( this.x == x && this.y == y && this.z == z && this.w == w);
	}

	public boolean isIdentical( double x, double y, double z, double w , double tol ) {
		return ( Math.abs( this.x - x ) <= tol && 
				Math.abs( this.y - y ) <= tol && 
				Math.abs( this.z - z ) <= tol && 
				Math.abs( this.w - w ) <= tol );
	}

	@Override
	public boolean isIdentical( GeoTuple4D_F64 t, double tol ) {
		return ( Math.abs( this.x - t.x ) <= tol && 
				Math.abs( this.y - t.y ) <= tol && 
				Math.abs( this.z - t.z ) <= tol &&
				Math.abs( this.w - t.w ) <= tol );
	}

	@Override
	public double getIdx(int index ) {
		switch( index ) {
			case 0: return x;
			case 1: return y;
			case 2: return z;
			case 3: return w;
			default: throw new IllegalArgumentException( "Invalid index" );
		}
	}

	@Override
	public void setIdx(int index, double value ) {
		switch( index ) {
			case 0: x = value; break;
			case 1: y = value; break;
			case 2: z = value; break;
			case 3: w = value; break;
			default: throw new IllegalArgumentException( "Invalid index "+index );
		}
	}

	public void scale( double scalar ) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
		w *= scalar;
	}

	/**
	 * <p>In-place addition</p>
	 *
	 * this.x = this.x + a.x;
	 *
	 * @param a value which is to be added
	 */
	public void plusIP( GeoTuple4D_F64 a ) {
		x += a.x;
		y += a.y;
		z += a.z;
		w += a.w;
	}

	public T times( double scalar ) {
		T ret = createNewInstance();
		ret.x = x*scalar;
		ret.y = y*scalar;
		ret.z = z*scalar;
		ret.w = w*scalar;
		return ret;
	}

	/**
	 * In-place scalar multiplication
	 * @param scalar value that it is multiplied by
	 */
	public void timesIP( double scalar ) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
		w *= scalar;
	}

	public void divideIP( double scalar ) {
		x /= scalar;
		y /= scalar;
		z /= scalar;
		w /= scalar;
	}

	public void normalize() {
		divideIP( norm() );
	}

	@Override
	public double norm() {
		return Math.sqrt( x * x + y * y + z * z + w * w);
	}

	@Override
	public double normSq() {
		return x * x + y * y + z * z + w * w;
	}

	@Override
	public double distance( GeoTuple4D_F64 t ) {
		double dx = t.x - x;
		double dy = t.y - y;
		double dz = t.z - z;
		double dw = t.w - w;

		return Math.sqrt( dx * dx + dy * dy + dz * dz + dw * dw);
	}

	public double distance( double x , double y , double z , double w ) {
		double dx = this.x - x;
		double dy = this.y - y;
		double dz = this.z - z;
		double dw = this.w - w;

		return Math.sqrt( dx * dx + dy * dy + dz * dz + dw * dw);
	}

	@Override
	public double distance2( GeoTuple4D_F64 t ) {
		double dx = t.x - x;
		double dy = t.y - y;
		double dz = t.z - z;
		double dw = t.w - w;

		return dx * dx + dy * dy + dz * dz + dw * dw;
	}

	public double distance2( double x , double y , double z , double w ) {
		double dx = this.x - x;
		double dy = this.y - y;
		double dz = this.z - z;
		double dw = this.w - w;

		return dx * dx + dy * dy + dz * dz + dw * dw;
	}

	public void print() {
		System.out.println( this );
	}

	public boolean isNaN() {
		return ( Double.isNaN( x ) || Double.isNaN( y ) || Double.isNaN( z ) || Double.isNaN( w ));
	}

	/**
	 * Returns the absolute value of the component with the largest absolute value
	 * @return max absolute value
	 */
	public double maxAbs() {
		double absX = Math.abs(x);
		double absY = Math.abs(y);
		double absZ = Math.abs(z);
		double absW = Math.abs(w);

		double found = Math.max(absX,absY);
		if( found < absZ )
			found = absZ;
		if( found < absW )
			found = absW;
		return found;
	}

	@Override
	public void setTo(T src) {
		this.x = src.x;
		this.y = src.y;
		this.z = src.z;
		this.w = src.w;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;

		if(!(obj instanceof GeoTuple4D_F64))
			return false;

		var o = (GeoTuple4D_F64)obj;
		return Double.compare(x,o.x)==0 && Double.compare(y,o.y)==0 &&
				Double.compare(z,o.z)==0 && Double.compare(w,o.w)==0;
	}

	protected String toString( String name ) {
		DecimalFormat format = new DecimalFormat("#");
		String sx = UtilEjml.fancyString(x,format, MatrixIO.DEFAULT_LENGTH,4);
		String sy = UtilEjml.fancyString(y,format, MatrixIO.DEFAULT_LENGTH,4);
		String sz = UtilEjml.fancyString(z,format, MatrixIO.DEFAULT_LENGTH,4);
		String sw = UtilEjml.fancyString(w,format, MatrixIO.DEFAULT_LENGTH,4);

		return name+"( " + sx + " " + sy + " " + sz + " " + sw + " )";
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z, w);
	}
}
